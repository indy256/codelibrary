package experimental.spoj;
import java.io.*;
import java.util.*;

public class DYNACON_REF {

	static class Graph {
		Set<Integer>[] edges;

		public Graph(int n) {
			edges = new Set[n];
			for (int i = 0; i < n; i++) {
				edges[i] = new HashSet<>();
			}
		}

		void add(int u, int v) {
			edges[u].add(v);
			edges[v].add(u);
		}

		void remove(int u, int v) {
			edges[u].remove(v);
			edges[v].remove(u);
		}

		boolean connected(int u, int v, boolean[] vis) {
			vis[u] = true;
			if (u == v)
				return true;
			for (int x : edges[u]) {
				if (!vis[x])
					if (connected(x, v, vis))
						return true;
			}
			return false;
		}
	}

	public static void main(String[] args) throws Exception {
		PrintWriter pw = new PrintWriter(System.out);

		int n = nextInt();
		Graph g = new Graph(n);

		int q = nextInt();

		long t1 = 0;
		long time = 0;

		time = System.currentTimeMillis();

		boolean[] vis = new boolean[n];

		for (int i = 0; i < q; i++) {
			String type = nextToken();
			int t = "add".equals(type) ? 0 : "rem".equals(type) ? 1 : 2;
			int a = nextInt() - 1;
			int b = nextInt() - 1;

			if (t == 0) {
				g.add(a, b);
			} else if (t == 1) {
				g.remove(a, b);
			} else {
				Arrays.fill(vis, false);
				boolean res = g.connected(a, b, vis);
				pw.println(res ? "YES" : "NO");
			}
		}

		t1 += System.currentTimeMillis() - time;

		pw.close();
		System.err.println(t1);
	}

	static int nextInt() throws IOException {
		return Integer.parseInt(nextToken());
	}

//	 static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	static BufferedReader reader;
	static {
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("d:/dynacon1_input0.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	static StringTokenizer tokenizer;

	static String nextToken() throws IOException {
		while (tokenizer == null || !tokenizer.hasMoreTokens()) {
			tokenizer = new StringTokenizer(reader.readLine());
		}
		return tokenizer.nextToken();
	}
}
