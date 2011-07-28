import java.util.*;

public class TreapAsIndexedList {
	static Random random = new Random();

	static class Treap {
		long y;
		int count;
		int value;
		Treap left = null;
		Treap right = null;

		Treap(int value) {
			y = random.nextLong();
			count = 1;
			this.value = value;
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

	static TreapPair split(Treap root, int index) {
		if (root == null)
			return new TreapPair(null, null);
		if (getCount(root.left) >= index) {
			TreapPair sub = split(root.left, index);
			root.left = sub.right;
			root.update();
			sub.right = root;
			return sub;
		} else {
			TreapPair sub = split(root.right, index - getCount(root.left) - 1);
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
		return root.value;
	}

	static void print(Treap root) {
		if (root == null)
			return;
		print(root.left);
		System.out.print(root.value + " ");
		print(root.right);
	}

	// Usage example
	public static void main(String[] args) {
		Treap treap = null;
		List<Integer> list = new ArrayList<Integer>();

		for (int i = 0; i < 100000; i++) {
			if (random.nextInt(10) != 0) {
				int p = random.nextInt(list.size() + 1);
				treap = insert(treap, p, i);
				list.add(p, i);
			} else if (list.size() > 0) {
				int p = random.nextInt(list.size());
				if (random.nextBoolean()) {
					treap = remove(treap, p);
					list.remove(p);
				} else {
					int v1 = get(treap, p);
					int v2 = list.get(p);
					if (v1 != v2) {
						System.err.println(v2 + " " + v1);
						break;
					}
				}
			}
			if (list.size() != getCount(treap)) {
				System.err.println(list.size() + " " + getCount(treap));
				break;
			}
		}
	}
}
