package obsolete;

import java.util.*;

public class SuffixArrayOld {

	// suffix array in O(n*log(n))
	public static int[] suffixArray(CharSequence str) {
		int n = str.length();
		// sa[i] - suffix on i'th position after sorting by first len characters
		int[] sa = new int[n];
		// rank[i] - position of the i'th suffix after sorting by first len characters
		int[] rank = new int[n];
		int maxlen = n;
		for (int i = 0; i < n; i++) {
			rank[i] = str.charAt(i);
			maxlen = Math.max(maxlen, str.charAt(i) + 1);
		}

		// counting sort of input characters
		int[] cnt = new int[maxlen];
		for (int i = 0; i < n; i++)
			++cnt[rank[i]];
		for (int i = 1; i < cnt.length; i++)
			cnt[i] += cnt[i - 1];
		for (int i = 0; i < n; i++)
			sa[--cnt[rank[i]]] = i;

		for (int len = 1; len < n; len *= 2) {
			// Suffixes are already sorted by first len characters.
			// Now sort suffixes by first len * 2 characters.
			int[] r = rank.clone();
			rank[sa[0]] = 0;
			for (int i = 1; i < n; i++) {
				int s1 = sa[i - 1];
				int s2 = sa[i];
				rank[s2] = r[s1] == r[s2] && Math.max(s1, s2) + len < n && r[s1 + len / 2] == r[s2 + len / 2] ? rank[s1] : i;
			}
			for (int i = 0; i < n; i++)
				cnt[i] = i;
			int[] s = sa.clone();
			for (int i = 0; i < n; i++) {
				// s[i] - order of suffixes sorted by first len characters
				// (s[i] - len) - order of suffixes sorted by second len characters
				int s1 = s[i] - len;
				if (s1 >= 0)
					sa[cnt[rank[s1]]++] = s1;
			}
		}
		return sa;
	}

	// longest common prefixes array in O(n)
	public static int[] lcp(int[] sa, String s) {
		int n = sa.length;
		int[] rank = new int[n];
		for (int i = 0; i < n; i++)
			rank[sa[i]] = i;
		int[] lcp = new int[n - 1];
		for (int i = 0, h = 0; i < n; i++) {
			if (rank[i] < n - 1) {
				for (int j = sa[rank[i] + 1]; Math.max(i, j) + h < s.length() && s.charAt(i + h) == s.charAt(j + h); ++h)
					;
				lcp[rank[i]] = h;
				if (h > 0)
					--h;
			}
		}
		return lcp;
	}

	// Usage example
	public static void main(String[] args) {
		String s = "abcab";
		int[] sa = suffixArray(s);

		// print suffixes in lexicographic order
		for (int p : sa)
			System.out.println(s.substring(p));

		System.out.println("lcp = " + Arrays.toString(lcp(sa, s)));

		// random test
		Random rnd = new Random();
		for (int step = 0; step < 100000; step++) {
			int n = rnd.nextInt(100);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < n; i++)
				sb.append((char) ('\0' + rnd.nextInt(10)));
			int[] a = suffixArray(sb);
			for (int i = 0; i + 1 < n; i++)
				if (sb.substring(a[i]).compareTo(sb.substring(a[i + 1])) >= 0)
					throw new RuntimeException("error");
		}
		System.out.println("Test passed");
	}
}
