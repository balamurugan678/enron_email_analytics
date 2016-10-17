name := "enron_email_analytics"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {

  val sparkVersion = "2.0.0"

  Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "com.jsuereth" %% "scala-arm" % "1.4",
    "org.apache.avro" % "avro" % "1.8.1"
  )

}

seq( sbtavro.SbtAvro.avroSettings : _*)