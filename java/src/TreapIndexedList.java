import java.util.*;

public class TreapIndexedList {
	// specific code
	static final int NEUTRAL_VALUE = Integer.MIN_VALUE;
	static final int NEUTRAL_DELTA = 0;

	static int joinValues(int leftValue, int rightValue) {
		return Math.max(leftValue, rightValue);
	}

	static int joinDeltas(int oldDelta, int newDelta) {
		return oldDelta + newDelta;
	}

	static int joinValueDelta(int value, int delta, int length) {
		return value + delta;
	}

	static Random random = new Random();

	static class Treap {
		int nodeValue;
		int value;
		int delta;
		int count;
		long prio;
		Treap left;
		Treap right;

		Treap(int value) {
			nodeValue = value;
			this.value = nodeValue;
			delta = NEUTRAL_DELTA;
			prio = random.nextLong();
			count = 1;
		}

		void update() {
			count = 1 + getCount(left) + getCount(right);
			value = joinValues(joinValues(getValue(left), nodeValue), getValue(right));
		}
	}

	static void pushDelta(Treap root) {
		if (root == null)
			return;      
		if (root.left != null) {
			root.left.delta = joinDeltas(root.left.delta, root.delta);
			root.left.value = joinValueDelta(root.left.value, root.delta, root.left.count);
			root.left.nodeValue = joinValueDelta(root.left.nodeValue, root.delta, 1);
		}
		if (root.right != null) {
			root.right.delta = joinDeltas(root.right.delta, root.delta);
			root.right.value = joinValueDelta(root.right.value, root.delta, root.right.count);
			root.right.nodeValue = joinValueDelta(root.right.nodeValue, root.delta, 1);
		}
		root.nodeValue = joinValueDelta(root.nodeValue, root.delta, 1);
		root.delta = NEUTRAL_DELTA;
	}

	static int getCount(Treap root) {
		return root == null ? 0 : root.count;
	}

	static int getValue(Treap root) {
		return root == null ? NEUTRAL_VALUE : root.value;
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
		pushDelta(root);
		if (getCount(root.left) >= minRight) {
			TreapPair sub = split(root.left, minRight);
			root.left = sub.right;
			root.update();
			sub.right = root;
			return sub;
		} else {
			TreapPair sub = split(root.right, minRight - getCount(root.left) - 1);
			root.right = sub.left;
			root.update();
			sub.left = root;
			return sub;
		}
	}

	static Treap merge(Treap left, Treap right) {
		pushDelta(left);
		pushDelta(right);
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

	static Treap insert(Treap root, int index, int x) {
		TreapPair t = split(root, index);
		return merge(merge(t.left, new Treap(x)), t.right);
	}

	static Treap remove(Treap root, int index) {
		TreapPair t = split(root, index);
		return merge(t.left, split(t.right, index + 1 - getCount(t.left)).right);
	}

	static int get(Treap root, int index) {
		if (index < getCount(root.left))
			return get(root.left, index);
		else if (index > getCount(root.left))
			return get(root.right, index - getCount(root.left) - 1);
		return root.nodeValue;
	}

	static class TreapAndValue {
		Treap t;
		int value;

		TreapAndValue(Treap t, int value) {
			this.t = t;
			this.value = value;
		}
	}

	static TreapAndValue query(Treap root, int a, int b) {
		TreapPair t1 = split(root, b + 1);
		TreapPair t2 = split(t1.left, a);
		int value = getValue(t2.right);
		Treap t = merge(merge(t2.left, t2.right), t1.right);
		return new TreapAndValue(t, value);
	}

	static TreapAndValue modify(Treap root, int a, int b, int value) {
		TreapPair t1 = split(root, b + 1);
		TreapPair t2 = split(t1.left, a);
		int value = getValue(t2.right);
		Treap t = merge(merge(t2.left, t2.right), t1.right);
		return new TreapAndValue(t, value);
	}

	static void print(Treap root) {
		if (root == null)
			return;
		print(root.left);
		System.out.print(root.nodeValue + " ");
		print(root.right);
	}

	// Usage example
	public static void main(String[] args) {
		Treap treap = null;
		treap = insert(treap, 0, 1);
		treap = insert(treap, 0, 2);
		treap = insert(treap, 0, 3);
		System.out.println(getValue(treap));
		print(treap);
		System.out.println();
		TreapAndValue tv = query(treap, 1, 2);
		treap = tv.t;
		System.out.println(tv.value);
		// List<Integer> list = new ArrayList<Integer>();
		//
		// for (int i = 0; i < 100000; i++) {
		// if (random.nextInt(10) != 0) {
		// int p = random.nextInt(list.size() + 1);
		// treap = insert(treap, p, i);
		// list.add(p, i);
		// } else if (list.size() > 0) {
		// int p = random.nextInt(list.size());
		// if (random.nextBoolean()) {
		// treap = remove(treap, p);
		// list.remove(p);
		// } else {
		// int v1 = get(treap, p);
		// int v2 = list.get(p);
		// if (v1 != v2) {
		// System.err.println(v2 + " " + v1);
		// break;
		// }
		// }
		// }
		// if (list.size() != getCount(treap)) {
		// System.err.println(list.size() + " " + getCount(treap));
		// break;
		// }
		// }
	}
}
