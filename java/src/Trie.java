import java.util.*;

public class Trie {
	static class TrieNode {
		Map<Character, TrieNode> children = new TreeMap<Character, TrieNode>();
		boolean leaf;
	}

	TrieNode root = new TrieNode();

	void insertString(String s) {
		TrieNode v = root;
		for (char ch : s.toCharArray()) {
			if (!v.children.containsKey(ch)) {
				v.children.put(ch, new TrieNode());
			}
			v = v.children.get(ch);
		}
		v.leaf = true;
	}

	// Usage example
	public static void main(String[] args) {
		Trie trie = new Trie();
		trie.insertString("hello");
		trie.insertString("world");
		trie.insertString("hi");
		printSorted(trie.root, "");
	}

	static void printSorted(TrieNode node, String s) {
		for (Character ch : node.children.keySet()) {
			printSorted(node.children.get(ch), s + ch);
		}
		if (node.leaf) {
			System.out.println(s);
		}
	}
}
