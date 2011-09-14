import java.util.List;
import java.util.Random;

public class MaxFlowTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int V = 2000;
		int E = 2000000;
		Random rnd = new Random(1);
		List<Integer>[] g = RandomGraph.getRandomUndirectedConnectedGraph(V, E, rnd);
		MaxFlowDinic f1 = new MaxFlowDinic();
		MaxFlowDinic2 f2 = new MaxFlowDinic2();
		MaxFlowDinic3 f3 = new MaxFlowDinic3();

		System.out.println("starting");

		f1.init(V, E * 4);
		f2.init(V);
		f3.init(V);

		for (int i = 0; i < g.length; i++) {
			for (int j : g[i]) {
				int cap = rnd.nextInt(100) + 1;
				f1.addEdge(i, j, cap, 0);
				f2.addEdge(i, j, cap);
				f3.addEdge(i, j, cap);
			}
		}

		long time = System.currentTimeMillis();
		int flow1 = f1.maxFlow(0, V - 1);
		System.out.println(System.currentTimeMillis() - time);

		time = System.currentTimeMillis();
		int flow2 = f2.maxFlow(0, V - 1);
		System.out.println(System.currentTimeMillis() - time);

		time = System.currentTimeMillis();
		int flow3 = f3.maxFlow(0, V - 1);
		System.out.println(System.currentTimeMillis() - time);

		System.out.println(flow1 + " " + flow2 + " " + flow3);
	}
}
