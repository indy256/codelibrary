import java.lang.reflect.Method;
import java.util.*;

public class ChainingHashMap<K, V> extends AbstractMap<K, V> {
	private int size;
	private Entry<K, V>[] table;
	private final Set<Map.Entry<K, V>> entrySet;

	private static final int[] shifts = new int[10];

	static {
		Random random = new Random();
		for (int i = 0; i < shifts.length; i++)
			shifts[i] = 1 + 3 * i + random.nextInt(3);
	}

	public ChainingHashMap() {
		this(4);
	}

	@SuppressWarnings("unchecked")
	private void setCapacity(int size) {
		table = new Entry[Integer.highestOneBit(size) * 4];
	}

	public ChainingHashMap(int initialCapacity) {
		setCapacity(initialCapacity);
		entrySet = new AbstractSet<Map.Entry<K, V>>() {
			@Override
			public Iterator<Map.Entry<K, V>> iterator() {
				return new Iterator<Map.Entry<K, V>>() {
					private Entry<K, V> lastReturned;
					private Entry<K, V> next;
					private int index;

					@Override
					public boolean hasNext() {
						while (next == null && index < table.length)
							next = table[index++];
						return next != null;
					}

					@Override
					public Map.Entry<K, V> next() {
						if (!hasNext())
							throw new NoSuchElementException();
						lastReturned = next;
						next = next.next;
						return lastReturned;
					}

					@Override
					public void remove() {
						if (lastReturned == null)
							throw new IllegalStateException();
						ChainingHashMap.this.remove(lastReturned.getKey());
						lastReturned = null;
					}
				};
			}

			@Override
			public int size() {
				return size;
			}
		};
	}

	public ChainingHashMap(Map<K, V> map) {
		this(map.size());
		putAll(map);
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return entrySet;
	}

	@Override
	public void clear() {
		Arrays.fill(table, null);
		size = 0;
	}

	private int index(Object o) {
		return getHash(o.hashCode()) & (table.length - 1);
	}

	private int getHash(int h) {
		int result = h;
		for (int i : shifts)
			result ^= h >>> i;
		return result;
	}

	@Override
	public V remove(Object key) {
		Objects.requireNonNull(key);
		int index = index(key);
		Entry<K, V> prev = null;
		for (Entry<K, V> current = table[index]; current != null; current = current.next) {
			if (current.getKey().equals(key)) {
				if (prev == null)
					table[index] = current.next;
				else
					prev.next = current.next;
				--size;
				return current.getValue();
			}
			prev = current;
		}
		return null;
	}

	@Override
	public V put(K key, V value) {
		Objects.requireNonNull(key);
		int index = index(key);
		Entry<K, V> current = table[index];
		for (; current != null; current = current.next) {
			if (current.getKey().equals(key)) {
				V oldValue = current.getValue();
				current.setValue(value);
				return oldValue;
			}
			if (current.next == null)
				break;
		}
		if (current == null)
			table[index] = new Entry<>(key, value);
		else
			current.next = new Entry<>(key, value);
		++size;
		if (2 * size > table.length) {
			Entry<K, V>[] oldTable = table;
			setCapacity(size);
			for (Entry<K, V> entry : oldTable) {
				for (; entry != null; ) {
					Entry<K, V> next = entry.next;
					index = index(entry.getKey());
					entry.next = table[index];
					table[index] = entry;
					entry = next;
				}
			}
		}
		return null;
	}

	@Override
	public V get(Object key) {
		Objects.requireNonNull(key);
		Entry<K, V> entry = getEntry(key);
		return entry != null ? entry.getValue() : null;
	}

	@Override
	public boolean containsKey(Object key) {
		Objects.requireNonNull(key);
		return getEntry(key) != null;
	}

	private Entry<K, V> getEntry(Object key) {
		for (Entry<K, V> entry = table[index(key)]; entry != null; entry = entry.next)
			if (entry.getKey().equals(key))
				return entry;
		return null;
	}

	@Override
	public int size() {
		return size;
	}

	private static class Entry<E, V> extends SimpleEntry<E, V> {
		private Entry<E, V> next;

		private Entry(E key, V value) {
			super(key, value);
		}
	}

	// random tests
	public static void main(String[] args) {
		Random rnd = new Random();
		int range = 50;
		String[] methods0 = {"size", "isEmpty", "toArray", "clear", "keySet"};
		String[] methods1 = {"get", "containsKey", "containsValue", "remove"};
		String[] iteratorMethods = {"hasNext", "next", "remove"};
		for (int step = 0; step < 1000; step++) {
			Map<Integer, Integer> m1 = new HashMap<>();
			Map<Integer, Integer> m2 = new ChainingHashMap<>();
			for (int i = 0; i < 1000; i++) {
				if (!m1.equals(m2))
					throw new RuntimeException();
				int arg = rnd.nextInt(range) - range / 2;
				int arg2 = rnd.nextInt(range) - range / 2;
				if (rnd.nextInt(3) == 0 && !Objects.equals(m1.put(arg, arg2), m2.put(arg, arg2)))
					throw new RuntimeException();
				int op = rnd.nextInt(methods0.length + methods1.length);
				check(m1, m2, op < methods0.length ? methods0[op] : methods1[op - methods0.length], op < methods0.length ? null : arg);

				if (rnd.nextInt(100) == 0) {
					Map<Integer, Integer> lm = new LinkedHashMap<>(m2);
					Iterator<Map.Entry<Integer, Integer>> it1 = lm.entrySet().iterator();
					Iterator<Map.Entry<Integer, Integer>> it2 = m2.entrySet().iterator();
					for (int j = 0; j < 10; j++)
						check(it1, it2, iteratorMethods[rnd.nextInt(iteratorMethods.length)], null);
					m1 = new HashMap<>(lm);
				}
			}
		}
	}

	static void check(Object obj1, Object obj2, String methodName, Integer arg) {
		Object result1 = invoke(obj1, methodName, arg);
		Object result2 = invoke(obj2, methodName, arg);
		if (!((result1 instanceof Exception && result2 instanceof Exception) || "next".equals(methodName) || Objects.deepEquals(result1, result2)))
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
