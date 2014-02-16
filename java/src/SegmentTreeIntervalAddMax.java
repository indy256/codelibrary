public class SegmentTreeIntervalAddMax {

	int n;
	int[] tmax;
	int[] tadd; // tadd[i] affects tmax[i], tadd[2*i+1] and tadd[2*i+2]

	void push(int root) {
		tmax[root] += tadd[root];
		tadd[2 * root + 1] += tadd[root];
		tadd[2 * root + 2] += tadd[root];
		tadd[root] = 0;
	}

	public SegmentTreeIntervalAddMax(int n) {
		this.n = n;
		tmax = new int[4 * n];
		tadd = new int[4 * n];
	}

	public int max(int from, int to) {
		return max(from, to, 0, 0, n - 1);
	}

	int max(int from, int to, int root, int left, int right) {
		if (from == left && to == right) {
			return tmax[root] + tadd[root];
		}
		push(root);
		int mid = (left + right) >> 1;
		if (from <= mid && to > mid)
			return Math.max(
					max(from, Math.min(to, mid), 2 * root + 1, left, mid),
					max(Math.max(from, mid + 1), to, 2 * root + 2, mid + 1, right)
			);
		else if (from <= mid)
			return max(from, Math.min(to, mid), 2 * root + 1, left, mid);
		else if (to > mid)
			return max(Math.max(from, mid + 1), to, 2 * root + 2, mid + 1, right);
		else
			throw new RuntimeException();
	}

	public void add(int from, int to, int delta) {
		add(from, to, delta, 0, 0, n - 1);
	}

	void add(int from, int to, int delta, int root, int left, int right) {
		if (from == left && to == right) {
			tadd[root] += delta;
			return;
		}
		// push can be skipped for add, but is necessary for other operations such as set
		push(root);
		int mid = (left + right) >> 1;
		if (from <= mid)
			add(from, Math.min(to, mid), delta, 2 * root + 1, left, mid);
		if (to > mid)
			add(Math.max(from, mid + 1), to, delta, 2 * root + 2, mid + 1, right);
		tmax[root] = Math.max(tmax[2 * root + 1] + tadd[2 * root + 1], tmax[2 * root + 2] + tadd[2 * root + 2]);
	}

	// tests
	public static void main(String[] args) {
		SegmentTreeIntervalAddMax t = new SegmentTreeIntervalAddMax(10);
		t.add(0, 9, 1);
		t.add(2, 4, 2);
		t.add(3, 5, 3);
		System.out.println(t.max(0, 9));
		System.out.println(t.tmax[0] + t.tadd[0]);
		System.out.println(t.max(0, 0));
	}
}
