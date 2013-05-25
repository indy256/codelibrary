import java.util.*;

public class AhoCorasickSimple {

	static final int ALPHABET_SIZE = 26;

	String[] prefixes;

	public int[][] buildAutomata(String[] words) {
		Map<String, Integer> prefixMap = new TreeMap<String, Integer>();
		for (String s : words) {
			for (int i = 0; i <= s.length(); i++) {
				prefixMap.put(s.substring(0, i), 0);
			}
		}
		prefixes = prefixMap.keySet().toArray(new String[0]);
		for (int i = 0; i < prefixes.length; i++) {
			prefixMap.put(prefixes[i], i);
		}
		int[][] graph = new int[prefixes.length][ALPHABET_SIZE];
		for (int i = 0; i < prefixes.length; i++) {
			prefixMap.put(prefixes[i], i);
			for (int j = 0; j < ALPHABET_SIZE; j++) {
				String s = prefixes[i] + (char) ('a' + j);
				while (!prefixMap.containsKey(s)) {
					s = s.substring(1);
				}
				graph[i][j] = prefixMap.get(s);
			}
		}
		return graph;
	}

	// Usage example
	public static void main(String[] args) {
	}
}
