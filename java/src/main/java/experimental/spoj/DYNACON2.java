package experimental.spoj;
import java.io.*;
import java.util.*;

public class DYNACON2 {

	static class DisjointSets {
		int[] p, rank, oldNode, oldParent, oldRank;
		int size;

		DisjointSets(int maxsize) {
			p = new int[maxsize];
			for (int i = 0; i < maxsize; i++) {
				p[i] = i;
			}
			rank = new int[maxsize];
			oldNode = new int[maxsize];
			oldParent = new int[maxsize];
			oldRank = new int[maxsize];
		}

		int root(int x) {
			while (x != p[x])
				x = p[x];
			return x;
		}

		void unite(int a, int b) {
			a = root(a);
			b = root(b);
			if (a == b)
				return;
			if (rank[a] < rank[b]) {
				int t = a;
				a = b;
				b = t;
			}
			oldNode[size] = b;
			oldParent[size] = p[b];
			oldRank[size] = rank[a];
			++size;
			p[b] = a;
			if (rank[a] == rank[b])
				++rank[a];
		}

		void undo() {
			--size;
			int b = oldNode[size];
			rank[p[b]] = oldRank[size];
			p[b] = oldParent[size];
		}
	}

	static final int maxsize = 2 * 100000;
	static DisjointSets ds = new DisjointSets(maxsize);
	static int[] v1 = new int[maxsize];
	static int[] v2 = new int[maxsize];
	static int[] pr = new int[maxsize];
	static boolean[] res = new boolean[maxsize];
	static char[] query = new char[maxsize];

	static void solve(int l, int r) {
		if (l == r) {
			if (query[l] == 'c')
				res[l] = ds.root(v1[l]) == ds.root(v2[l]);
			return;
		}

		int oldSize = ds.size;
		int m = (l + r) >> 1;

		for (int i = l; i <= m; ++i)
			if (query[i] == 'a' && pr[i] > r)
				ds.unite(v1[i], v2[i]);
		solve(m + 1, r);
		while (ds.size > oldSize)
			ds.undo();

		for (int i = m + 1; i <= r; ++i)
			if (query[i] == 'r' && pr[i] < l)
				ds.unite(v1[i], v2[i]);
		solve(l, m);
		while (ds.size > oldSize)
			ds.undo();
	}

	public static void main(String[] args) throws Exception {
		long time = System.currentTimeMillis();
		int n = nextInt();
		int q = nextInt();
		Map<Long, Integer> m = new TreeMap<>();
		for (int i = 0; i < q; i++) {
			String type = nextToken();
			query[i] = type.charAt(0);
			int u = nextInt() - 1;
			int v = nextInt() - 1;
			v1[i] = Math.min(u, v);
			v2[i] = Math.max(u, v);
			long e = ((long) v1[i] << 32) + v2[i];
			if (query[i] == 'a') {
				m.put(e, i);
			} else if (query[i] == 'r') {
				int j = m.remove(e);
				pr[i] = j;
				pr[j] = i;
			}
		}
		for (Map.Entry<Long, Integer> entry : m.entrySet()) {
			query[q] = 'r';
			v1[q] = (int) (entry.getKey() >>> 32);
			v2[q] = entry.getKey().intValue();
			pr[q] = entry.getValue();
			pr[entry.getValue()] = q;
			++q;
		}
		solve(0, q - 1);
		PrintWriter pw = new PrintWriter(System.out);
		for (int i = 0; i < q; i++) {
			if (query[i] == 'c')
				pw.println(res[i] ? "YES" : "NO");
		}
		pw.close();
		System.err.println(System.currentTimeMillis() - time);
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