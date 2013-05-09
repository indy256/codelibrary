import java.util.*;

public class Pair implements Comparable<Pair> {
	final long u;
	final long v;

	public Pair(long u, long v) {
		this.u = u;
		this.v = v;
	}

	public int hashCode() {
		int hu = (int) (u ^ (u >>> 32));
		int hv = (int) (v ^ (v >>> 32));
		return 31 * hu + hv;
	}

	public boolean equals(Object o) {
		Pair other = (Pair) o;
		return u == other.u && v == other.v;
	}

	public int compareTo(Pair other) {
		return Long.compare(u, other.u) != 0 ? Long.compare(u, other.u) : Long.compare(v, other.v);
	}

	public String toString() {
		return "[u=" + u + ", v=" + v + "]";
	}

	// Usage example
	public static void main(String[] args) {
		Set<Pair> set1 = new TreeSet<>();
		Set<Pair> set2 = new HashSet<>();
		for (int i = 0; i < 20; i++) {
			Pair p = new Pair(i % 5, i % 10);
			set1.add(p);
			set2.add(p);
		}
		System.out.println(true == (set1.size() == set2.size()));
		System.out.println(set1);
		System.out.println(set2);
	}
}
