public class Trie {
	static class TrieNode {
		TrieNode[] children = new TrieNode[26];
		boolean leaf;
	}

	public static void insertString(TrieNode root, String s) {
		TrieNode v = root;
		for (char ch : s.toCharArray()) {
			TrieNode next = v.children[ch - 'a'];
			if (next == null)
				v.children[ch - 'a'] = next = new TrieNode();
			v = next;
		}
		v.leaf = true;
	}

	public static void printSorted(TrieNode node, String s) {
		for (char ch = 'a'; ch <= 'z'; ch++) {
			TrieNode child = node.children[ch - 'a'];
			if (child != null)
				printSorted(child, s + ch);
		}
		if (node.leaf) {
			System.out.println(s);
		}
	}

	// Usage example
	public static void main(String[] args) {
		TrieNode root = new TrieNode();
		insertString(root, "hello");
		insertString(root, "world");
		insertString(root, "hi");
		printSorted(root, "");
	}
}
