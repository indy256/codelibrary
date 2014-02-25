public class BinaryHeapWithIncreasePriority {
	int[] heap;
	int[] pos2Id;
	int[] id2Pos;
	int size;

	public BinaryHeapWithIncreasePriority(int n) {
		heap = new int[n];
		pos2Id = new int[n];
		id2Pos = new int[n];
	}

	public int remove() {
		int removed = pos2Id[0];
		heap[0] = heap[--size];
		pos2Id[0] = pos2Id[size];
		id2Pos[pos2Id[0]] = 0;
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

	public void add(int id, int value) {
		heap[size] = value;
		pos2Id[size] = id;
		id2Pos[id] = size;
		popUp(size++);
	}

	public void increasePriority(int id, int value) {
		heap[id2Pos[id]] = value;
		popUp(id2Pos[id]);
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
		t = pos2Id[i];
		pos2Id[i] = pos2Id[j];
		pos2Id[j] = t;
		id2Pos[pos2Id[i]] = i;
		id2Pos[pos2Id[j]] = j;
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
			System.out.println(heap.heap[0] + " " + heap.remove());
		}
	}
}