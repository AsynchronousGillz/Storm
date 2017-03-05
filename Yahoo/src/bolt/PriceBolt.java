package bolt;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

public class PriceBolt implements IRichBolt {
	final private Map<String, Integer> priceMap;
	final private Map<String, Boolean> resultMap;

	private OutputCollector collector;

	@Override
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.priceMap = new HashMap <String, Double>();
		this.resultMap = new HashMap<String, Double>();
		this.collector = collector;
	}

	@Override
	public void execute(Tuple tuple) {
		String company = tuple.getString(0);
		Double price = tuple.getDouble(1);
		System.out.println(" - Input to bolt: "+company+" price: "+ price);
		if (this.priceMap.containsKey(company)) {
			Integer cutOffPrice = this.priceMap.get(company);
			if (price < cutOffPrice)
				this.resultMap.put(company, price);
		} else {
			this.resultMap.put(company, price);
		}
		collector.ack(tuple);
	}

	@Override
	public void cleanup() {
		System.out.println("Finshed print reults.");
		for(Map.Entry<String, Boolean> entry:resultMap.entrySet()){
			System.out.println(entry.getKey()+" : " + entry.getValue());
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("cut_off_price"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
