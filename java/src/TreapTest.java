import java.util.*;

public class TreapTest {
	static Random random = new Random();

	static class Treap {
		long x;
		long y;
		long sum;
		int count;
		Treap left = null;
		Treap right = null;

		Treap(long x) {
			this.x = x;
			y = random.nextLong();
			sum = x;
			count = 1;
		}

		void update() {
			sum = x + getSum(left) + getSum(right);
			count = 1 + getCount(left) + getCount(right);
		}
	}

	static long getMin(Treap root) {
		if (root == null)
			return Long.MAX_VALUE;
		while (root.left != null) {
			root = root.left;
		}
		return root.x;
	}

	static int getCount(Treap root) {
		if (root == null)
			return 0;
		else
			return root.count;
	}

	static long getSum(Treap root) {
		if (root == null)
			return 0;
		else
			return root.sum;
	}

	static class TreapPair {
		Treap left;
		Treap right;

		TreapPair(Treap left, Treap right) {
			this.left = left;
			this.right = right;
		}
	}

	static TreapPair split(Treap root, long minRight) {
		if (root == null)
			return new TreapPair(null, null);
		if (root.x >= minRight) {
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
		if (left.y > right.y) {
			left.right = merge(left.right, right);
			left.update();
			return left;
		} else {
			right.left = merge(left, right.left);
			right.update();
			return right;
		}
	}

	static Treap insert(Treap root, long x) {
		TreapPair t = split(root, x);
		Treap cur = merge(t.left, new Treap(x));
		return merge(cur, t.right);
	}

	static Treap remove(Treap root, int x) {
		TreapPair t = split(root, x);
		if (getMin(t.right) == x) {
			TreapPair tmp = split(t.right, x + 1);
			t.right = tmp.right;
		}
		return merge(t.left, t.right);
	}

	static long kth(Treap root, int k) {
		if (k < getCount(root.left))
			return kth(root.left, k);
		else if (k > getCount(root.left))
			return kth(root.right, k - getCount(root.left) - 1);
		return root.x;
	}

	static void print(Treap root) {
		if (root == null)
			return;
		print(root.left);
		System.out.println(root.x);
		print(root.right);
	}

	public static void main(String[] args) {
		Treap t = new Treap(5);
		t = insert(t, 6);
		t = insert(t, 1);
		t = insert(t, 2);
		t = remove(t, 5);
		print(t);
		System.out.println(2 == kth(t, 1));
	}
}
