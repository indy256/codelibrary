import obsolete.MaxFlowDinicMatrix;

import java.util.List;
import java.util.Random;

public class MaxFlowTest {

	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 10000; step++) {

			int V = rnd.nextInt(100)+2;
			int E = rnd.nextInt(V*(V-1)/2 - (V-2))+V-1;
			int EE = 2;
			List<Integer>[] g = RandomGraph.getRandomUndirectedConnectedGraph(V, E, rnd);
//			MaxFlowDinicMatrix f2 = new MaxFlowDinicMatrix();
//			MaxFlowPreflowN3 f3 = new MaxFlowPreflowN3();

//			System.out.println("starting");

//			final List<MaxFlowDinic2.Edge>[] g0 = MaxFlowDinic2.createGraph(V);
			final List<MaxFlowDinic.Edge>[] g1 = MaxFlowDinic.createGraph(V);
//			f2.init(V);
//			f3.init(V);
			final List<MaxFlowRetreat.Edge>[] g4 = MaxFlowRetreat.createGraph(V);

			final List<MaxFlowDinicLinkCut.Edge>[] g5 = MaxFlowDinicLinkCut.createGraph(V);

			System.out.println(step);
			int cnt = 0;
			for (int i = 0; i < g.length; i++) {
				for (int j : g[i]) {
					int cap = rnd.nextInt(100);
//					MaxFlowDinic2.addEdge(g0, i, j, cap);
					MaxFlowDinic.addEdge(g1, i, j, cap);
//					f2.addEdge(i, j, cap);
//					f3.addEdge(i, j, cap);
					if (cap > 0)
					{
						++cnt;
						MaxFlowRetreat.addEdge(g4, i, j, cap);
						MaxFlowDinicLinkCut.addEdge(g5, i, j, cap);
//						System.out.println(i + " " + j + " " + cap);
					}
				}
			}
//			if (cnt > EE) continue;

			long time = System.currentTimeMillis();
//			int flow0 = MaxFlowDinic2.maxFlow(g0, 0, V - 1);
//			System.out.println(System.currentTimeMillis() - time);

			time = System.currentTimeMillis();
			int flow1 = MaxFlowDinic.maxFlow(g1, 0, V - 1);
//			System.out.println(System.currentTimeMillis() - time);

			time = System.currentTimeMillis();
			int flow2 = -1;//f2.maxFlow(0, V - 1);
//			System.out.println(System.currentTimeMillis() - time);

			time = System.currentTimeMillis();
			int flow3 = -1;//f3.maxFlow(0, V - 1);
//			System.out.println(System.currentTimeMillis() - time);

			time = System.currentTimeMillis();
			int flow4 = MaxFlowRetreat.maxFlow(g4, 0, V - 1);
//			System.out.println(System.currentTimeMillis() - time);

			time = System.currentTimeMillis();
			int flow5 = MaxFlowDinicLinkCut.maxFlow(g5, 0, V - 1);
//			System.out.println(System.currentTimeMillis() - time);

//			if (flow1 != flow5)
			{
//				System.out.println(flow1 + flow4 + " " + flow5);
//				return;
			}
			if (flow1 != flow4 || flow1 != flow5) {
				System.out.println(flow1 +" " + flow4 + " " + flow5);
				return;
			}
		}
	}
}
