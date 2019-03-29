package cn.datashoe.dataWrangling

import java.text.SimpleDateFormat
import java.util.TimeZone

import cn.datashoe.sparkBase.TestSparkAPP
import org.scalatest.FunSuite

class TimeInSpark extends FunSuite{
  test("看下为什么不同excutor不同的展示") {
    import java.sql.Timestamp
    import java.util.Date

    val sqlc = TestSparkAPP.sqlc

    import org.joda.time.DateTime

    val data = Array(
      22.93,
      15.45,
      12.61,
      12.84,
      15.38,
      13.43,
      11.58,
      15.1,
      14.87,
      14.9,
      15.22,
      16.11,
      18.65,
      17.75,
      18.3,
      18.68,
      19.44,
      20.07,
      21.34,
      20.31,
      19.53,
      19.86,
      18.85,
      17.27,
      17.13,
      16.8,
      16.2,
      17.86,
      17.42,
      16.53,
      15.5,
      15.52,
      14.54,
      13.77,
      14.14,
      16.38,
      18.02,
      17.94,
      19.48,
      21.07,
      20.12,
      20.05,
      19.78,
      18.58,
      19.59,
      20.1,
      19.86,
      21.1,
      22.86,
      22.11,
      20.39,
      18.43,
      18.2,
      16.7,
      18.45,
      27.31,
      33.51,
      36.04,
      32.33,
      27.28,
      25.23,
      20.48,
      19.9,
      20.83,
      21.23,
      20.19,
      21.4,
      21.69,
      21.89,
      23.23,
      22.46,
      19.5,
      18.79,
      19.01,
      18.92,
      20.23,
      20.98,
      22.38,
      21.77,
      21.34,
      21.88,
      21.68,
      20.34,
      19.41,
      19.03,
      20.09,
      20.32,
      20.25,
      19.95,
      19.09,
      17.89,
      18.01,
      17.5,
      18.15,
      16.61,
      14.51,
      15.03,
      14.78,
      14.68,
      16.42,
      17.89,
      19.06,
      19.65,
      18.38,
      17.45,
      17.72,
      18.07,
      17.16,
      18.04,
      18.57,
      18.54,
      19.9,
      19.74,
      18.45,
      17.32,
      18.02,
      18.23,
      17.43,
      17.99,
      19.03,
      18.85,
      19.09,
      21.33,
      23.5,
      21.16,
      20.42,
      21.3,
      21.9,
      23.97,
      24.88,
      23.7,
      25.23,
      25.13,
      22.18,
      20.97,
      19.7,
      20.82,
      19.26,
      19.66,
      19.95,
      19.8,
      21.32,
      20.19,
      18.33,
      16.72,
      16.06,
      15.12,
      15.35,
      14.91,
      13.72,
      14.17,
      13.47,
      15.03,
      14.46,
      13,
      11.35,
      12.51,
      12.01,
      14.68,
      17.31,
      17.72,
      17.92,
      20.1,
      21.28,
      23.8,
      22.69,
      25,
      26.1,
      27.26,
      29.37,
      29.84,
      25.72,
      28.79,
      31.82,
      29.7,
      31.26,
      33.88,
      33.11,
      34.42,
      28.44,
      29.59,
      29.61,
      27.24,
      27.49,
      28.63,
      27.6,
      26.42,
      27.37,
      26.2,
      22.17,
      19.64,
      19.39,
      19.71,
      20.72,
      24.53,
      26.18,
      27.04,
      25.52,
      26.97,
      28.39,
      29.66,
      28.84,
      26.35,
      29.46,
      32.95,
      35.83,
      33.51,
      28.17,
      28.11,
      30.66,
      30.75,
      31.57,
      28.31,
      30.34,
      31.11,
      32.13,
      34.31,
      34.68,
      36.74,
      36.75,
      40.27,
      38.02,
      40.78,
      44.9,
      45.94,
      53.28,
      48.47,
      43.15,
      46.84,
      48.15,
      54.19,
      52.98,
      49.83,
      56.35,
      58.99,
      64.98,
      65.59,
      62.26,
      58.32,
      59.41,
      65.48
    )

    /** 将数据添加上时间标签 起始时间是1986.01.01 按月数据 */
//    val startTime = new DateTime(1986, 1, 1, 0, 0, 0)
//    var i = -1
//
//    val millis = data.map {
//      _ =>
//        i += 1
//        startTime.plusMonths(i).getMillis
//    }


//    ts.foreach(println)

    val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")

    val dateTime = new DateTime(2019, 8, 1, 0 ,0 ,0)
    println(dateTime.getMillis)

    // chardet

    // 631123200000L +08
    // 644166000000L +09
    // 因为是夏令时
    val dt = new Date(1564588800000L)
    println(sdf.format(dt), dt.getTimezoneOffset)
    val dt2 = new java.sql.Date(644166000000L)
    println(sdf.format(dt2), dt2.getTimezoneOffset)
    val dt3 = new Timestamp(644166000000L)
    println(sdf.format(dt3), dt3.getTimezoneOffset)
    //

//    println(dateTime.plusMonths(5).getMillis)
//    Array.range(0, 280).foreach {
//      i =>
//        // 644166000000L
//        val dt = new Date(644166000000L)
//        println(sdf.format(dt), dt.getTimezoneOffset)
//    }


//    val newDataFrame = sqlc.createDataFrame(ts).repartition(1).toDF("time", "price")

//    newDataFrame.cache()
//    newDataFrame.createOrReplaceTempView("<#rddtablename#>")
//    outputrdd.put("<#rddtablename#>", newDataFrame)

  }



  test("字符编码") {
    // chardet
  }

}
