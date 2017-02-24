package spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;

/**
 * Created by skorrico on 2/24/17.
 */
public class RandomAccountSpout extends BaseRichSpout{

    private static final Logger LOG = LoggerFactory.getLogger(RandomAccountSpout.class);

    SpoutOutputCollector _collector;
    Random _rand;



    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("Integer"));

    }

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        _collector = spoutOutputCollector;
        _rand = new Random();
    }

    public void nextTuple() {
        Utils.sleep(100);
        Integer account = _rand.nextInt(9000);
        LOG.debug("Emitting tuple: {}", account);
        _collector.emit(new Values(account));

    }
}
