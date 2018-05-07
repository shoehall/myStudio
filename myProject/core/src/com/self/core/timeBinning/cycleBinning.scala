package com.self.core.timeBinning

import com.zzjz.deepinsight.basic.BaseMain
import java.sql.Date
import java.text.SimpleDateFormat
import com.google.gson.{Gson, JsonParser}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row
import org.apache.spark.ml.feature.Bucketizer
import org.apache.spark.sql.types.StructType


/**
  * editor：Xuhao
  * date： 2017/10/04 10:00:00
  */

/**
  * 周期分箱（0.7平台）
  */
object cycleBinning extends BaseMain{
  override def run(): Unit = {
    val jsonparam = "<#jsonparam#>"
    val gson = new Gson()
    val p : java.util.Map[String, String] = gson.fromJson(jsonparam, classOf[java.util.Map[String,String]])

    /**
      * 参数
      */
    val rddTableName = "<#rddtablename#>"
    val tableName = p.get("inputTableName").trim
    val rawDataDF = z.rdd(tableName).asInstanceOf[org.apache.spark.sql.DataFrame]
    val colnames: String = p.get("colnames").trim
    val bucket = p.get("bucket").trim
    val timeFormat = p.get("timeFormat")
    val stepLength = p.get("stepLength").toDouble
    val stepUnit = p.get("stepUnit")


    val parser = new JsonParser()
    val pJsonParser = parser.parse(jsonparam).getAsJsonObject
    val timeAxisObj = pJsonParser.getAsJsonObject("timeAxis")
    val timeAxis = timeAxisObj.get("timeAxis").getAsString

    val (start_time, end_time) = timeAxis match {
      case "byHand" =>
        val timeString = timeAxisObj.get("byHandInterval").getAsString.split(",")
        (timeString(0).trim, timeString(1).trim)
      case "minMax" => throw new Exception("Sorry, 暂不支持该功能，敬请期待")
      case "smoothMinMax" => throw new Exception("Sorry, 暂不支持该功能，敬请期待")
    }


    val periodSelectObj = pJsonParser.getAsJsonObject("periodSelect")
    val periodSelect = periodSelectObj.get("periodSelect").getAsString

    val cycleInterval = p.get("cycleInterval").toString
    val (interval_start, interval_end) = {
      val u = cycleInterval.split(",")
      (u(0).trim.toDouble, u(1).trim.toDouble)
    }
    val intervalUnit = p.get("intervalUnit").toString
    val phase = p.get("phase").toString


    /**
      * 测试参数
      */
//
//    val path = "G://网络行为分析数据/ipsession.csv"
//    val header = "true"
//    var rawDataDF = sqlc.read.format("com.databricks.spark.csv")
//      .option("header", header).option("inferSchema", true.toString).load(path)
//    rawDataDF = rawDataDF.withColumnRenamed(rawDataDF.columns(0), "DRETBEGINTIME")
//
//    // 分箱的列名
//    val colnames = "DRETBEGINTIME"
//
//    // 输出的分箱列名
//    val bucket = "bucket"
//
//    // 假定选择时间数据分箱
//    val timeFormat: String = "yyyy/MM/dd HH:mm:ss"
//
//    // 步长
//    val stepLength = 2
//
//    // 步长单位
//    val stepUnit = "h"
//
//    // 时间轴起始值(从timeAxis得到)
//    val start_time = "2017/1/18 00:00:00"
//    val end_time = "2017/1/22 00:00:00"
//
//    // 周期分箱区间单位
//    val intervalUnit = "h"
//
//    // 周期内分箱区间
//    val interval_start = 9
//    val interval_end = 15
//
//
//    // 周期选择
//    val periodSelect = "unitPeriod"



    /**
      * 一些用到的工具设定（公共）
      */
    // 将单位由字符转为数值
    def getUnit(unit: String): Int = {
      unit match {
        case "d" => 24 * 3600 * 1000
        case "h" => 3600 * 1000
        case "m" => 60 * 1000
        case "s" => 1000
        case _ => throw new Exception("周期单位只能为：天、小时、分、秒，对应符号为：d、h、m、s")
      }
    }
    val timeFmt: SimpleDateFormat = new SimpleDateFormat(timeFormat)

    // 初相的初始值
    val defaultPhase = "1970-01-01 00:00:00"
    val defaultTimeFormat = "yyyy-MM-dd HH:mm:ss"
    val newTimeFmt = new SimpleDateFormat(defaultTimeFormat)
    var phaseStamp = newTimeFmt.parse(defaultPhase).getTime

    if(phase != ""){
      phaseStamp = timeFmt.parse(phase.trim).getTime
    }



    // 时间戳转时间格式
    def formatDate(date: Long, timeFormat: String) = {
      val timeFmt: SimpleDateFormat = new SimpleDateFormat(timeFormat)
      val theDate = new Date(date)
      timeFmt.format(theDate)
    }


    val start_timeStamp = timeFmt.parse(start_time).getTime - phaseStamp
    val end_timeStamp = timeFmt.parse(end_time).getTime - phaseStamp


    /**
      * 分箱
      */
    val new_df = {
        var period  = 1.0
        var periodUnit = "d"
        var presentationFormat = "timeFormatWithID"

        if(periodSelect == "unitPeriod"){
          periodUnit = periodSelectObj.get("periodUnit").getAsString
          presentationFormat = periodSelectObj.get("presentationFormat").getAsString
        }else{
          periodUnit = periodSelectObj.get("periodUnit").getAsString
          period = periodSelectObj.get("period").getAsString.toDouble
          presentationFormat = periodSelectObj.get("presentationFormat").getAsString
        }



        // 根据起止时间和步长构建splits
        val splits = (interval_start*getUnit(intervalUnit)).
          to(end = interval_end*getUnit(intervalUnit), step = stepLength*getUnit(stepUnit)).
          toList.map(_.toDouble).toArray
        val splitsBC: Broadcast[Array[Double]] = sc.broadcast(splits)


        // 将数据转换为带时间戳的rdd
        val schemaFields = rawDataDF.schema.fields
        val new_rdd = rawDataDF.rdd.map(x => {
          var array = x.toSeq.toBuffer
          val dreBegin_time = x(x.fieldIndex(colnames)).toString
          val timeStamp: Double = util.Try(timeFmt.parse(dreBegin_time).getTime.toDouble - phaseStamp) getOrElse
            (-1000000000000000.0 - phaseStamp)
          val periodTimeStamp: Long = timeStamp.toLong % (period * getUnit(periodUnit)).toLong
          val phases: Double = (timeStamp.toLong / (period * getUnit(periodUnit)).toLong) * (period * getUnit(periodUnit))
          array += timeStamp.toDouble
          array += periodTimeStamp.toDouble
          array += phases.toDouble
          Row.fromSeq(array)
        })

        var v = schemaFields.toBuffer
        v += StructField("timeStamp", DoubleType)
        v += StructField("periodTimeStamp", DoubleType)
        v += StructField("phasesTimeStamp", DoubleType)
        val newSchemaName = v.toArray

        // 将新的rdd变为dataframe
        var df_with_timeStamp = sqlc.createDataFrame(new_rdd, StructType(newSchemaName))





        // 筛选出给定的时间，如果筛选时间不为空的话，否则不用筛选。这样会自动忽略无法解析的数据
      val interval_startTimeStamp = interval_start*getUnit(intervalUnit)
      val interval_endTimeStamp = interval_end*getUnit(intervalUnit)
      df_with_timeStamp = df_with_timeStamp.where(s"periodTimeStamp >= $interval_startTimeStamp")
      df_with_timeStamp = df_with_timeStamp.where(s" and periodTimeStamp <= $interval_endTimeStamp")
      df_with_timeStamp = df_with_timeStamp.where(s" and timeStamp >= $start_timeStamp and timeStamp <= $end_timeStamp")



        val bucketizer = new Bucketizer()
          .setInputCol("periodTimeStamp")
          .setOutputCol(bucket)
          .setSplits(splitsBC.value)
        var bucketedData = bucketizer.transform(df_with_timeStamp)

        bucketedData = presentationFormat match {
          case "numericFormatWithNotID" => bucketedData
          case "numericFormatWithID" => throw new Exception("Sorry,该功能还未开发")
          case "timeFormatWithID" => {
            // 以时间显示
            val time_format_rdd = bucketedData.rdd.map(x => {
              val phasesTimeStamp = x(x.fieldIndex("phasesTimeStamp")).toString.toDouble
              val binningID = x(x.fieldIndex(bucket)).toString.toDouble
              val tmp = splitsBC.value(binningID.toInt) + phasesTimeStamp + phaseStamp
              val binningTime = formatDate(tmp.toLong, timeFormat)
              var array = x.toSeq.toBuffer
              array += binningTime
              Row.fromSeq(array)
            })


            val newDataDF = sqlc.createDataFrame(time_format_rdd, {
              val newSchemaFields = bucketedData.schema.fields
              var w = newSchemaFields.toBuffer
              w += StructField(bucket + "_timeFormat", StringType)
              StructType(w.toArray)})
            newDataDF
          }
          case "timeFormatWithNotID" => throw new Exception("Sorry,该功能还未开发")
        }

        bucketedData = bucketedData.drop("timeStamp")
        bucketedData = bucketedData.drop("periodTimeStamp")
        bucketedData
    }

    new_df.show()

    /**
      * 输出
      */
    new_df.cache()
    outputrdd.put(rddTableName, new_df)
    new_df.registerTempTable(rddTableName)
    sqlc.cacheTable(rddTableName)


  }
}
