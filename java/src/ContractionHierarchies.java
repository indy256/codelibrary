import java.util.*;

/**
 * @author Andrey Naumenko
 */
public class ContractionHierarchies {

	final int NODES = 100000;
	final int EDGES = 100000;

	int[] levels = new int[NODES];

	int[] len = new int[EDGES];
	int[] u = new int[EDGES];
	int[] v = new int[EDGES];
	int[] originalEdges = new int[EDGES];
	int[] degree = new int[NODES];
	int[] restore0 = new int[NODES];
	int[] restore1 = new int[NODES];

	int[][] tail = {new int[NODES], new int[NODES]};
	int[][] prev = {new int[EDGES], new int[EDGES]};

	static int reduction;

	PriorityQueue<Long> priorities = new PriorityQueue<>();

	{
		Arrays.fill(prev[0], -1);
		Arrays.fill(prev[1], -1);
		Arrays.fill(tail[0], -1);
		Arrays.fill(tail[1], -1);
		Arrays.fill(restore0, -1);
		Arrays.fill(restore1, -1);
	}

	int edges = 0;
	int nodes = 0;

	public int addEdge(int s, int t, int len) {
		nodes = Math.max(nodes, s + 1);
		nodes = Math.max(nodes, t + 1);

		++degree[s];
		++degree[t];

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

		originalEdges[edges] = 1;
		return edges++;
	}

	private Map<Integer, Integer> findWitness(int s, int forbidden, boolean[] targets, int upperBound) {
		int targetCount = 0;
		for (boolean target : targets) if (target) ++targetCount;
		Map<Integer, Integer> prio = new HashMap<>();
		prio.put(s, 0);
		Map<Integer, Integer> hops = new HashMap<>();
		hops.put(s, 0);
		PriorityQueue<Long> q = new PriorityQueue<>();
		q.add((long) s);
		while (!q.isEmpty()) {
			long cur = q.remove();
			int u = (int) cur;
			int priou = prio.get(u);
			if (priou >= upperBound)
				break;
			if (cur >>> 32 != priou)
				continue;

			if (targets[u]) {
				targets[u] = false;
				if (--targetCount == 0)
					break;
			}

			for (int edge = tail[0][u]; edge != -1; edge = prev[0][edge]) {
				int v = this.v[edge];
				if (levels[v] < levels[forbidden] || v == forbidden)
					continue;
				int nhops = hops.get(u) + 1;
				int nprio = priou + len[edge];
				Integer priov = prio.get(v);
				if ((priov == null || priov > nprio) && nhops <= 8) {
					prio.put(v, nprio);
					hops.put(v, nhops);
					q.add(((long) nprio << 32) + v);
				}
			}
		}
		return prio;
	}

	private static class ShortcutsInfo {
		final int shortcuts;
		final int originalEdges;

		ShortcutsInfo(int shortcuts, int originalEdges) {
			this.shortcuts = shortcuts;
			this.originalEdges = originalEdges;
		}
	}

	private ShortcutsInfo addShortcuts(int v, boolean realRun) {
		boolean[] targets = new boolean[nodes];
		int shortcuts = 0;
		int totalOriginalEdges = 0;
		for (int uv = tail[1][v]; uv != -1; uv = prev[1][uv]) {
			int u = this.u[uv];
			if (levels[u] < levels[v])
				continue;

			int maxLenVW = 0;

			for (int vw = tail[0][v]; vw != -1; vw = prev[0][vw]) {
				int w = this.v[vw];
				if (levels[w] < levels[v] || u == w)
					continue;
				targets[w] = true;
				maxLenVW = Math.max(maxLenVW, len[vw]);
			}

			Map<Integer, Integer> distu = findWitness(u, v, targets, len[uv] + maxLenVW);

			// edge reduction
			for (int ux = tail[0][u]; ux != -1; ux = prev[0][ux]) {
				int x = this.v[ux];
				Integer distux = distu.get(x);
				if (distux != null && len[ux] > distux && realRun)
					++reduction;
			}

			for (int vw = tail[0][v]; vw != -1; vw = prev[0][vw]) {
				int w = this.v[vw];
				if (levels[w] < levels[v] || u == w)
					continue;
				targets[w] = false;

				final Integer distuw = distu.get(w);
				if (distuw == null || distuw > len[uv] + len[vw]) {
					++shortcuts;
					totalOriginalEdges += originalEdges[uv] + originalEdges[vw];
					if (realRun) {
						int edge = addEdge(u, w, len[uv] + len[vw]);
						originalEdges[edge] = originalEdges[uv] + originalEdges[vw];
						restore0[edge] = uv;
						restore1[edge] = vw;
//						System.out.println("(" + u + "," + w + ") -> " + (len[uv] + len[vw]));
					}
				}
			}
		}
		return new ShortcutsInfo(shortcuts, totalOriginalEdges);
	}

