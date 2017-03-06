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
import java.io.File;
import java.io.FileNotFoundException;

public class YahooSpout extends BaseRichSpout implements ISpout {
	public static final int TOTAL_NUMBERS = 1000;

	private SpoutOutputCollector collector;
	private String[] companies = generateCompanyList();
	private int index = 0;

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("companies"));
	}

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void nextTuple() {
		if(index < TOTAL_NUMBERS) {
			collector.emit(new Values(companies[index++]));
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

	private static String[] generateCompanyList() {
		String[] companyList = new String[TOTAL_NUMBERS];
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("companylist.csv"));
		} catch (FileNotFoundException e) {}
		for (int i = 0; i < TOTAL_NUMBERS; scanner.hasNextLine()){
			companyList[i++] = scanner.nextLine().split(",")[0];
		}
		scanner.close();
		return companyList;
	}
}
