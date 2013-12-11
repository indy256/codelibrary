public class BinaryHeap {
	int[] heap;
	int size;

	public BinaryHeap(int n) {
		heap = new int[n];
	}

	// build heap in O(n)
	public BinaryHeap(int[] keys) {
		heap = keys.clone();
		size = keys.length;
		for (int pos = size / 2 - 1; pos >= 0; pos--) {
			moveDown(pos);
		}
	}

	public void add(int node) {
		heap[size++] = node;
		moveUp(heap[size - 1]);
	}

	public int removeMin() {
		int removed = heap[0];
		heap[0] = heap[--size];
		moveDown(0);
		return removed;
	}

	void moveUp(int pos) {
		while (pos > 0) {
			int parent = (pos - 1) / 2;
			if (heap[pos] >= heap[parent]) {
				break;
			}
			swap(heap, pos, parent);
			pos = parent;
		}
	}

	void moveDown(int pos) {
		while (pos < size / 2) {
			int child = 2 * pos + 1;
			if (child + 1 < size && heap[child] > heap[child + 1]) {
				++child;
			}
			if (heap[pos] <= heap[child]) {
				break;
			}
			swap(heap, pos, child);
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
			int x = heap.removeMin();
			System.out.println(x);
		}
	}
}