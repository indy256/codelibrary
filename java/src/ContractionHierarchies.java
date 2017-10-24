import java.util.*;

/**
 * @author Andrey Naumenko
 */
public class ContractionHierarchies {

	public static class LayerGraph implements Cloneable {
		final int nodes;
		int edges = 0;
		final int[] levels;
		final int[] firstEdge;
		final int[] secondEdge;
		final int[] len;
		final int[] u;
		final int[] v;
		final int[][] tail;
		final int[][] prev;

		public LayerGraph(int nodes, int maxEdges) {
			this.nodes = nodes;
			levels = new int[nodes];
			firstEdge = new int[maxEdges];
			secondEdge = new int[maxEdges];
			len = new int[maxEdges];
			u = new int[maxEdges];
			v = new int[maxEdges];
			tail = new int[][]{new int[nodes], new int[nodes]};
			prev = new int[][]{new int[maxEdges], new int[maxEdges]};
			Arrays.fill(tail[0], -1);
			Arrays.fill(tail[1], -1);
			Arrays.fill(prev[0], -1);
			Arrays.fill(prev[1], -1);
			Arrays.fill(firstEdge, -1);
			Arrays.fill(secondEdge, -1);
		}

		public int addEdge(int s, int t, int len) {
			for (int edge = tail[0][s]; edge != -1; edge = prev[0][edge]) {
				if (v[edge] == t) {
					this.len[edge] = Math.min(this.len[edge], len);
					return edge;
				}
			}

			this.len[edges] = len;
			u[edges] = s;
			v[edges] = t;

			// outgoing arc
			prev[0][edges] = tail[0][s];
			tail[0][s] = edges;

			// incoming arc
			prev[1][edges] = tail[1][t];
			tail[1][t] = edges;

			return edges++;
		}

		@Override
		public Object clone() {
			LayerGraph g = new LayerGraph(nodes, len.length);
			g.edges = edges;
			int[][] arrays1 = {levels, firstEdge, secondEdge, len, u, v, tail[0], tail[1], prev[0], prev[1]};
			int[][] arrays2 = {g.levels, g.firstEdge, g.secondEdge, g.len, g.u, g.v, g.tail[0], g.tail[1], g.prev[0], g.prev[1]};
			for (int i = 0; i < arrays1.length; i++) {
				System.arraycopy(arrays1[i], 0, arrays2[i], 0, arrays2[i].length);
			}
			return g;
		}

		public LayerGraph compact() {
			LayerGraph g = new LayerGraph(nodes, edges);
			g.edges = edges;
			System.arraycopy(levels, 0, g.levels, 0, g.levels.length);
			System.arraycopy(len, 0, g.len, 0, edges);
			System.arraycopy(firstEdge, 0, g.firstEdge, 0, edges);
			System.arraycopy(secondEdge, 0, g.secondEdge, 0, edges);
			for (int i = 0; i < nodes; i++) {
				for (int dir = 0; dir < 2; dir++) {
					for (int edge = tail[dir][i]; edge != -1; edge = prev[dir][edge]) {
						int u = this.u[edge];
						int v = this.v[edge];
						if (dir == 0 && levels[u] > levels[v] || dir == 1 && levels[u] < levels[v])
							continue;
						g.u[edge] = u;
						g.v[edge] = v;

						g.prev[dir][edge] = g.tail[dir][dir == 0 ? u : v];
						g.tail[dir][dir == 0 ? u : v] = edge;
					}
				}
			}
			return g;
		}
	}

	private static List<Integer> findWitness(LayerGraph g, int s, int forbidden, int[] prio, boolean[] targets, int targetCount, int[] hops, int upperBound) {
		hops[s] = 0;
		PriorityQueue<Long> q = new PriorityQueue<>();
		q.add((long) s);
		prio[s] = 0;
		List<Integer> visited = new ArrayList<>();
		visited.add(s);
		while (!q.isEmpty()) {
			long cur = q.remove();
			int u = (int) cur;
			int priou = prio[u];
			if (priou >= upperBound)
				break;
			if (cur >>> 32 != priou)
				continue;
			if (targets[u]) {
				targets[u] = false;
				if (--targetCount == 0)
					break;
			}
			if (hops[u] == 8)
				continue;

			for (int edge = g.tail[0][u]; edge != -1; edge = g.prev[0][edge]) {
				int v = g.v[edge];
				if (g.levels[v] < g.levels[forbidden] || v == forbidden)
					continue;
				int nprio = priou + g.len[edge];
				if (prio[v] > nprio) {
					prio[v] = nprio;
					visited.add(v);
					hops[v] = hops[u] + 1;
					q.add(((long) nprio << 32) | v);
				}
			}
		}
		return visited;
	}

