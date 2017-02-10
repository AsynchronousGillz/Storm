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

public class SplitBolt extends BaseRichBolt implements IBolt{
	private OutputCollector collector;

	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	public void execute(Tuple tuple) {
		String sentence = tuple.getString(0);
		for(String word: sentence.split(" ")) {
			this.collector.emit(new Values(word));
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("int"));
	}

}
