public class PersistentTree {

	static class Node {
		Node left, right;
		int sum;

		Node(int value) {
			this.sum = value;
		}

		Node(Node left, Node right) {
			this.left = left;
			this.right = right;
			if (left != null)
				sum += left.sum;
			if (right != null)
				sum += right.sum;
		}
	};

	static Node build(int left, int right) {
		if (left == right)
			return new Node(0);
		return new Node(build(left, (left + right) / 2), build((left + right) / 2 + 1, right));
	}

	static int sum(int a, int b, Node root, int left, int right) {
		if (a > right || b < left)
			return 0;
		if (a <= left && right <= b)
			return root.sum;
		return sum(a, b, root.left, left, (left + right) / 2) + sum(a, b, root.right, (left + right) / 2 + 1, right);
	}

	static Node set(int pos, int value, Node root, int left, int right) {
		if (left == right)
			return new Node(value);
		int mid = (left + right) / 2;
		if (pos <= mid)
			return new Node(set(pos, value, root.left, left, mid), root.right);
		else
			return new Node(root.left, set(pos, value, root.right, mid + 1, right));
	}

	// usage example
	public static void main(String[] args) {
		int n = 10;
		Node t1 = build(0, n - 1);
		Node t2 = set(3, 1, t1, 0, n - 1);
		System.out.println(0 == sum(0, 5, t1, 0, n - 1));
		System.out.println(1 == sum(0, 5, t2, 0, n - 1));
	}
}
