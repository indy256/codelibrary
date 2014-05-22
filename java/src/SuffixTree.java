import java.util.Random;

public class SuffixTree {
	static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz1234567890\1\2";

	public static class Node {
		int begin;
		int end;
		int depth; // distance in characters from tree root to this node
		Node parent;
		Node[] children;
		Node suffixLink; // null means link to root

		Node(int begin, int end, int depth, Node parent) {
			children = new Node[ALPHABET.length()];
			this.begin = begin;
			this.end = end;
			this.parent = parent;
			this.depth = depth;
		}

		boolean contains(int d) {
			return depth <= d && d < depth + (end - begin);
		}
	}

	public static Node buildSuffixTree(CharSequence s) {
		int n = s.length();
		byte[] a = new byte[n];
		for (int i = 0; i < n; i++)
			a[i] = (byte) ALPHABET.indexOf(s.charAt(i));
		Node root = new Node(0, 0, 0, null);
		Node cn = root;
		// root.suffixLink must be null, but that way it gets more convenient processing
		root.suffixLink = root;
		Node needsSuffixLink = null;
		int lastRule = 0;
		int j = 0;
		for (int i = -1; i < n - 1; i++) {// strings s[j..i] are already in tree, add s[i+1] to it
			int cur = a[i + 1]; // last char of current string
			for (; j <= i + 1; j++) {
				int curDepth = i + 1 - j;
				if (lastRule != 3) {
					cn = cn.suffixLink != null ? cn.suffixLink : cn.parent.suffixLink;
					int k = j + cn.depth;
					while (curDepth > 0 && !cn.contains(curDepth - 1)) {
						k += cn.end - cn.begin;
						cn = cn.children[a[k]];
					}
				}
				if (!cn.contains(curDepth)) { // explicit node
					if (needsSuffixLink != null) {
						needsSuffixLink.suffixLink = cn;
						needsSuffixLink = null;
					}
					if (cn.children[cur] == null) {
						// no extension - add leaf
						cn.children[cur] = new Node(i + 1, n, curDepth, cn);
						lastRule = 2;
					} else {
						cn = cn.children[cur];
						lastRule = 3; // already exists
						break;
					}
				} else { // implicit node
					int end = cn.begin + curDepth - cn.depth;
					if (a[end] != cur) { // split implicit node here
						Node newn = new Node(cn.begin, end, cn.depth, cn.parent);
						newn.children[cur] = new Node(i + 1, n, curDepth, newn);
						newn.children[a[end]] = cn;
						cn.parent.children[a[cn.begin]] = newn;
						if (needsSuffixLink != null) {
							needsSuffixLink.suffixLink = newn;
						}
						cn.begin = end;
						cn.depth = curDepth;
						cn.parent = newn;
						cn = needsSuffixLink = newn;
						lastRule = 2;
					} else if (cn.end != n || cn.begin - cn.depth < j) {
						lastRule = 3;
						break;
					} else {
						lastRule = 1;
					}
				}
			}
		}
		root.suffixLink = null;
		return root;
	}

	// random test
	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 100_000; step++) {
			int n1 = rnd.nextInt(10);
			int n2 = rnd.nextInt(10);
			String s1 = getRandomString(n1, rnd);
			String s2 = getRandomString(n2, rnd);
			// build generalized suffix tree (see Gusfield, p.125)
			String s = s1 + '\1' + s2 + '\2';
			Node tree = buildSuffixTree(s);
			lcsLength = 0;
			lcsBeginIndex = 0;
			// find longest common substring
			lcs(tree, s1.length(), s1.length() + s2.length() + 1);
			int res2 = slowLcs(s1, s2);
			if (lcsLength != res2) {
				System.err.println(s.substring(lcsBeginIndex - 1, lcsBeginIndex + lcsLength - 1));
				throw new RuntimeException();
			}
		}
	}

	static int lcsLength;
	static int lcsBeginIndex;

	// traverse suffix tree to find longest common substring
	public static int lcs(Node node, int i1, int i2) {
		if (node.begin <= i1 && i1 < node.end) {
			return 1;
		}
		if (node.begin <= i2 && i2 < node.end) {
			return 2;
		}
		int mask = 0;
		for (char f = 0; f < ALPHABET.length(); f++) {
			if (node.children[f] != null) {
				mask |= lcs(node.children[f], i1, i2);
			}
		}
		if (mask == 3) {
			int curLength = node.depth + node.end - node.begin;
			if (lcsLength < curLength) {
				lcsLength = curLength;
				lcsBeginIndex = node.begin;
			}
		}
		return mask;
	}

	static int slowLcs(String a, String b) {
		int[][] lcs = new int[a.length()][b.length()];
		int res = 0;
		for (int i = 0; i < a.length(); i++) {
			for (int j = 0; j < b.length(); j++) {
				if (a.charAt(i) == b.charAt(j))
					lcs[i][j] = 1 + (i > 0 && j > 0 ? lcs[i - 1][j - 1] : 0);
				res = Math.max(res, lcs[i][j]);
			}
		}
		return res;
	}

	static String getRandomString(int n, Random rnd) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append((char) ('a' + rnd.nextInt(3)));
		}
		return sb.toString();
	}
}
