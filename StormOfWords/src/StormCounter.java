import bolt.AdderBolt;
import bolt.SubtractorBolt;
import bolt.SumBolt;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import spout.NumberSpout;
import util.LocalSubmitter;

public class StormCounter {
    private static final int SECOND = 1000;
    private static final int MINUTE = SECOND * 60;

    /* Example execution
    *
    * # Runs in local mode (LocalCluster), with topology name "StormCounter"
    * $ storm jar StormOfWords-1.0.jar StormBreaker
    *
    * # Runs in local mode (LocalCluster), with topology name "foobar"
    * $ storm jar StormOfWords-1.0.jar StormBreaker foobar
    *
    * # Runs in local mode (LocalCluster), with topology name "foobar"
    * $ storm jar StormOfWords-1.0.jar StormBreaker foobar local
    *
    * # Runs in remote/cluster mode, with topology name "production-topology"
    * $ storm jar StormOfWords-1.0.jar StormBreaker production-topology remote
    *
    *
    * */
    public static void main(String[] args) throws Exception {
        System.out.println("***************** StormCounter Started *****************");

        String topologyName = "StormCounter";
        if (args.length >= 1) {
            topologyName = args[0];
        }
        boolean runLocally = true;
        if (args.length >= 2 && args[1].equalsIgnoreCase("remote")) {
            runLocally = false;
        }

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("word", new RandomWordSpout());
        builder.setBolt("split", new SplitBolt()).shuffleGrouping("word");
        builder.setBolt("count", new CountBolt()).shuffleGrouping("split");

        Config conf = new Config();
        conf.setDebug(false);

        if (runLocally) {
            LocalSubmitter.submitTopology(topologyName, conf, builder.createTopology(), SECOND * 20);
        } else {
            StormSubmitter.submitTopologyWithProgressBar(topologyName, conf, builder.createTopology());
        }
    }
}
