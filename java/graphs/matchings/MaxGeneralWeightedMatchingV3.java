package graphs.matchings;

import java.util.*;

// Taken from http://uoj.ac/submission/187480
public class MaxGeneralWeightedMatchingV3 {
    // N^3 (but fast in practice)
    static final int INF = Integer.MAX_VALUE;
    static final int N = 1032;

    static class edge {
        int u, v, w;

        public edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    int n, n_x;
    edge[][] g = new edge[N * 2][N * 2];
    int[] lab = new int[N * 2];
    int[] match = new int[N * 2], slack = new int[N * 2], st = new int[N * 2], pa = new int[N * 2];
    int[][] flower_from = new int[N * 2][N + 1];
    int[] S = new int[N * 2];
    int[] vis = new int[N * 2];
    List<Integer>[] flower = new List[N * 2];

    {
        for (int i = 0; i < flower.length; i++) {
            flower[i] = new ArrayList();
        }
    }

    Deque<Integer> q = new ArrayDeque<>();

    int e_delta(edge e) {
        return lab[e.u] + lab[e.v] - g[e.u][e.v].w * 2;
    }

    void update_slack(int u, int x) {
        if (slack[x] == 0 || e_delta(g[u][x]) < e_delta(g[slack[x]][x]))
            slack[x] = u;
    }

    void set_slack(int x) {
        slack[x] = 0;
        for (int u = 1; u <= n; ++u)
            if (g[u][x].w > 0 && st[u] != x && S[st[u]] == 0)
                update_slack(u, x);
    }

    void q_push(int x) {
        if (x <= n)
            q.addLast(x);
        else
            for (int i : flower[x]) q_push(i);
    }

    void set_st(int x, int b) {
        st[x] = b;
        if (x > n)
            for (int i : flower[x]) set_st(i, b);
    }

    int get_pr(int b, int xr) {
        int pr = flower[b].indexOf(xr);
        //        int pr = find(flower[b].begin(), flower[b].end(), xr) - flower[b].begin();
        if (pr % 2 == 1) {
            reverse(flower[b], 1, flower[b].size());
            // reverse(flower[b].begin() + 1, flower[b].end());
            return (int) flower[b].size() - pr;
        } else
            return pr;
    }

    void set_match(int u, int v) {
        match[u] = g[u][v].v;
        if (u <= n)
            return;
        edge e = g[u][v];
        int xr = flower_from[u][e.u], pr = get_pr(u, xr);
        for (int i = 0; i < pr; ++i) set_match(flower[u].get(i), flower[u].get(i ^ 1));
        set_match(xr, v);
        rotate(flower[u], 0, pr, flower[u].size());
        //        rotate(flower[u].begin(), flower[u].begin() + pr, flower[u].end());
    }

    static void rotate(List<Integer> a, int first, int middle, int last) {
        reverse(a, first, middle);
        reverse(a, middle, last);
        reverse(a, first, last);
    }

    static void reverse(List<Integer> a, int from, int to) {
        while (from + 1 < to) swap(a, from++, --to);
    }

    static void swap(List<Integer> a, int i, int j) {
        a.set(i, a.set(j, a.get(i)));
    }

    void augment(int u, int v) {
        for (;;) {
            int xnv = st[match[u]];
            set_match(u, v);
            if (xnv == 0)
                return;
            set_match(xnv, st[pa[xnv]]);
            u = st[pa[xnv]];
            v = xnv;
        }
    }

    static int t = 0;

    int get_lca(int u, int v) {
        for (++t; u != 0 || v != 0;) {
            if (u == 0) {
                int tmp = u;
                u = v;
                v = tmp;
                continue;
            }
            if (vis[u] == t)
                return u;
            vis[u] = t;
            u = st[match[u]];
            if (u != 0)
                u = st[pa[u]];
            int tmp = u;
            u = v;
            v = tmp;
        }
        return 0;
    }

    void add_blossom(int u, int lca, int v) {
        int b = n + 1;
        while (b <= n_x && st[b] != 0) ++b;
        if (b > n_x)
            ++n_x;
        lab[b] = 0;
        S[b] = 0;
        match[b] = match[lca];
        flower[b].clear();
        flower[b].add(lca);
        for (int x = u, y; x != lca; x = st[pa[y]]) {
            flower[b].add(x);
            flower[b].add(y = st[match[x]]);
            q_push(y);
        }
        reverse(flower[b], 1, flower[b].size());
        // reverse(flower[b].begin() + 1, flower[b].end());
        for (int x = v, y; x != lca; x = st[pa[y]]) {
            flower[b].add(x);
            flower[b].add(y = st[match[x]]);
            q_push(y);
        }
        set_st(b, b);
        for (int x = 1; x <= n_x; ++x) g[b][x].w = g[x][b].w = 0;
        for (int x = 1; x <= n; ++x) flower_from[b][x] = 0;
        for (int i = 0; i < flower[b].size(); ++i) {
            int xs = flower[b].get(i);
            for (int x = 1; x <= n_x; ++x)
                if (g[b][x].w == 0 || e_delta(g[xs][x]) < e_delta(g[b][x])) {
                    g[b][x] = g[xs][x];
                    g[x][b] = g[x][xs];
                }
            for (int x = 1; x <= n; ++x)
                if (flower_from[xs][x] != 0)
                    flower_from[b][x] = xs;
        }
        set_slack(b);
    }

