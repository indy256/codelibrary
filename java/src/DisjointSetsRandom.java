import java.util.*;

public class DisjointSetsRandom {
	static Random rnd = new Random();
	int[] p;

	void init() {
		for (int i = 0; i < p.length; i++) {
			p[i] = i;
		}
	}

	DisjointSetsRandom(int size) {
		p = new int[size];
		init();
	}

	int root(int x) {
		if (p[x] != x) {
			p[x] = root(p[x]);
		}
		return p[x];
	}

	void unite(int a, int b) {
		a = root(a);
		b = root(b);
		if (rnd.nextBoolean()) {
			p[a] = b;
		} else {
			p[b] = a;
		}
	}

	public static void main(String[] args) {
		DisjointSetsRandom ds = new DisjointSetsRandom(10);
		System.out.println(false == (ds.root(0) == ds.root(3)));
		ds.unite(0, 3);
		System.out.println(true == (ds.root(0) == ds.root(3)));
	}
}
