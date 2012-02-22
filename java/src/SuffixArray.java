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
		int classes = 0;
		int[] c = new int[n];
		int[] cnt = new int[n];
		for (int i = 1; i < n; i++) {
			if (a[p[i - 1]] != a[p[i]]) {
				cnt[++classes] = i;
			}
			c[p[i]] = classes;
		}
		int[] pn = new int[n];
		int[] cn = new int[n];
		for (int d = 1; d < n; d *= 2) {
			System.arraycopy(p, 0, pn, 0, n);
			for (int i = 0; i < n; i++) {
				int p1 = pn[i] - d;
				if (p1 >= 0) {
					p[cnt[c[p1]]++] = p1;
				}
			}
			classes = 0;
			for (int i = 1; i < n; i++) {
				if (n - p[i] <= d * 2 || n - p[i - 1] <= d * 2 || c[p[i - 1]] != c[p[i]]
						|| c[p[i - 1] + d] != c[p[i] + d]) {
					cnt[++classes] = i;
				}
				cn[p[i]] = classes;
			}
			final int[] t = c;
			c = cn;
			cn = t;
		}
		int[] res = new int[p.length - 1];
		System.arraycopy(p, 1, res, 0, res.length);
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
