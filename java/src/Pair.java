import java.util.*;

public class Pair<U extends Comparable<U>, V extends Comparable<V>> implements Comparable<Pair<U, V>> {
	final U u;
	final V v;

	public Pair(U u, V v) {
		this.u = u;
		this.v = v;
	}

	public int hashCode() {
		return (u == null ? 0 : u.hashCode() * 31) + (v == null ? 0 : v.hashCode());
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Pair<U, V> p = (Pair<U, V>) o;
		return (u == null ? p.u == null : u.equals(p.u)) && (v == null ? p.v == null : v.equals(p.v));
	}

	public int compareTo(Pair<U, V> b) {
		int cmpU = u.compareTo(b.u);
		return cmpU != 0 ? cmpU : v.compareTo(b.v);
	}

	// Usage example
	public static void main(String[] args) {
		Pair<Integer, Integer>[] a = new Pair[10];
		for (int i = 0; i < a.length; i++) {
			a[i] = new Pair(a.length - i, i);
		}
		Arrays.sort(a);
		for (Pair p : a) {
			System.out.println(p.u + " " + p.v);
		}
	}
}
