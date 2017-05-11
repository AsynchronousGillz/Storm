package spout;

import org.apache.storm.spout.ISpout;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Random;
import java.util.Map;

public class LSpout extends BaseRichSpout implements ISpout {
	public static final int TOTAL_NUMBERS = 1000;
	public static long AVG = 0;
	public static long CON = 0;
	public static long TIM = System.nanoTime();
	public static String NAME;

	private SpoutOutputCollector collector;
	private int index = 0;

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("number"));
	}

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		this.NAME = context.getThisComponentId();
	}

	public void close() {
		System.out.println(">> Average Time: "+this.AVG / this.CON);
	}

	public void nextTuple() {
		Random rand = new Random();
		while (true) {
			this.AVG += System.nanoTime() - this.TIM;
			this.TIM = System.nanoTime();
			this.CON ++;
			System.out.println("<"+this.NAME+"> SPOUT AVG TIME: "+this.AVG / this.CON);
			byte[] b = new byte[1024];
			rand.nextBytes(b);
			collector.emit(new Values(b));
		}
	}

	@Override
	public void ack(Object msgId) {
		System.out.println("ack on msgId" + msgId);
	}

	@Override
	public void fail(Object msgId){
		System.out.println("fail on msgId" + msgId);
	}

}
