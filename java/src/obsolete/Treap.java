package obsolete;
import java.util.*;

public class Treap {

	static class Node {
		int cnt;
		int prio;
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

	void update(Node p) {
		if (p != null)
			p.cnt = 1 + cnt(p.l) + cnt(p.r);
	}

	Node[] split(Node t, int index) {
		if (t == null)
			return new Node[2];
		Node[] res;
		if (index <= cnt(t.l)) {
			Node[] r = split(t.l, index);
			t.l = r[1];
			res = new Node[] { r[0], t };
		} else {
			Node[] r = split(t.r, index - cnt(t.l) - 1);
			t.r = r[0];
			res = new Node[] { t, r[1] };
		}
		update(t);
		return res;
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
		update(res);
		return res;
	}

	int get(Node t, int index) {
		if (index < cnt(t.l))
			return get(t.l, index);
		else if (index > cnt(t.l))
			return get(t.r, index - cnt(t.l) - 1);
		return t.value;
	}

	public int get(int index) {
		return get(root, index);
	}

	Node add(Node t, Node it, int index) {
		Node res;
		if (t == null)
			res = it;
		else if (it.prio < t.prio) {
			Node[] r = split(t, index);
			it.l = r[0];
			it.r = r[1];
			res = it;
		} else {
			if (index <= cnt(t.l))
				t.l = add(t.l, it, index);
			else
				t.r = add(t.r, it, index - cnt(t.l) - 1);
			res = t;
		}
		update(res);
		return res;
	}

	public void add(int index, int value) {
		Node it = new Node();
		it.value = value;
		it.prio = rnd.nextInt();
		root = add(root, it, index);
	}

	Node remove(Node t, int index) {
		Node res;
		if (index == cnt(t.l))
			res = merge(t.l, t.r);
		else {
			if (index < cnt(t.l))
				t.l = remove(t.l, index);
			else
				t.r = remove(t.r, index - cnt(t.l) - 1);
			res = t;
		}
		update(res);
		return res;
	}

	public void remove(int index) {
		root = remove(root, index);
	}

	void print(Node t) {
		if (t != null) {
			print(t.l);
			System.out.print(t.value + " ");
			print(t.r);
		}
	}

	public void print() {
		print(root);
		System.out.println();
	}

	// Usage example
	public static void main(String[] args) {
		Treap treap = new Treap();
		treap.add(0, 1);
		treap.add(0, 2);
		treap.add(0, 3);
		treap.remove(2);
		treap.remove(1);
		for (int i = 0; i < treap.size(); i++) {
			System.out.println(treap.get(i));
		}
		treap.print();
		treap.remove(0);
	}
}
