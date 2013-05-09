package experimental;

import java.io.*;
import java.util.*;

public class MLE {

	static class CoverTree {
		static double dist(Node p1, Node p2) {
			long dx = p1.x - p2.x;
			long dy = p1.y - p2.y;
			return Math.sqrt(dx * dx + dy * dy);
		}

		static class Node {
			long x, y;
			List<Node>[] children = new List[levels];
			int maxChildLevel = 0;
			static final List<Node> emptyList = new ArrayList<>();

			public void addChild(Node node, int level) {
				if (children[level] == null) {
					children[level] = new ArrayList<>();
				}
				children[level].add(node);
				maxChildLevel = Math.max(maxChildLevel, level);
			}

			public List<Node> getChildren(int level) {
				if (children[level] == null)
					return emptyList;
				return children[level];
			}

			public Node(long x, long y) {
				this.x = x;
				this.y = y;
			}
		}

		static final int levels = 64;
		double[] layerSize = new double[levels];

		public CoverTree() {
			layerSize[0] = 1 << 30;
			for (int i = 1; i < layerSize.length; i++)
				layerSize[i] = layerSize[i - 1] / 2;
		}

		Node root;

		public void insert(Node p) {
			if (root == null)
				root = p;
			else
				insert(p, Arrays.asList(new Node[] { root }), 0);
		}

		boolean insert(Node p, List<Node> Qi, int level) {
			Node parent = null;
			List<Node> nQi = new ArrayList<Node>();
			for (Node q : Qi) {
				if (dist(p, q) <= layerSize[level]) {
					nQi.add(q);
					parent = q;
				}
			}

			if (parent == null) // separation holds
				return true;

			for (Node q : Qi)
				for (Node ch : q.getChildren(level))
					if (dist(p, ch) <= layerSize[level])
						nQi.add(ch);

			if (insert(p, nQi, level + 1))
				parent.addChild(p, level);

			return false;
		}

		double bestDist;
		Node bestNode;

		public Node findNearest(Node p) {
			bestDist = p != root ? dist(p, root) : Double.POSITIVE_INFINITY;
			bestNode = p != root ? root : null;
			findNearest(p, Arrays.asList(new Node[] { root }), 0);
			return bestNode;
		}

		void findNearest(Node p, List<Node> Qi, int level) {
			for (; !Qi.isEmpty(); level++) {
				List<Node> Q = new ArrayList<>();
				for (Node q : Qi) {
					Q.add(q);
					for (Node ch : q.getChildren(level)) {
						if (ch != p) {
							double dist = dist(p, ch);
							if (bestDist > dist) {
								bestDist = dist;
								bestNode = ch;
							}
						}
						Q.add(ch);
					}
				}

				Qi = new ArrayList<Node>();
				for (Node q : Q)
					if (q.maxChildLevel > level && dist(p, q) <= bestDist + layerSize[level])
						Qi.add(q);
			}
		}
	}

	static long dist2(CoverTree.Node p1, CoverTree.Node p2) {
		long dx = p1.x - p2.x;
		long dy = p1.y - p2.y;
		return dx * dx + dy * dy;
	}

	// Usage example
	public static void main(String[] args) throws Exception {
		PrintWriter pw = new PrintWriter(System.out);
		int t = nextInt();
		for (int i = 0; i < t; i++) {
			CoverTree tree = new CoverTree();

			int n = nextInt();
			CoverTree.Node[] nodes = new CoverTree.Node[n];
			for (int j = 0; j < n; j++) {
				nodes[j] = new CoverTree.Node(nextInt(), nextInt());
				tree.insert(nodes[j]);
			}
			for (int j = 0; j < n; j++) {
				CoverTree.Node p = tree.findNearest(nodes[j]);
				pw.println(dist2(p, nodes[j]));
			}
		}
		pw.close();
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
