import java.util.*;

public class AhoCorasickSimple {

	static final int ALPHABET_SIZE = 26;

	String[] prefixes;

	public int[][] buildAutomata(String[] words) {
		Map<String, Integer> prefixMap = new TreeMap<>();
		for (String s : words) {
			for (int i = 0; i <= s.length(); i++) {
				prefixMap.put(s.substring(0, i), 0);
			}
		}
		prefixes = prefixMap.keySet().toArray(new String[0]);
		for (int i = 0; i < prefixes.length; i++) {
			prefixMap.put(prefixes[i], i);
		}
		int[][] transitions = new int[prefixes.length][ALPHABET_SIZE];
		for (int i = 0; i < prefixes.length; i++) {
			for (int j = 0; j < ALPHABET_SIZE; j++) {
				String s = prefixes[i] + (char) ('a' + j);
				while (!prefixMap.containsKey(s)) {
					s = s.substring(1);
				}
				transitions[i][j] = prefixMap.get(s);
			}
		}
		return transitions;
	}

	// Usage example
	public static void main(String[] args) {
	}
}
