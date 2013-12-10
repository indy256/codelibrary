package experimental;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinaryHeapOnArrayList<E extends Comparable<E>> {
	List<E> h = new ArrayList<E>();

	public BinaryHeapOnArrayList() {
	}

	// build heap in O(n)
	public BinaryHeapOnArrayList(E[] keys) {
		for (E key : keys) {
			h.add(key);
		}
		for (int pos = h.size() / 2 - 1; pos >= 0; pos--) {
			moveDown(pos);
		}
	}

	public void add(E node) {
		h.add(node);
		moveUp(h.size() - 1);
	}

	void moveUp(int pos) {
		while (pos > 0) {
			int parent = (pos - 1) / 2;
			if (h.get(pos).compareTo(h.get(parent)) >= 0) {
				break;
			}
			Collections.swap(h, pos, parent);
			pos = parent;
		}
	}

	public E remove() {
		E removedNode = h.get(0);
		E lastNode = h.remove(h.size() - 1);
		if (!h.isEmpty()) {
			h.set(0, lastNode);
			moveDown(0);
		}
		return removedNode;
	}

	void moveDown(int pos) {
		while (pos < h.size() / 2) {
			int child = 2 * pos + 1;
			if (child < h.size() - 1 && h.get(child).compareTo(h.get(child + 1)) > 0) {
				++child;
			}
			if (h.get(pos).compareTo(h.get(child)) <= 0) {
				break;
			}
			Collections.swap(h, pos, child);
			pos = child;
		}
	}

	// Usage example
	public static void main(String[] args) {
		BinaryHeapOnArrayList<Integer> heap = new BinaryHeapOnArrayList<Integer>();
		heap.add(2);
		heap.add(5);
		heap.add(1);

		// print elements in sorted order
		while (!heap.h.isEmpty()) {
			int x = heap.remove();
			System.out.println(x);
		}
	}
}