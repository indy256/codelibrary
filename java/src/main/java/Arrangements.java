import java.util.Arrays;

public class Arrangements {
	public static boolean nextArrangement(int[] p, int n) {
		boolean[] used = new boolean[n];
		for (int x : p) {
			used[x] = true;
		}
		int m = p.length;
		for (int i = m - 1; i >= 0; i--) {
			used[p[i]] = false;
			for (int j = p[i] + 1; j < n; j++) {
				if (!used[j]) {
					p[i++] = j;
					used[j] = true;
					for (int k = 0; k < n && i < m; k++) {
						if (!used[k]) {
							p[i++] = k;
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	public static boolean nextArrangement2(int[] p, int n) {
		if (nextPermutation(p)) {
			return true;
		}
		for (int i = 0, j = p.length - 1; i < j; i++, j--) {
			int t = p[i];
			p[i] = p[j];
			p[j] = t;
		}
		return nextCombination(p, n);
	}

	public static boolean nextArrangementWithRepeats(int[] p, int n) {
		for (int i = 0; i < p.length; i++) {
			if (p[i] < n - 1) {
				++p[i];
				while (--i >= 0) {
					p[i] = 0;
				}
				return true;
			}
		}
		return false;
	}

	// auxiliary
	static boolean nextCombination(int[] p, int n) {
		int m = p.length;
		for (int i = m - 1; i >= 0; i--) {
			if (p[i] < n + i - m) {
				++p[i];
				while (++i < m) {
					p[i] = p[i - 1] + 1;
				}
				return true;
			}
		}
		return false;
	}

	// auxiliary
	static boolean nextPermutation(int[] p) {
		for (int a = p.length - 2; a >= 0; --a)
			if (p[a] < p[a + 1])
				for (int b = p.length - 1;; --b)
					if (p[b] > p[a]) {
						int t = p[a];
						p[a] = p[b];
						p[b] = t;
						for (++a, b = p.length - 1; a < b; ++a, --b) {
							t = p[a];
							p[a] = p[b];
							p[b] = t;
						}
						return true;
					}
		return false;
	}

	// Usage example
	public static void main(String[] args) {
		// print all arrangements
		int[] p = { 0, 1, 2 };
		do {
			System.out.println(Arrays.toString(p));
		} while (nextArrangement(p, 4));

	}
}