	private static class ShortcutsInfo {
		final int shortcuts;
		final int originalEdges;

		ShortcutsInfo(int shortcuts, int originalEdges) {
			this.shortcuts = shortcuts;
			this.originalEdges = originalEdges;
		}
	}

	private static ShortcutsInfo addShortcuts(LayerGraph g, int v, boolean realRun, int[] prio, boolean[] targets, int[] hops, int[] degree, int[] originalEdges) {
		int shortcuts = 0;
		int totalOriginalEdges = 0;
		for (int uv = g.tail[1][v]; uv != -1; uv = g.prev[1][uv]) {
			int u = g.u[uv];
			if (g.levels[u] < g.levels[v])
				continue;

			int maxLenVW = 0;

			int targetCount = 0;
			for (int vw = g.tail[0][v]; vw != -1; vw = g.prev[0][vw]) {
				int w = g.v[vw];
				if (g.levels[w] < g.levels[v] || u == w)
					continue;
				if (!targets[w]) {
					targets[w] = true;
					++targetCount;
				}
				maxLenVW = Math.max(maxLenVW, g.len[vw]);
			}

			List<Integer> visited = findWitness(g, u, v, prio, targets, targetCount, hops, g.len[uv] + maxLenVW);

			// edge reduction
//			for (int ux = g.tail[0][u]; ux != -1; ux = g.prev[0][ux]) {
//				int x = g.v[ux];
//				int distux = prio[x];
//				if (g.len[ux] > distux && realRun)
//					++reduction;
//			}

			for (int vw = g.tail[0][v]; vw != -1; vw = g.prev[0][vw]) {
				int w = g.v[vw];
				if (g.levels[w] < g.levels[v] || u == w)
					continue;
				targets[w] = false;

				int distuw = prio[w];
				if (distuw > g.len[uv] + g.len[vw]) {
					++shortcuts;
					totalOriginalEdges += originalEdges[uv] + originalEdges[vw];
					if (realRun) {
						int edge = g.addEdge(u, w, g.len[uv] + g.len[vw]);
						originalEdges[edge] = originalEdges[uv] + originalEdges[vw];
						++degree[u];
						++degree[w];
						g.firstEdge[edge] = uv;
						g.secondEdge[edge] = vw;
					}
				}
			}
			for (int x : visited) {
				prio[x] = Integer.MAX_VALUE;
				hops[x] = 0;
			}
		}
		return new ShortcutsInfo(shortcuts, totalOriginalEdges);
	}

	private static int calcPriority(LayerGraph g, int v, int[] prio, boolean[] targets, int[] hops, int[] degree, int[] originalEdges) {
		ShortcutsInfo shortcutsInfo = addShortcuts(g, v, false, prio, targets, hops, degree, originalEdges);
		int edgeDifference = shortcutsInfo.shortcuts - degree[v];
		int contractedNeighbors = 0;
		for (int vw = g.tail[0][v]; vw != -1; vw = g.prev[0][vw])
			if (g.levels[g.v[vw]] != Integer.MAX_VALUE)
				++contractedNeighbors;
		for (int uv = g.tail[1][v]; uv != -1; uv = g.prev[1][uv])
			if (g.levels[g.u[uv]] != Integer.MAX_VALUE)
				++contractedNeighbors;
		return 40 * edgeDifference + 60 * shortcutsInfo.originalEdges + 2 * contractedNeighbors;
	}

