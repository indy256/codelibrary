import java.util.*;

// https://en.wikipedia.org/wiki/Aho–Corasick_algorithm
public class AhoCorasickQueue {

	static final int ALPHABET_SIZE = 26;

	Node[] nodes;
	int nodeCount;

	public static class Node {
		int parent;
		char charFromParent;
		int suffLink = -1;
		int[] children = new int[ALPHABET_SIZE];
		int[] transitions = new int[ALPHABET_SIZE];
		boolean leaf;

		{
			Arrays.fill(children, -1);
			Arrays.fill(transitions, -1);
		}
	}

	public AhoCorasickQueue(int maxNodes) {
		nodes = new Node[maxNodes];
		// create root
		nodes[0] = new Node();
		nodes[0].suffLink = 0;
		nodes[0].parent = -1;
		nodeCount = 1;
	}

	public void addString(String s) {
		int cur = 0;
		for (char ch : s.toCharArray()) {
			int c = ch - 'a';
			if (nodes[cur].children[c] == -1) {
				nodes[nodeCount] = new Node();
				nodes[nodeCount].parent = cur;
				nodes[nodeCount].charFromParent = ch;
				nodes[cur].children[c] = nodeCount++;
			}
			cur = nodes[cur].children[c];
		}
		nodes[cur].leaf = true;
	}

	public void bfs() {
		int st = 0;
		int fi = 1;
		int[] que = new int[nodeCount];
		que[0] = 0;
		while (st < fi) {
			int v = que[st++];
			int u = nodes[v].suffLink;
			for (int c = 0; c < ALPHABET_SIZE; c++) {
				if (nodes[v].children[c] != -1) {
					nodes[nodes[v].children[c]].suffLink = v!=0 ? nodes[v].children[c] : 0;
					que[fi++] = nodes[v].children[c];
				} else {
//					to[v][c] = to[u][c];
				}
			}
		}
	}

	public int suffLink(int nodeIndex) {
		Node node = nodes[nodeIndex];
		if (node.suffLink == -1)
			node.suffLink = node.parent == 0 ? 0 : transition(suffLink(node.parent), node.charFromParent);
		return node.suffLink;
	}

	public int transition(int nodeIndex, char ch) {
		int c = ch - 'a';
		Node node = nodes[nodeIndex];
		if (node.transitions[c] == -1)
			node.transitions[c] = node.children[c] != -1 ? node.children[c] : (nodeIndex == 0 ? 0 : transition(suffLink(nodeIndex), ch));
		return node.transitions[c];
	}

	// Usage example
	public static void main(String[] args) {
		AhoCorasickQueue ahoCorasick = new AhoCorasickQueue(1000);
		ahoCorasick.addString("bc");
		ahoCorasick.addString("abc");

		String s = "tabcbc";
		int node = 0;
		List<Integer> positions = new ArrayList<>();
		for (int i = 0; i < s.length(); i++) {
			node = ahoCorasick.transition(node, s.charAt(i));
			if (ahoCorasick.nodes[node].leaf)
				positions.add(i);
		}
		System.out.println(positions);
	}
}
