package experimental;

import java.util.*;

public class AhoCorasick2 {

	class Node {

		Map<Character, Node> edges = new TreeMap<>();
		List<Integer> accept = new ArrayList<>();
		Node suffLink;
	}

	int m;
	final Node root;

	public AhoCorasick2(String[] s) {
		m = s.length;
		root = new Node();
		for (int i = 0; i < m; i++) {
			Node node = root;
			for (char c : s[i].toCharArray()) {
				if (!node.edges.containsKey(c)) {
					node.edges.put(c, new Node());
				}
				node = node.edges.get(c);
			}
			node.accept.add(i);
		}
		Queue<Node> q = new LinkedList<>();
		q.add(root);
		while (!q.isEmpty()) {
			Node parent = q.remove();
			for (Map.Entry<Character, Node> edge : parent.edges.entrySet()) {
				char c = edge.getKey();
				Node u = edge.getValue();
				q.add(u);
				Node x = parent.suffLink;
				while (x != null && !x.edges.containsKey(c)) {
					x = x.suffLink;
				}
				u.suffLink = x != null ? x.edges.get(c) : root;
				u.accept.addAll(u.suffLink.accept);
			}
		}
	}

	public int[] searchFrom(char[] t) {
		int[] count = new int[m];
		Node u = root;
		for (char c : t) {
			while (u != null && !u.edges.containsKey(c)) {
				u = u.suffLink;
			}
			u = u != null ? u.edges.get(c) : root;
			for (int j : u.accept) {
				count[j]++;
			}
		}
		return count;
	}
}