	public static LayerGraph preprocess(LayerGraph origGraph) {
		LayerGraph g = (LayerGraph) origGraph.clone();

		PriorityQueue<Long> priorities = new PriorityQueue<>();
		int[] hops = new int[g.nodes];
		boolean[] targets = new boolean[g.nodes];
		int[] prio = new int[g.nodes];
		Arrays.fill(prio, Integer.MAX_VALUE);
		int[] degree = new int[g.nodes];
		for (int e = 0; e < g.edges; e++) {
			++degree[g.u[e]];
			++degree[g.v[e]];
		}
		int[] originalEdges = new int[g.len.length];
		Arrays.fill(originalEdges, 0, g.edges, 1);

		for (int v = 0; v < g.nodes; v++)
			priorities.add(((long) calcPriority(g, v, prio, targets, hops, degree, originalEdges) << 32) | v);
		Arrays.fill(g.levels, Integer.MAX_VALUE);

		for (int i = 0; i < g.nodes - 2; i++) {
			while (g.levels[priorities.peek().intValue()] != Integer.MAX_VALUE) priorities.remove();
			int v = priorities.remove().intValue();
			int priority = calcPriority(g, v, prio, targets, hops, degree, originalEdges);
			while (g.levels[priorities.peek().intValue()] != Integer.MAX_VALUE) priorities.remove();
			if (priority > priorities.peek() >>> 32) {
				priorities.add(((long) priority << 32) | v);
				--i;
				continue;
			}
			g.levels[v] = i;
			addShortcuts(g, v, true, prio, targets, hops, degree, originalEdges);

			for (int edge = g.tail[0][v]; edge != -1; edge = g.prev[0][edge]) {
				int w = g.v[edge];
				if (g.levels[w] == Integer.MAX_VALUE)
					priorities.add(((long) calcPriority(g, w, prio, targets, hops, degree, originalEdges) << 32) | w);
			}
			for (int edge = g.tail[1][v]; edge != -1; edge = g.prev[1][edge]) {
				int u = g.u[edge];
				if (g.levels[u] == Integer.MAX_VALUE)
					priorities.add(((long) calcPriority(g, u, prio, targets, hops, degree, originalEdges) << 32) | u);
			}
		}
//		System.out.println(reduction);
		return g;
	}

	private static List<Integer> extractEdges(LayerGraph g, int edge) {
		if (g.firstEdge[edge] == -1) // edge is not a shortcut
			return Collections.singletonList(edge);
		List<Integer> res = new ArrayList<>();
		res.addAll(extractEdges(g, g.firstEdge[edge]));
		res.addAll(extractEdges(g, g.secondEdge[edge]));
		return res;
	}

	private static List<Integer> buildPath(LayerGraph g, int[][] pred, int top) {
		List<Integer> path = new ArrayList<>();
		for (int edge0 = pred[0][top]; edge0 != -1; edge0 = pred[0][g.u[edge0]]) {
			List<Integer> p = extractEdges(g, edge0);
			Collections.reverse(p);
			path.addAll(p);
		}
		Collections.reverse(path);
		for (int edge1 = pred[1][top]; edge1 != -1; edge1 = pred[1][g.v[edge1]])
			path.addAll(extractEdges(g, edge1));
		return path;
	}

	public static class PathInfo {
		public final int len;
		public final List<Integer> edges;

		public PathInfo(int len, List<Integer> edges) {
			this.len = len;
			this.edges = edges;
		}
	}

	public static PathInfo shortestPath(LayerGraph g, int s, int t) {
		int[][] prio = {new int[g.nodes], new int[g.nodes]};
		Arrays.fill(prio[0], Integer.MAX_VALUE / 2);
		Arrays.fill(prio[1], Integer.MAX_VALUE / 2);
		prio[0][s] = 0;
		prio[1][t] = 0;
		int[][] pred = {new int[g.nodes], new int[g.nodes]};
		Arrays.fill(pred[0], -1);
		Arrays.fill(pred[1], -1);
		PriorityQueue<Long>[] q = new PriorityQueue[]{new PriorityQueue<Long>(), new PriorityQueue<Long>()};
		q[0].add((long) s);
		q[1].add((long) t);
		int res = Integer.MAX_VALUE - 1;
		int top = -1;
		m1:
		for (int dir = 0; ; dir = !q[1 - dir].isEmpty() ? 1 - dir : dir) {
			if (res <= Math.min(q[0].isEmpty() ? Integer.MAX_VALUE : (int) (q[0].peek() >>> 32), q[1].isEmpty() ? Integer.MAX_VALUE : (int) (q[1].peek() >>> 32)))
				break;
			long cur = q[dir].remove();
			int u = (int) cur;
			if (cur >>> 32 != prio[dir][u])
				continue;

			// stall-on-demand
			for (int edge = g.tail[1 - dir][u]; edge != -1; edge = g.prev[1 - dir][edge])
				if (prio[dir][u] > prio[dir][dir == 0 ? g.u[edge] : g.v[edge]] + g.len[edge])
					continue m1;

			int curLen = prio[dir][u] + prio[1 - dir][u];
			if (res > curLen) {
				res = curLen;
				top = u;
			}

			for (int edge = g.tail[dir][u]; edge != -1; edge = g.prev[dir][edge]) {
				int v = dir == 0 ? g.v[edge] : g.u[edge];
				if (g.levels[v] < g.levels[u])
					continue;
				int nprio = prio[dir][u] + g.len[edge];
				if (prio[dir][v] > nprio) {
					prio[dir][v] = nprio;
					pred[dir][v] = edge;
					q[dir].add(((long) nprio << 32) | v);
				}
			}
		}

		return new PathInfo(res, buildPath(g, pred, top));
	}

