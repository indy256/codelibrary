import java.util.*;

public class SimpleTreap {
	static Random random = new Random();

	static class Treap {
		int key;
		long prio;
		Treap left;
		Treap right;
		int count;

		Treap(int key) {
			this.key = key;
			prio = random.nextLong();
			count = 1;
		}

		void update() {
			count = 1 + getCount(left) + getCount(right);
		}
	}

	static int getCount(Treap root) {
		return root == null ? 0 : root.count;
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
			TreapPair sub = split(root.left, minRight);
			root.left = sub.right;
			root.update();
			sub.right = root;
			return sub;
		} else {
			TreapPair sub = split(root.right, minRight);
			root.right = sub.left;
			root.update();
			sub.left = root;
			return sub;
		}
	}

	static Treap merge(Treap left, Treap right) {
		if (left == null)
			return right;
		if (right == null)
			return left;
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
		TreapPair t = split(root, x);
		return merge(t.left, split(t.right, x + 1).right);
	}

	static int kth(Treap root, int k) {
		if (k < getCount(root.left))
			return kth(root.left, k);
		else if (k > getCount(root.left))
			return kth(root.right, k - getCount(root.left) - 1);
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
			if (set.size() != getCount(treap))
				throw new RuntimeException();
		}
		// print(treap);
	}
}
