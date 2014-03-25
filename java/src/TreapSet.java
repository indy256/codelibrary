import java.lang.reflect.Method;
import java.util.*;

public class TreapSet<E> extends AbstractSet<E> implements NavigableSet<E> {
	protected static final Random rnd = new Random();

	protected class RootContainer {
		protected Node root;
	}

	protected final Comparator<? super E> comparator;
	protected final RootContainer rc;
	private final E from;
	private final E to;
	private final boolean fromInclusive;
	private final boolean toInclusive;

	public TreapSet() {
		this(null);
	}

	public TreapSet(Comparator<? super E> comparator) {
		this(null, null, false, false, comparator, null);
	}

	protected TreapSet(E from, E to, boolean fromInclusive, boolean toInclusive, Comparator<? super E> comparator, RootContainer rc) {
		this.comparator = comparator;
		this.from = from;
		this.to = to;
		this.fromInclusive = fromInclusive;
		this.toInclusive = toInclusive;
		this.rc = rc != null ? rc : new RootContainer();
	}

	@SuppressWarnings("unchecked")
	protected int compare(E first, E second) {
		return comparator != null ? comparator.compare(first, second) : ((Comparable<? super E>) first).compareTo(second);
	}

	private boolean inRange(E e) {
		return (from == null || compare(from, e) < (fromInclusive ? 1 : 0)) && (to == null || compare(e, to) < (toInclusive ? 1 : 0));
	}

	private E keyOrNull(Node node) {
		return node != null && inRange(node.key) ? node.key : null;
	}

	@Override
	public E lower(E e) {
		return rc.root == null ? null : keyOrNull(rc.root.lower(e));
	}

	@Override
	public E floor(E e) {
		return rc.root == null ? null : keyOrNull(rc.root.floor(e));
	}

	@Override
	public E ceiling(E e) {
		return rc.root == null ? null : keyOrNull(rc.root.ceil(e));
	}

	@Override
	public E higher(E e) {
		return rc.root == null ? null : keyOrNull(rc.root.higher(e));
	}

	@Override
	public E pollFirst() {
		E first = getFirst();
		if (first == null)
			return null;
		rc.root = rc.root.remove(first);
		return first;
	}

	@Override
	public E pollLast() {
		E last = getLast();
		if (last == null)
			return null;
		rc.root = rc.root.remove(last);
		return last;
	}

	@Override
	public int size() {
		if (rc.root == null)
			return 0;
		if (from == null && to == null)
			return rc.root.size;
		if (from == null) {
			Node to = toInclusive ? rc.root.floor(this.to) : rc.root.lower(this.to);
			if (to == null)
				return 0;
			return rc.root.indexOf(to.key) + 1;
		}
		if (to == null) {
			Node from = fromInclusive ? rc.root.ceil(this.from) : rc.root.higher(this.from);
			if (from == null)
				return 0;
			return rc.root.size - rc.root.indexOf(from.key);
		}
		Node from = fromInclusive ? rc.root.ceil(this.from) : rc.root.higher(this.from);
		if (from == null || !inRange(from.key))
			return 0;
		Node to = toInclusive ? rc.root.floor(this.to) : rc.root.lower(this.to);
		if (to == null || !inRange(to.key))
			return 0;
		return rc.root.indexOf(to.key) - rc.root.indexOf(from.key) + 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		return rc.root != null && inRange((E) o) && rc.root.search((E) o) != null;
	}

	private abstract class TreapIterator<E> implements Iterator<E> {
		private E next = getStartKey();
		private E lastReturned = null;

		abstract E getStartKey();

		abstract E nextKey(E e);

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public E next() {
			if (next == null)
				throw new NoSuchElementException();
			lastReturned = next;
			next = nextKey(next);
			return lastReturned;
		}

		@Override
		public void remove() {
			if (lastReturned == null)
				throw new IllegalStateException();
			TreapSet.this.remove(lastReturned);
			lastReturned = null;
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new TreapIterator<E>() {
			@Override
			protected E getStartKey() {
				return getFirst();
			}

			@Override
			protected E nextKey(E e) {
				return higher(e);
			}
		};
	}

	@Override
	public Iterator<E> descendingIterator() {
		return new TreapIterator<E>() {
			@Override
			protected E getStartKey() {
				return getLast();
			}

			@Override
			protected E nextKey(E e) {
				return lower(e);
			}
		};
	}

