package obsolete;

public class HashingOld {

	final int prime = 1000000009;
	long[] p;
	long[] hash;
	int n;

	public void buildHash(String s) {
		n = s.length();
		p = new long[n + 1];
		hash = new long[n + 1];

		p[0] = 1;
		for (int i = 0; i < n; i++) {
			hash[i + 1] = hash[i] + s.charAt(i) * p[i];
			p[i + 1] = p[i] * prime;
		}
	}

	public long getHash(int i, int len) {
		return (hash[i + len] - hash[i]) * p[n - i - len];
	}

	public boolean equals(int i, int j, int len) {
		return getHash(i, len) == getHash(j, len);
	}

	// Usage example
	public static void main(String[] args) {
		String s = "123123";
		HashingOld h = new HashingOld();
		h.buildHash(s);

		System.out.println(true == h.equals(0, 3, 3));
		System.out.println(false == h.equals(1, 3, 2));
	}
}
