import java.util.*;

public class SplayBST {

	static class Node {
		int key;
		Node left;
		Node right;
		Node parent;
		int size;

		Node(int key) {
			this.key = key;
			size = 1;
		}

		void update() {
			size = 1 + getSize(left) + getSize(right);
		}

	}

	static int getSize(Node root) {
		return root == null ? 0 : root.size;
	}

	static void connect(Node ch, Node p, Boolean isLeftChild) {
		if (ch != null)
			ch.parent = p;
		if (isLeftChild != null) {
			if (isLeftChild)
				p.left = ch;
			else
				p.right = ch;
		}
	}

	// rotates edge (x, x.parent)
	//        g            g
	//       /            /
	//      p            x
	//     / \    ->    / \
	//    x  p.r      x.l  p
	//   / \              / \
	// x.l x.r          x.r p.r
	static void rotate(Node x) {
		Node p = x.parent;
		Node g = p.parent;
		boolean isRootP = p.parent == null;
		boolean leftChildX = (x == p.left);

		// create 3 edges: (x.r(l),p), (p,x), (x,g)
		connect(leftChildX ? x.right : x.left, p, leftChildX);
		connect(p, x, !leftChildX);
		connect(x, g, isRootP ? null : p == g.left);
		p.update();
	}

	// brings x to the root, balancing tree
	//
	// zig-zig case
	//        g                                  x
	//       / \               p                / \
	//      p  g.r rot(p)    /   \     rot(x) x.l  p
	//     / \      -->    x       g    -->       / \
	//    x  p.r          / \     / \           x.r  g
	//   / \            x.l x.r p.r g.r             / \
	// x.l x.r                                    p.r g.r
	//
	// zig-zag case
	//      g               g
	//     / \             / \               x
	//    p  g.r rot(x)   x  g.r rot(x)    /   \
	//   / \      -->    / \      -->    p       g
	// p.l  x           p  x.r          / \     / \
	//     / \         / \            p.l x.l x.r g.r
	//   x.l x.r     p.l x.l
	static void splay(Node x) {
		while (x.parent != null) {
			Node p = x.parent;
			Node g = p.parent;
			if (p.parent != null)
				rotate((x == p.left) == (p == g.left) ? p/*zig-zig*/ : x/*zig-zag*/);
			rotate(x);
		}
		x.update();
	}

	static class NodePair {
		Node left;
		Node right;

		NodePair(Node left, Node right) {
			this.left = left;
			this.right = right;
		}
	}

	static NodePair split(Node root, int minRight) {
		if (root == null)
			return new NodePair(null, null);
		Node rightRoot = null;
		while (true) {
			if (root.key >= minRight) {
				rightRoot = root;
				if (root.left != null)
					root = root.left;
				else
					break;
			} else {
				if (root.right != null)
					root = root.right;
				else
					break;
			}
		}
		splay(root);
		if (rightRoot == null)
			return new NodePair(root, null);
		splay(rightRoot);
		Node left = rightRoot.left;
		if (left != null) {
			left.parent = null;
			rightRoot.left = null;
			rightRoot.update();
		}
		return new NodePair(left, rightRoot);
	}

	static Node merge(Node left, Node right) {
		if (left == null)
			return right;
		if (right == null)
			return left;
		while (left.right != null) {
			left = left.right;
		}
		splay(left);
		left.right = right;
		right.parent = left;
		left.update();
		return left;
	}

	static Node insert(Node root, int key) {
		if (root == null) {
			return new Node(key);
		}
		Node node;
		while (true) {
			if (key < root.key) {
				if (root.left == null) {
					node = new Node(key);
					root.left = node;
					node.parent = root;
					break;
				}
				root = root.left;
			} else {
				if (root.right == null) {
					node = new Node(key);
					root.right = node;
					node.parent = root;
					break;
				}
				root = root.right;
			}
		}
		splay(node);
		return node;
	}

	static Node insert2(Node root, int key) {
		NodePair t = split(root, key);
		return merge(merge(t.left, new Node(key)), t.right);
	}

	static Node remove(Node root, int key) {
		if (root == null) return null;
		while (true) {
			if (key == root.key) {
				splay(root);
				if (root.left != null)
					root.left.parent = null;
				if (root.right != null)
					root.right.parent = null;
				return merge(root.left, root.right);
			} else if (key < root.key) {
				if (root.left == null) {
					splay(root);
					return root;
				}
				root = root.left;
			} else {
				if (root.right == null) {
					splay(root);
					return root;
				}
				root = root.right;
			}
		}
	}

	static Node remove2(Node root, int x) {
		NodePair t = split(root, x);
		return merge(t.left, split(t.right, x + 1).right);
	}

	static int kth(Node root, int k) {
		if (k < getSize(root.left))
			return kth(root.left, k);
		else if (k > getSize(root.left))
			return kth(root.right, k - getSize(root.left) - 1);
		return root.key;
	}

	static void print(Node root) {
		print0(root);
		System.out.println();
	}

	static void print0(Node root) {
		if (root == null)
			return;
		print0(root.left);
		System.out.print(root.key + " ");
		print0(root.right);
	}

	// Random test
	public static void main(String[] args) {
		Random random = new Random(1);
		long time = System.currentTimeMillis();
		Node splay = null;
		Set<Integer> set = new TreeSet<>();
		for (int i = 0; i < 1000_000; i++) {
//			System.out.println(i);
			int x = random.nextInt(100_000);
			if (random.nextBoolean()) {
				splay = remove(splay, x);
				set.remove(x);
			} else if (!set.contains(x)) {
				splay = insert(splay, x);
				set.add(x);
			}
//			System.out.println(set);
//			print(splay);
			if (set.size() != getSize(splay)) {
				throw new RuntimeException();
			}
		}
		for (int i = 0; i < getSize(splay); i++) {
			if (!set.contains(kth(splay, i)))
				throw new RuntimeException();
		}
		System.out.println(System.currentTimeMillis() - time);
		// print(splay);
	}
}