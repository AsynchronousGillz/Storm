import bolt.SumBolt;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.BoltDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import spout.AccountSpout;
import util.LocalSubmitter;

/* 
 * Example execution
 *
 * # Runs in local mode (LocalCluster), with topology name "BankStorm"
 * $ storm jar StormOfBanks-1.0.jar BankStorm
 *
 * # Runs in local mode (LocalCluster), with topology name "foobar"
 * $ storm jar StormOfBanks-1.0.jar BankStorm foobar
 *
 * # Runs in local mode (LocalCluster), with topology name "foobar"
 * $ storm jar StormOfBanks-1.0.jar BankStorm foobar local
 *
 * # Runs in remote/cluster mode, with topology name "production-topology"
 * $ storm jar StormOfBanks-1.0.jar BankStorm production-topology remote
 *
 */

public class BankStorm {
	private static final int SECOND = 1000;
	private static final int MINUTE = SECOND * 60;


	public static void main(String[] args) throws Exception {
		System.out.println("***************** BankStorm Started *****************");

		String topologyName = "BankStorm";
		if (args.length >= 1) {
			topologyName = args[0];
		}
		boolean runLocally = true;
		if (args.length >= 2 && args[1].equalsIgnoreCase("remote")) {
			runLocally = false;
		}

		TopologyBuilder builder = new TopologyBuilder();

		for (int i = 0; i < 10; i++) {
			builder.setSpout(""+i, new AccountSpout());
		}

		BoltDeclarer bd = builder.setBolt("S", new SumBolt());
		
		for (int i = 0; i < 10; i++) {
			bd.shuffleGrouping(""+i);
		}

		Config conf = new Config();
		conf.setDebug(true);

		if (runLocally) {
			LocalSubmitter.submitTopology(topologyName, conf, builder.createTopology(), SECOND * 20);
		} else {
			StormSubmitter.submitTopologyWithProgressBar(topologyName, conf, builder.createTopology());
		}
	}
}
