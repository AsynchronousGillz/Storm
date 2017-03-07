package bolt;

import org.apache.storm.task.IBolt;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.HashMap;

import java.util.Map;

public class WordCountBolt extends BaseRichBolt implements IBolt{

	private OutputCollector collector;

	private Map<String, Integer> counts = new HashMap<String, Integer>();

	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	public void execute(Tuple tuple) {
		String word = tuple.getString(0);
		Integer count = counts.get(word);
		if (count == null)
			count = 0;
		counts.put(word, ++count);
	}

	@Override
	public void cleanup() {
		System.out.println("Finshed print reults.");
		for(Map.Entry<String, Integer> entry:counts.entrySet()){
			System.out.println(entry.getKey()+" : " + entry.getValue());
		}
	}


	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("String"));
	}

}
