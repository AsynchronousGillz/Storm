package spout;

import org.apache.storm.spout.ISpout;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.Scanner;

public class YahooSpout extends BaseRichSpout implements ISpout {
	public static final int TOTAL_NUMBERS = 1000;

	private SpoutOutputCollector collector;
	private String[] companies = generateCompanyList();
	private int index = 0;

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("number"));
	}

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void nextTuple() {
		if(index < TOTAL_NUMBERS) {
			collector.emit(new Values(numbers[index++]));
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

	private static String[] generateAscendingNumbers() {
		String[] companyList = new String[TOTAL_NUMBERS];
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("doc.csv"));
		} catch (FileNotFoundException e) {}
		scanner.useDelimiter(",");
		for (int i = 0; i < TOTAL_NUMBERS; scanner.hasNext()){
			numberList[i++] = scanner.next();
		}
		scanner.close();
		return numberList;
	}
}
