package sandbox;

// M is even and M >= 4
public class BTree<Key extends Comparable<Key>, Value> {
	private static final int M = 4; // max children per B-tree node = M-1

	private Node<Key, Value> root; // root of the B-tree
	private int HT; // height of the B-tree
	private int N; // number of key-value pairs in the B-tree

	// helper B-tree node data type
	private static final class Node<Key, Value> {
		private int m; // number of children
		private final Entry<Key, Value>[] children = new Entry[M]; // the array of children

		private Node(int k) {
			m = k;
		}
	}

	// internal nodes: only use key and next
	// external nodes: only use key and value
	private static class Entry<Key, Value> {
		private Key key;
		private final Value value;
		private Node<Key, Value> next; // helper field to iterate over array entries

		public Entry(Key key, Value value, Node<Key, Value> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}

	// constructor
	public BTree() {
		root = new Node<Key, Value>(0);
	}

	// return number of key-value pairs in the B-tree
	public int size() {
		return N;
	}

	// return height of B-tree
	public int height() {
		return HT;
	}

	// search for given key, return associated value; return null if no such key
	public Value get(Key key) {
		return search(root, key, HT);
	}

	private Value search(Node<Key, Value> x, Key key, int ht) {
		Entry<Key, Value>[] children = x.children;

		// external node
		if (ht == 0) {
			for (int j = 0; j < x.m; j++) {
				if (eq(key, children[j].key))
					return children[j].value;
			}
		}

		// internal node
		else {
			for (int j = 0; j < x.m; j++) {
				if (j + 1 == x.m || less(key, children[j + 1].key))
					return search(children[j].next, key, ht - 1);
			}
		}
		return null;
	}

	// insert key-value pair
	// add code to check for duplicate keys
	public void put(Key key, Value value) {
		Node<Key, Value> u = insert(root, key, value, HT);
		N++;
		if (u == null)
			return;

		// need to split root
		Node<Key, Value> t = new Node<Key, Value>(2);
		t.children[0] = new Entry<Key, Value>(root.children[0].key, null, root);
		t.children[1] = new Entry<Key, Value>(u.children[0].key, null, u);
		root = t;
		HT++;
	}

	private Node<Key, Value> insert(Node<Key, Value> h, Key key, Value value, int ht) {
		int j;
		Entry<Key, Value> t = new Entry<Key, Value>(key, value, null);

		// external node
		if (ht == 0) {
			for (j = 0; j < h.m; j++) {
				if (less(key, h.children[j].key))
					break;
			}
		}

		// internal node
		else {
			for (j = 0; j < h.m; j++) {
				if ((j + 1 == h.m) || less(key, h.children[j + 1].key)) {
					Node<Key, Value> u = insert(h.children[j++].next, key, value, ht - 1);
					if (u == null)
						return null;
					t.key = u.children[0].key;
					t.next = u;
					break;
				}
			}
		}

		for (int i = h.m; i > j; i--)
			h.children[i] = h.children[i - 1];
		h.children[j] = t;
		h.m++;
		if (h.m < M)
			return null;
		else
			return split(h);
	}

	// split node in half
	private Node<Key, Value> split(Node<Key, Value> h) {
		Node<Key, Value> t = new Node<Key, Value>(M / 2);
		h.m = M / 2;
		for (int j = 0; j < M / 2; j++)
			t.children[j] = h.children[M / 2 + j];
		return t;
	}

	// for debugging
	public String toString() {
		return toString(root, HT, "") + "\n";
	}

	private String toString(Node<Key, Value> h, int ht, String indent) {
		String s = "";
		Entry<Key, Value>[] children = h.children;

		if (ht == 0) {
			for (int j = 0; j < h.m; j++) {
				s += indent + children[j].key + " " + children[j].value + "\n";
			}
		} else {
			for (int j = 0; j < h.m; j++) {
				if (j > 0)
					s += indent + "(" + children[j].key + ")\n";
				s += toString(children[j].next, ht - 1, indent + "     ");
			}
		}
		return s;
	}

	private boolean less(Key k1, Key k2) {
		return k1.compareTo(k2) < 0;
	}

	private boolean eq(Key k1, Key k2) {
		return k1.compareTo(k2) == 0;
	}

	/*************************************************************************
	 * test client
	 *************************************************************************/
	public static void main(String[] args) {
		BTree<String, String> st = new BTree<String, String>();

		// st.put("www.cs.princeton.edu", "128.112.136.12");
		st.put("www.cs.princeton.edu", "128.112.136.11");
		st.put("www.princeton.edu", "128.112.128.15");
		st.put("www.yale.edu", "130.132.143.21");
		st.put("www.simpsons.com", "209.052.165.60");
		st.put("www.apple.com", "17.112.152.32");
		st.put("www.amazon.com", "207.171.182.16");
		st.put("www.ebay.com", "66.135.192.87");
		st.put("www.cnn.com", "64.236.16.20");
		st.put("www.google.com", "216.239.41.99");
		st.put("www.nytimes.com", "199.239.136.200");
		st.put("www.microsoft.com", "207.126.99.140");
		st.put("www.dell.com", "143.166.224.230");
		st.put("www.slashdot.org", "66.35.250.151");
		st.put("www.espn.com", "199.181.135.201");
		st.put("www.weather.com", "63.111.66.11");
		st.put("www.yahoo.com", "216.109.118.65");

		System.out.println("cs.princeton.edu:  " + st.get("www.cs.princeton.edu"));
		System.out.println("hardvardsucks.com: " + st.get("www.harvardsucks.com"));
		System.out.println("simpsons.com:      " + st.get("www.simpsons.com"));
		System.out.println("apple.com:         " + st.get("www.apple.com"));
		System.out.println("ebay.com:          " + st.get("www.ebay.com"));
		System.out.println("dell.com:          " + st.get("www.dell.com"));
		System.out.println();

		System.out.println("size:    " + st.size());
		System.out.println("height:  " + st.height());
		System.out.println(st);
		System.out.println();
	}
}