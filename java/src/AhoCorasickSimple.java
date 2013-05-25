import java.util.*;

public class AhoCorasickSimple {

	static final int ALPHABET_SIZE = 26;
	static final int TOTAL_SYMBOLS = 1000;

	int[][] graph = new int[TOTAL_SYMBOLS + 1][ALPHABET_SIZE];
	String[] prefixes = new String[TOTAL_SYMBOLS + 1];

	int go(String s, char ch) {
		s += ch;
		int best = -1;
		int res = -1;
		for (int i = 0; i < prefixes.length; i++)
			if (best < prefixes[i].length() && s.endsWith(prefixes[i])) {
				best = prefixes[i].length();
				res = i;
			}
		return res;
	}

	public void init(String[] words) {
		Set<String> prefixSet = new TreeSet<String>();
		for (String s : words)
			for (int i = 0; i <= s.length(); i++)
				prefixSet.add(s.substring(0, i));
		prefixes = prefixSet.toArray(new String[0]);
		for (int i = 0; i < prefixes.length; i++)
			for (int j = 0; j < ALPHABET_SIZE; j++)
				graph[i][j] = go(prefixes[i], (char) ('a' + j));
	}

	// Usage example
	public static void main(String[] args) {
	}
}