	public static int[][] manyToMany(LayerGraph g, int[] s, int[] t) {
		List<Long> bucketLists[] = new List[g.nodes];

		int[] prio = new int[g.nodes];
		Arrays.fill(prio, Integer.MAX_VALUE / 2);

		for (int i = 0; i < t.length; i++) {
			int target = t[i];
			prio[target] = 0;
			List<Integer> visited = new ArrayList<Integer>();
			visited.add(target);
			PriorityQueue<Long> q = new PriorityQueue<Long>();
			q.add((long) target);
			m1:
			while (!q.isEmpty()) {
				long cur = q.remove();
				int u = (int) cur;
				int priou = prio[u];
				if (cur >>> 32 != priou)
					continue;

				// stall-on-demand
				for (int edge = g.tail[0][u]; edge != -1; edge = g.prev[0][edge])
					if (prio[u] > prio[g.v[edge]] + g.len[edge])
						continue m1;

				if (bucketLists[u] == null)
					bucketLists[u] = new ArrayList<Long>(1);
				bucketLists[u].add(((long) priou << 32) | i);

				for (int edge = g.tail[1][u]; edge != -1; edge = g.prev[1][edge]) {
					int v = g.u[edge];
//					if (g.levels[v] < g.levels[u])
//						continue;
					int nprio = priou + g.len[edge];
					if (prio[v] > nprio) {
						prio[v] = nprio;
						visited.add(v);
						q.add(((long) nprio << 32) | v);
					}
				}
			}
			for (int v : visited) {
				prio[v] = Integer.MAX_VALUE / 2;
			}
		}

		int[][] d = new int[s.length][t.length];

		long[][] buckets = new long[g.nodes][];
		for (int i = 0; i < g.nodes; i++) {
			int cnt = bucketLists[i] == null ? 0 : bucketLists[i].size();
			buckets[i] = new long[cnt];
			for (int j = 0; j < cnt; j++)
				buckets[i][j] = bucketLists[i].get(j);
		}

		for (int i = 0; i < s.length; i++) {
			int source = s[i];
			Arrays.fill(d[i], Integer.MAX_VALUE - 1);
			prio[source] = 0;
			List<Integer> visited = new ArrayList<Integer>();
			visited.add(source);
			PriorityQueue<Long> q = new PriorityQueue<Long>();
			q.add((long) source);
			m1:
			while (!q.isEmpty()) {
				long cur = q.remove();
				int u = (int) cur;
				int priou = prio[u];
				if (cur >>> 32 != priou)
					continue;

				// stall-on-demand
				for (int edge = g.tail[1][u]; edge != -1; edge = g.prev[1][edge])
					if (prio[u] > prio[g.u[edge]] + g.len[edge])
						continue m1;

				for (long x : buckets[u]) {
					int j = (int) x;
					int priov = (int) (x >>> 32);
					d[i][j] = Math.min(d[i][j], priou + priov);
				}

				for (int edge = g.tail[0][u]; edge != -1; edge = g.prev[0][edge]) {
					int v = g.v[edge];
//					if (g.levels[v] < g.levels[u])
//						continue;
					int nprio = priou + g.len[edge];
					if (prio[v] > nprio) {
						prio[v] = nprio;
						visited.add(v);
						q.add(((long) nprio << 32) | v);
					}
				}
			}
			for (int v : visited) {
				prio[v] = Integer.MAX_VALUE / 2;
			}
		}

		return d;
	}

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		Random rnd = new Random(1);
		int totalShortcuts = 0;

