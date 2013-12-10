public class BinaryHeap {
	int[] h;
	int size;

	public BinaryHeap(int n) {
		h = new int[n];
	}

	// build heap in O(n)
	public BinaryHeap(int[] keys) {
		h = keys.clone();
		size = keys.length;
		for (int pos = size / 2 - 1; pos >= 0; pos--) {
			moveDown(pos);
		}
	}

	public void add(int node) {
		h[size++] = node;
		moveUp(h[size - 1]);
	}

	public int remove() {
		int removed = h[0];
		h[0] = h[--size];
		moveDown(0);
		return removed;
	}

	void moveUp(int pos) {
		while (pos > 0) {
			int parent = (pos - 1) / 2;
			if (h[pos] >= h[parent]) {
				break;
			}
			swap(h, pos, parent);
			pos = parent;
		}
	}

	void moveDown(int pos) {
		while (pos < size / 2) {
			int child = 2 * pos + 1;
			if (child + 1 < size && h[child] > h[child + 1]) {
				++child;
			}
			if (h[pos] <= h[child]) {
				break;
			}
			swap(h, pos, child);
			pos = child;
		}
	}

	static void swap(int[] a, int i, int j) {
		int t = a[i];
		a[i] = a[j];
		a[j] = t;
	}

	// Usage example
	public static void main(String[] args) {
		BinaryHeap heap = new BinaryHeap(10);
		heap.add(2);
		heap.add(5);
		heap.add(1);

		// print elements in sorted order
		while (heap.size > 0) {
			int x = heap.remove();
			System.out.println(x);
		}
	}
}