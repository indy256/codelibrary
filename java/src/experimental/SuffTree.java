package experimental;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

public class SuffTree implements Runnable {

	public class SuffixTree {

		public class Edge {
			int l, r;
			Vertex target;
			
			Edge(int ll, int rr, Vertex t) {
				l = ll;
				r = rr;
				target = t;
			}
			
		}

		public int nextInd = 0;

		public class Vertex {
			Vertex suffixLink;
			Map<Character, Edge> h;
			int ind;
			
			Vertex() {
				h = new TreeMap<Character, Edge>();
				ind = nextInd++;
			}		
		}
		
		public class RefPair { 
			Vertex s;	
			int k, p;
			
			public RefPair(Vertex ss, int kk, int pp) {
				s = ss;
				k = kk;
				p = pp;
			}

			public void canonize() {
				if (p < k) {
					return;
				}
				Edge e = s.h.get(charAt(k));
				while (e.r - e.l <= p - k) {
					k += e.r - e.l + 1;
					s = e.target;
					if (k <= p) {
						e = s.h.get(charAt(k));
					}
				}
			}	
		}
		
		public char[] str;
		public Vertex root;
		
		private RefPair active;
		private boolean end;
		
		private char charAt(int i) {
			if (i < 0) {
				return (char) ('A' - i + 1);
			}
			return str[i];
		}
		
		SuffixTree(char[] a) {
			str = a;
			root = new Vertex();
			root.suffixLink = new Vertex();
			active = new RefPair(root, 0, -1);
			for (int i = 0; i < 27; ++i) {
				root.suffixLink.h.put((char) ('A' + i), new Edge(-i - 1, -i - 1, root));
			}
			for (int i = 0; i < str.length; ++i) {
				addChar(i);
			}
		}

		private void addChar(int i) {
			update(i);
			active.p = i;
			active.canonize();
		}

		private void update(int i) {
			Vertex oldr = null;
			Vertex r = null;
			end = false;
			r = testSplit(active, charAt(i));
			while (!end) {
				r.h.put(charAt(i), new Edge(i, str.length - 1, new Vertex()));
				if (oldr != null) {
					oldr.suffixLink = r;
				}
				oldr = r;
				active.s = active.s.suffixLink;
				active.canonize();
				r = testSplit(active, charAt(i));
			}
			if (oldr != null) {
				oldr.suffixLink = active.s;
			}
		}

		private Vertex testSplit(RefPair a, char t) {
			if (a.k <= a.p) {
				Edge e = a.s.h.get(charAt(a.k));
				if (charAt(e.l + a.p - a.k + 1) == t) {
					end = true;
					return a.s;
				}
				Vertex r = new Vertex();
				r.h.put(charAt(e.l + a.p - a.k + 1), new Edge(e.l + a.p - a.k + 1, e.r, e.target));
				e.r = e.l + a.p - a.k;
				e.target = r;
				return r;
			} else {
				if (a.s.h.containsKey(t)) {
					end = true;
				}
				return a.s;
			}
		}

		public void print() {
			dfs(root);
		}

		private void dfs(Vertex v) {
			if (v.suffixLink == null) {
				out.println("Vertex #" + v.ind + ":");
			} else {
				out.println("Vertex #" + v.ind + ": suffix link to vertex #" + v.suffixLink.ind);
			}
			for (Edge e : v.h.values()) {
				out.println("Edge with string " + (new String(str)).substring(e.l, e.r + 1) + " to vertex #" + e.target.ind);
			}
			for (Edge e : v.h.values()) {
				dfs(e.target);
			}
		}
		
	}

	private BufferedReader in;
	private PrintWriter out;

	public void run() {
		try {
			solve();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-15);
		} finally {
			out.close();
		}
	}
	
	private void solve() throws Exception {
		in.readLine();
		char[] a = (in.readLine() + (char)('A' + 26)).toCharArray();
		char[] b = in.readLine().toCharArray();
		SuffixTree s = new SuffixTree(a);
		SuffixTree.RefPair cur = s.new RefPair(s.root, 0, -1);
		SuffixTree.RefPair ans = null;
		int ansLen = -1;
		int curLen = 0;
		for (int i = 0; i < b.length; ++i) {
			while (true) {
				if (cur.k <= cur.p) {
					SuffixTree.Edge e = cur.s.h.get(s.str[cur.k]);
					if (s.str[e.l + cur.p - cur.k + 1] == b[i]) {
						cur.p++;
						break;
					}
				} else {
					if (cur.s.h.containsKey(b[i])) {
						cur.k = cur.p = cur.s.h.get(b[i]).l;
						break;
					}
				}
				cur.s = cur.s.suffixLink;
				cur.canonize();
				curLen--;
			}
			curLen++;
			cur.canonize();
			if (curLen > ansLen) {
				ansLen = curLen;
				ans = s.new RefPair(cur.s, cur.k, cur.p);
			}
		}
		char[] c = new char[ansLen];
		int j = ans.p;
		if (ans.k > ans.p) {
			j = ans.s.h.values().iterator().next().l - 1;
		}
		for (int i = c.length - 1; i >= 0; --i) {
			c[i] = s.str[j--];
		}
		out.print(new String(c));
	}

	SuffTree() {
		in = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out);
	}
	
	public static void main(String[] args) {
		(new Thread(new SuffTree())).start();
	}

}