import java.util.*;

public class PersistentTreapSet<E> extends TreapSet<E> implements PersistentSet<E> {
	private Map<Object, Node> states = new HashMap<>();

	public PersistentTreapSet() {
		super();
	}

	public PersistentTreapSet(Comparator<? super E> comparator) {
		super(comparator);
	}

	private PersistentTreapSet(RootContainer rc, Comparator<? super E> comparator) {
		super(null, null, false, false, comparator, rc);
	}

	@Override
	public void markState(Object marker) {
		states.put(marker, rc.root);
	}

	@Override
	public PersistentTreapSet<E> getState(Object marker) {
		RootContainer rc = new RootContainer();
		rc.root = states.get(marker);
		return new PersistentTreapSet<E>(rc, comparator);
	}

	@Override
	protected Node createNode(E e) {
		return new PersistentNode(e, rnd.nextLong());
	}

	protected class PersistentNode extends Node {
		protected PersistentNode(E key, long priority) {
			super(key, priority);
		}

		@SuppressWarnings({"CloneDoesntCallSuperClone", "CloneDoesntDeclareCloneNotSupportedException"})
		@Override
		protected Node clone() {
			Node clone = new PersistentNode(key, priority);
			clone.left = left;
			clone.right = right;
			clone.size = size;
			return clone;
		}

		@SuppressWarnings({"unchecked"})
		@Override
		protected Node[] split(E minRightKey) {
			Node clone = clone();
			if (compare(minRightKey, key) < 0) {
				Node[] result = left == null ? new TreapSet.Node[2] : left.split(minRightKey);
				clone.left = result[1];
				result[1] = clone;
				clone.updateSize();
				return result;
			} else {
				Node[] result = right == null ? new TreapSet.Node[2] : right.split(minRightKey);
				clone.right = result[0];
				result[0] = clone;
				clone.updateSize();
				return result;
			}
		}

		@SuppressWarnings({"unchecked"})
		@Override
		protected Node insert(Node node) {
			if (node.priority > priority) {
				Node[] result = split(node.key);
				node.left = result[0];
				node.right = result[1];
				node.updateSize();
				return node;
			}
			Node clone = clone();
			if (compare(node.key, key) < 0) {
				clone.left = clone.left == null ? node : left.insert(node);
				clone.updateSize();
				return clone;
			} else {
				clone.right = clone.right == null ? node : right.insert(node);
				clone.updateSize();
				return clone;
			}
		}

		@Override
		protected Node merge(Node left, Node right) {
			if (left == null)
				return right;
			if (right == null)
				return left;
			if (left.priority > right.priority) {
				left = ((PersistentNode) left).clone();
				left.right = merge(left.right, right);
				left.updateSize();
				return left;
			} else {
				right = ((PersistentNode) right).clone();
				right.left = merge(left, right.left);
				right.updateSize();
				return right;
			}
		}

		@Override
		protected Node remove(E e) {
			int cmp = compare(e, key);
			if (cmp == 0)
				return merge(left, right);
			Node clone = clone();
			if (cmp < 0) {
				clone.left = clone.left == null ? null : left.remove(e);
				clone.updateSize();
				return clone;
			} else {
				clone.right = clone.right == null ? null : right.remove(e);
				clone.updateSize();
				return clone;
			}
		}
	}

	// random test
	public static void main(String[] args) {
		Object marker1 = new Object();
		Object marker2 = new Object();
		Object marker3 = new Object();
		Object marker4 = new Object();
		Object marker5 = new Object();

		PersistentSet<Integer> set = new PersistentTreapSet<>();
		set.markState(marker1);
		set.add(1);
		set.markState(marker2);
		set.add(2);
		set.markState(marker3);
		set.remove(1);
		set.markState(marker4);
		set.remove(2);
		set.markState(marker5);
		System.out.println(set.getState(marker1));
		System.out.println(set.getState(marker2));
		System.out.println(set.getState(marker3));
		System.out.println(set.getState(marker4));
		System.out.println(set.getState(marker5));
		System.out.println(set);
	}
}
