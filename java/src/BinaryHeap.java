public class BinaryHeap {
	int[] heap;
	int size;

	public BinaryHeap(int n) {
		heap = new int[n];
	}

	// build heap in O(n)
	public BinaryHeap(int[] values) {
		heap = values.clone();
		size = values.length;
		for (int pos = size / 2 - 1; pos >= 0; pos--) {
			pushDown(pos);
		}
	}

	public int removeMin() {
		int removed = heap[0];
		heap[0] = heap[--size];
		pushDown(0);
		return removed;
	}

	void pushDown(int pos) {
		while (2 * pos + 1 < size) {
			int child = 2 * pos + 1;
			if (child + 1 < size && heap[child + 1] < heap[child]) {
				++child;
			}
			if (heap[pos] <= heap[child]) {
				break;
			}
			swap(heap, pos, child);
			pos = child;
		}
	}

	public void add(int node) {
		heap[size++] = node;
		popUp(heap[size - 1]);
	}

	void popUp(int pos) {
		while (pos > 0) {
			int parent = (pos - 1) / 2;
			if (heap[pos] >= heap[parent]) {
				break;
			}
			swap(heap, pos, parent);
			pos = parent;
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