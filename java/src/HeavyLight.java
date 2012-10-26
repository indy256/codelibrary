import java.util.*;

public class HeavyLight {

	final int maxN = 100000;
	int size[], p[];
	List<Integer>[] c;
	int tn, root[], num[];
	int no[], pos[], t[];
	int mpos, mem[];
	int Time, t_in[], t_out[];

	public HeavyLight() {
		size = new int[maxN];
		p = new int[maxN];
		c = new List[maxN];
		for (int i = 0; i < maxN; i++) {
			c[i] = new ArrayList<Integer>();
		}
		root = new int[maxN];
		num = new int[maxN];
		no = new int[maxN];
		pos = new int[maxN];
		t = new int[maxN];
		mem = new int[2 * maxN];
		t_in = new int[maxN];
		t_out = new int[maxN];
	}

	void dfs(int v, int pr) {
		int x;
		t_in[v] = Time++;
		p[v] = pr;
		size[v] = 1;

		for (int i = 0; i < c[v].size(); i++)
			if ((x = c[v].get(i)) != pr) {
				dfs(x, v);
				size[v] += size[x];
			}
		t_out[v] = Time++;
	}

	int new_t(int v) {
		root[tn] = v;
		return tn++;
	}

	void build(int v, int v_no) {
		int x;
		no[v] = v_no;
		pos[v] = num[v_no]++;
		for (int i = 0; i < c[v].size(); i++)
			if ((x = c[v].get(i)) != p[v])
				build(x, 2 * size[x] > size[v] ? v_no : new_t(x));
	}

	void inc(int i, int j, int v) {
		j += num[i];
		// mem[t[i]][j] += v;
		for (j /= 2; j > 0; j /= 2) {
			// t[i][j] = Math.max(t[i][2 * (j)], t[i][2 * (j) + 1]);
		}
	}

	int get(int i, int l, int r) {
		int[] T = null;// t[i];
		int N = num[i];
		int res = 0;
		for (l += N, r += N; l <= r; l /= 2, r /= 2) {
			if (l % 2 == 1)
				res = Math.max(res, T[l++]);
			if (r % 2 == 0)
				res = Math.max(res, T[r--]);
		}
		return res;
	}

	boolean ancestor(int i, int j) {
		return t_in[i] <= t_in[j] && t_out[j] <= t_out[i];
	}

	public static void main(String[] args) {
		HeavyLight hv = new HeavyLight();

		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();

		for (int i = 0; i < n - 1; i++) {
			int a = sc.nextInt() - 1;
			int b = sc.nextInt() - 1;

			hv.c[a].add(b);
			hv.c[b].add(a);
		}

		hv.dfs(0, -1);
		hv.build(0, hv.new_t(0));

		for (int i = 0; i < hv.tn; i++) {
			// hv.t[i] = hv.mpos;
			hv.mpos += 2 * hv.num[i];
		}

		int m = sc.nextInt();

		while (m-- > 0) {
			String c = sc.next();
			if ("I".equals(c)) {
				int a = sc.nextInt() - 1;
				int b = sc.nextInt() - 1;
				hv.inc(hv.no[a], hv.pos[a], b);
			} else {
				int a = sc.nextInt() - 1;
				int b = sc.nextInt() - 1;
				int res = 0;
				for (int t = 0; t < 2; t++) {
					int v;
					while (!hv.ancestor(v = hv.root[hv.no[a]], b)) {
						res = Math.max(res, hv.get(hv.no[a], 0, hv.pos[a]));
						a = hv.p[v];
					}
					int x = a;
					a = b;
					b = x;
				}
				// printf("%d\n", max(res, get(no[a], min(pos[a], pos[b]), max(pos[a], pos[b]))));
			}
		}
	}
}
