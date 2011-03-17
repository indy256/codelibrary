import java.util.*;

public class SuffixArray2 {

	public static int[] suffixArray(String s) {
		int n = s.length();
		Integer[] sa = new Integer[n];
		int[] rank = new int[n];
		for (int i = 0; i < n; i++) {
			sa[i] = i;
			rank[i] = s.charAt(i);
		}
		final long[] key = new long[n];

		for (int k = 1; k < n; k *= 2) {
			for (int i = 0; i < n; i++)
				key[i] = ((long) rank[i] << 32) + (i + k < n ? rank[i + k] + 1 : 0);

			Arrays.sort(sa, new Comparator<Integer>() {
				public int compare(Integer i, Integer j) {
					return key[i] < key[j] ? -1 : key[i] > key[j] ? 1 : 0;
				}
			});

			for (int i = 0; i < n; i++)
				rank[sa[i]] = i > 0 && key[sa[i - 1]] == key[sa[i]] ? rank[sa[i - 1]] : i;
		}

		int[] res = new int[n];
		for (int i = 0; i < n; i++)
			res[i] = sa[i];
		return res;
	}

	public static int[] lcp(int[] sa, String s) {
		int n = sa.length;
		if (n <= 1)
			return new int[0];
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
		for (int p : sa) {
			System.out.println(s.substring(p));
		}

		System.out.println("lcp = " + Arrays.toString(lcp(sa, s)));
	}
}
