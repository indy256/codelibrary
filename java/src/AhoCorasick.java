public class AhoCorasick {

	static final int ALPHABET_SIZE = 26;

	static class Node {
		Node[] children = new Node[ALPHABET_SIZE];
		boolean leaf;
		Node parent;
		char charToParent;
		Node suffLink;
		Node[] go = new Node[ALPHABET_SIZE];
	}

	public static Node createRoot() {
		Node node = new Node();
		node.suffLink = node;
		return node;
	}

	public static void addString(Node node, String s) {
		for (char ch : s.toCharArray()) {
			int c = ch - 'a';
			if (node.children[c] == null) {
				Node n = new Node();
				n.parent = node;
				n.charToParent = ch;
				node.children[c] = n;
			}
			node = node.children[c];
		}
		node.leaf = true;
	}

	public static Node go(Node node, char ch) {
		int c = ch - 'a';
		if (node.go[c] == null) {
			if (node.children[c] != null) {
				node.go[c] = node.children[c];
			} else {
				node.go[c] = node.parent == null ? node : go(suffLink(node), ch);
			}
		}
		return node.go[c];
	}

	public static Node suffLink(Node node) {
		if (node.suffLink == null) {
			if (node.parent.parent == null) {
				node.suffLink = node.parent;
			} else {
				node.suffLink = go(suffLink(node.parent), node.charToParent);
			}
		}
		return node.suffLink;
	}

	// Usage example
	public static void main(String[] args) {
		Node tree = createRoot();
		addString(tree, "bc");
		addString(tree, "abc");

		String s = "tabc";
		Node node = tree;
		for (char ch : s.toCharArray()) {
			node = go(node, ch);
		}
		System.out.println(node.leaf);
	}
}
