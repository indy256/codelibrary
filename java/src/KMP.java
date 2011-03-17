public class KMP {
	public static int[] prefixFunction(String s) {
		int n = s.length();
		int[] p = new int[n];
		for (int i = 1; i < n; i++) {
			int k = p[i - 1];
			while (k > 0 && s.charAt(k) != s.charAt(i)) {
				k = p[k - 1];
			}
			p[i] = k + (s.charAt(k) == s.charAt(i) ? 1 : 0);
		}
		return p;
	}

	public static int kmpMatcher(String s, String pattern) {
		int m = pattern.length();
		if (m == 0) {
			return 0;
		}
		int n = s.length();
		int[] p = prefixFunction(pattern);
		int k = 0;
		for (int i = 0; i < n; i++) {
			while (k > 0 && s.charAt(i) != pattern.charAt(k)) {
				k = p[k - 1];
			}
			if (s.charAt(i) == pattern.charAt(k)) {
				k++;
			}
			if (k == m) {
				return i - m + 1;
			}
		}
		return -1;
	}

	// Usage example
	public static void main(String[] args) {
		int pos = kmpMatcher("00101", "010");
		System.out.println(pos == 1);
	}
}
