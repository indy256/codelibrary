package obsolete;
import java.util.*;

public class TreapExplicitKey {

	static class Node {
		int cnt;
		int prio;
		int key;
		int value;
		Node l;
		Node r;
	}

	static Random rnd = new Random(1);
	Node root;

	public int size() {
		return cnt(root);
	}

	int cnt(Node p) {
		return p == null ? 0 : p.cnt;
	}

	void update_cnt(Node p) {
		if (p != null)
			p.cnt = 1 + cnt(p.l) + cnt(p.r);
	}

	Node merge(Node l, Node r) {
		Node res;
		if (l == null)
			res = r;
		else if (r == null)
			res = l;
		else if (l.prio > r.prio) {
			l.r = merge(l.r, r);
			res = l;
		} else {
			r.l = merge(l, r.l);
			res = r;
		}
		update_cnt(res);
		return res;
	}

	Node[] split(Node t, int key) {
		if (t == null)
			return new Node[2];
		Node[] res;
		if (key <= t.key) {
			Node[] r = split(t.l, key);
			t.l = r[1];
			res = new Node[] { r[0], t };
		} else {
			Node[] r = split(t.r, key);
			t.r = r[0];
			res = new Node[] { t, r[1] };
		}
		update_cnt(t);
		return res;
	}

	int get(Node t, int key) {
		if (key < t.key)
			return get(t.l, key);
		else if (key > t.key)
			return get(t.r, key);

		return t.value;
	}

	public int get(int key) {
		return get(root, key);
	}

	int kth(Node t, int index) {
		if (index < cnt(t.l))
			return kth(t.l, index);
		else if (index > cnt(t.l))
			return kth(t.r, index - cnt(t.l) - 1);
		return t.key;
	}

	public int kth(int index) {
		return kth(root, index);
	}

	Node insert(Node t, Node it) {
		Node res;
		if (t == null)
			res = it;
		else if (it.prio < t.prio) {
			Node[] r = split(t, it.key);
			it.l = r[0];
			it.r = r[1];
			res = it;
		} else {
			if (it.key <= t.key)
				t.l = insert(t.l, it);
			else
				t.r = insert(t.r, it);
			res = t;
		}
		update_cnt(res);
		return res;
	}

	public void put(int key, int value) {
		Node it = new Node();
		it.key = key;
		it.value = value;
		it.prio = rnd.nextInt();
		root = insert(root, it);
	}

	Node remove(Node t, int key) {
		Node res;
		if (key == t.key)
			res = merge(t.l, t.r);
		else {
			if (key < t.key)
				t.l = remove(t.l, key);
			else
				t.r = remove(t.r, key);
			res = t;
		}
		update_cnt(res);
		return res;
	}

	public void remove(int key) {
		root = remove(root, key);
	}

	void print(Node t) {
		if (t != null) {
			print(t.l);
			System.out.print(t.key + " ");
			print(t.r);
		}
	}

	public void print() {
		print(root);
		System.out.println();
	}

	// Usage example
	public static void main(String[] args) {
		TreapExplicitKey treap = new TreapExplicitKey();
		treap.put(3, 30);
		treap.put(1, 10);
		treap.put(2, 20);
		treap.remove(1);
		for (int i = 0; i < treap.size(); i++) {
			int key = treap.kth(i);
			int value = treap.get(key);
			System.out.println(key + " " + value);
		}
		treap.print();
	}
}