		for (int step = 0; step < 100; step++) {
			int V = rnd.nextInt(100) + 2;
//			int E = V == 1 ? 0 : V + rnd.nextInt(V * (V - 1) - V + 1);
			int E = Math.max(V, Math.min(V * (V - 1), 5 * V));
			int[][] d = generateStronglyConnectedDigraph(V, E, rnd);
			LayerGraph origGraph = new LayerGraph(V, 100000);
			for (int i = 0; i < V; i++) {
				for (int j = 0; j < V; j++) {
					if (i != j && d[i][j] != Integer.MAX_VALUE / 2) {
						origGraph.addEdge(i, j, d[i][j]);
					}
				}
			}

			if (origGraph.edges > E) throw new RuntimeException(E + " " + origGraph.edges);

			for (int k = 0; k < V; k++)
				for (int i = 0; i < V; i++)
					for (int j = 0; j < V; j++)
						d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);

			for (int i = 0; i < V; i++)
				for (int j = 0; j < V; j++)
					if (d[i][j] == Integer.MAX_VALUE / 2) throw new RuntimeException();

			long time1 = System.currentTimeMillis();
			LayerGraph g = preprocess(origGraph);
//			System.out.println("1 " + (System.currentTimeMillis() - time1));
			int shortcuts = g.edges - origGraph.edges;
			totalShortcuts += shortcuts;
			System.out.println("edges = " + origGraph.edges + " shortcuts = " + shortcuts + " nodes = " + g.nodes);

			int[] vertices = new int[V];
			for (int i = 0; i < V; i++) vertices[i] = i;
			time1 = System.currentTimeMillis();
			int[][] d2 = manyToMany(g.compact(), vertices, vertices);
//			System.out.println("2 " + (System.currentTimeMillis() - time1));

			for (int step1 = 0; step1 < 10; step1++) {
				int a = rnd.nextInt(V);
				int b = rnd.nextInt(V);

				int res3 = d2[a][b];
				PathInfo pathInfo = shortestPath(g, a, b);
				int res0 = 0;
				int prev = -1;
				for (int edge : pathInfo.edges) {
					res0 += g.len[edge];
					if (prev != -1 && prev != g.u[edge]) throw new RuntimeException();
					prev = g.v[edge];
				}
				int res1 = pathInfo.len;
				int res2 = d[a][b];
				if (res0 != res1 || res0 != res2 || res0 != res3)
					throw new RuntimeException(res0 + " " + res1 + " " + res2 + " " + res3);
			}
		}
		System.out.println("totalShortcuts = " + totalShortcuts);
		System.out.println("time = " + (System.currentTimeMillis() - time));
	}

	static void debug(LayerGraph g) {
		for (int edge = 0; edge < g.edges; edge++)
			System.out.println("(" + g.u[edge] + "," + g.v[edge] + ") = " + g.len[edge]);
	}

	static int[][] generateStronglyConnectedDigraph(int V, int upperBoundE, Random rnd) {
		if (upperBoundE == 0) return new int[][]{{0}};
		List<Integer> p = new ArrayList<>();
		for (int i = 0; i < V; i++)
			p.add(i);
		while (p.size() < upperBoundE)
			p.add(rnd.nextInt(V));
		Collections.shuffle(p, rnd);
		for (int i = 0; i < p.size(); i++) {
			int a = p.get((i + p.size() - 1) % p.size());
			int b = p.get(i);
			int c = p.get((i + 1) % p.size());
			while (b == a || b == c)
				b = (b + 1) % V;
			p.set(i, b);
		}
		int[][] d = new int[V][V];
		for (int i = 0; i < V; i++) {
			Arrays.fill(d[i], Integer.MAX_VALUE / 2);
			d[i][i] = 0;
		}
		for (int i = 0; i < p.size(); i++) {
			int a = p.get(i);
			int b = p.get((i + 1) % p.size());
			d[a][b] = rnd.nextInt(10);
		}
		return d;
	}
}
