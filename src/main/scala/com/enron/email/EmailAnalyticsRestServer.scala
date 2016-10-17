package com.enron.email

import java.io.File

import com.enron.mailrecord.MailRecord

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Bala.
  */
object EmailAnalyticsRestServer extends App {

  val conf = new SparkConf().setAppName("SparkMe Application").setMaster("local[*]")
  val sc = new SparkContext(conf)

  val lines = sc.textFile("C:\\Balamurugan\\AfterSales\\Logs\\sample_logs\\*").cache

  val c = lines.count()
  println(s"Total lines are ${c}")
  val splits = lines.flatMap(line => line.split(" ")).map(word => (word, 1))
  val counts = splits.reduceByKey((x, y) => x + y)
  /*splits.saveAsTextFile("C:\\Balamurugan\\AfterSales\\Logs\\SplitOutput")
  counts.saveAsTextFile("C:\\Balamurugan\\AfterSales\\Logs\\CountOutput")*/
  val wordCounts = lines.flatMap(line => line.split(" ")).collect().size
  println(s"Word count is ${wordCounts} in this folder")

  val mails = new MailRecord()
  mails.getBody
  println(s"There are ${counts} words in this folder")

  val bodyLinesRdd: RDD[String] = lines.flatMap { body => body.split("\n") }
  val bodyWordsRdd: RDD[String] = bodyLinesRdd.flatMap { line => line.split("""\W+""") }

  val stopWords = List("in", "it", "let", "no", "or", "the")
  val wordsRdd = bodyWordsRdd.filter(!stopWords.contains(_))

  println(s"There were ${wordsRdd.count()} words.")


  val dir = new File("C:\\Balamurugan\\AfterSales\\Logs\\sample_logs\\")
  val files = dir.listFiles.filter(_.isFile).toList.size
  println(s"Files are ${files}")
}
