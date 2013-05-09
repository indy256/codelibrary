package experimental.spoj;
import java.io.*;
import java.util.*;

public class DYNALCA {

	static int[] left;
	static int[] right;
	static int[] parent;

	static void init(int n) {
		left = new int[n];
		right = new int[n];
		parent = new int[n];
	}

	// Whether x is a root of a splay tree
	static boolean isRoot(int x) {
		return parent[x] == 0 || (left[parent[x]] != x && right[parent[x]] != x);
	}

	static void connect(int ch, int p, boolean leftChild) {
		if (leftChild)
			left[p] = ch;
		else
			right[p] = ch;
		if (ch != 0)
			parent[ch] = p;
	}

	static void rotate(int x) {
		int p = parent[x];
		int g = parent[p];
		boolean isRootP = isRoot(p);
		boolean leftChildX = (x == left[p]);

		connect(leftChildX ? right[x] : left[x], p, leftChildX);
		connect(p, x, !leftChildX);

		if (!isRootP)
			connect(x, g, p == left[g]);
		else
			parent[x] = g;
	}

	static void splay(int x) {
		while (!isRoot(x)) {
			int p = parent[x];
			int g = parent[p];
			if (!isRoot(p))
				rotate((x == left[p]) == (p == left[g]) ? p : x);
			rotate(x);
		}
	}

	// Makes node x the root of the virtual tree, and also x is the leftmost node in its splay tree
	static int expose(int x) {
		int last = 0;
		for (int y = x; y != 0; y = parent[y]) {
			splay(y);
			left[y] = last;
			last = y;
		}
		splay(x);
		return last;
	}

	public static int findRoot(int x) {
		expose(x);
		while (right[x] != 0)
			x = right[x];
		return x;
	}

	// prerequisite: x is a root node, y is in another tree
	public static void link(int x, int y) {
		expose(x);
		if (right[x] != 0)
			throw new RuntimeException("error: x is not a root node");
		parent[x] = y;
	}

	public static void cut(int x) {
		expose(x);
		if (right[x] == 0)
			throw new RuntimeException("error: x is a root node");
		parent[right[x]] = 0;
		right[x] = 0;
	}

	public static int lca(int x, int y) {
		if (findRoot(x) != findRoot(y))
			throw new RuntimeException("error: x and y are not connected");
		expose(x);
		return expose(y);
	}

	// Usage example
	public static void main(String[] args) throws Exception {
		long time = System.currentTimeMillis();
		PrintWriter pw = new PrintWriter(System.out);

		int n = nextInt();
		init(n + 1);

		int q = nextInt();
		for (int i = 0; i < q; i++) {
			String type = nextToken();
			if ("link".equals(type)) {
				int u = nextInt();
				int v = nextInt();
				link(u, v);
			} else if ("cut".equals(type)) {
				int u = nextInt();
				cut(u);
			} else {
				int u = nextInt();
				int v = nextInt();
				int lca = lca(u, v);
				pw.println(lca);
			}
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
