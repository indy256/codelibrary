#include <bits/stdc++.h>

using namespace std;

// Taken from https://gist.github.com/min-25/aed29a23b004505d2094a5cddaf56ff9
// Tested: https://codeforces.com/contest/1198/submission/58096981

class MaximumMatching {
    /*
      Maximum Cardinality Matching in General Graphs.
      - O(\sqrt{n} m \log_{\max\{2, 1 + m/n\}} n) time
      - O(n + m) space

      Note: each vertex is 1-indexed.

      Ref:
        Harold N. Gabow,
        "The Weighted Matching Approach to Maximum Cardinality Matching" (2017)
        (https://arxiv.org/abs/1703.03998)
    */
   public:
    struct Edge {
        int from, to;
    };
    static constexpr int Inf = 1 << 30;

   private:
    enum Label {
        kInner = -1,  // should be < 0
        kFree = 0     // should be 0
    };
    struct Link {
        int from, to;
    };
    struct Log {
        int v, par;
    };

    struct LinkedList {
        LinkedList() {}

        LinkedList(int N, int M) : N(N), next(M) { clear(); }

        void clear() { head.assign(N, -1); }

        void push(int h, int u) { next[u] = head[h], head[h] = u; }

        int N;
        vector<int> head, next;
    };

    template <typename T>
    struct Queue {
        Queue() {}

        Queue(int N) : qh(0), qt(0), data(N) {}

        T operator[](int i) const { return data[i]; }

        void enqueue(int u) { data[qt++] = u; }

        int dequeue() { return data[qh++]; }

        bool empty() const { return qh == qt; }

        void clear() { qh = qt = 0; }

        int size() const { return qt; }

        int qh, qt;
        vector<T> data;
    };

    struct DisjointSetUnion {
        DisjointSetUnion() {}

        DisjointSetUnion(int N) : par(N) {
            for (int i = 0; i < N; ++i)
                par[i] = i;
        }

        int find(int u) { return par[u] == u ? u : (par[u] = find(par[u])); }

        void unite(int u, int v) {
            u = find(u), v = find(v);
            if (u != v)
                par[v] = u;
        }

        vector<int> par;
    };

   public:
    MaximumMatching(int N, const vector<Edge> &in) : N(N), NH(N >> 1), ofs(N + 2, 0), edges(in.size() * 2) {
        for (auto &e : in)
            ofs[e.from + 1] += 1, ofs[e.to + 1] += 1;
        for (int i = 1; i <= N + 1; ++i)
            ofs[i] += ofs[i - 1];
        for (auto &e : in) {
            edges[ofs[e.from]++] = e;
            edges[ofs[e.to]++] = {e.to, e.from};
        }
        for (int i = N + 1; i > 0; --i)
            ofs[i] = ofs[i - 1];
        ofs[0] = 0;
    }

    int maximum_matching() {
        initialize();
        int match = 0;
        while (match * 2 + 1 < N) {
            reset_count();
            bool has_augmenting_path = do_edmonds_search();
            if (!has_augmenting_path)
                break;
            match += find_maximal();
            clear();
        }
        return match;
    }

   private:
    void reset_count() {
        time_current_ = 0;
        time_augment_ = Inf;
        contract_count_ = 0;
        outer_id_ = 1;
        dsu_changelog_size_ = dsu_changelog_last_ = 0;
    }

    void clear() {
        que.clear();
        for (int u = 1; u <= N; ++u)
            potential[u] = 1;
        for (int u = 1; u <= N; ++u)
            dsu.par[u] = u;
        for (int t = time_current_; t <= N / 2; ++t)
            list.head[t] = -1;
        for (int u = 1; u <= N; ++u)
            blossom.head[u] = -1;
    }

    // first phase

    inline void grow(int x, int y, int z) {
        label[y] = kInner;
        potential[y] = time_current_;  // visited time
        link[z] = {x, y};
        label[z] = label[x];
        potential[z] = time_current_ + 1;
        que.enqueue(z);
    }

