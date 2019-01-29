//package com.self.core.KLInfoMethod
//
//import com.self.core.baseApp.myAPP
//import com.self.core.featurePretreatment.utils.Tools
//import org.apache.spark.rdd.RDD
//import com.zzjz.deepinsight.core.{KLMeta, KLInfoTools, LagInfo, LagInfoForPartition}
//
//object KLInfoTest extends myAPP {
//  override def run(): Unit = {
//    println("This is a test file.")
//
//    /** 构造数据 */
//    val metaData = Seq("日期", "工业增加值同比", "轻工业同比", "重工业同比", "工业增加值环比", "轻工业环比", "重工业环比", "M0", "M1", "M2")
//    val rawData = Seq(
//      ("2000年1月", 8.90, 8.70, 9.00, 8.90, 8.70, 9.00, 16093.90, 46570.10, 121220.40),
//      ("2000年2月", 12.00, 11.60, 12.40, 10.40, 10.10, 10.60, 13983.00, 44679.20, 121583.40),
//      ("2000年3月", 11.90, 10.00, 13.40, 10.70, 9.80, 11.50, 13235.40, 45158.45, 122606.82),
//      ("2000年4月", 11.40, 10.40, 12.10, 10.90, 9.90, 11.60, 13675.50, 46319.03, 124121.87),
//      ("2000年5月", 11.50, 9.60, 12.90, 11.00, 9.80, 11.90, 13075.45, 46490.23, 124053.25),
//      ("2000年6月", 12.20, 10.40, 13.50, 11.20, 9.90, 12.20, 13006.04, 48024.40, 126605.33),
//      ("2000年7月", 12.80, 10.60, 14.70, 11.40, 10.00, 12.60, 13156.47, 47803.09, 126323.92),
//      ("2000年8月", 12.80, 10.30, 14.90, 11.60, 10.00, 12.90, 13378.68, 48885.38, 127790.30),
//      ("2000年9月", 12.00, 8.80, 14.40, 11.60, 9.90, 13.00, 13894.69, 50616.89, 130473.84),
//      ("2000年10月", 11.40, 8.90, 13.40, 11.60, 9.80, 13.00, 13589.45, 49952.84, 129522.44),
//      ("2000年11月", 10.60, 7.50, 13.20, 11.50, 9.60, 13.00, 13877.70, 50787.49, 130994.07),
//      ("2000年12月", 10.40, 8.00, 12.40, 11.40, 9.50, 13.00, 14652.65, 53147.15, 134610.26),
//      ("2001年1月", 2.30, 0.50, 3.80, 2.30, 0.50, 3.80, 17018.98, 54406.23, 137543.63),
//      ("2001年2月", 19.00, 15.80, 21.70, 19.00, 15.80, 21.70, 14910.39, 51997.68, 136210.17),
//      ("2001年3月", 12.10, 9.20, 14.40, 11.20, 8.60, 13.40, 14362.12, 53033.36, 138744.46),
//      ("2001年4月", 11.50, 9.00, 13.50, 11.50, 9.00, 13.50, 14622.99, 53261.32, 139949.85),
//      ("2001年5月", 10.20, 8.70, 11.30, 10.20, 8.70, 11.30, 13942.28, 52542.99, 139015.84),
//      ("2001年6月", 10.10, 9.30, 10.70, 11.00, 9.20, 12.70, 13943.44, 55187.36, 147809.67),
//      ("2001年7月", 8.10, 7.10, 8.80, 10.70, 8.90, 12.10, 14071.62, 53502.80, 149228.73),
//      ("2001年8月", 8.10, 7.80, 8.30, 10.40, 8.80, 11.60, 14370.13, 55808.92, 149941.76),
//      ("2001年9月", 9.50, 9.40, 9.50, 10.30, 8.90, 11.40, 15064.60, 56824.00, 151822.60),
//      ("2001年10月", 8.80, 7.50, 9.70, 10.20, 8.80, 11.40, 14484.61, 56114.90, 151497.25),
//      ("2001年11月", 7.90, 6.80, 8.80, 10.00, 8.70, 11.20, 14780.00, 56579.60, 154088.30),
//      ("2001年12月", 8.70, 7.00, 10.00, 9.90, 8.60, 11.10, 15688.80, 59871.59, 158301.92),
//      ("2002年1月", 18.60, 17.40, 19.60, 18.60, 17.40, 19.60, 16725.89, 60576.06, 159639.27),
//      ("2002年2月", 2.70, 3.70, 1.90, 10.90, 10.90, 11.00, 16641.55, 58702.87, 160935.59),
//      ("2002年3月", 10.90, 11.40, 10.50, 10.90, 11.30, 10.60, 15544.63, 59474.83, 164064.57),
//      ("2002年4月", 12.10, 11.50, 12.50, 11.20, 11.40, 11.10, 15864.18, 60461.31, 164570.56),
//      ("2002年5月", 12.90, 12.60, 13.20, 11.60, 11.60, 11.50, 15243.07, 61246.86, 166023.00),
//      ("2002年6月", 12.40, 12.60, 12.20, 11.70, 11.80, 11.60, 15097.35, 63144.00, 169601.24),
//      ("2002年7月", 12.80, 12.10, 13.30, 11.80, 11.80, 11.80, 15357.66, 63487.78, 170851.14),
//      ("2002年8月", 12.70, 12.30, 13.10, 12.00, 11.80, 12.10, 15712.61, 64868.83, 173250.92),
//      ("2002年9月", 13.80, 13.20, 14.30, 12.20, 12.00, 12.30, 16233.58, 66799.76, 176985.21),
//      ("2002年10月", 14.20, 12.30, 15.80, 12.30, 12.00, 12.60, 16014.66, 67100.25, 177294.15),
//      ("2002年11月", 14.50, 12.90, 15.70, 12.40, 12.10, 12.80, 16346.39, 67992.78, 179736.26),
//      ("2002年12月", 14.90, 12.80, 16.80, 12.60, 12.10, 13.10, 17278.03, 70881.79, 185006.97),
//      ("2003年1月", 14.80, 13.10, 16.00, 14.80, 13.10, 16.00, 21244.73, 72405.66, 190545.05),
//      ("2003年2月", 19.80, 15.90, 22.60, 17.50, 14.40, 19.70, 17937.17, 69756.64, 190108.41),
//      ("2003年3月", 16.90, 13.90, 18.80, 17.20, 14.10, 19.20, 17106.50, 71438.82, 194487.30),
//      ("2003年4月", 14.90, 13.30, 15.90, 16.40, 13.90, 18.20, 17441.14, 71321.24, 196130.13),
//      ("2003年5月", 13.70, 12.50, 14.50, 15.90, 13.70, 17.50, 17115.03, 72777.84, 199505.19),
//      ("2003年6月", 16.90, 14.90, 18.20, 16.20, 13.90, 17.80, 16956.89, 75923.23, 204907.42),
//      ("2003年7月", 16.50, 12.70, 19.10, 16.40, 13.90, 18.10, 17362.13, 76152.77, 206193.07),
//      ("2003年8月", 17.10, 13.70, 19.40, 16.50, 13.90, 18.30, 17606.76, 77032.98, 210591.90),
//      ("2003年9月", 16.30, 13.10, 18.40, 16.50, 13.90, 18.40, 18306.36, 79163.88, 213567.13),
//      ("2003年10月", 17.20, 15.30, 18.40, 16.70, 14.10, 18.50, 18250.67, 80267.10, 214469.36),
//      ("2003年11月", 17.90, 16.70, 18.60, 16.80, 14.40, 18.60, 18439.56, 80814.93, 216351.73),
//      ("2003年12月", 18.10, 16.80, 19.00, 17.00, 14.60, 18.60, 19745.99, 84118.57, 221222.82),
//      ("2004年1月", 7.20, 4.90, 9.10, 7.20, 4.90, 9.10, 22287.43, 83805.90, 225101.93),
//      ("2004年2月", 23.20, 19.90, 26.30, 16.60, 13.80, 19.00, 19893.44, 83556.43, 227050.72),
//      ("2004年3月", 19.40, 16.60, 21.90, 17.70, 14.90, 20.10, 19297.43, 85815.57, 231654.60),
//      ("2004年4月", 19.10, 16.60, 21.40, 18.20, 15.40, 20.60, 19878.40, 85603.64, 233627.86),
//      ("2004年5月", 17.50, 16.00, 18.90, 18.10, 15.60, 20.20, 19048.43, 86780.37, 234842.40),
//      ("2004年6月", 16.20, 15.30, 17.40, 17.70, 15.60, 19.70, 19017.58, 88627.14, 238427.49),
//      ("2004年7月", 15.50, 14.40, 17.20, 17.30, 15.20, 19.40, 19409.10, 87982.23, 238126.97),
//      ("2004年8月", 15.90, 15.60, 16.30, 17.10, 15.40, 18.60, 19517.94, 89125.33, 239729.19),
//      ("2004年9月", 16.10, 15.10, 17.20, 17.00, 15.40, 18.50, 20524.17, 90439.05, 243756.88),
//      ("2004年10月", 15.70, 15.10, 16.60, 16.90, 15.50, 18.40, 20078.25, 90782.48, 243740.32),
//      ("2004年11月", 14.80, 13.00, 16.40, 16.80, 15.30, 18.30, 20209.25, 92387.13, 247135.58),
//      ("2004年12月", 14.40, 13.00, 15.70, 16.70, 14.70, 18.20, 21468.30, 95970.82, 253207.70),
//      ("2005年1月", 20.90, 19.90, 21.10, 20.90, 19.90, 21.10, 24015.41, 97079.03, 257708.47),
//      ("2005年2月", 7.60, 7.20, 7.70, 16.90, 17.00, 16.90, 22667.97, 92814.95, 259357.29),
//      ("2005年3月", 15.10, 14.30, 15.40, 16.20, 16.00, 16.30, 21238.95, 94743.19, 264588.94),
//      ("2005年4月", 16.00, 14.90, 16.50, 16.20, 15.70, 16.50, 21666.56, 94593.72, 266992.66),
//      ("2005年5月", 16.60, 14.90, 17.40, 16.30, 15.30, 16.70, 20811.59, 95802.01, 269240.49),
//      ("2005年6月", 16.80, 16.00, 17.20, 16.40, 15.40, 16.90, 20848.76, 98601.25, 275785.53),
//      ("2005年7月", 16.10, 13.90, 17.20, 16.30, 14.90, 16.90, 21171.20, 97674.10, 276966.28),
//      ("2005年8月", 16.00, 15.10, 16.40, 16.30, 14.90, 16.90, 21351.56, 99377.70, 281288.22),
//      ("2005年9月", 16.50, 15.60, 17.00, 16.30, 14.90, 16.90, 22272.92, 100964.00, 287438.27),
//      ("2005年10月", 16.10, 15.40, 16.50, 16.30, 14.80, 17.00, 21892.98, 101751.98, 287591.61),
//      ("2005年11月", 16.60, 16.20, 16.80, 16.40, 15.20, 17.00, 22409.39, 104125.78, 292350.39),
//      ("2005年12月", 16.50, 16.40, 16.50, 16.40, 15.20, 17.00, 24031.67, 107278.76, 298755.67),
//      ("2006年1月", 16.40, 16.60, 16.20, 16.40, 15.20, 17.00, 29310.37, 107250.68, 303571.65),
//      ("2006年2月", 20.10, 18.00, 21.10, 16.20, 14.20, 17.10, 24482.02, 104357.08, 304516.27),
//      ("2006年3月", 17.80, 16.10, 18.60, 16.70, 14.70, 17.60, 23472.03, 106737.08, 310490.65),
//      ("2006年4月", 15.50, 14.20, 16.10, 17.20, 15.20, 18.10, 24155.73, 106389.11, 313702.34),
//      ("2006年5月", 17.90, 15.70, 18.80, 17.00, 15.40, 17.70, 23465.31, 109219.21, 316709.80),
//      ("2006年6月", 19.50, 16.80, 20.80, 17.70, 15.80, 18.50, 23469.08, 112342.36, 322756.35),
//      ("2006年7月", 16.70, 14.00, 18.00, 17.60, 15.70, 18.50, 23752.59, 112653.04, 324010.76),
//      ("2006年8月", 15.70, 12.70, 17.10, 17.30, 15.30, 18.30, 24185.36, 114845.67, 327885.67),
//      ("2006年9月", 16.10, 13.00, 17.60, 17.20, 14.90, 18.20, 25687.38, 116814.10, 331865.36),
//      ("2006年10月", 14.70, 10.70, 16.60, 16.90, 14.50, 18.00, 24964.17, 118359.97, 332747.18),
//      ("2006年11月", 14.90, 11.20, 16.60, 16.80, 14.20, 17.90, 25527.26, 121644.96, 337504.16),
//      ("2006年12月", 14.70, 9.80, 17.00, 16.60, 13.80, 17.90, 27072.62, 126035.13, 345603.59),
//      ("2007年1月", 14.77, 9.67, 17.13, 16.47, 13.47, 17.83, 27949.13, 128484.06, 351498.77),
//      ("2007年2月", 12.60, 10.20, 13.60, 18.50, 15.20, 20.00, 30627.93, 126258.08, 358659.25),
//      ("2007年3月", 17.60, 16.00, 18.30, 18.30, 15.60, 19.60, 27387.95, 127881.31, 364093.66),
//      ("2007年4月", 17.40, 15.40, 18.30, 18.00, 15.60, 19.20, 27813.89, 127678.34, 367326.46),
//      ("2007年5月", 18.10, 16.40, 18.90, 18.10, 15.90, 19.10, 26727.97, 130275.80, 369718.15),
//      ("2007年6月", 19.40, 17.50, 20.20, 18.50, 16.40, 19.50, 26881.10, 135847.41, 377832.15),
//      ("2007年7月", 18.00, 15.90, 18.90, 18.50, 16.20, 19.50, 27326.26, 136237.43, 383884.88),
//      ("2007年8月", 17.50, 14.60, 18.80, 18.40, 16.00, 19.40, 27822.39, 140993.31, 387205.15),
//      ("2007年9月", 18.90, 16.40, 20.00, 18.50, 16.10, 19.60, 29030.58, 142591.57, 393098.91),
//      ("2007年10月", 17.90, 14.40, 19.40, 18.50, 15.90, 19.60, 28317.78, 144649.33, 394204.17),
//      ("2007年11月", 17.30, 14.30, 18.60, 18.50, 16.00, 19.50, 28987.92, 148009.82, 399757.91),
//      ("2007年12月", 17.40, 15.20, 18.40, 18.50, 16.30, 19.60, 30375.23, 152560.08, 403442.21),
//      ("2008年1月", 17.50, 16.10, 18.20, 18.50, 16.60, 19.70, 36673.15, 154870.16, 417818.67),
//      ("2008年2月", 15.40, 11.80, 16.90, 15.40, 13.70, 16.20, 32454.47, 150177.88, 421037.84),
//      ("2008年3月", 17.80, 15.70, 18.70, 16.40, 14.70, 17.30, 30433.07, 150867.47, 423054.53),
//      ("2008年4月", 15.70, 12.10, 17.20, 16.30, 14.00, 17.30, 30789.61, 151694.91, 429313.72),
//      ("2008年5月", 16.00, 13.50, 17.00, 16.30, 13.90, 17.30, 30169.30, 153344.75, 436221.60),
//      ("2008年6月", 16.00, 13.30, 17.10, 16.30, 13.80, 17.30, 30181.32, 154820.15, 443141.02),
//      ("2008年7月", 14.70, 12.20, 15.70, 16.10, 13.50, 17.20, 30687.19, 154992.44, 446362.17),
//      ("2008年8月", 12.80, 11.70, 13.20, 15.70, 13.30, 16.80, 30851.62, 156889.92, 448846.68),
//      ("2008年9月", 11.40, 11.20, 11.50, 15.20, 13.10, 16.00, 31724.88, 155748.97, 452898.70),
//      ("2008年10月", 8.20, 10.30, 7.30, 14.40, 12.80, 15.10, 31317.84, 157194.36, 453133.32),
//      ("2008年11月", 5.40, 10.10, 3.40, 13.70, 12.70, 14.10, 31607.34, 157826.61, 458644.65),
//      ("2008年12月", 5.70, 8.10, 4.70, 12.90, 12.30, 13.20, 34218.96, 166217.13, 475166.60),
//      ("2009年1月", 6.00, 6.10, 6.00, 12.10, 11.90, 12.30, 41082.37, 165214.97, 496136.64),
//      ("2009年2月", 11.00, 14.40, 9.60, 3.80, 6.50, 2.70, 35141.64, 166149.60, 506708.08),
//      ("2009年3月", 8.30, 8.50, 8.30, 5.10, 6.80, 4.50, 33746.42, 176541.13, 530626.71),
//      ("2009年4月", 7.30, 8.20, 6.90, 5.50, 7.00, 4.90, 34257.27, 178213.57, 540481.21),
//      ("2009年5月", 8.90, 9.70, 8.60, 6.30, 7.80, 5.70, 33559.52, 182025.58, 548263.51),
//      ("2009年6月", 10.70, 10.20, 10.90, 7.00, 8.20, 6.60, 33640.98, 193138.15, 568916.20),
//      ("2009年7月", 10.80, 9.20, 11.30, 7.50, 8.10, 7.20, 34239.30, 195889.26, 573102.85),
//      ("2009年8月", 12.30, 9.80, 13.20, 8.10, 8.40, 8.00, 34406.62, 200394.83, 576698.95),
//      ("2009年9月", 13.90, 11.80, 14.80, 8.70, 8.70, 8.70, 36787.89, 201708.14, 585405.34),
//      ("2009年10月", 16.10, 11.30, 18.10, 9.40, 9.00, 9.60, 35730.23, 207545.74, 586643.29),
//      ("2009年11月", 19.20, 12.60, 22.20, 10.30, 9.30, 10.70, 36343.86, 212493.20, 594604.72),
//      ("2009年12月", 18.50, 12.10, 21.40, 11.00, 9.70, 11.50, 38245.97, 220001.51, 606225.01),
//      ("2010年1月", 17.80, 11.60, 20.60, 11.70, 10.10, 12.30, 40758.58, 229588.98, 625609.29),
//      ("2010年2月", 12.80, 7.50, 15.20, 20.70, 14.50, 23.70, 42865.79, 224286.95, 636072.26),
//      ("2010年3月", 18.10, 13.40, 20.00, 19.60, 14.10, 22.10, 39080.58, 229397.93, 649947.46),
//      ("2010年4月", 17.80, 14.10, 19.40, 19.10, 14.10, 21.40, 39657.54, 233909.76, 656561.22),
//      ("2010年5月", 16.50, 13.60, 17.80, 18.50, 14.00, 20.50, 38652.97, 236497.88, 663351.37),
//      ("2010年6月", 13.70, 12.00, 14.50, 17.60, 13.60, 19.40, 38904.85, 240580.00, 673921.72),
//      ("2010年7月", 13.40, 13.50, 13.30, 17.00, 13.60, 18.40, 39543.16, 240664.07, 674051.48),
//      ("2010年8月", 13.90, 13.10, 14.20, 16.60, 13.60, 17.90, 39922.76, 244340.64, 687506.92),
//      ("2010年9月", 13.30, 13.00, 13.40, 16.30, 13.60, 17.50, 41854.41, 243821.90, 696471.50))
//
//    var rawDataFrame = sqlc.createDataFrame(rawData).toDF(metaData: _*).repartition(10)
//
//
//
//    /** 选定一列key作为唯一标识并且添加顺序id */
//    import org.apache.spark.sql.functions.col
//    val hasKey = true
//    if (hasKey) {
//      val keyName = "日期"
//      val judgeValidKey = "true"
//      Tools.columnExists(keyName, rawDataFrame, true)
//      if (judgeValidKey == "true") {
//        rawDataFrame.cache()
//        require(rawDataFrame.select(keyName).distinct().count() == rawDataFrame.count(), "您的顺序标识列不唯一")
//      }
//
//      /** 按key列排序重排序 */
//      val sort = true
//      if (sort) {
//        val order = "desc"
//        val sortCol = if (order == "desc") {
//          col(keyName).desc
//        } else {
//          col(keyName).asc
//        }
//        rawDataFrame = rawDataFrame.orderBy(sortCol)
//      }
//    }
//
//    /** 选定对比变量 */
//    val referenceVariable = "工业增加值同比"
//    val variables = Array("轻工业同比", "重工业同比", "工业增加值环比", "轻工业环比", "重工业环比", "M0", "M1", "M2")
//
//    val numFeatures = variables.length
//
//
//    /** KL进行的范围 */
//    val range = 18
//
//    /** 获得基础处理的数据并缓存 */
//    val rdd: RDD[KLMeta] = rawDataFrame.rdd.map {
//      row =>
//        val observes = variables.map { name =>
//          val value = row.get(row.fieldIndex(name)) match {
//            case s: String => s.toDouble
//            case i: Int => i.toDouble
//            case d: Double => d
//            case f: Float => f.toDouble
//            case l: Long => l.toDouble
//            case _ => throw new Exception("目前只支持string、double、int、float、long类型")
//          }
//          require(value >= 0.0, s"因为KL算法是将两个序列作为分布，因此需要数据大于等于0.0，但您的数据中出现了$value")
//          value
//        }
//
//        val reference = row.get(row.fieldIndex(referenceVariable)) match {
//          case s: String => s.toDouble
//          case i: Int => i.toDouble
//          case d: Double => d
//          case f: Float => f.toDouble
//          case l: Long => l.toDouble
//          case _ => throw new Exception("目前只支持string、double、int、float、long类型")
//        }
//        require(reference >= 0.0, s"因为KL算法是将两个序列作为分布，因此需要数据大于等于0.0，但您的数据中出现了$reference")
//
//        KLMeta(reference, observes)
//    }.repartition(10)
//
//    rdd.cache()
//
//
//    /** 求出各种掐头去尾的和，用于后面的归一化  @return 滞后阶数 -> 求和 */
//    val sumDict: Map[Int, KLMeta] = KLInfoTools.getSum2(rdd, variables.length, Range(-range, range))
//
//    /** 求出每个分区的前后min(range, elementNum)个基准变量的元素 @return 分区id -> 先期滞后信息 */
//    val sidesElements: collection.Map[Int, (LagInfoForPartition, Int)] = rdd.mapPartitionsWithIndex {
//      case (idx, values) =>
//        val arr = values.toArray
//        val takeNum = scala.math.min(arr.length, range)
//        val headerElements = arr.map(_.refResult).take(takeNum)
//        val tailElements = arr.map(_.refResult).takeRight(takeNum)
//        Array((idx, (LagInfoForPartition(headerElements, tailElements), arr.length))).toIterator
//    }.collectAsMap()
//
//    val headTailInfo: LagInfo = sc.broadcast(new LagInfo(sidesElements.mapValues(_._1).map(identity), sidesElements.mapValues(_._2).map(identity), range, range)).value
//
//    /** 获得KL每种先期滞后阶数的每条记录统计信息量 */
//    val klMap = rdd.mapPartitionsWithIndex {
//      case (idx, valuesIte) =>
//        val values = valuesIte.toArray
//        // 掐头去尾留中间，这里按最大的先期滞后变量获取能够进行转换的分区id
//        if (idx > headTailInfo.lastPartitionNumCanLag || idx < headTailInfo.leastPartitionNumCanLead) {
//          var m = Map.empty[Int, Array[Array[Double]]]
//          Array.range(0, range).foreach { i => // todo:这里算法还需要针对每个移动设定一个lastPartitionNumCanLag或leastPartitionNumCanLead
//            if (headTailInfo.numsForPartitions(idx) > i) {
//              val refVal = values.map(_.refResult).dropRight(i)
//              val variables = values.map(_.varResult).drop(i)
//              val sumForIndex = sumDict(0) // 求出对应的求和，注意sliding效果是倒叙
//
//              m += i -> refVal.zip(variables).map {
//                case (ref, other) =>
//                  (KLMeta(ref, other) / sumForIndex).KL // 是一个Array形式，每个位置对应相应变量的KL信息量
//              }
//            }
//          }
//
//
//          //          val u = m.toArray.flatMap { case (key, klValues) => klValues.map(klValue => Map(key -> klValue)) }
//          val u = m.toArray.map {
//            case (key, v) =>
//              Map(key -> (if (v.isEmpty) Array.fill(numFeatures)(0.0) else v.reduce((k1, k2) => k1.zip(k2).map { case (v1, v2) => v1 + v2 })))
//          }
//
//          u.toIterator
//        } else {
//          val refVal = try {
//            headTailInfo.headKAfterPartitionP(idx) ++ values.map(_.refResult) ++ headTailInfo.tailKBeforePartitionP(idx)
//          } catch {
//            case e: Exception => throw new Exception(s"在分区${idx}中获取前后若干元素时失败, 具体信息${e.getMessage}")
//          }
//
//          val variablesVal: Array[Array[Double]] = values.map(_.varResult)
//          val numsForPartitions = headTailInfo.numsForPartitions(idx) // 分区内的元素个数
//
//          /** 先求所有变量的滞后KL：
//            * 参照变量按相反方向移动 -- 先期
//            * 滞后阶数 -> 每条记录[每个特征[KL信息量] ] */
//          val m: Map[Int, Array[Array[Double]]] = refVal.take(numsForPartitions + headTailInfo.rangeForHead - 1).sliding(numsForPartitions).zipWithIndex // 前面
//            .map {
//            case (arr, index) =>
//              val sumForIndex = sumDict(0) // 求出对应的求和，注意sliding效果是倒叙
//              (index, arr.zip(variablesVal).map {
//                case (ref, other) =>
//                  (KLMeta(ref, other) / sumForIndex).KL // 是一个Array形式，每个位置对应相应变量的KL信息量
//              }) // 归一化并求出KL散度
//          }.toMap
//
//          val u = m.toArray.map {
//            case (key, v) =>
//              Map(key -> (if (v.isEmpty) Array.fill(numFeatures)(0.0) else v.reduce((k1, k2) => k1.zip(k2).map { case (v1, v2) => v1 + v2 })))
//          }
//
//          u.toIterator
//        }
//    }
//
//
//    val klMap2 = rdd.mapPartitionsWithIndex {
//      case (idx, valuesIte) =>
//        val values = valuesIte.toArray
//        // 掐头去尾留中间，这里按最大的先期滞后变量获取能够进行转换的分区id
//        if (idx > headTailInfo.lastPartitionNumCanLag || idx < headTailInfo.leastPartitionNumCanLead) {
//          var m = Map.empty[Int, Array[Array[Double]]]
//          Array.range(-range, 0).foreach { negI => // todo:每个partition一个last和least值，一个sum值
//            val i = - negI
//            if (headTailInfo.numsForPartitions(idx) > i) {
//              val refVal = values.map(_.refResult).drop(i)
//              val variables = values.map(_.varResult).dropRight(i)
//              val sumForIndex = sumDict(0) // 求出对应的求和，注意sliding效果是倒叙 @todo: 暂时以全部和求结果
//
//              // @todo: 写出公式 -> 写出算法 -> 写出框架 -> 写出代码
//              m += negI -> refVal.zip(variables).map {
//                case (ref, other) =>
//                  (KLMeta(ref, other) / sumForIndex).KL // 是一个Array形式，每个位置对应相应变量的KL信息量
//              }
//            }
//          }
//
//          //          val u = m.toArray.flatMap { case (key, klValues) => klValues.map(klValue => Map(key -> klValue)) }
//          val u = m.toArray.map {
//            case (key, v) =>
//              Map(key -> (if (v.isEmpty) Array.fill(numFeatures)(0.0) else v.reduce((k1, k2) => k1.zip(k2).map { case (v1, v2) => v1 + v2 })))
//          }
//
//          u.toIterator
//        } else {
//          val refVal = try {
//            values.map(_.refResult) ++ headTailInfo.tailKBeforePartitionP(idx)
//          } catch {
//            case e: Exception => throw new Exception(s"在分区${idx}中获取前后若干元素时失败, 具体信息${e.getMessage}")
//          }
//
//          val variablesVal: Array[Array[Double]] = values.map(_.varResult)
//          val numsForPartitions = headTailInfo.numsForPartitions(idx) // 分区内的元素个数
//
//          /** 先求所有变量的滞后KL：
//            * 参照变量按相反方向移动 -- 先期
//            * 滞后阶数 -> 每条记录[每个特征[KL信息量] ] */
//          val m: Map[Int, Array[Array[Double]]] = refVal.sliding(numsForPartitions).zipWithIndex // 前面
//            .map {
//            case (arr, index) =>
//              val sumForIndex = sumDict(0) // 求出对应的求和，注意sliding效果是倒叙
//              (- index, arr.zip(variablesVal).map {
//                case (ref, other) =>
//                  (KLMeta(ref, other) / sumForIndex).KL // 是一个Array形式，每个位置对应相应变量的KL信息量
//              }) // 归一化并求出KL散度
//          }.toMap
//
//          val u = m.toArray.map {
//            case (key, v) =>
//              Map(key -> (if (v.isEmpty) Array.fill(numFeatures)(0.0) else v.reduce((k1, k2) => k1.zip(k2).map { case (v1, v2) => v1 + v2 })))
//          }
//
//          u.toIterator
//        }
//    }
//
//
//
//    val klResult1: Map[Int, Array[Double]] = klMap.reduce {
//      case (map1, map2) =>
//        var u = map1
//        map2.foreach {
//          case (key, value) =>
//            if(u contains key)
//              u.updated(key, u(key).zip(value).map { case (v1, v2) => v1 + v2 })
//            else
//              u += (key -> value)
//        }
//
//        u
//    }
//
//
//    val klResult2: Map[Int, Array[Double]] = klMap2.reduce {
//      case (map1, map2) =>
//        var u = map1
//        map2.foreach {
//          case (key, value) =>
//            if(u contains key)
//              u.updated(key, u(key).zip(value).map { case (v1, v2) => v1 + v2 })
//            else
//              u += (key -> value)
//        }
//
//        u
//    }
//
//
//    val pp = klResult1 ++ klResult2
//
//    case class FeatureLag(feature: Int, lag: Int)
//
//    val uu = pp.flatMap {
//      case (lag, value) =>
//        value.zipWithIndex.map {
//          case (v, featureId) => (featureId, lag, v)
//        }
//    }.toArray
//    val uuu =  uu.groupBy(_._1).mapValues(value => value.minBy(_._3)._2) // 求出每个feature对应的最优滞后阶数
//    uuu.foreach(println)
//
//
//  }
//}