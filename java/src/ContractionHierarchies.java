import java.util.*;

/**
 * @author Andrey Naumenko
 */
public class ContractionHierarchies {

	final int NODES = 10000;
	final int EDGES = 10000;

	int[] levels = new int[NODES];

	int[] len = new int[EDGES];
	int[] u = new int[EDGES];
	int[] v = new int[EDGES];
	int[] inDegree = new int[NODES];
	int[] outDegree = new int[NODES];

	int[][] tail = {new int[NODES], new int[NODES]};
	int[][] prev = {new int[EDGES], new int[EDGES]};

	PriorityQueue<Long> priorities = new PriorityQueue<>();

	{
		Arrays.fill(prev[0], -1);
		Arrays.fill(prev[1], -1);
		Arrays.fill(tail[0], -1);
		Arrays.fill(tail[1], -1);
//		for (int i = 0; i < levels.length; i++)
//			levels[i] = i;
	}

	int edges = 0;
	int nodes = 0;

	public void addEdge(int s, int t, int len) {
		nodes = Math.max(nodes, s + 1);
		nodes = Math.max(nodes, t + 1);

		++outDegree[s];
		++inDegree[t];

		for (int edge = tail[0][s]; edge != -1; edge = prev[0][edge]) {
			if (v[edge] == t) {
				this.len[edge] = Math.min(this.len[edge], len);
				return;
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

		++edges;
	}

	Map<Integer, Integer> dijkstra(int s, boolean[] targets) {
		int targetCount = 0;
		for (boolean target : targets) if (target) ++targetCount;
		Map<Integer, Integer> prio = new HashMap<>();
		prio.put(s, 0);
		PriorityQueue<Long> q = new PriorityQueue<>();
		q.add((long) s);
		while (!q.isEmpty()) {
			long cur = q.remove();
			int u = (int) cur;
			int priou = prio.get(u);
			if (cur >>> 32 != priou)
				continue;

			if (targets[u]) {
				targets[u] = false;
				if (--targetCount == 0)
					break;
			}

			for (int edge = tail[0][u]; edge != -1; edge = prev[0][edge]) {
				int v = this.v[edge];
				if (levels[v] < levels[s])
					continue;
				int nprio = priou + len[edge];
				final Integer priov = prio.get(v);
				if (priov == null || priov > nprio) {
					prio.put(v, nprio);
					q.add(((long) nprio << 32) + v);
				}
			}
		}
		return prio;
	}

	int calculatePriority(int v) {
		int shortcuts = addShortcuts(v, false, -1);
		int degree = inDegree[v] + outDegree[v];
		int edgeDifference = shortcuts - degree;
		return edgeDifference;
	}

	void calcPriorities() {
		for (int v = 0; v < nodes; v++) {
			int p = calculatePriority(v);
			priorities.add(((long) p << 32) + v);
		}
	}

	public int addShortcuts(int v, boolean nonSimulate, int level) {
		boolean[] targets = new boolean[nodes];
		int shortcuts = 0;
		for (int uv = tail[1][v]; uv != -1; uv = prev[1][uv]) {
			int u = this.u[uv];
			if (levels[u] < levels[v])
				continue;

			for (int vw = tail[0][v]; vw != -1; vw = prev[0][vw]) {
				int w = this.v[vw];
				if (levels[w] < levels[v] || u == w)
					continue;
				targets[w] = true;
			}

			Map<Integer, Integer> prio = dijkstra(u, targets);

			for (int vw = tail[0][v]; vw != -1; vw = prev[0][vw]) {
				int w = this.v[vw];
				if (levels[w] < levels[v] || u == w)
					continue;
				targets[w] = false;

				final Integer priow = prio.get(w);
				if (priow == null || priow > len[uv] + len[vw]) {
					if (nonSimulate) {
						addEdge(u, w, len[uv] + len[vw]);
						++shortcuts;
//						System.out.println("(" + u + "," + w + ") -> " + (len[uv] + len[vw]));
					}
				}
			}
		}
		return shortcuts;
	}

	public int shortestPath(int s, int t) {
		int[][] prio = {new int[nodes], new int[nodes]};
		Arrays.fill(prio[0], Integer.MAX_VALUE / 2);
		Arrays.fill(prio[1], Integer.MAX_VALUE / 2);
		prio[0][s] = 0;
		prio[1][t] = 0;
		PriorityQueue<Long>[] q = new PriorityQueue[]{new PriorityQueue<Long>(), new PriorityQueue<Long>()};
		q[0].add((long) s);
		q[1].add((long) t);
		int res = Integer.MAX_VALUE;
		for (int dir = 0; ; dir = !q[1 - dir].isEmpty() ? 1 - dir : dir) {
			if (res <= Math.min(q[0].isEmpty() ? Integer.MAX_VALUE : q[0].peek() >>> 32, q[1].isEmpty() ? Integer.MAX_VALUE : q[1].peek() >>> 32))
				break;
			long cur = q[dir].remove();
			int u = (int) cur;
			if (cur >>> 32 != prio[dir][u])
				continue;
			res = Math.min(res, prio[dir][u] + prio[1 - dir][u]);

			for (int edge = tail[dir][u]; edge != -1; edge = prev[dir][edge]) {
				int v = dir == 0 ? this.v[edge] : this.u[edge];
				if (levels[v] < levels[u])
					continue;
				int nprio = prio[dir][u] + len[edge];
				if (prio[dir][v] > nprio) {
					prio[dir][v] = nprio;
					q[dir].add(((long) nprio << 32) + v);
				}
			}
		}

		return res;
	}

	void debug() {
		for (int edge = 0; edge < edges; edge++) {
			System.out.println("(" + u[edge] + "," + v[edge] + ") = " + len[edge]);
		}
	}

	static int[][] generateStronglyConnectedDigraph(int V, int E, Random rnd) {
		List<Integer> p = new ArrayList<>();
		for (int i = 0; i < V; i++)
			p.add(i);
		while (p.size() < E)
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

	public static void main(String[] args) {
//		ContractionHierarchies ch = new ContractionHierarchies();
//		ch.addEdge(0, 1, 1);
//		ch.addEdge(2, 0, 1);
//		ch.preprocess();
//		ch.debug();
//		System.out.println(ch.shortestPath(2, 1));

		Random rnd = new Random(1);

		for (int step = 0; step < 1000; step++) {
			ContractionHierarchies ch = new ContractionHierarchies();
			int V = rnd.nextInt(50) + 2;
//			V = 5;
			int E = V + rnd.nextInt(V * (V - 1) - V + 1);
			int[][] d = generateStronglyConnectedDigraph(V, E, rnd);
			for (int i = 0; i < V; i++) {
				for (int j = 0; j < V; j++) {
					if (i != j && d[i][j] != Integer.MAX_VALUE / 2) {
						ch.addEdge(i, j, d[i][j]);
					}
				}
			}

			for (int k = 0; k < V; k++)
				for (int i = 0; i < V; i++)
					for (int j = 0; j < V; j++)
						d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);

			for (int i = 0; i < V; i++)
				for (int j = 0; j < V; j++)
					if (d[i][j] == Integer.MAX_VALUE / 2) throw new RuntimeException();

			int shortcuts = ch.edges;
			ch.calcPriorities();
			List<Long> list = new ArrayList<>();
			int level = 0;
			while (!ch.priorities.isEmpty()) {
				long cur = ch.priorities.remove();
				ch.levels[(int) cur] = level++;
				list.add(cur);
			}
			ch.priorities.addAll(list);
			for (int i = 0; i < ch.nodes - 2; i++) {
				long cur = ch.priorities.remove();
				int v = (int) cur;
				ch.addShortcuts(v, true, i);
			}
			shortcuts = ch.edges - shortcuts;
			System.out.println("edges = " + (ch.edges - shortcuts) + " shortcuts = " + shortcuts);

//			ch.debug();
			for (int step1 = 0; step1 < 100; step1++) {
				int a = rnd.nextInt(V);
				int b = rnd.nextInt(V);

				final int res1 = ch.shortestPath(a, b);
				final int res2 = d[a][b];
				if (res1 != res2) throw new RuntimeException(res1 + " " + res2);
			}
		}

		/*
		ch.addEdge(0, 1, 2);
		ch.addEdge(1, 0, 2);

		ch.addEdge(0, 2, 2);
		ch.addEdge(2, 0, 2);

		ch.addEdge(0, 3, 2);
		ch.addEdge(3, 0, 2);

		ch.addEdge(0, 4, 2);
		ch.addEdge(4, 0, 2);

		ch.addEdge(1, 2, 1);
		ch.addEdge(2, 1, 1);

		ch.addEdge(2, 3, 2);
		ch.addEdge(3, 2, 2);

		ch.addEdge(3, 4, 2);
		ch.addEdge(4, 3, 2);
		ch.preprocess();
		ch.debug();
		System.out.println(ch.shortestPath(1, 4));
		*/
	}
}
