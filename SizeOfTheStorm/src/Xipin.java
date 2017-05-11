import bolt.SumBolt;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import spout.XSpout;
import util.LocalSubmitter;

/* Example execution
 *
 * # Runs in local mode (LocalCluster), with topology name "Xipin"
 * $ storm jar SizeOfTheStorm-1.0.jar Xipin
 *
 * # Runs in local mode (LocalCluster), with topology name "foobar"
 * $ storm jar SizeOfTheStorm-1.0.jar Xipin foobar
 *
 * # Runs in local mode (LocalCluster), with topology name "foobar"
 * $ storm jar SizeOfTheStorm-1.0.jar Xipin foobar local
 *
 * # Runs in remote/cluster mode, with topology name "production-topology"
 * $ storm jar SizeOfTheStorm-1.0.jar Xipin production-topology remote
 *
 */

public class Xipin {
	private static final int SECOND = 1000;
	private static final int MINUTE = SECOND * 60;

	public static void main(String[] args) throws Exception {
		System.out.println("***************** Xipin Started *****************");

		String topologyName = "Xipin";
		if (args.length >= 1) 
			topologyName = args[0];

		boolean runLocally = true;
		if (args.length >= 2 && args[1].equalsIgnoreCase("remote")) 
			runLocally = false;

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("xlarge", new XSpout());
		builder.setBolt("sum", new SumBolt()).shuffleGrouping("xlarge");

		Config conf = new Config();
		conf.setDebug(true);

		if (runLocally) 
			LocalSubmitter.submitTopology(topologyName, conf, builder.createTopology(), SECOND * 20);
		else 
			StormSubmitter.submitTopologyWithProgressBar(topologyName, conf, builder.createTopology());
		
	}
}
