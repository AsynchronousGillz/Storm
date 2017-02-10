# Storm
---

Apache Storm is a distributed real-time big data-processing system.

Storm is designed to process vast amount of data in a fault-tolerant and horizontal scalable method. It is a streaming data framework that has the capability of highest ingestion rates. Though Storm is stateless, it manages distributed environment and cluster state via Apache ZooKeeper. It is simple and you can execute all kinds of manipulations on real-time data in parallel.

Apache Storm is continuing to be a leader in real-time data analytics. Storm is easy to setup, operate and it guarantees that every message will be processed through the topology at least once.
Apache Storm vs Hadoop

Basically Hadoop and Storm frameworks are used for analyzing big data. Both of them complement each other and differ in some aspects. Apache Storm does all the operations except persistency, while Hadoop is good at everything but lags in real-time computation. The following table compares the attributes of Storm and Hadoop.

## Places to Learn

+ [Udacity Class](https://classroom.udacity.com/courses/ud381/lessons/2731858540/concepts/34138986130923)
+ Getting Started With Storm By, Jonathan Leibiusky Gabriel

## Storm vs Hadoop

### Storm

+ Real-time stream processing
+ Stateless
+ Master/Slave architecture with ZooKeeper based coordination. The master node is called as nimbus and slaves are supervisors.
+ A Storm streaming process can access tens of thousands messages per second on cluster.
+ Storm topology runs until shutdown by the user or an unexpected unrecoverable failure.

### Hadoop

+ Batch processing
+ Stateful
+ Master-slave architecture with/without ZooKeeper based coordination. Master node is job tracker and slave node is task tracker.
+ Hadoop Distributed File System (HDFS) uses MapReduce framework to process vast amount of data that takes minutes or hours.
+ MapReduce jobs are executed in a sequential order and completed eventually.

## Both are distributed and fault-tolerant

+ If nimbus / supervisor dies, restarting makes it continue from where it stopped, hence nothing gets affected.
+ If the JobTracker dies, all the running jobs are lost.

## Storm ACK

A Storm topology has a set of special "acker" tasks that track the DAG of tuples for every spout tuple. When an acker sees that a DAG is complete, it sends a message to the spout task that created the spout tuple to ack the message. 

You can set the number of acker tasks for a topology in the topology configuration using `Config.TOPOLOGY_ACKERS`. Storm defaults `TOPOLOGY_ACKERS` to one task per worker.

## Use-Cases of Apache Storm

Apache Storm is very famous for real-time big data stream processing. For this reason, most of the companies are using Storm as an integral part of their system.

+ Twitter 
+ NaviSite
+ Wego

## Apache Storm Benefits

+ Storm is open source, robust, and user friendly. It could be utilized in small companies as well as large corporations.
+ Storm is fault tolerant, flexible, reliable, and supports any programming language.
+ Allows real-time stream processing.
+ Storm is unbelievably fast because it has enormous power of processing the data.
+ Storm can keep up the performance even under increasing load by adding resources linearly. It is highly scalable.
+ Storm performs data refresh and end-to-end delivery response in seconds or minutes depends upon the problem. It has very low latency.
+ Storm has operational intelligence.
+ Storm provides guaranteed data processing even if any of the connected nodes in the cluster die or messages are lost.

