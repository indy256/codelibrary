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

		this.len[edges] = len;
		u[edges] = s;
		v[edges] = t;

		// add (s,t) to normal graph
		prev[0][edges] = tail[0][s];
		tail[0][s] = edges;

		// add (t,s) to reverse graph
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

					addEdge(u, v, len[uv] + len[vw]);
					System.out.println("(" + u + "," + v + ")");
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
		for (int dir = 0; !q[0].isEmpty() || !q[1].isEmpty(); ) {
			long min1 = q[0].isEmpty() ? Long.MAX_VALUE : q[0].peek();
			long min2 = q[1].isEmpty() ? Long.MAX_VALUE : q[1].peek();
			long min = Math.min(min1, min2);
			if (res < min)
				break;
			long cur = q[dir].poll();
			if (res < cur >>> 32)
				break;
			int u = (int) cur;
			res = Math.min(res, prio[dir][u] + prio[1 - dir][u]);

			for (int edge = tail[dir][u]; edge != -1; edge = prev[dir][edge]) {
				int v = this.v[edge];
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

	public static void main(String[] args) {
		ContractionHierarchies ch = new ContractionHierarchies();
		ch.addEdge(0, 1, 1);
		ch.addEdge(2, 0, 1);
		ch.preprocess();
	}
}
