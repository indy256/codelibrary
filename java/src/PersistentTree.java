// https://en.wikipedia.org/wiki/Persistent_data_structure
public class PersistentTree {

	public static class Node {
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
	}

	public static Node build(int left, int right) {
		if (left == right)
			return new Node(0);
		int mid = (left + right) >> 1;
		return new Node(build(left, mid), build(mid + 1, right));
	}

	public static int sum(int a, int b, Node root, int left, int right) {
		if (a == left && b == right)
			return root.sum;
		int mid = (left + right) >> 1;
		if (a <= mid && b > mid)
			return sum(a, Math.min(b, mid), root.left, left, mid) + sum(Math.max(a, mid + 1), b, root.right, mid + 1, right);
		else if (a <= mid)
			return sum(a, Math.min(b, mid), root.left, left, mid);
		else if (b > mid)
			return sum(Math.max(a, mid + 1), b, root.right, mid + 1, right);
		else
			throw new RuntimeException();
	}

	public static Node set(int pos, int value, Node root, int left, int right) {
		if (left == right)
			return new Node(value);
		int mid = (left + right) >> 1;
		if (pos <= mid)
			return new Node(set(pos, value, root.left, left, mid), root.right);
		else
			return new Node(root.left, set(pos, value, root.right, mid + 1, right));
	}

	// Usage example
	public static void main(String[] args) {
		int n = 10;
		Node t1 = build(0, n - 1);
		Node t2 = set(0, 1, t1, 0, n - 1);
		System.out.println(0 == sum(0, 9, t1, 0, n - 1));
		System.out.println(1 == sum(0, 9, t2, 0, n - 1));
	}
}
