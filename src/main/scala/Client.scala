import Utils.{getData, sendData}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import java.nio.file.{Files, Paths}
import java.util.TimeZone

object Client extends App{
  val spark = SparkSession.builder()
    .master("local")
    .appName("client")
    .enableHiveSupport()
    .config("spark.dynamicAllocation.enabled", "false")
    .config("spark.yarn.maxAppAttempts", "1")
    .config("spark.sql.warehouse.dir", "file:///c:/tmp/spark-warehouse")
    .getOrCreate()

  spark.conf.set("spark.sql.session.timeZone", "UTC")
  TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
  spark.conf.set("spark.sql.hive.caseSensitiveInferenceMode", "INFER_ONLY")

  val siteName = "http://localhost:9999/"

  replicationAllDataToLocal()
  addNewPersonToTable()

  def replicationAllDataToLocal()={
    Files.write(Paths.get("temp.txt"), getData(siteName, "/all_data/").getBytes())

    val df = spark.read.json("temp.txt").withColumn("processed_dttm", current_timestamp())
    df.write.mode("append").csv("AllData")
  }

  def addNewPersonToTable() = {
    val source = scala.io.Source.fromFile("data.txt")
    val name = try source.mkString finally source.close()
    sendData(name, siteName,"/add_person/")
  }

}
