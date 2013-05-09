package experimental;

import java.util.HashMap;
import java.util.Map;

public class HashTable<K, V> {

	Entry<K, V>[] table;

	static int hash(int h) {
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}

	static int indexFor(int h, int length) {
		return h & (length - 1);
	}

	static class Entry<K, V> {
		final int hash;
		final K key;
		V value;
		Entry<K, V> next;

		Entry(int h, K k, V v, Entry<K, V> n) {
			hash = h;
			key = k;
			value = v;
			next = n;
		}
	}

	public V get(K key) {
		int hash = hash(key.hashCode());
		for (Entry<K, V> e = table[indexFor(hash, table.length)]; e != null; e = e.next) {
			K k;
			if (e.hash == hash && ((k = e.key) == key || key.equals(k)))
				return e.value;
		}
		return null;
	}

	// Usage example
	public static void main(String[] args) {

	}

}
