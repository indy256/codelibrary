package experimental.spoj;
import java.io.*;
import java.util.*;

public class DYNACON1 {

	static int[] left;
	static int[] right;
	static int[] parent;
	static boolean[] flip;

	static void init(int n) {
		left = new int[n];
		right = new int[n];
		parent = new int[n];
		flip = new boolean[n];
	}

	static void push(int x) {
		if (!flip[x])
			return;
		flip[x] = false;
		int t = left[x];
		left[x] = right[x];
		right[x] = t;
		if (left[x] != 0)
			flip[left[x]] = !flip[left[x]];
		if (right[x] != 0)
			flip[right[x]] = !flip[right[x]];
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

	// rotate edge (x, x.parent)
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
				push(g);
			push(p);
			push(x);
			if (!isRoot(p))
				rotate((x == left[p]) == (p == left[g]) ? p : x);
			rotate(x);
		}
		push(x);
	}

	// Makes node x the root of the virtual tree, and also x is the leftmost
	// node in its splay tree
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

	// prerequisite: x is a root node, y is in another tree
	public static void link(int x, int y) {
		expose(x);
		flip[x] = !flip[x];
		parent[x] = y;
	}

	static int findRoot(int x) {
		expose(x);
		while (right[x] != 0) {
			x = right[x];
			push(x);
		}
		return x;
	}

	public static boolean connected(int x, int y) {
		return findRoot(x) == findRoot(y);
	}

	public static void cut(int x, int y) {
		expose(x);
		flip[x] = !flip[x];
		expose(y);
		parent[right[y]] = 0;
		right[y] = 0;
	}

	public static void main(String[] args) throws Exception {
		long time = System.currentTimeMillis();
		PrintWriter pw = new PrintWriter(System.out);

		int n = nextInt();
		init(n + 1);

		int q = nextInt();
		for (int i = 0; i < q; i++) {
			String type = nextToken();
			if ("add".equals(type)) {
				int u = nextInt();
				int v = nextInt();
				link(u, v);
			} else if ("rem".equals(type)) {
				int u = nextInt();
				int v = nextInt();
				cut(u, v);
			} else {
				int u = nextInt();
				int v = nextInt();
				boolean conn = connected(u, v);
				pw.println(conn ? "YES" : "NO");
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