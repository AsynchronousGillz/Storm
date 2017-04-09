package spout;

import org.apache.storm.spout.ISpout;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

public class AccountSpout extends BaseRichSpout implements ISpout {
	public static final int TOTAL_NUMBERS = 100;
	public static long AVG = 0;
	public static long CON = 0;
	public static long TIM = System.nanoTime();

	private SpoutOutputCollector collector;

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("account", "amount"));
	}

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void close() {
		System.out.println(">> Average Time: "+AVG / CON);
	}

	public void nextTuple() {
		Random rand = new Random();
		while (true) {
			AVG += System.nanoTime - TIM;
			TIM = System.nanoTime;
			CON ++;
			int account = rand.nextInt(TOTAL_NUMBERS);
			double amount = (-1*TOTAL_NUMBERS) + (TOTAL_NUMBERS - (-1*TOTAL_NUMBERS)) * rand.nextDouble();
			collector.emit(new Values(account, amount));
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
