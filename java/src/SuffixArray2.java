import java.util.*;

public class SuffixArray2 {

	// suffix array in O(n*log^2(n))
	public static Integer[] suffixArray(CharSequence s) {
		int n = s.length();
		Integer[] sa = new Integer[n];
		int[] rank = new int[n];
		for (int i = 0; i < n; i++) {
			sa[i] = i;
			rank[i] = s.charAt(i);
		}
		for (int len = 1; len < n; len *= 2) {
			long[] rank2 = new long[n];
			for (int i = 0; i < n; i++)
				rank2[i] = ((long) rank[i] << 32) + (i + len < n ? rank[i + len] + 1 : 0);

			Arrays.sort(sa, (a, b) -> Long.compare(rank2[a], rank2[b]));

			for (int i = 0; i < n; i++)
				rank[sa[i]] = i > 0 && rank2[sa[i - 1]] == rank2[sa[i]] ? rank[sa[i - 1]] : i;
		}
		return sa;
	}

	// random test
	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 100000; step++) {
			int n = rnd.nextInt(100);
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < n; i++)
				s.append((char) ('\0' + rnd.nextInt(10)));
			Integer[] sa = suffixArray(s);
			for (int i = 0; i + 1 < n; i++)
				if (s.substring(sa[i]).compareTo(s.substring(sa[i + 1])) >= 0)
					throw new RuntimeException("error");
		}
		System.out.println("Test passed");
	}
}