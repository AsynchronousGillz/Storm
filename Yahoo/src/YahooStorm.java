import bolt.PriceBolt;
import spout.YahooSpout;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import util.LocalSubmitter;

public class YahooStorm {
    private static final int SECOND = 1000;
    private static final int MINUTE = SECOND * 60;

    /* Example execution
    *
    * # Runs in local mode (LocalCluster), with topology name "StormYahoo"
    * $ storm jar YahooStorm-1.0.jar StormYahoo
    *
    * # Runs in local mode (LocalCluster), with topology name "foobar"
    * $ storm jar YahooStorm-1.0.jar StormYahoo foobar
    *
    * # Runs in local mode (LocalCluster), with topology name "foobar"
    * $ storm jar YahooStorm-1.0.jar StormYahoo foobar local
    *
    * # Runs in remote/cluster mode, with topology name "production-topology"
    * $ storm jar YahooStorm-1.0.jar StormYahoo production-topology remote
    *
    *
    * */
    public static void main(String[] args) throws Exception {
        System.out.println("***************** StormYahoo Started *****************");

        String topologyName = "StormYahoo";
        if (args.length >= 1) {
            topologyName = args[0];
        }
        boolean runLocally = true;
        if (args.length >= 2 && args[1].equalsIgnoreCase("remote")) {
            runLocally = false;
        }

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("yahoo", new YahooSpout());
        builder.setBolt("price", new PriceBolt()).shuffleGrouping("yahoo");

        Config conf = new Config();
        conf.setDebug(true);

        if (runLocally) {
            LocalSubmitter.submitTopology(topologyName, conf, builder.createTopology(), SECOND * 20);
        } else {
            StormSubmitter.submitTopologyWithProgressBar(topologyName, conf, builder.createTopology());
        }
    }
}
