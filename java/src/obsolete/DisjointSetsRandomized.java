package obsolete;
import java.util.*;

public class DisjointSetsRandomized {
	static Random rnd = new Random();
	int[] p;

	void init() {
		for (int i = 0; i < p.length; i++) {
			p[i] = i;
		}
	}

	DisjointSetsRandomized(int size) {
		p = new int[size];
		init();
	}

	int root(int x) {
		if (x == p[x])
			return x;
		return p[x] = root(p[x]);
	}

	void unite(int a, int b) {
		a = root(a);
		b = root(b);
		if (a == b)
			return;
		if (rnd.nextBoolean()) {
			p[a] = b;
		} else {
			p[b] = a;
		}
	}

	public static void main(String[] args) {
		DisjointSetsRandomized ds = new DisjointSetsRandomized(10);
		System.out.println(false == (ds.root(0) == ds.root(3)));
		ds.unite(0, 3);
		System.out.println(true == (ds.root(0) == ds.root(3)));
	}
}
