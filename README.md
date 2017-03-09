# Storm

Apache Storm is a distributed real-time big data-processing system.

> Storm integrates with any queueing system and any database system. Storm's spout abstraction makes it easy to integrate a new queuing system. Likewise, integrating Storm with database systems is easy.

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

## At least once, At Most once, Exactly Once

### Exactly
To have exactly once semantics use the Trident API. 

> Trident is a high-level abstraction for doing realtime computing on top of Storm. It allows you to seamlessly intermix high throughput (millions of messages per second), stateful stream processing with low latency distributed querying. If you're familiar with high level batch processing tools like Pig or Cascading, the concepts of Trident will be very familiar – Trident has joins, aggregations, grouping, functions, and filters. In addition to these, Trident adds primitives for doing stateful, incremental processing on top of any database or persistence store. Trident has consistent, exactly-once semantics, so it is easy to reason about Trident topologies.

### At least once
In some cases, like with a lot of analytics, dropping data is OK so disabling the fault tolerance by setting the number of acker bolts to 0 Config.TOPOLOGY_ACKERS. 

### At most once
Last you want to be sure that everything was processed at least once and nothing was dropped. This is especially useful if all operations are idenpotent or if deduping can happen aferwards.

## Example

```
Consider a list of 2000 statements:

1) Exactly one statement on this list is false. 
2) Exactly two statements on this list are false. 
3) Exactly three statements on this list are false. 
. . . 
2000) Exactly 2000 statements on this list are false. 
```

**Which statements are true and which are false?**

**What happens if you replace "exactly" with "at least"?**

**What happens if you replace "exactly" with "at most"?**

**What happens in all three cases if you replace "false" with "true"?**

> Note: "The 'exactly . . . false' problem was posed by David L. Silverman for 1969 statements in the January, 1969 issue of the Journal of Recreational Mathematics. I got the problem from Martin Gardner's "Knotted Doughnuts and Other Mathematical Entertainments", where he discusses it and some of the variants above." - Paul Sinclair

---

The sentence 'x is at least 5' means that the least x is allowed to be is 5;
it can be 5, or any number greater than 5.
So, the phrase 'x is at least 5' means 'x≥5'.

The sentence 'x is at most 10' means that the most x is allowed to be is 10; it can be 10, or any number less than 10. So, the phrase 'x is at most 10' means 'x≤10'.

#### More generally:
| sentence | meaning of sentence | equivalent sentence |
|---|---|---|
| x | is at least k | the least x is allowed to be is k; it can be k or any number greater than k | x ≥ k |
| x | is at most k the most x is allowed to be is k; it can be k or any number less than k | x ≤ k |

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