    void expand_blossom(int b) {
        for (int i : flower[b]) set_st(i, i);
        int xr = flower_from[b][g[b][pa[b]].u];
        int pr = get_pr(b, xr);
        for (int i = 0; i < pr; i += 2) {
            int xs = flower[b].get(i);
            int xns = flower[b].get(i + 1);
            pa[xs] = g[xns][xs].u;
            S[xs] = 1;
            S[xns] = 0;
            slack[xs] = 0;
            set_slack(xns);
            q_push(xns);
        }
        S[xr] = 1;
        pa[xr] = pa[b];
        for (int i = pr + 1; i < flower[b].size(); ++i) {
            int xs = flower[b].get(i);
            S[xs] = -1;
            set_slack(xs);
        }
        st[b] = 0;
    }

    boolean on_found_edge(edge e) {
        int u = st[e.u], v = st[e.v];
        if (S[v] == -1) {
            pa[v] = e.u;
            S[v] = 1;
            int nu = st[match[v]];
            slack[v] = slack[nu] = 0;
            S[nu] = 0;
            q_push(nu);
        } else if (S[v] == 0) {
            int lca = get_lca(u, v);
            if (lca == 0) {
                augment(u, v);
                augment(v, u);
                return true;
            } else {
                add_blossom(u, lca, v);
            }
        }
        return false;
    }

    boolean matching() {
        Arrays.fill(S, 1, n_x + 1, -1);
        //        memset(S + 1, -1, sizeof( int) *n_x);
        Arrays.fill(slack, 1, n_x + 1, 0);
        //        memset(slack + 1, 0, sizeof( int) *n_x);
        q = new ArrayDeque<>();
        for (int x = 1; x <= n_x; ++x)
            if (st[x] == x && match[x] == 0) {
                pa[x] = 0;
                S[x] = 0;
                q_push(x);
            }
        if (q.isEmpty())
            return false;
        for (;;) {
            while (!q.isEmpty()) {
                int u = q.removeFirst();
                if (S[st[u]] == 1)
                    continue;
                for (int v = 1; v <= n; ++v)
                    if (g[u][v].w > 0 && st[u] != st[v]) {
                        if (e_delta(g[u][v]) == 0) {
                            if (on_found_edge(g[u][v]))
                                return true;
                        } else
                            update_slack(u, st[v]);
                    }
            }
            int d = INF;
            for (int b = n + 1; b <= n_x; ++b)
                if (st[b] == b && S[b] == 1)
                    d = Math.min(d, lab[b] / 2);
            for (int x = 1; x <= n_x; ++x)
                if (st[x] == x && slack[x] != 0) {
                    if (S[x] == -1)
                        d = Math.min(d, e_delta(g[slack[x]][x]));
                    else if (S[x] == 0)
                        d = Math.min(d, e_delta(g[slack[x]][x]) / 2);
                }
            for (int u = 1; u <= n; ++u) {
                if (S[st[u]] == 0) {
                    if (lab[u] <= d)
                        return false;
                    lab[u] -= d;
                } else if (S[st[u]] == 1)
                    lab[u] += d;
            }
            for (int b = n + 1; b <= n_x; ++b)
                if (st[b] == b) {
                    if (S[st[b]] == 0)
                        lab[b] += d * 2;
                    else if (S[st[b]] == 1)
                        lab[b] -= d * 2;
                }
            q = new ArrayDeque<>();
            for (int x = 1; x <= n_x; ++x)
                if (st[x] == x && slack[x] != 0 && st[slack[x]] != x && e_delta(g[slack[x]][x]) == 0)
                    if (on_found_edge(g[slack[x]][x]))
                        return true;
            for (int b = n + 1; b <= n_x; ++b)
                if (st[b] == b && S[b] == 1 && lab[b] == 0)
                    expand_blossom(b);
        }
    }

    static class Result {
        long totalWeight;
        int matches;

        public Result(long totalWeight, int matches) {
            this.totalWeight = totalWeight;
            this.matches = matches;
        }
    }

    Result solve() {
        //        memset(match + 1, 0, sizeof(int) * n);
        Arrays.fill(match, 1, n + 1, 0);
        n_x = n;
        int n_matches = 0;
        long tot_weight = 0;
        for (int u = 0; u <= n; ++u) {
            st[u] = u;
            flower[u].clear();
        }
        int w_max = 0;
        for (int u = 1; u <= n; ++u)
            for (int v = 1; v <= n; ++v) {
                flower_from[u][v] = (u == v ? u : 0);
                w_max = Math.max(w_max, g[u][v].w);
            }
        for (int u = 1; u <= n; ++u) lab[u] = w_max;
        while (matching()) ++n_matches;
        for (int u = 1; u <= n; ++u)
            if (match[u] != 0 && match[u] < u)
                tot_weight += g[u][match[u]].w;
        return new Result(tot_weight, n_matches);
    }

    void add_edge(int u, int v, int w) {
        g[u][v].w = g[v][u].w = w;
    }

    void init(int _n) {
        n = _n;
        for (int u = 1; u <= n; ++u)
            for (int v = 1; v <= n; ++v) g[u][v] = new edge(u, v, 0);
    }

    // usage example
    public static void main(String[] args) {
        int n = 4;
        MaxGeneralWeightedMatchingV3 mm = new MaxGeneralWeightedMatchingV3();
        mm.init(n);
        mm.add_edge(1, 2, 3);
        mm.add_edge(1, 3, 4);
        mm.add_edge(4, 3, 4);
        Result res = mm.solve();
        System.out.println(res.totalWeight + " " + res.matches);
    }
}