    void contract(int x, int y) {
        int bx = dsu.find(x), by = dsu.find(y);
        const int h = -(++contract_count_) + kInner;
        label[mate[bx]] = label[mate[by]] = h;
        int lca = -1;
        while (1) {
            if (mate[by] != 0)
                swap(bx, by);
            bx = lca = dsu.find(link[bx].from);
            if (label[mate[bx]] == h)
                break;
            label[mate[bx]] = h;
        }
        for (auto bv : {dsu.par[x], dsu.par[y]}) {
            for (; bv != lca; bv = dsu.par[link[bv].from]) {
                int mv = mate[bv];
                link[mv] = {x, y};
                label[mv] = label[x];
                potential[mv] = 1 + (time_current_ - potential[mv]) + time_current_;
                que.enqueue(mv);
                dsu.par[bv] = dsu.par[mv] = lca;
                dsu_changelog[dsu_changelog_last_++] = {bv, lca};
                dsu_changelog[dsu_changelog_last_++] = {mv, lca};
            }
        }
    }

    bool find_augmenting_path() {
        while (!que.empty()) {
            int x = que.dequeue(), lx = label[x], px = potential[x], bx = dsu.find(x);
            for (int eid = ofs[x]; eid < ofs[x + 1]; ++eid) {
                int y = edges[eid].to;
                if (label[y] > 0) {  // outer blossom/vertex
                    int time_next = (px + potential[y]) >> 1;
                    if (lx != label[y]) {
                        if (time_next == time_current_)
                            return true;
                        time_augment_ = min(time_next, time_augment_);
                    } else {
                        if (bx == dsu.find(y))
                            continue;
                        if (time_next == time_current_) {
                            contract(x, y);
                            bx = dsu.find(x);
                        } else if (time_next <= NH)
                            list.push(time_next, eid);
                    }
                } else if (label[y] == kFree) {  // free vertex
                    int time_next = px + 1;
                    if (time_next == time_current_)
                        grow(x, y, mate[y]);
                    else if (time_next <= NH)
                        list.push(time_next, eid);
                }
            }
        }
        return false;
    }

    bool adjust_dual_variables() {
        // Return true if the current matching is maximum.
        const int time_lim = min(NH + 1, time_augment_);
        for (++time_current_; time_current_ <= time_lim; ++time_current_) {
            dsu_changelog_size_ = dsu_changelog_last_;
            if (time_current_ == time_lim)
                break;
            bool updated = false;
            for (int h = list.head[time_current_]; h >= 0; h = list.next[h]) {
                auto &e = edges[h];
                int x = e.from, y = e.to;
                if (label[y] > 0) {
                    // Case: outer -- (free => inner => outer)
                    if (potential[x] + potential[y] != (time_current_ << 1))
                        continue;
                    if (dsu.find(x) == dsu.find(y))
                        continue;
                    if (label[x] != label[y]) {
                        time_augment_ = time_current_;
                        return false;
                    }
                    contract(x, y);
                    updated = true;
                } else if (label[y] == kFree) {
                    grow(x, y, mate[y]);
                    updated = true;
                }
            }
            list.head[time_current_] = -1;
            if (updated)
                return false;
        }
        return time_current_ > NH;
    }

    bool do_edmonds_search() {
        label[0] = kFree;
        for (int u = 1; u <= N; ++u) {
            if (mate[u] == 0) {
                que.enqueue(u);
                label[u] = u;  // component id
            } else
                label[u] = kFree;
        }
        while (1) {
            if (find_augmenting_path())
                break;
            bool maximum = adjust_dual_variables();
            if (maximum)
                return false;
            if (time_current_ == time_augment_)
                break;
        }
        for (int u = 1; u <= N; ++u) {
            if (label[u] > 0)
                potential[u] -= time_current_;
            else if (label[u] < 0)
                potential[u] = 1 + (time_current_ - potential[u]);
        }
        return true;
    }

    // second phase

