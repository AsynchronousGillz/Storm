package spout;

import org.apache.storm.spout.ISpout;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

//import yahoofinace packages
import yahoofinance.YahooFinance;
import yahoofinance.Stock;

import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

public class YahooSpout extends BaseRichSpout implements ISpout {
	public static final int TOTAL_NUMBERS = 1000;

	private SpoutOutputCollector collector;
	private String[] companies = generateCompanyList();
	private int index = 0;

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("company", "price"));
	}

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void nextTuple() {
		if(index < TOTAL_NUMBERS) {
			try {
				String name = companies[index++];
				BigDecimal price = YahooFinance.get(name).getQuote().getPrice();
				System.out.println("[ SPOUT INFO ] nextTuple: "+name+" -> "+price);
				collector.emit(new Values(name, price.doubleValue()));
			} catch(Exception e) {}
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
			scanner = new Scanner(new File("/s/bach/g/under/ganvana/reu/Storm-Projects/Yahoo/companylist.csv"));
		} catch (FileNotFoundException e) {}
		for (int i = 0; i < TOTAL_NUMBERS && scanner.hasNextLine() == true;){
			companyList[i++] = scanner.nextLine().split(",")[0];
		}
		scanner.close();
		return companyList;
	}
}
