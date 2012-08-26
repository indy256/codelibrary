import java.util.*;

public class MinCostFlowBF {

	static class Edge {
		int to, f, cap, cost, rev;

		Edge(int v, int cap, int cost, int rev) {
			this.to = v;
			this.cap = cap;
			this.cost = cost;
			this.rev = rev;
		}
	}

	static List<Edge>[] graph;
	static int[] prio, curflow, prevedge, prevnode, q;
	static boolean[] inqueue;
	static int nodes;

	static void init(int n) {
		nodes = n;
		graph = new List[n];
		for (int i = 0; i < n; i++) {
			graph[i] = new ArrayList<Edge>();
		}
		prio = new int[n];
		curflow = new int[n];
		prevedge = new int[n];
		prevnode = new int[n];
		q = new int[n];
		inqueue = new boolean[n];
	}

	static void addEdge(int s, int t, int cap, int cost) {
		graph[s].add(new Edge(t, cap, cost, graph[t].size()));
		graph[t].add(new Edge(s, 0, -cost, graph[s].size() - 1));
	}

	static void bellmanFord(int s) {
		Arrays.fill(prio, 0, nodes, Integer.MAX_VALUE);
		prio[s] = 0;
		int qt = 0;
		q[qt++] = s;
		for (int qh = 0; (qh - qt) % nodes != 0; qh++) {
			int u = q[qh % nodes];
			inqueue[u] = false;
			for (int i = 0; i < (int) graph[u].size(); i++) {
				Edge e = graph[u].get(i);
				if (e.cap <= e.f)
					continue;
				int v = e.to;
				int ndist = prio[u] + e.cost;
				if (prio[v] > ndist) {
					prio[v] = ndist;
					prevnode[v] = u;
					prevedge[v] = i;
					curflow[v] = Math.min(curflow[u], e.cap - e.f);
					if (!inqueue[v]) {
						inqueue[v] = true;
						q[qt++ % nodes] = v;
					}
				}
			}
		}
	}

	static int[] minCostFlow(int s, int t, int maxf) {
		int flow = 0;
		int flowCost = 0;
		while (flow < maxf) {
			curflow[s] = Integer.MAX_VALUE;
			bellmanFord(s);
			if (prio[t] == Integer.MAX_VALUE)
				break;
			int df = Math.min(curflow[t], maxf - flow);
			flow += df;
			for (int v = t; v != s; v = prevnode[v]) {
				Edge e = graph[prevnode[v]].get(prevedge[v]);
				e.f += df;
				graph[v].get(e.rev).f -= df;
				flowCost += df * e.cost;
			}
		}
		return new int[] { flow, flowCost };
	}

	// Usage example
	public static void main(String[] args) {
		int[][] capacity = { { 0, 3, 2 }, { 0, 0, 2 }, { 0, 0, 0 } };
		int n = capacity.length;
		init(n);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (capacity[i][j] != 0)
					addEdge(i, j, capacity[i][j], 1);
		int s = 0;
		int t = 2;
		int[] res = minCostFlow(s, t, Integer.MAX_VALUE);
		int flow = res[0];
		int flowCost = res[1];
		System.out.println(4 == flow);
		System.out.println(6 == flowCost);
	}
}
