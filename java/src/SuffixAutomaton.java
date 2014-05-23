import java.util.*;

public class SuffixAutomaton {

	public static class State {
		int length;
		int link;
		int[] next = new int[128];

		{
			Arrays.fill(next, -1);
		}

		int endpos;
		List<Integer> ilink = new ArrayList<>(0);
	}

	public static State[] buildSuffixAutomaton(String s) {
		int n = s.length();
		State[] st = new State[Math.max(2, 2 * n - 1)];
		st[0] = new State();
		st[0].link = -1;
		st[0].endpos = -1;
		int last = 0;
		int size = 1;
		for (char c : s.toCharArray()) {
			int cur = size++;
			st[cur] = new State();
			st[cur].length = st[last].length + 1;
			st[cur].endpos = st[last].length;
			int p;
			for (p = last; p != -1 && st[p].next[c] == -1; p = st[p].link) {
				st[p].next[c] = cur;
			}
			if (p == -1) {
				st[cur].link = 0;
			} else {
				int q = st[p].next[c];
				if (st[p].length + 1 == st[q].length)
					st[cur].link = q;
				else {
					int clone = size++;
					st[clone] = new State();
					st[clone].length = st[p].length + 1;
					st[clone].next = st[q].next.clone();
					st[clone].link = st[q].link;
					for (; p != -1 && st[p].next[c] == q; p = st[p].link)
						st[p].next[c] = clone;
					st[q].link = clone;
					st[cur].link = clone;
					st[clone].endpos = -1;
				}
			}
			last = cur;
		}
		for (int i = 1; i < size; i++) {
			st[st[i].link].ilink.add(i);
		}
		return Arrays.copyOf(st, size);
	}

	// random tests
	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 100_000; step++) {
			int n1 = rnd.nextInt(10);
			int n2 = rnd.nextInt(10);
			String s1 = getRandomString(n1, rnd);
			String s2 = getRandomString(n2, rnd);
			String res1 = lcs(s1, s2);
			int res2 = slowLcs(s1, s2);
			if (res1.length() != res2)
				throw new RuntimeException();
		}
	}

	static int bestState;

	static String lcs(String a, String b) {
		State[] st = buildSuffixAutomaton(a);
		bestState = 0;
		int len = 0;
		int bestLen = 0;
		int bestPos = -1;
		for (int i = 0, cur = 0; i < b.length(); ++i) {
			char c = b.charAt(i);
			if (st[cur].next[c] == -1) {
				for (; cur != -1 && st[cur].next[c] == -1; cur = st[cur].link) {
				}
				if (cur == -1) {
					cur = 0;
					len = 0;
					continue;
				}
				len = st[cur].length;
			}
			++len;
			cur = st[cur].next[c];
			if (bestLen < len) {
				bestLen = len;
				bestPos = i;
				bestState = cur;
			}
		}
		return b.substring(bestPos - bestLen + 1, bestPos + 1);
	}

	static int[] occurrences(String haystack, String needle) {
		String common = lcs(haystack, needle);
		if (!common.equals(needle))
			return new int[0];
		List<Integer> list = new ArrayList<>();
		dfs(buildSuffixAutomaton(haystack), bestState, needle.length(), list);
		int[] res = new int[list.size()];
		for (int i = 0; i < res.length; i++)
			res[i] = list.get(i);
		Arrays.sort(res);
		return res;
	}

	static void dfs(State[] st, int p, int len, List<Integer> list) {
		if (st[p].endpos != -1 || p == 0)
			list.add(st[p].endpos - len + 1);
		for (int x : st[p].ilink)
			dfs(st, x, len, list);
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
