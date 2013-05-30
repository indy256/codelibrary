public class LinkedCutTree {

	static final int MAXN = 110000;

	static class Node {
		static final Node NILL = new Node();
		Node l, r, p, pp;
		int size;
		int nodeValue;
		int subTreeValue;
		boolean rev;

		Node(int size, int nodeValue) {
			this.size = size;
			this.nodeValue = nodeValue;
			l = new Node();
			r = new Node();
			p = new Node();
			pp = new Node();
		}

		Node() {
			l = r = p = pp = this;
		}

		void push() {
			if (rev) {
				l.rev = !l.rev;
				r.rev = !r.rev;
				rev = false;
				Node t = l;
				l = r;
				r = t;
			}
		}

		void update() {
			size = (this != NILL ? 1 : 0) + l.size + r.size;
			subTreeValue = nodeValue + l.subTreeValue + r.subTreeValue;
			l.p = r.p = this;
		}
	}

	static Node[] v2n = new Node[MAXN];

	static void rotate(Node v) {
		Node u = v.p;
		if (v == u.l) {
			u.l = v.r;
			v.r = u;
		} else {
			u.r = v.l;
			v.l = u;
		}
		Node t = u.p;
		u.p = v.p;
		v.p = t;
		t = v.pp;
		v.pp = u.pp;
		u.pp = t;
		if (v.p != Node.NILL)
			if (v.p.r == u)
				v.p.r = v;
			else
				v.p.l = v;
		u.update();
		v.update();
	}

	static void bigRotate(Node v) {
		v.p.p.push();
		v.p.push();
		v.push();
		if (v.p.p == Node.NILL)
			rotate(v);
		else if ((v.p.l == v) ^ (v.p.p.r == v.p)) {
			rotate(v.p);
			rotate(v);
		} else {
			rotate(v);
			rotate(v);
		}

	}

	static void splay(Node v) {
		while (v.p != Node.NILL)
			bigRotate(v);
	}

	static void splitAfter(Node v) {
		v.push();
		splay(v);
		v.r.p = Node.NILL;
		v.r.pp = v;
		v.r = Node.NILL;
		v.update();
	}

	static void expose(int x) {
		Node v = v2n[x];
		splitAfter(v);
		while (v.pp != Node.NILL) {
			splitAfter(v.pp);
			v.pp.r = v;
			v.pp.update();
			v = v.pp;
			v.r.pp = Node.NILL;
		}
		splay(v2n[x]);
	}

	static void makeRoot(int x) {
		expose(x);
		v2n[x].rev = !v2n[x].rev;
	}

	static void link(int ch, int p) {
		makeRoot(ch);
		makeRoot(p);
		v2n[ch].pp = v2n[p];
	}

	static void cut(int x, int y) {
		expose(x);
		splay(v2n[y]);
		if (v2n[y].pp != v2n[x]) {
			int t = x;
			x = y;
			y = t;
			expose(x);
			splay(v2n[y]);
		}
		v2n[y].pp = Node.NILL;
	}

	static int get(int x, int y) {
		if (x == y)
			return 0;
		makeRoot(x);
		expose(y);
		expose(x);
		splay(v2n[y]);
		if (v2n[y].pp != v2n[x])
			return -1;
		return v2n[y].subTreeValue;
	}

	public static void main(String[] args) {
		for (int i = 0; i < MAXN; i++)
			v2n[i] = new Node(1, i);
		link(1, 0);
		link(2, 0);
		link(3, 2);
		System.out.println(get(1, 3));
	}
}
