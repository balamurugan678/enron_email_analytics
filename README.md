spark-mail
==========

# Overview
The Spark Mail project contains code for a tutorial on how to use Apache Spark to analyze email data. That data comes from two different sources:

Tutorial on parsing Enron email to Avro and then explore the email set using Spark.

# Building the project

```
mvn clean install
```


# ETL (Extract Transform Load)
The Enron file set has over 500,000 files.

I parsed the input once and aggregate the emails into the following [MailRecord format in Avro IDL]

```
@version("1.0.0")
@namespace("com.uebercomputing.mailrecord")
protocol MailRecordProtocol {

  record Attachment {
    string mimeType;
    bytes data;
  }

  record MailRecord {
    string uuid;
    string from;
    union{null, array<string>} to = null;
    union{null, array<string>} cc = null;
    union{null, array<string>} bcc = null;
    long dateUtcEpoch;
    string subject;
    union{null, map<string>} mailFields = null;
    string body;
    union{null, array<Attachment>} attachments = null;
  }
}
```



### Parsing Enron Email set into Apache Avro binary format

This data set at 423MB compressed is small but using the default small files
format to process this via FileInputFormat creates over 500,000 splits to be
processed. By doing some preprocesing and storing all the file artifacts in
Apache Avro records we can make the analytics processing more effective.

We parse out specific headers like Message-ID (uuid), From (from) etc. and store
all the other headers in a mailFields map. We also store the body in its own
field.

The [mailrecord-utils mailparser enronfiles Main class]
allows us to convert the directory/file-based Enron data set into one Avro files
with all the corresponding MailRecord Avro records. To run this class from the
spark-mail root directory after doing a mvn clean install:

```
java -cp parser/target/parser-1.2.0-SNAPSHOT-shaded.jar \
com.uebercomputing.mailparser.enronfiles.AvroMain \
--mailDir /opt/local/datasets/enron/enron_mail_20150507/maildir \
--avroOutput /opt/local/datasets/enron/mail-2015.avro
```

