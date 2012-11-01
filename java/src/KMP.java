public class KMP {

	public static int[] prefixFunction(String s) {
		int[] p = new int[s.length()];
		for (int i = 1, k = 0; i < s.length(); i++)
			for (;; k = p[k - 1]) {
				if (s.charAt(k) == s.charAt(i)) {
					p[i] = ++k;
					break;
				}
				if (k == 0)
					break;
			}
		return p;
	}

	public static int kmpMatcher(String s, String pattern) {
		int m = pattern.length();
		if (m == 0)
			return 0;
		int[] p = prefixFunction(pattern);
		for (int i = 0, k = 0; i < s.length(); i++)
			for (;; k = p[k - 1]) {
				if (pattern.charAt(k) == s.charAt(i)) {
					if (++k == m)
						return i + 1 - m;
					break;
				}
				if (k == 0)
					break;
			}
		return -1;
	}

	// Usage example
	public static void main(String[] args) {
		int pos = kmpMatcher("00101", "010");
		System.out.println(1 == pos);
	}
}
