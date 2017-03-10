
import bolt.AccountBolt;
import bolt.CountBolt;
import spout.MoneySpout;
import spout.RandomAccountSpout;
import spout.RandomTransactionSpout;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import spout.RandomAccountSpout;
import spout.RandomTransactionSpout;
import util.LocalSubmitter;

public class StormCounter {
    private static final int SECOND = 1000;
    private static final int MINUTE = SECOND * 60;

    /* Example execution
    *
    * # Runs in local mode (LocalCluster), with topology name "StormCounter"
    * $ storm jar StormOfWords-1.0.jar StormBank
    *
    * # Runs in local mode (LocalCluster), with topology name "foobar"
    * $ storm jar StormOfWords-1.0.jar StormBank foobar
    *
    * # Runs in local mode (LocalCluster), with topology name "foobar"
    * $ storm jar StormOfWords-1.0.jar StormBank foobar local
    *
    * # Runs in remote/cluster mode, with topology name "production-topology"
    * $ storm jar StormOfWords-1.0.jar StormBank production-topology remote
    *
    *
    * */
    public static void main(String[] args) throws Exception {
        System.out.println("***************** StormCounter Started *****************");

        String topologyName = "StormBank";
        if (args.length >= 1) {
            topologyName = args[0];
        }
        boolean runLocally = true;
        if (args.length >= 2 && args[1].equalsIgnoreCase("remote")) {
            runLocally = false;
        }

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("0", new MoneySpout());
        builder.setSpout("1", new MoneySpout());
        builder.setSpout("2", new MoneySpout());
        builder.setSpout("3", new MoneySpout());
        builder.setSpout("4", new MoneySpout());

        builder.setBolt("bolt", new CountBolt(), 10)
                 .shuffleGrouping("0")
                 .shuffleGrouping("1")
                 .shuffleGrouping("2")
                 .shuffleGrouping("3")
                 .shuffleGrouping("4");

        Config conf = new Config();
        conf.setDebug(false);

        if (runLocally) {
            LocalSubmitter.submitTopology(topologyName, conf, builder.createTopology(), SECOND * 20);
        } else {
            StormSubmitter.submitTopologyWithProgressBar(topologyName, conf, builder.createTopology());
        }
    }
}
