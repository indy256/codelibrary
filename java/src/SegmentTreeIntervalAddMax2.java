public class SegmentTreeIntervalAddMax2 {

	int n;
	int[] tmax;
	int[] tadd; // tadd[i] affects tmax[2*i+1], tmax[2*i+2], tadd[2*i+1] and tadd[2*i+2]

	public SegmentTreeIntervalAddMax2(int n) {
		this.n = n;
		tmax = new int[4 * n];
		tadd = new int[4 * n];
	}

	public int max(int a, int b) {
		return max(a, b, 0, 0, n - 1);
	}

	int max(int a, int b, int root, int left, int right) {
		if (a == left && b == right) {
			return tmax[root];
		}
		push(root);
		int mid = (left + right) >> 1;
		int res1 = 0;
		if (a <= mid)
			res1 = max(a, Math.min(b, mid), 2 * root + 1, left, mid);
		int res2 = 0;
		if (b > mid)
			res2 = max(Math.max(a, mid + 1), b, 2 * root + 2, mid + 1, right);
		return Math.max(res1, res2);
	}

	void push(int root) {
		tmax[2 * root + 1] += tadd[root];
		tmax[2 * root + 2] += tadd[root];
		tadd[2 * root + 1] += tadd[root];
		tadd[2 * root + 2] += tadd[root];
		tadd[root] = 0;
	}

	public void add(int a, int b, int delta) {
		add(a, b, delta, 0, 0, n - 1);
	}

	void add(int a, int b, int delta, int root, int left, int right) {
		if (a == left && b == right) {
			tadd[root] += delta;
			tmax[root] += delta;
			return;
		}
		push(root);
		int mid = (left + right) >> 1;
		if (a <= mid)
			add(a, Math.min(b, mid), delta, 2 * root + 1, left, mid);
		if (b > mid)
			add(Math.max(a, mid + 1), b, delta, 2 * root + 2, mid + 1, right);
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
