import java.util.*;

// https://en.wikipedia.org/wiki/Treap
public class TreapSimple {
	static Random random = new Random();

	static class Treap {
		int key;
		long prio;
		Treap left;
		Treap right;
		int size;

		Treap(int key) {
			this.key = key;
			prio = random.nextLong();
			size = 1;
		}

		void update() {
			size = 1 + getSize(left) + getSize(right);
		}
	}

	static int getSize(Treap root) {
		return root == null ? 0 : root.size;
	}

	static class TreapPair {
		Treap left;
		Treap right;

		TreapPair(Treap left, Treap right) {
			this.left = left;
			this.right = right;
		}
	}

	static TreapPair split(Treap root, int minRight) {
		if (root == null)
			return new TreapPair(null, null);
		if (root.key >= minRight) {
			TreapPair leftSplit = split(root.left, minRight);
			root.left = leftSplit.right;
			root.update();
			leftSplit.right = root;
			return leftSplit;
		} else {
			TreapPair rightSplit = split(root.right, minRight);
			root.right = rightSplit.left;
			root.update();
			rightSplit.left = root;
			return rightSplit;
		}
	}

	static Treap merge(Treap left, Treap right) {
		if (left == null)
			return right;
		if (right == null)
			return left;
		// if (random.nextInt(left.size + right.size) < left.size) {
		if (left.prio > right.prio) {
			left.right = merge(left.right, right);
			left.update();
			return left;
		} else {
			right.left = merge(left, right.left);
			right.update();
			return right;
		}
	}

	static Treap insert(Treap root, int x) {
		TreapPair t = split(root, x);
		return merge(merge(t.left, new Treap(x)), t.right);
	}

	static Treap remove(Treap root, int x) {
		if (root == null) {
			return null;
		}
		if (x < root.key) {
			root.left = remove(root.left, x);
			root.update();
			return root;
		} else if (x > root.key) {
			root.right = remove(root.right, x);
			root.update();
			return root;
		} else {
			return merge(root.left, root.right);
		}
	}

	static int kth(Treap root, int k) {
		if (k < getSize(root.left))
			return kth(root.left, k);
		else if (k > getSize(root.left))
			return kth(root.right, k - getSize(root.left) - 1);
		return root.key;
	}

	static void print(Treap root) {
		if (root == null)
			return;
		print(root.left);
		System.out.println(root.key);
		print(root.right);
	}

	// random test
	public static void main(String[] args) {
		Treap treap = null;
		Set<Integer> set = new TreeSet<>();
		for (int i = 0; i < 100000; i++) {
			int x = random.nextInt(100000);
			if (random.nextBoolean()) {
				treap = remove(treap, x);
				set.remove(x);
			} else if (!set.contains(x)) {
				treap = insert(treap, x);
				set.add(x);
			}
			if (set.size() != getSize(treap))
				throw new RuntimeException();
		}
		// print(treap);
	}
}
