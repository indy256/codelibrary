import java.util.*;

/**
 * @author Andrey Naumenko
 */
public class ContractionHierarchies {

	final int NODES = 1000;
	final int EDGES = 1000;

	int[] levels = new int[NODES];

	int[] len = new int[EDGES];
	int[] u = new int[EDGES];
	int[] v = new int[EDGES];

	int[][] tail = {new int[NODES], new int[NODES]};
	int[][] prev = {new int[EDGES], new int[EDGES]};

	{
		Arrays.fill(prev[0], -1);
		Arrays.fill(prev[1], -1);
		Arrays.fill(tail[0], -1);
		Arrays.fill(tail[1], -1);
		for (int i = 0; i < levels.length; i++)
			levels[i] = i;
	}

	int edges = 0;
	int nodes = 0;

	public void addEdge(int s, int t, int len) {
		nodes = Math.max(nodes, s + 1);
		nodes = Math.max(nodes, t + 1);

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

	public void preprocess() {
		for (int v = 0; v < nodes - 2; v++) {
			for (int vw = tail[0][v]; vw != -1; vw = prev[0][vw]) {
				int w = this.v[vw];
				if (levels[w] <= levels[v])
					continue;
				for (int uv = tail[1][v]; uv != -1; uv = prev[1][uv]) {
					int u = this.u[uv];
					if (levels[u] <= levels[v] || u == w)
						continue;

					addEdge(u, w, len[uv] + len[vw]);
					System.out.println("(" + u + "," + w + ") -> " + (len[uv] + len[vw]));
				}
			}
		}
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
		for (int dir = 0; ; ) {
			if (res <= Math.min(q[0].isEmpty() ? Integer.MAX_VALUE : q[0].peek() >>> 32, q[1].isEmpty() ? Integer.MAX_VALUE : q[1].peek() >>> 32))
				break;
			long cur = q[dir].poll();
			int u = (int) cur;
			if (cur >>> 32 != prio[dir][u])
				continue;
			res = Math.min(res, prio[dir][u] + prio[1 - dir][u]);

			for (int edge = tail[dir][u]; edge != -1; edge = prev[dir][edge]) {
				int v = dir == 0 ? this.v[edge] : this.u[edge];
				if (levels[v] <= levels[u])
					continue;
				int nprio = prio[dir][u] + len[edge];
				if (prio[dir][v] > nprio) {
					prio[dir][v] = nprio;
					q[dir].add(((long) nprio << 32) + v);
				}
			}
			if (!q[1 - dir].isEmpty())
				dir = 1 - dir;
		}

		return res;
	}

	void debug() {
		for (int edge = 0; edge < edges; edge++) {
			System.out.println("(" + u[edge] + "," + v[edge] + ") = " + len[edge]);
		}
	}

	public static void main(String[] args) {
//		ContractionHierarchies ch = new ContractionHierarchies();
//		ch.addEdge(0, 1, 1);
//		ch.addEdge(2, 0, 1);
//		ch.preprocess();
//		ch.debug();
//		System.out.println(ch.shortestPath(2, 1));

		ContractionHierarchies ch = new ContractionHierarchies();
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
	}
}
