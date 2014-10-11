import java.util.*;

public class SuffixAutomaton {

	public static class State {
		int length;
		int suffLink;
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
		st[0].suffLink = -1;
		st[0].endpos = -1;
		int last = 0;
		int size = 1;
		for (char c : s.toCharArray()) {
			int cur = size++;
			st[cur] = new State();
			st[cur].length = st[last].length + 1;
			st[cur].endpos = st[last].length;
			int p;
			for (p = last; p != -1 && st[p].next[c] == -1; p = st[p].suffLink) {
				st[p].next[c] = cur;
			}
			if (p == -1) {
				st[cur].suffLink = 0;
			} else {
				int q = st[p].next[c];
				if (st[p].length + 1 == st[q].length)
					st[cur].suffLink = q;
				else {
					int clone = size++;
					st[clone] = new State();
					st[clone].length = st[p].length + 1;
					st[clone].next = st[q].next.clone();
					st[clone].suffLink = st[q].suffLink;
					for (; p != -1 && st[p].next[c] == q; p = st[p].suffLink)
						st[p].next[c] = clone;
					st[q].suffLink = clone;
					st[cur].suffLink = clone;
					st[clone].endpos = -1;
				}
			}
			last = cur;
		}
		for (int i = 1; i < size; i++) {
			st[st[i].suffLink].ilink.add(i);
		}
		return Arrays.copyOf(st, size);
	}

	// random tests
	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 100_000; step++) {
			int n1 = rnd.nextInt(20);
			int n2 = rnd.nextInt(20) + 1;
			String a = getRandomString(n1, rnd);
			String b = getRandomString(n2, rnd);
			String res1 = lcs(a, b);
			int res2 = slowLcs(a, b);
			if (res1.length() != res2)
				throw new RuntimeException();
			Integer[] occurrences1 = occurrences(a, b);
			List<Integer> occurrences2 = new ArrayList<>();
			for (int p = a.indexOf(b); p != -1; p = a.indexOf(b, p + 1))
				occurrences2.add(p);
			if (!Arrays.equals(occurrences1, occurrences2.toArray(new Integer[occurrences2.size()])))
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
				for (; cur != -1 && st[cur].next[c] == -1; cur = st[cur].suffLink) {
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

	static Integer[] occurrences(String haystack, String needle) {
		String common = lcs(haystack, needle);
		if (!common.equals(needle))
			return new Integer[0];
		List<Integer> list = new ArrayList<>();
		occurrencesDfs(buildSuffixAutomaton(haystack), bestState, needle.length(), list);
		Collections.sort(list);
		return list.toArray(new Integer[list.size()]);
	}

	static void occurrencesDfs(State[] st, int p, int len, List<Integer> list) {
		if (st[p].endpos != -1 || p == 0)
			list.add(st[p].endpos - len + 1);
		for (int x : st[p].ilink)
			occurrencesDfs(st, x, len, list);
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
