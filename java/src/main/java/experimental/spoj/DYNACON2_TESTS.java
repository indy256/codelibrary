package experimental.spoj;
import java.io.*;
import java.util.*;

public class DYNACON2_TESTS {

	static class Graph {
		Set<Integer>[] edges;

		public Graph(int n) {
			edges = new Set[n];
			for (int i = 0; i < n; i++) {
				edges[i] = new HashSet<>();
			}
		}

		void add(int a, int b) {
			edges[a].add(b);
			edges[b].add(a);
		}

		void remove(int a, int b) {
			edges[a].remove(b);
			edges[b].remove(a);
		}

		boolean hasEdge(int a, int b) {
			return edges[a].contains(b) || edges[b].contains(a);
		}

		boolean connected(int a, int b, boolean[] vis) {
			vis[a] = true;
			if (a == b)
				return true;
			for (int v : edges[a]) {
				if (!vis[v])
					if (connected(v, b, vis))
						return true;
			}
			return false;
		}
	}

	// Usage example
	public static void main(String[] args) throws Exception {
		Random rnd = new Random(1);
		PrintWriter pw = new PrintWriter("d:/dynacon_input2.txt");
		PrintWriter pw1 = new PrintWriter("d:/dynacon_output2.txt");

		int n = 100000;
		Graph g = new Graph(n);

		int q = 100000;
		pw.println(n + " " + q);
		for (int i = 0; i < q; i++) {
			int a;
			int b;
			do {
				a = rnd.nextInt(n);
				b = rnd.nextInt(n);
			} while (a >= b);
			int t = rnd.nextInt(10);
			if (t < 2) {
				pw.println("conn " + (a + 1) + " " + (b + 1));
				pw1.println(g.connected(a, b, new boolean[n]) ? "YES" : "NO");
			} else if (t < 7) {
				if (!g.hasEdge(a, b)) {
					--i;
					continue;
				}
				g.remove(a, b);
				pw.println("rem " + (a + 1) + " " + (b + 1));
			} else {
				if (g.hasEdge(a, b)) {
					--i;
					continue;
				}
				g.add(a, b);
				pw.println("add " + (a + 1) + " " + (b + 1));
			}
		}

		pw.close();
		pw1.close();
	}

	static int nextInt() throws IOException {
		return Integer.parseInt(nextToken());
	}

	static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer tokenizer;

	static String nextToken() throws IOException {
		while (tokenizer == null || !tokenizer.hasMoreTokens()) {
			tokenizer = new StringTokenizer(reader.readLine());
		}
		return tokenizer.nextToken();
	}
}
