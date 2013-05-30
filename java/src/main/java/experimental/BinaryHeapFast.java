package experimental;

import java.util.*;

public class BinaryHeapFast<E extends Comparable<E>> {
	List<E> h = new ArrayList<E>();

	public BinaryHeapFast() {
	}

	public BinaryHeapFast(E[] keys) {
		for (E key : keys) {
			h.add(key);
		}
		for (int pos = h.size() / 2 - 1; pos >= 0; pos--) {
			moveDown(pos, h.get(pos));
		}
	}

	public E min() {
		return h.get(0);
	}

	public boolean isEmpty() {
		return h.isEmpty();
	}

	public void add(E node) {
		h.add(null);
		moveUp(h.size() - 1, node);
	}

	void moveUp(int pos, E node) {
		while (pos > 0) {
			int parent = (pos - 1) >> 1;
			E parentNode = h.get(parent);
			if (node.compareTo(parentNode) >= 0) {
				break;
			}
			h.set(pos, parentNode);
			pos = parent;
		}
		h.set(pos, node);
	}

	public E remove() {
		E removedNode = h.get(0);
		E lastNode = h.remove(h.size() - 1);
		if (!h.isEmpty()) {
			moveDown(0, lastNode);
		}
		return removedNode;
	}

	void moveDown(int pos, E node) {
		int half = h.size() >> 1;
		while (pos < half) {
			int child = 2 * pos + 1;
			if (child < h.size() - 1 && h.get(child).compareTo(h.get(child + 1)) > 0) {
				++child;
			}
			if (node.compareTo(h.get(child)) <= 0) {
				break;
			}
			h.set(pos, h.get(child));
			pos = child;
		}
		h.set(pos, node);
	}

	// Usage example
	public static void main(String[] args) {
		BinaryHeapFast<Integer> heap1 = new BinaryHeapFast<Integer>();
		heap1.add(2);
		heap1.add(5);
		heap1.add(1);
		heap1.add(3);

		BinaryHeapFast<Integer> heap2 = new BinaryHeapFast<Integer>(new Integer[] { 2, 5, 1, 3 });

		// print keys in sorted order
		while (!heap1.isEmpty()) {
			int v1 = heap1.remove();
			int v2 = heap2.remove();
			System.out.println(v1 + " " + (v1 == v2));
		}

		// random test
		Random rnd = new Random();
		Queue<Integer> ref = new PriorityQueue<Integer>();

		for (int steps = 0; steps < 100000; steps++) {
			int x = rnd.nextInt(1000);
			heap1.add(x);
			ref.add(x);
		}

		for (int steps = 0; steps < 100000; steps++) {
			int x = rnd.nextInt(1000);
			heap1.add(x);
			ref.add(x);

			int v1 = heap1.remove();
			int v2 = (int) ref.remove();
			if (v1 != v2) {
				System.out.println("Error");
			}
		}

		while (!heap1.isEmpty()) {
			int v1 = heap1.remove();
			int v2 = (int) ref.remove();

			if (v1 != v2) {
				System.out.println("Error");
			}
		}
	}
}