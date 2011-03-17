public class DisjointSets {
	int[] p;
	int[] rank;

	public DisjointSets(int size) {
		p = new int[size];
		for (int i = 0; i < size; i++) {
			p[i] = i;
		}
		rank = new int[size];
	}

	public int root(int x) {
		if (p[x] != x) {
			p[x] = root(p[x]);
		}
		return p[x];
	}

	public void unite(int a, int b) {
		a = root(a);
		b = root(b);
		if (rank[a] < rank[b]) {
			p[a] = b;
		} else {
			p[b] = a;
			if (rank[a] == rank[b]) {
				++rank[a];
			}
		}
	}

	public static void main(String[] args) {
		DisjointSets ds = new DisjointSets(10);
		System.out.println(false == (ds.root(0) == ds.root(3)));
		ds.unite(0, 3);
		System.out.println(true == (ds.root(0) == ds.root(3)));
	}
}
