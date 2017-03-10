package bolt;

import org.apache.storm.task.IBolt;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Hashtable;
import java.util.Map;

public class CountBolt extends BaseRichBolt implements IBolt {
    private OutputCollector collector;
    private Map<Integer, Double> accountsMap;


    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.accountsMap = new Hashtable<Integer, Double>();
    }

    public void execute(Tuple tuple) {
        Integer account = tuple.getInteger(0);
        Double transaction = tuple.getDouble(1);
        System.out.println("-Input to bolt: " + account + "price: " + transaction);
        if(this.accountsMap.containsKey(account)){
            Double currentPrice = this.accountsMap.get(account);
            this.accountsMap.put(account, currentPrice+transaction);
        }
        else{
            this.accountsMap.put(account, transaction);
        }
        collector.ack(tuple);
    }

    @Override
    public void cleanup() {
        System.out.println("Finshed print reults.");
        for(Map.Entry<Integer, Double> entry:accountsMap.entrySet()){
            System.out.println(entry.getKey()+" : " + entry.getValue());
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("int"));
    }
}
