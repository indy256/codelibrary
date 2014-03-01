package experimental;

import java.lang.reflect.Method;
import java.util.*;

// todo
public class OpenAddressingHashMap<K, V> extends AbstractMap<K, V> {
	private int size;
	private K[] keys;
	private V[] values;
	private final Set<Map.Entry<K, V>> entrySet;

	private static final int[] shifts = new int[10];

	static {
		Random random = new Random();
		for (int i = 0; i < shifts.length; i++)
			shifts[i] = 1 + 3 * i + random.nextInt(3);
	}

	public OpenAddressingHashMap() {
		this(4);
	}

	@SuppressWarnings("unchecked")
	private void setCapacity(int size) {
		keys = (K[]) new Object[Integer.highestOneBit(size) * 4];
		values = (V[]) new Object[Integer.highestOneBit(size) * 4];
	}

	public OpenAddressingHashMap(int initialCapacity) {
		setCapacity(initialCapacity);
		entrySet = new AbstractSet<Map.Entry<K, V>>() {
			@Override
			public Iterator<Map.Entry<K, V>> iterator() {
				return new Iterator<Map.Entry<K, V>>() {
					private int index;
					private Entry lastReturned;

					@Override
					public boolean hasNext() {
						for (; index < keys.length; index++)
							if (keys[index] != null)
								return true;
						return false;
					}

					@Override
					public Map.Entry<K, V> next() {
						if (!hasNext())
							throw new NoSuchElementException();
						return lastReturned = new Entry(index++);
					}

					@Override
					public void remove() {
						if (lastReturned == null)
							throw new IllegalStateException();
						OpenAddressingHashMap.this.remove(lastReturned);
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

	public OpenAddressingHashMap(Map<K, V> map) {
		this(map.size());
		putAll(map);
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return entrySet;
	}

	@Override
	public void clear() {
		Arrays.fill(keys, null);
		Arrays.fill(values, null);
		size = 0;
	}

	private int index(Object o) {
		return getHash(o.hashCode()) & (keys.length - 1);
	}

	private int getHash(int h) {
		int result = h;
		for (int i : shifts)
			result ^= h >>> i;
		return result;
	}

	private void shiftEntries(int pos) {
		for (int i = nextIndex(pos); keys[i] != null; i = nextIndex(i)) {
			int slot = index(keys[i]);
			if ((i < slot && (slot <= pos || pos <= i)) || (slot <= pos && pos <= i)) {
				keys[pos] = keys[i];
				keys[i] = null;
				values[pos] = values[i];
				values[i] = null;
				pos = i;
			}
		}
	}

	private int nextIndex(int index) {
		return (index + 1) & (keys.length - 1);
	}

	@Override
	public V remove(Object key) {
		Objects.requireNonNull(key);
		int index = index(key);
		while (true) {
			if (keys[index] == null)
				return null;
			if (keys[index].equals(key)) {
				--size;
				V oldValue = values[index];
				keys[index] = null;
				values[index] = null;
				shiftEntries(index);
				return oldValue;
			}
			index = nextIndex(index);
		}
	}

	@Override
	public V put(K key, V value) {
		Objects.requireNonNull(key);
		int index = index(key);
		while (keys[index] != null) {
			if (keys[index].equals(key)) {
				V oldValue = values[index];
				values[index] = value;
				return oldValue;
			}
			index = nextIndex(index);
		}
		keys[index] = key;
		values[index] = value;
		++size;
		if (2 * size > keys.length) {
			K[] oldKeys = keys;
			V[] oldValues = values;
			setCapacity(size);
			for (int i = 0; i < oldKeys.length; i++) {
				if (oldKeys[i] == null)
					continue;
				index = index(oldKeys[i]);
				while (keys[index] != null)
					index = nextIndex(index);
				keys[index] = oldKeys[i];
				values[index] = oldValues[i];
			}
		}
		return null;
	}

	@Override
	public V get(Object key) {
		Objects.requireNonNull(key);
		Integer index = getIndex(key);
		return index != null ? values[index] : null;
	}

	@Override
	public boolean containsKey(Object key) {
		Objects.requireNonNull(key);
		return getIndex(key) != null;
	}

	private Integer getIndex(Object key) {
		int index = index(key);
		while (keys[index] != null) {
			if (keys[index].equals(key))
				return index;
			index = nextIndex(index);
		}
		return null;
	}

	@Override
	public int size() {
		return size;
	}

	private class Entry implements Map.Entry<K, V> {
		private final int index;

		private Entry(int index) {
			this.index = index;
		}

		@Override
		public K getKey() {
			return keys[index];
		}

		@Override
		public V getValue() {
			return values[index];
		}

		@Override
		public V setValue(V value) {
			V oldValue = values[index];
			values[index] = value;
			return oldValue;
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
			Map<Integer, Integer> m2 = new OpenAddressingHashMap<>();
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