	@Override
	public boolean add(E e) {
		Objects.requireNonNull(e);
		if (!inRange(e))
			throw new IllegalArgumentException("key out of range");
		if (rc.root == null) {
			rc.root = createNode(e);
			return true;
		}
		if (contains(e))
			return false;
		rc.root = rc.root.insert(createNode(e));
		return true;
	}

	protected Node createNode(E e) {
		return new Node(e, rnd.nextLong());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		if (!contains(o))
			return false;
		rc.root = rc.root.remove((E) o);
		return true;
	}

	@Override
	public Comparator<? super E> comparator() {
		return comparator;
	}

	@Override
	public NavigableSet<E> descendingSet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
		return new TreapSet<E>(fromElement, toElement, fromInclusive, toInclusive, comparator, rc);
	}

	@Override
	public SortedSet<E> subSet(E fromElement, E toElement) {
		return subSet(fromElement, true, toElement, false);
	}

	@Override
	public NavigableSet<E> headSet(E toElement, boolean inclusive) {
		return subSet(null, false, toElement, inclusive);
	}

	@Override
	public SortedSet<E> headSet(E toElement) {
		return headSet(toElement, false);
	}

	@Override
	public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
		return subSet(fromElement, inclusive, null, false);
	}

	@Override
	public SortedSet<E> tailSet(E fromElement) {
		return tailSet(fromElement, true);
	}

	private E getFirst() {
		if (isEmpty())
			return null;
		if (from == null)
			return rc.root.first().key;
		return fromInclusive ? ceiling(from) : higher(from);
	}

	@Override
	public E first() {
		E res = getFirst();
		if (res == null)
			throw new NoSuchElementException();
		return res;
	}

	private E getLast() {
		if (isEmpty())
			return null;
		if (to == null)
			return rc.root.last().key;
		return toInclusive ? floor(to) : lower(to);
	}

	@Override
	public E last() {
		E res = getLast();
		if (res == null)
			throw new NoSuchElementException();
		return res;
	}

	public E getByIndex(int index) {
		if (index < 0)
			throw new IndexOutOfBoundsException(Integer.toString(index));
		if (from != null)
			index += headSet(from, !fromInclusive).size();
		if (index >= size() - (to == null ? 0 : tailSet(to, !toInclusive).size()))
			throw new IndexOutOfBoundsException(Integer.toString(index));
		return rc.root.get(index);
	}

	public int indexOf(E e) {
		return rc.root != null && inRange(e) ? rc.root.indexOf(e) : -1;
	}

	protected class Node {
		protected final E key;
		protected final long priority;
		protected Node left;
		protected Node right;
		protected int size;

		protected Node(E key, long priority) {
			this.key = key;
			this.priority = priority;
			left = null;
			right = null;
			size = 1;
		}

		protected void updateSize() {
			size = size(left) + size(right) + 1;
		}

		@SuppressWarnings("unchecked")
		protected Node[] split(E minRightKey) {
			if (compare(minRightKey, key) < 0) {
				Node[] result = left == null ? new TreapSet.Node[2] : left.split(minRightKey);
				left = result[1];
				updateSize();
				result[1] = this;
				return result;
			} else {
				Node[] result = right == null ? new TreapSet.Node[2] : right.split(minRightKey);
				right = result[0];
				updateSize();
				result[0] = this;
				return result;
			}
		}

		protected Node merge(Node left, Node right) {
			if (left == null)
				return right;
			if (right == null)
				return left;
			if (left.priority > right.priority) {
				left.right = merge(left.right, right);
				left.updateSize();
				return left;
			} else {
				right.left = merge(left, right.left);
				right.updateSize();
				return right;
			}
		}

		protected Node insert(Node node) {
			if (node.priority > priority) {
				Node[] result = split(node.key);
				node.left = result[0];
				node.right = result[1];
				node.updateSize();
				return node;
			}
			if (compare(node.key, key) < 0) {
				left = left == null ? node : left.insert(node);
				updateSize();
				return this;
			} else {
				right = right == null ? node : right.insert(node);
				updateSize();
				return this;
			}
		}

		protected Node remove(E e) {
			int cmp = compare(e, key);
			if (cmp == 0)
				return merge(left, right);
			if (cmp < 0) {
				left = left == null ? null : left.remove(e);
				updateSize();
				return this;
			} else {
				right = right == null ? null : right.remove(e);
				updateSize();
				return this;
			}
		}

		protected Node lower(E e) {
			if (compare(key, e) >= 0)
				return left == null ? null : left.lower(e);
			Node result = right == null ? null : right.lower(e);
			return result == null ? this : result;
		}

		protected Node floor(E e) {
			if (compare(key, e) > 0)
				return left == null ? null : left.floor(e);
			Node result = right == null ? null : right.floor(e);
			return result == null ? this : result;
		}

		protected Node ceil(E e) {
			if (compare(key, e) < 0)
				return right == null ? null : right.ceil(e);
			Node result = left == null ? null : left.ceil(e);
			return result == null ? this : result;
		}

		protected Node higher(E e) {
			if (compare(key, e) <= 0)
				return right == null ? null : right.higher(e);
			Node result = left == null ? null : left.higher(e);
			return result == null ? this : result;
		}

		protected Node first() {
			return left == null ? this : left.first();
		}

		protected Node last() {
			return right == null ? this : right.last();
		}

		protected Node search(E e) {
			int cmp = compare(e, key);
			if (cmp == 0)
				return this;
			if (cmp < 0)
				return left == null ? null : left.search(e);
			else
				return right == null ? null : right.search(e);
		}

		protected int indexOf(E e) {
			int cmp = compare(e, key);
			if (cmp == 0)
				return size(left);
			if (cmp < 0)
				return left == null ? 0 : left.indexOf(e);
			else
				return size(left) + 1 + (right == null ? 0 : right.indexOf(e));
		}

		protected E get(int index) {
			if (index < size(left))
				return left.get(index);
			else if (index == size(left))
				return key;
			else
				return right.get(index - size(left) - 1);
		}

		private int size(Node node) {
			return node == null ? 0 : node.size;
		}
	}

	// random tests
	public static void main(String[] args) {
		Random rnd = new Random();
		int range = 50;
		String[] methods0 = {"size", "isEmpty", "first", "last", "pollFirst", "pollLast", "toArray", "clear"};
		String[] methods1 = {"add", "add", "add", "contains", "remove", "lower", "floor", "ceiling", "higher", "headSet", "tailSet"};
		String[] iteratorMethods = {"hasNext", "next", "remove"};
		for (int step = 0; step < 1000; step++) {
			NavigableSet<Integer> s1 = new TreeSet<>();
			TreapSet<Integer> s2 = new TreapSet<>();
			int left = rnd.nextInt(range) - range / 2;
			int right = rnd.nextInt(range) - range / 2;
			SortedSet<Integer> view1 = s1.subSet(Math.min(left, right), Math.max(left, right));
			SortedSet<Integer> view2 = s2.subSet(Math.min(left, right), Math.max(left, right));
			for (int i = 0; i < 1000; i++) {
				if (!view1.equals(view2))
					throw new RuntimeException();
				int pos = 0;
				for (int item1 : s1) {
					Integer item2 = s2.getByIndex(pos);
					if (!Objects.equals(item1, item2) || s2.indexOf(item1) != pos)
						throw new RuntimeException();
					++pos;
				}
				int arg = rnd.nextInt(range) - range / 2;
				int op = rnd.nextInt(methods0.length + methods1.length);
				check(s1, s2, op < methods0.length ? methods0[op] : methods1[op - methods0.length], op < methods0.length ? null : arg);

				if (rnd.nextInt(100) == 0) {
					Iterator<Integer> it1 = s1.iterator();
					Iterator<Integer> it2 = s2.iterator();
					for (int j = 0; j < 10; j++)
						check(it1, it2, iteratorMethods[rnd.nextInt(iteratorMethods.length)], null);
					it1 = s1.descendingIterator();
					it2 = s2.descendingIterator();
					for (int j = 0; j < 10; j++)
						check(it1, it2, iteratorMethods[rnd.nextInt(iteratorMethods.length)], null);
				}
			}
		}
	}

	static void check(Object obj1, Object obj2, String methodName, Integer arg) {
		Object result1 = invoke(obj1, methodName, arg);
		Object result2 = invoke(obj2, methodName, arg);
		if (!((result1 instanceof Exception && result2 instanceof Exception) || Objects.deepEquals(result1, result2)))
			throw new RuntimeException(result1 + " " + result2);
	}

	static Object invoke(Object obj, String methodName, Integer arg) {
		try {
			Method method = arg != null ? obj.getClass().getMethod(methodName, Object.class) : obj.getClass().getMethod(methodName);
			method.setAccessible(true);
			return arg != null ? method.invoke(obj, arg) : method.invoke(obj);
		} catch (Exception e) {
			return e;
		}
	}
}
