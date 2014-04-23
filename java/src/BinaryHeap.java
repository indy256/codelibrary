import java.util.*;

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
		for (int pos = size / 2 - 1; pos >= 0; pos--)
			pushDown(pos);
	}

	public int removeMin() {
		int removed = heap[0];
		heap[0] = heap[--size];
		pushDown(0);
		return removed;
	}

	void pushDown(int pos) {
		while (true) {
			int child = 2 * pos + 1;
			if (child >= size)
				break;
			if (child + 1 < size && heap[child + 1] < heap[child])
				++child;
			if (heap[pos] <= heap[child])
				break;
			swap(pos, child);
			pos = child;
		}
	}

	public void add(int value) {
		heap[size] = value;
		popUp(size++);
	}

	void popUp(int pos) {
		while (pos > 0) {
			int parent = (pos - 1) / 2;
			if (heap[pos] >= heap[parent])
				break;
			swap(pos, parent);
			pos = parent;
		}
	}

	void swap(int i, int j) {
		int t = heap[i];
		heap[i] = heap[j];
		heap[j] = t;
	}

	// random test
	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(100) + 1;
			PriorityQueue<Integer> q = new PriorityQueue<>();
			BinaryHeap h = new BinaryHeap(n);
			for (int op = 0; op < 1000; op++) {
				if (rnd.nextBoolean() && q.size() < n) {
					int v = rnd.nextInt();
					q.add(v);
					h.add(v);
				} else if (!q.isEmpty()) {
					if (q.remove() != h.removeMin())
						throw new RuntimeException();
				}
			}
		}
	}
}