	private int calcPriority(int v) {
		ShortcutsInfo shortcutsInfo = addShortcuts(v, false);
		int edgeDifference = shortcutsInfo.shortcuts - degree[v];
		int contractedNeighbors = 0;
		for (int vw = tail[0][v]; vw != -1; vw = prev[0][vw])
			if (levels[this.v[vw]] != Integer.MAX_VALUE)
				++contractedNeighbors;
		for (int uv = tail[1][v]; uv != -1; uv = prev[1][uv])
			if (levels[this.u[uv]] != Integer.MAX_VALUE)
				++contractedNeighbors;
		return 10 * edgeDifference + 50 * shortcutsInfo.originalEdges + 1 * contractedNeighbors;
	}

	private void preprocess() {
		reduction = 0;
		for (int v = 0; v < nodes; v++)
			priorities.add(((long) calcPriority(v) << 32) + v);
		Arrays.fill(levels, Integer.MAX_VALUE);

		for (int i = 0; i < nodes - 2; i++) {
			while (levels[priorities.peek().intValue()] != Integer.MAX_VALUE) priorities.remove();
			long cur = priorities.remove();
			int v = (int) cur;
			int prio = calcPriority(v);
			while (levels[priorities.peek().intValue()] != Integer.MAX_VALUE) priorities.remove();
			if (prio > priorities.peek() >>> 32) {
				priorities.add(((long) prio << 32) + v);
				--i;
				continue;
			}
			levels[v] = i;
			addShortcuts(v, true);

			for (int edge = tail[0][v]; edge != -1; edge = prev[0][edge]) {
				int w = this.v[edge];
				if (levels[w] == Integer.MAX_VALUE)
					priorities.add(((long) calcPriority(w) << 32) + w);
			}
			for (int edge = tail[1][v]; edge != -1; edge = prev[1][edge]) {
				int u = this.u[edge];
				if (levels[u] == Integer.MAX_VALUE)
					priorities.add(((long) calcPriority(u) << 32) + u);
			}
		}
//		System.out.println(reduction);
	}

	public static class PathInfo {
		public final int len;
		public final List<Integer> edges;

		public PathInfo(int len, List<Integer> edges) {
			this.len = len;
			this.edges = edges;
		}
	}

	public PathInfo shortestPath(int s, int t) {
		int iterations = 0;
		int[][] prio = {new int[nodes], new int[nodes]};
		Arrays.fill(prio[0], Integer.MAX_VALUE / 2);
		Arrays.fill(prio[1], Integer.MAX_VALUE / 2);
		prio[0][s] = 0;
		prio[1][t] = 0;
		int[][] pred = {new int[nodes], new int[nodes]};
		Arrays.fill(pred[0], -1);
		Arrays.fill(pred[1], -1);
		PriorityQueue<Long>[] q = new PriorityQueue[]{new PriorityQueue<Long>(), new PriorityQueue<Long>()};
		q[0].add((long) s);
		q[1].add((long) t);
		int res = Integer.MAX_VALUE;
		int top = -1;
		m1:
		for (int dir = 0; ; dir = !q[1 - dir].isEmpty() ? 1 - dir : dir) {
			++iterations;
			if (res <= Math.min(q[0].isEmpty() ? Integer.MAX_VALUE : (int) (q[0].peek() >>> 32), q[1].isEmpty() ? Integer.MAX_VALUE : (int) (q[1].peek() >>> 32)))
				break;
			long cur = q[dir].remove();
			int u = (int) cur;
			if (cur >>> 32 != prio[dir][u])
				continue;

			// stall-on-demand
			for (int edge = tail[1 - dir][u]; edge != -1; edge = prev[1 - dir][edge]) {
				int v = dir == 0 ? this.u[edge] : this.v[edge];
				if (prio[dir][u] > prio[dir][v] + len[edge])
					continue m1;
			}

			int curLen = prio[dir][u] + prio[1 - dir][u];
			if (res > curLen) {
				res = curLen;
				top = u;
			}

			for (int edge = tail[dir][u]; edge != -1; edge = prev[dir][edge]) {
				int v = dir == 0 ? this.v[edge] : this.u[edge];
				if (levels[v] < levels[u])
					continue;
				int nprio = prio[dir][u] + len[edge];
				if (prio[dir][v] > nprio) {
					prio[dir][v] = nprio;
					pred[dir][v] = edge;
					q[dir].add(((long) nprio << 32) + v);
				}
			}
		}

//		System.out.println(iterations);
		return new PathInfo(res, buildPath(pred, top));
	}

