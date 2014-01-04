public class BinaryHeapWithIncreasePriority {
	int[] heap;
	int[] values;
	int[] id2Pos;
	int size;

	public BinaryHeapWithIncreasePriority(int n) {
		heap = new int[n];
		values = new int[n];
		id2Pos = new int[n];
	}

	public int remove() {
		int removed = heap[0];
		heap[0] = heap[--size];
		id2Pos[heap[0]] = 0;
		pushDown(0);
		return removed;
	}

	void pushDown(int pos) {
		while (2 * pos + 1 < size) {
			int child = 2 * pos + 1;
			if (child + 1 < size && values[heap[child + 1]] < values[heap[child]]) {
				++child;
			}
			if (values[heap[pos]] <= values[heap[child]]) {
				break;
			}
			swap(pos, child);
			pos = child;
		}
	}

	public void add(int id, int value) {
		values[size] = value;
		heap[size] = id;
		id2Pos[id] = size;
		popUp(size++);
	}

	public void increasePriority(int id, int value) {
		values[id] = value;
		popUp(id2Pos[id]);
	}

	void popUp(int pos) {
		while (pos > 0) {
			int parent = (pos - 1) / 2;
			if (values[heap[pos]] >= values[heap[parent]]) {
				break;
			}
			swap(pos, parent);
			pos = parent;
		}
	}

	void swap(int i, int j) {
		int t = heap[i];
		heap[i] = heap[j];
		heap[j] = t;
		id2Pos[heap[i]] = i;
		id2Pos[heap[j]] = j;
	}

	// Usage example
	public static void main(String[] args) {
		BinaryHeapWithIncreasePriority heap = new BinaryHeapWithIncreasePriority(10);
		heap.add(0, 4);
		heap.add(1, 5);
		heap.add(2, 2);

		heap.increasePriority(1, 3);

		// print elements in sorted order
		while (heap.size != 0) {
			int x = heap.remove();
			System.out.println(heap.values[x]);
		}
	}
}