    void rematch(int v, int w) {
        int t = mate[v];
        mate[v] = w;
        if (mate[t] != v)
            return;
        if (link[v].to == dsu.find(link[v].to)) {
            mate[t] = link[v].from;
            rematch(mate[t], t);
        } else {
            int x = link[v].from, y = link[v].to;
            rematch(x, y);
            rematch(y, x);
        }
    }

    bool dfs_augment(int x, int bx) {
        int px = potential[x], lx = label[bx];
        for (int eid = ofs[x]; eid < ofs[x + 1]; ++eid) {
            int y = edges[eid].to;
            if (px + potential[y] != 0)
                continue;
            int by = dsu.find(y), ly = label[by];
            if (ly > 0) {  // outer
                if (lx >= ly)
                    continue;
                int stack_beg = stack_last_;
                for (int bv = by; bv != bx; bv = dsu.find(link[bv].from)) {
                    int bw = dsu.find(mate[bv]);
                    stack[stack_last_++] = bw;
                    link[bw] = {x, y};
                    dsu.par[bv] = dsu.par[bw] = bx;
                }
                while (stack_last_ > stack_beg) {
                    int bv = stack[--stack_last_];
                    for (int v = blossom.head[bv]; v >= 0; v = blossom.next[v]) {
                        if (!dfs_augment(v, bx))
                            continue;
                        stack_last_ = stack_beg;
                        return true;
                    }
                }
            } else if (ly == kFree) {
                label[by] = kInner;
                int z = mate[by];
                if (z == 0) {
                    rematch(x, y);
                    rematch(y, x);
                    return true;
                }
                int bz = dsu.find(z);
                link[bz] = {x, y};
                label[bz] = outer_id_++;
                for (int v = blossom.head[bz]; v >= 0; v = blossom.next[v]) {
                    if (dfs_augment(v, bz))
                        return true;
                }
            }
        }
        return false;
    }

    int find_maximal() {
        // discard blossoms whose potential is 0.
        for (int u = 1; u <= N; ++u)
            dsu.par[u] = u;
        for (int i = 0; i < dsu_changelog_size_; ++i) {
            dsu.par[dsu_changelog[i].v] = dsu_changelog[i].par;
        }
        for (int u = 1; u <= N; ++u) {
            label[u] = kFree;
            blossom.push(dsu.find(u), u);
        }
        int ret = 0;
        for (int u = 1; u <= N; ++u)
            if (!mate[u]) {
                int bu = dsu.par[u];
                if (label[bu] != kFree)
                    continue;
                label[bu] = outer_id_++;
                for (int v = blossom.head[bu]; v >= 0; v = blossom.next[v]) {
                    if (!dfs_augment(v, bu))
                        continue;
                    ret += 1;
                    break;
                }
            }
        assert(ret >= 1);
        return ret;
    }

    // init

    void initialize() {
        que = Queue<int>(N);

        mate.assign(N + 1, 0);
        potential.assign(N + 1, 1);
        label.assign(N + 1, kFree);
        link.assign(N + 1, {0, 0});

        dsu_changelog.resize(N);

        dsu = DisjointSetUnion(N + 1);
        list = LinkedList(NH + 1, edges.size());

        blossom = LinkedList(N + 1, N + 1);
        stack.resize(N);
        stack_last_ = 0;
    }

   public:
    const int N, NH;
    vector<int> ofs;
    vector<Edge> edges;

    Queue<int> que;

    vector<int> mate, potential;
    vector<int> label;
    vector<Link> link;

    vector<Log> dsu_changelog;
    int dsu_changelog_last_, dsu_changelog_size_;

    DisjointSetUnion dsu;
    LinkedList list, blossom;
    vector<int> stack;
    int stack_last_;

    int time_current_, time_augment_;
    int contract_count_, outer_id_;
};

using Edge = MaximumMatching::Edge;

// usage example
int main() {
    int n = 3;
    vector<Edge> es = {{0, 1}, {1, 2}, {0, 1}};

    auto mm = MaximumMatching(n, es);
    auto ans = mm.maximum_matching();

    cout << ans << endl;
}
