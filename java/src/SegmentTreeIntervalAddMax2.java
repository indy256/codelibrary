public class SegmentTreeIntervalAddMax2 {

	int n;
	int[] tmax;
	int[] tadd; // tadd[i] affects tmax[2*i+1], tmax[2*i+2], tadd[2*i+1] and tadd[2*i+2]

	void push(int root) {
		tmax[2 * root + 1] += tadd[root];
		tmax[2 * root + 2] += tadd[root];
		tadd[2 * root + 1] += tadd[root];
		tadd[2 * root + 2] += tadd[root];
		tadd[root] = 0;
	}

	public SegmentTreeIntervalAddMax2(int n) {
		this.n = n;
		tmax = new int[4 * n];
		tadd = new int[4 * n];
	}

	public int max(int from, int to) {
		return max(from, to, 0, 0, n - 1);
	}

	int max(int from, int to, int root, int left, int right) {
		if (from == left && to == right) {
			return tmax[root];
		}
		push(root);
		int mid = (left + right) >> 1;
		int res = Integer.MIN_VALUE;
		if (from <= mid)
			res = Math.max(res, max(from, Math.min(to, mid), 2 * root + 1, left, mid));
		else if (to > mid)
			res = Math.max(res, max(Math.max(from, mid + 1), to, 2 * root + 2, mid + 1, right));
		return res;
	}

	public void add(int from, int to, int delta) {
		add(from, to, delta, 0, 0, n - 1);
	}

	void add(int from, int to, int delta, int root, int left, int right) {
		if (from == left && to == right) {
			tadd[root] += delta;
			tmax[root] += delta;
			return;
		}
		push(root);
		int mid = (left + right) >> 1;
		if (from <= mid)
			add(from, Math.min(to, mid), delta, 2 * root + 1, left, mid);
		if (to > mid)
			add(Math.max(from, mid + 1), to, delta, 2 * root + 2, mid + 1, right);
		tmax[root] = Math.max(tmax[2 * root + 1], tmax[2 * root + 2]);
	}

	public static void main(String[] args) {
		SegmentTreeIntervalAddMax2 t = new SegmentTreeIntervalAddMax2(10);
		t.add(0, 9, 1);
		t.add(2, 4, 2);
		t.add(3, 5, 3);
		System.out.println(t.max(0, 9));
		System.out.println(t.tmax[0]);
		System.out.println(t.max(0, 0));
	}
}
