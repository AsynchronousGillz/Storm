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
import java.util.HashMap;

public class SumBolt extends BaseRichBolt implements IBolt{

	private Map<Integer, Double> map;

	private OutputCollector collector;

	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.map = new HashMap<Integer, Double>();
		this.collector = collector;
	}

	public void execute(Tuple tuple) {
		Integer account = tuple.getInteger(0);
		Double ammount = tuple.getDouble(1);
		if (this.map.containsKey(account))
			ammount += this.map.get(account);
		this.map.put(account, ammount);
		collector.ack(tuple);
	}

	@Override
	public void cleanup() {
		System.out.println("Finished print results.");
		for(Map.Entry<Integer, Double> entry : map.entrySet()){
			System.out.println("Account: "+entry.getKey()+" : "+entry.getValue());
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("sum"));
	}

}