	private List<Integer> buildPath(int[][] pred, int top) {
		List<Integer> path = new ArrayList<>();
		for (int edge0 = pred[0][top]; edge0 != -1; edge0 = pred[0][this.u[edge0]]) {
			List<Integer> p = extractEdges(edge0);
			Collections.reverse(p);
			path.addAll(p);
		}
		Collections.reverse(path);
		for (int edge1 = pred[1][top]; edge1 != -1; edge1 = pred[1][this.v[edge1]])
			path.addAll(extractEdges(edge1));
		return path;
	}

	private List<Integer> extractEdges(int edge) {
		if (restore0[edge] == -1) // edge is not a shortcut
			return Collections.singletonList(edge);
		List<Integer> res = new ArrayList<>();
		res.addAll(extractEdges(restore0[edge]));
		res.addAll(extractEdges(restore1[edge]));
		return res;
	}

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		Random rnd = new Random(1);
		int totalShortcuts = 0;

		for (int step = 0; step < 100; step++) {
			ContractionHierarchies ch = new ContractionHierarchies();
			int V = rnd.nextInt(50) + 40;
//			int E = V + rnd.nextInt(V * (V - 1) - V + 1);
			int E = 3 * V;
			int[][] d = generateStronglyConnectedDigraph(V, E, rnd);
			for (int i = 0; i < V; i++) {
				for (int j = 0; j < V; j++) {
					if (i != j && d[i][j] != Integer.MAX_VALUE / 2) {
						ch.addEdge(i, j, d[i][j]);
					}
				}
			}

			if (ch.edges > E) throw new RuntimeException(E + " " + ch.edges);

			for (int k = 0; k < V; k++)
				for (int i = 0; i < V; i++)
					for (int j = 0; j < V; j++)
						d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);

			for (int i = 0; i < V; i++)
				for (int j = 0; j < V; j++)
					if (d[i][j] == Integer.MAX_VALUE / 2) throw new RuntimeException();

			int shortcuts = ch.edges;
			ch.preprocess();
			shortcuts = ch.edges - shortcuts;
			totalShortcuts += shortcuts;
			System.out.println("edges = " + (ch.edges - shortcuts) + " shortcuts = " + shortcuts + " nodes = " + ch.nodes);

			for (int step1 = 0; step1 < 10000; step1++) {
				int a = rnd.nextInt(V);
				int b = rnd.nextInt(V);

				PathInfo pathInfo = ch.shortestPath(a, b);
				int res0 = 0;
				int prev = -1;
				for (int edge : pathInfo.edges) {
					res0 += ch.len[edge];
					if (prev != -1 && prev != ch.u[edge]) throw new RuntimeException();
					prev = ch.v[edge];
				}
				int res1 = pathInfo.len;
				int res2 = d[a][b];
				if (res1 != res2 || res0 != res2) throw new RuntimeException(res0 + " " + res1 + " " + res2);
			}
		}
		System.out.println("totalShortcuts = " + totalShortcuts);
		System.out.println("time = " + (System.currentTimeMillis() - time));
	}

	void debug() {
		for (int edge = 0; edge < edges; edge++)
			System.out.println("(" + u[edge] + "," + v[edge] + ") = " + len[edge]);
	}

	static int[][] generateStronglyConnectedDigraph(int V, int upperBoundE, Random rnd) {
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
