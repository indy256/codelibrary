import java.util.*;

public class SuffixArray {

	// Builds suffix array in O(n*log(n))
	public static int[] suffixArray(String s) {
		s += '\0';
		int n = s.length();
		char[] a = s.toCharArray();
		int[] p = new int[n];
		int[] cnt256 = new int[256];
		for (int i = 0; i < n; i++) {
			++cnt256[a[i]];
		}
		for (int i = 1; i < 256; i++) {
			cnt256[i] += cnt256[i - 1];
		}
		for (int i = n - 1; i >= 0; i--) {
			p[--cnt256[a[i]]] = i;
		}
		int ranks = 0;
		int[] r = new int[n];
		int[] cnt = new int[n];
		for (int i = 1; i < n; i++) {
			if (a[p[i - 1]] != a[p[i]]) {
				cnt[++ranks] = i;
			}
			r[p[i]] = ranks;
		}
		int[] pp = new int[n];
		int[] rn = new int[n];
		for (int d = 1; d < n; d *= 2) {
			System.arraycopy(p, 0, pp, 0, n);
			for (int i = 0; i < n; i++) {
				int p1 = pp[i] - d;
				if (p1 >= 0) {
					p[cnt[r[p1]]++] = p1;
				}
			}
			ranks = 0;
			for (int i = 1; i < n; i++) {
				if (n - p[i] <= d * 2 || n - p[i - 1] <= d * 2 || r[p[i - 1]] != r[p[i]]
						|| r[p[i - 1] + d] != r[p[i] + d]) {
					cnt[++ranks] = i;
				}
				rn[p[i]] = ranks;
			}
			int[] t = r;
			r = rn;
			rn = t;
		}
		int[] res = new int[p.length - 1];
		for (int i = 0; i < res.length; i++)
			res[i] = p[i + 1];
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
