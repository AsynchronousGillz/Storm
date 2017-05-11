package bolt;

import org.apache.storm.task.IBolt;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class SumBolt extends BaseRichBolt implements IBolt{
	private OutputCollector collector;
	private long AVG = 0;
	private long CON = 0;
	private long TIM = System.nanoTime();
	private String NAME;

	private int runningTotal = 0;

	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.NAME = context.getThisComponentId();
	}

	public void execute(Tuple tuple) {
		this.AVG += System.nanoTime() - this.TIM;
		this.TIM = System.nanoTime();
		this.CON ++;
		System.out.println("<"+this.NAME+"> BOLT AVG TIME: "+(this.AVG / this.CON));
		byte[] array = tuple.getBinary(0);
		collector.ack(tuple);
	}

	public void close() {
		System.out.println("<"+this.NAME+"> BOLT AVG TIME: "+(this.AVG / this.CON));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("sum"));
	}

}
