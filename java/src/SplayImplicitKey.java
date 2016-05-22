import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SplayImplicitKey {

	// Modify the following 5 methods to implement your custom operations on the tree.
	// This example implements Add/Max operations. Operations like Add/Sum, Set/Max can also be implemented.
	static int modifyOperation(int x, int y) {
		return x + y;
	}

	// query (or combine) operation
	static int queryOperation(int leftValue, int rightValue) {
		return Math.max(leftValue, rightValue);
	}

	static int deltaEffectOnSegment(int delta, int segmentLength) {
		if (delta == getNeutralDelta()) return getNeutralDelta();
		// Here you must write a fast equivalent of following slow code:
		// int result = delta;
		// for (int i = 1; i < segmentLength; i++) result = queryOperation(result, delta);
		// return result;
		return delta;
	}

	static int getNeutralDelta() {
		return 0;
	}

	static int getNeutralValue() {
		return Integer.MIN_VALUE;
	}

	// generic code
	static int joinValueWithDelta(int value, int delta) {
		if (delta == getNeutralDelta()) return value;
		return modifyOperation(value, delta);
	}

	static int joinDeltas(int delta1, int delta2) {
		if (delta1 == getNeutralDelta()) return delta2;
		if (delta2 == getNeutralDelta()) return delta1;
		return modifyOperation(delta1, delta2);
	}

	static void applyDelta(Node root, int delta) {
		if (root == null)
			return;
		root.delta = joinDeltas(root.delta, delta);
		root.nodeValue = joinValueWithDelta(root.nodeValue, delta);
		root.subTreeValue = joinValueWithDelta(root.subTreeValue, deltaEffectOnSegment(delta, root.size));
	}

	public static class Node {
		Node left;
		Node right;
		Node parent;
		int size;
		int nodeValue;
		int subTreeValue;
		int delta;

		public Node(int nodeValue) {
			this.nodeValue = nodeValue;
		}

		// tests whether x is a root of a splay tree
		boolean isRoot() {
			return parent == null || (parent.left != this && parent.right != this);
		}

		void push() {
			applyDelta(left, delta);
			applyDelta(right, delta);
			delta = getNeutralDelta();
		}

		void update() {
			size = 1;
			subTreeValue = nodeValue;
			if (left != null) {
				subTreeValue += left.subTreeValue + left.delta * left.size;
				size += left.size;
			}
			if (right != null) {
				subTreeValue += right.subTreeValue + right.delta * right.size;
				size += right.size;
			}
		}
	}


	static int getSize(Node root) {
		return root == null ? 0 : root.size;
	}

	static int getSubTreeValue(Node root) {
		return root == null ? getNeutralValue() : root.subTreeValue;
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
		boolean isRootP = p.isRoot();
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
		boolean isRoot = x.isRoot();
		while (!x.isRoot()) {
			Node p = x.parent;
			Node g = p.parent;
			if (!p.isRoot())
				g.push();
			p.push();
			x.push();
			if (!p.isRoot())
				rotate((x == p.left) == (p == g.left) ? p/*zig-zig*/ : x/*zig-zag*/);
			rotate(x);
		}
		x.update();
		x.push();
	}

	public static class NodePair {
		Node left;
		Node right;

		NodePair(Node left, Node right) {
			this.left = left;
			this.right = right;
		}
	}

	public static NodePair split(Node root, int minRight) {
		return null;
	}

	public static Node merge(Node left, Node right) {
		return null;
	}

	public static Node insert(Node root, int index, int value) {
		NodePair t = split(root, index);
		return merge(merge(t.left, new Node(value)), t.right);
	}

	public static Node remove(Node root, int index) {
		NodePair t = split(root, index);
		return merge(t.left, split(t.right, index + 1 - getSize(t.left)).right);
	}

	public static Node modify(Node root, int a, int b, int delta) {
		NodePair t1 = split(root, b + 1);
		NodePair t2 = split(t1.left, a);
		applyDelta(t2.right, delta);
		return merge(merge(t2.left, t2.right), t1.right);
	}

	public static class NodeAndResult {
		Node root;
		int value;

		NodeAndResult(Node t, int value) {
			this.root = t;
			this.value = value;
		}
	}

	public static NodeAndResult query(Node root, int a, int b) {
		NodePair t1 = split(root, b + 1);
		NodePair t2 = split(t1.left, a);
		int value = getSubTreeValue(t2.right);
		return new NodeAndResult(merge(merge(t2.left, t2.right), t1.right), value);
	}

	// Random test
	public static void main(String[] args) {
		Node splay = null;
		List<Integer> list = new ArrayList<>();
		Random rnd = new Random();
		for (int step = 0; step < 100000; step++) {
			int cmd = rnd.nextInt(6);
			if (cmd < 2 && list.size() < 100) {
				int pos = rnd.nextInt(list.size() + 1);
				int value = rnd.nextInt(100);
				list.add(pos, value);
				splay = insert(splay, pos, value);
			} else if (cmd < 3 && list.size() > 0) {
				int pos = rnd.nextInt(list.size());
				list.remove(pos);
				splay = remove(splay, pos);
			} else if (cmd < 4 && list.size() > 0) {
				int b = rnd.nextInt(list.size());
				int a = rnd.nextInt(b + 1);
				int res = list.get(a);
				for (int i = a + 1; i <= b; i++)
					res = queryOperation(res, list.get(i));
				NodeAndResult tr = query(splay, a, b);
				splay = tr.root;
				if (res != tr.value)
					throw new RuntimeException();
			} else if (cmd < 5 && list.size() > 0) {
				int b = rnd.nextInt(list.size());
				int a = rnd.nextInt(b + 1);
				int delta = rnd.nextInt(100) - 50;
				for (int i = a; i <= b; i++)
					list.set(i, joinValueWithDelta(list.get(i), delta));
				splay = modify(splay, a, b, delta);
			} else {
				for (int i = 0; i < list.size(); i++) {
					NodeAndResult tr = query(splay, i, i);
					splay = tr.root;
					int v = tr.value;
					if (list.get(i) != v)
						throw new RuntimeException();
					{
					}
				}
			}
			System.out.println("Test passed");
		}
	}
}