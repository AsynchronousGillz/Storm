package spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;

/**
 * Created by skorrico on 2/24/17.
 */
public class MoneySpout extends BaseRichSpout{
    private static final Logger LOG = LoggerFactory.getLogger(MoneySpout.class);

    SpoutOutputCollector _collector;
    Random _rand;

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        _collector = spoutOutputCollector;
        _rand = new Random();
    }

    public void nextTuple() {
        Utils.sleep(100);
        Integer Account = -_rand.nextInt(9000);
        Integer Transaction = _rand.nextInt();
        String combo = Account + " " + Transaction;
        LOG.debug("Emitting tuple: {}", combo);
        _collector.emit(new Values(combo));
    }

    @Override
    public void ack(Object id) {
    }

    @Override
    public void fail(Object id) {
    }
}
