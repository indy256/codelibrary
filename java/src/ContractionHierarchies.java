import java.util.*;

/**
 * @author Andrey Naumenko
 */
public class ContractionHierarchies {

	final int NODES = 1000;
	final int EDGES = 1000;

	int[] levels = new int[NODES];
	int[] tail = new int[NODES];
	int[] rtail = new int[NODES];

	int[] len = new int[EDGES];
	int[] u = new int[EDGES];
	int[] v = new int[EDGES];
	int[] prev = new int[EDGES];

	int[] rlen = new int[EDGES];
	int[] ru = new int[EDGES];
	int[] rv = new int[EDGES];
	int[] rprev = new int[EDGES];

	{
		Arrays.fill(prev, -1);
		Arrays.fill(rprev, -1);
		Arrays.fill(tail, -1);
		Arrays.fill(rtail, -1);
		for (int i = 0; i < levels.length; i++)
			levels[i] = i;
	}

	int edges = 0;
	int nodes = 0;

	public void addEdge(int u, int v, int len) {
		nodes = Math.max(nodes, u + 1);
		nodes = Math.max(nodes, v + 1);

		// add (u,v) to normal graph
		this.u[edges] = u;
		this.v[edges] = v;
		this.len[edges] = len;
		prev[edges] = tail[u];
		tail[u] = edges;

		// add (v,u) to reverse graph
		ru[edges] = v;
		rv[edges] = u;
		rlen[edges] = len;
		rprev[edges] = rtail[v];
		rtail[v] = edges;

		++edges;
	}

	public void preprocess() {
		for (int v = 0; v < nodes - 2; v++) {
			for (int vw = tail[v]; vw != -1; vw = prev[vw]) {
				int w = this.v[vw];
				if (levels[w] <= levels[v])
					continue;
				for (int uv = rtail[v]; uv != -1; uv = rprev[uv]) {
					int u = rv[uv];
					if (levels[u] <= levels[v] || u == w)
						continue;

					addEdge(u, v, rlen[uv] + len[vw]);
					System.out.println("(" + u + "," + v + ")");
				}
			}
		}
	}

	public int shortestPath(int s, int t) {
		int[] prio1 = new int[nodes];
		int[] prio2 = new int[nodes];
		Arrays.fill(prio1, Integer.MAX_VALUE/2);
		Arrays.fill(prio2, Integer.MAX_VALUE/2);
		prio1[s] = 0;
		prio2[t] = 0;
		PriorityQueue<Long> q1 = new PriorityQueue<Long>();
		q1.add((long) s);
		PriorityQueue<Long> q2 = new PriorityQueue<Long>();
		q2.add((long) t);
		int res = Integer.MAX_VALUE;
		while (!q1.isEmpty() || !q2.isEmpty()) {
			PriorityQueue<Long> q = q2.isEmpty() || !q1.isEmpty() && q1.peek() < q2.peek() ? q1 : q2;
			long cur = q.poll();
			if (res < cur >>> 32)
				break;
			int curu = (int) cur;
			res = Math.min(res, prio1[curu] + prio2[curu]);



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
