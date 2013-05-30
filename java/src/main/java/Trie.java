import java.util.*;

public class Trie {
	static class TrieNode {
		Map<Character, TrieNode> children = new TreeMap<>();
		boolean leaf;
	}

	public static void insertString(TrieNode root, String s) {
		TrieNode v = root;
		for (char ch : s.toCharArray()) {
			TrieNode next = v.children.get(ch);
			if (next == null)
				v.children.put(ch, next = new TrieNode());
			v = next;
		}
		v.leaf = true;
	}

	public static void printSorted(TrieNode node, String s) {
		for (Character ch : node.children.keySet()) {
			printSorted(node.children.get(ch), s + ch);
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
