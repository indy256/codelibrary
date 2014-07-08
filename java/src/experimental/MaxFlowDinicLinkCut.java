package experimental;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaxFlowDinicLinkCut {

	public static class Edge {
		int t, rev, cap, f;

		public Edge(int t, int rev, int cap) {
			this.t = t;
			this.rev = rev;
			this.cap = cap;
		}
	}

	public static List<Edge>[] createGraph(int nodes) {
		List<Edge>[] graph = new List[nodes];
		for (int i = 0; i < nodes; i++)
			graph[i] = new ArrayList<>();
		return graph;
	}

	public static void addEdge(List<Edge>[] graph, int s, int t, int cap) {
		graph[s].add(new Edge(t, graph[t].size(), cap));
		graph[t].add(new Edge(s, graph[s].size() - 1, 0));
	}

	static boolean dinicBfs(List<Edge>[] graph, int src, int dest, int[] dist) {
		Arrays.fill(dist, -1);
		dist[src] = 0;
		int[] Q = new int[graph.length];
		int sizeQ = 0;
		Q[sizeQ++] = src;
		for (int i = 0; i < sizeQ; i++) {
			int u = Q[i];
			for (Edge e : graph[u]) {
				if (dist[e.t] < 0 && e.f < e.cap) {
					dist[e.t] = dist[u] + 1;
					Q[sizeQ++] = e.t;
				}
			}
		}
		return dist[dest] >= 0;
	}

	public static int maxFlow(List<Edge>[] graph, int src, int dest) {
		int flow = 0;
		int[] dist = new int[graph.length];
		while (dinicBfs(graph, src, dest, dist)) {
			int[] ptr = new int[graph.length];

			LinkCutTreeMin.Node[] nodes = new LinkCutTreeMin.Node[graph.length];
			for (int i = 0; i < nodes.length; i++) {
				nodes[i] = new LinkCutTreeMin.Node(i, Integer.MAX_VALUE);
			}

			final LinkCutTreeMin.Node s = nodes[src];
			final LinkCutTreeMin.Node t = nodes[dest];
			List<Integer>[] lists = new List[graph.length];
			for (int i = 0; i < lists.length; i++) {
				lists[i] = new ArrayList<>();
			}
			while (true) {
				LinkCutTreeMin.Node v = LinkCutTreeMin.findRoot(s);
				if (v == t) {
					v = nodes[LinkCutTreeMin.minId(s)];
					LinkCutTreeMin.expose(v);
					flow += v.min;
					LinkCutTreeMin.add(s, -v.min);

					while (true) {
						v = nodes[LinkCutTreeMin.minId(s)];
						LinkCutTreeMin.expose(v);
						if (v.value > 0)
							break;
						Edge edge = (Edge) v.o;
						int df = edge.cap - edge.f;
						edge.f += df;
						graph[edge.t].get(edge.rev).f -= df;
						LinkCutTreeMin.cut(v);
					}
				} else {
					if (ptr[v.id] < graph[v.id].size()) {
						Edge e = graph[v.id].get(ptr[v.id]++);
						if (dist[e.t] == dist[v.id] + 1 && e.f < e.cap) {
							LinkCutTreeMin.link(v, nodes[e.t], e.cap - e.f, e);
							lists[e.t].add(v.id);
						}
					} else {
						if (v == s) {
							for (List<Integer> list : lists) {
								for (int u : list) {
									if(LinkCutTreeMin.findRoot(nodes[u]) == nodes[u])
										continue;
									Edge edge = (Edge) nodes[u].o;

									LinkCutTreeMin.expose(nodes[u]);
									long df = edge.cap - edge.f - (nodes[u].value < Integer.MAX_VALUE / 2 ? nodes[u].value : nodes[u].savedValue);
									edge.f += df;
									graph[edge.t].get(edge.rev).f -= df;
								}
								list.clear();
							}
							break;
						}
						for (int u : lists[v.id]) {
							if(LinkCutTreeMin.findRoot(nodes[u]) == nodes[u])
								continue;
							Edge edge = (Edge) nodes[u].o;

							LinkCutTreeMin.expose(nodes[u]);
							long df = edge.cap - edge.f - nodes[u].value;
							edge.f += df;
							graph[edge.t].get(edge.rev).f -= df;
							LinkCutTreeMin.cut(nodes[u]);
						}
						lists[v.id].clear();
					}
				}
			}
		}
		return flow;
	}

	// Usage example
	public static void main(String[] args) {
		List<Edge>[] graph = createGraph(3);
		addEdge(graph, 0, 1, 3);
		addEdge(graph, 0, 2, 2);
		addEdge(graph, 1, 2, 2);
		System.out.println(4 == maxFlow(graph, 0, 2));
	}
}
