package com.self.core.generalTimeBinning

import java.sql.Timestamp

import com.self.core.baseApp.myAPP
import com.self.core.generalTimeBinning.models.{LongTypeTimeColInfo, StringTypeTimeColInfo, TimeParser, TimestampTypeTimeColInfo}
import org.apache.spark.mllib.linalg.{DenseVector, SparseVector, Vector}
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.GenericMutableRow
import org.apache.spark.sql.catalyst.util.GenericArrayData
import org.apache.spark.sql.types._

object GeneralTImeBinning extends myAPP{
  val testData1 = Array(
    ("2017/02/10 00:00:00", 1, 2),
    ("2017/02/08 01:00:01", 1, 2),
    ("2017/3/1 04:00:02", 1, 2),
    ("2017/4/10 00:15:03", 1, 2),
    (null, 1, 2),
    ("2017/04/20 07:20:05", 1, 2),
    ("2017/04/30 08:01:06", 1, 2),
    ("2017/04/30 09:11:06", 1, 2),
    ("2017/04/30 16:01:06", 1, 2),
    ("2017/06/10 13:01:06", 1, 2),
    ("2017/08/10 00:00:00", 1, 2),
    ("2017/08/18 01:00:01", 1, 2),
    ("2017/11/1 04:00:02", 1, 2),
    ("2017/12/31 00:15:03", 1, 2),
    ("2017/04/10 06:20:04", 1, 2),
    ("2018/01/1 07:20:05", 1, 2),
    ("2018/02/19 13:01:06", 1, 2),
    ("2018/03/2 13:01:06", 1, 2),
    ("2018/03/9 13:01:06", 1, 2),
    ("2018/04/1 13:01:06", 1, 2))

  def testParser(): Unit = {
    val timeColName = "time"
    val timeFormat = "yyyy/MM/dd HH:mm:ss"
    val time1 = "2018/04/1 13:01:06"
    val time2 = new java.text.SimpleDateFormat(timeFormat).parse(time1).getTime

    val time3 = new Timestamp(time2)
    println("time3", time3)
    val timeColInfo = new StringTypeTimeColInfo(timeColName, "yyyy/MM/dd HH:mm:ss")
    println(new TimeParser(timeColInfo).parse(time1))

    val timeColInfo2 = new LongTypeTimeColInfo(timeColName, "millisecond")
    println(new TimeParser(timeColInfo2).parse(time2))

    val timeColInfo3 = new TimestampTypeTimeColInfo(timeColName)
    println(new TimeParser(timeColInfo3).parse(time3))
  }


  def testBinner() = {

  }


  override def run(): Unit = {
    /** 先通过rdd看看到底核心的数据流转需要什么类型工程结构，形成较为清晰的需求 */
    val rawDataFrame = sqlc.createDataFrame(testData1).toDF("time", "col1", "col2")

    /** 1)解析器 */
    /** 需要时间列名、列类型、时间信息 --窄口进 */
    val timeColName = "time"
    val timeColType = StringType // 这里要精准匹配
    val timeFormat = "yyyy/MM/dd HH:mm:ss"

    testParser() // 测试一下解析器
    val timeColInfo = new StringTypeTimeColInfo(timeColName, timeFormat)
    val timeParser = new TimeParser(timeColInfo)

    // 完整时间

    /** 2)分箱器 */
    // 按时长分箱
    // 分箱时长 单位为毫秒







    //    class VectorUDT extends UserDefinedType[Vector] {
//
//      override def sqlType: StructType = {
//        // type: 0 = sparse, 1 = dense
//        // We only use "values" for dense vectors, and "size", "indices", and "values" for sparse
//        // vectors. The "values" field is nullable because we might want to add binary vectors later,
//        // which uses "size" and "indices", but not "values".
//        StructType(Seq(
//          StructField("type", ByteType, nullable = false),
//          StructField("size", IntegerType, nullable = true),
//          StructField("indices", ArrayType(IntegerType, containsNull = false), nullable = true),
//          StructField("values", ArrayType(DoubleType, containsNull = false), nullable = true)))
//      }
//
//      override def serialize(obj: Any): InternalRow = {
//        obj match {
//          case SparseVector(size, indices, values) =>
//            val row = new GenericMutableRow(4)
//            row.setByte(0, 0)
//            row.setInt(1, size)
//            row.update(2, new GenericArrayData(indices.map(_.asInstanceOf[Any])))
//            row.update(3, new GenericArrayData(values.map(_.asInstanceOf[Any])))
//            row
//          case DenseVector(values) =>
//            val row = new GenericMutableRow(4)
//            row.setByte(0, 1)
//            row.setNullAt(1)
//            row.setNullAt(2)
//            row.update(3, new GenericArrayData(values.map(_.asInstanceOf[Any])))
//            row
//        }
//      }
//
//      override def deserialize(datum: Any): Vector = {
//        datum match {
//          case row: InternalRow =>
//            require(row.numFields == 4,
//              s"VectorUDT.deserialize given row with length ${row.numFields} but requires length == 4")
//            val tpe = row.getByte(0)
//            tpe match {
//              case 0 =>
//                val size = row.getInt(1)
//                val indices = row.getArray(2).toIntArray()
//                val values = row.getArray(3).toDoubleArray()
//                new SparseVector(size, indices, values)
//              case 1 =>
//                val values = row.getArray(3).toDoubleArray()
//                new DenseVector(values)
//            }
//        }
//      }
//
//      override def pyUDT: String = "pyspark.mllib.linalg.VectorUDT"
//
//      override def userClass: Class[Vector] = classOf[Vector]
//
//      override def equals(o: Any): Boolean = {
//        o match {
//          case v: VectorUDT => true
//          case _ => false
//        }
//      }
//
//      // see [SPARK-8647], this achieves the needed constant hash code without constant no.
//      override def hashCode(): Int = classOf[VectorUDT].getName.hashCode()
//
//      override def typeName: String = "vector"
//
//      private[spark] override def asNullable: VectorUDT = this
//    }


  }
}
