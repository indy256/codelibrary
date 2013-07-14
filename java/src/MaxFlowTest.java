import obsolete.MaxFlowDinicMatrix;

import java.util.List;
import java.util.Random;

public class MaxFlowTest {

	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 1000; step++) {

			int V = 300000;
			int E = 3000000;
			int EE = 26;
			List<Integer>[] g = RandomGraph.getRandomUndirectedConnectedGraph2(V, E, rnd);
//			MaxFlowDinicMatrix f2 = new MaxFlowDinicMatrix();
//			MaxFlowPreflowN3 f3 = new MaxFlowPreflowN3();
			MaxFlowRetreat f4 = new MaxFlowRetreat();

//			System.out.println("starting");

			final List<MaxFlowDinic.Edge>[] g1 = MaxFlowDinic.createGraph(V);
//			f2.init(V);
//			f3.init(V);
			f4.init(V);
			final List<MaxFlowDinicLinkCut.Edge>[] g5 = MaxFlowDinicLinkCut.createGraph(V);

//			System.out.println();
			int cnt = 0;
			for (int i = 0; i < g.length; i++) {
				for (int j : g[i]) {
					int cap = rnd.nextInt(100);
					MaxFlowDinic.addEdge(g1, i, j, cap);
//					f2.addEdge(i, j, cap);
//					f3.addEdge(i, j, cap);
					f4.addEdge(i, j, cap);
//					if (cap > 0)
					{
						++cnt;
						MaxFlowDinicLinkCut.addEdge(g5, i, j, cap);
//						System.out.println(i + " " + j + " " + cap);
					}
				}
			}
//			if (cnt > EE) continue;

			long time = System.currentTimeMillis();
			int flow1 = MaxFlowDinic.maxFlow(g1, 0, V - 1);
			System.out.println(System.currentTimeMillis() - time);

			time = System.currentTimeMillis();
			int flow2 = -1;//f2.maxFlow(0, V - 1);
//			System.out.println(System.currentTimeMillis() - time);

			time = System.currentTimeMillis();
			int flow3 = -1;//f3.maxFlow(0, V - 1);
//			System.out.println(System.currentTimeMillis() - time);

			time = System.currentTimeMillis();
			int flow4 = f4.maxFlow(0, V - 1);
			System.out.println(System.currentTimeMillis() - time);

			time = System.currentTimeMillis();
			int flow5 = MaxFlowDinicLinkCut.maxFlow(g5, 0, V - 1);
			System.out.println(System.currentTimeMillis() - time);

//			if (flow1 != flow5)
			{
				System.out.println(flow1 + " " + flow2 + " " + flow3 + " " + flow4 + " " + flow5);
//				return;
			}
			if (flow1 != flow5) {
				return;
			}
		}
	}
}
