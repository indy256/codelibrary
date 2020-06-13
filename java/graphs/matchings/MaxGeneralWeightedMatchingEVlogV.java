package graphs.matchings;

import java.util.Arrays;
import java.util.PriorityQueue;

// Taken from https://gist.github.com/min-25/b984122f97dd7f72500e0bd6e49906ca

/*
  Maximum Weighted Matching in General Graphs.
  - O(nm \log(n)) time
  - O(n + m) space

  Note: each vertex is 1-indexed.

  Ref:
    Harold N. Gabow,
    "Data Structures for Weighted Matching and
     Extensions to b-matching and f-factors" (2016)
    (https://arxiv.org/abs/1611.07541)
*/
public class MaxGeneralWeightedMatchingEVlogV {
    static final int kSeparated = -2, kInner = -1, kFree = 0, kOuter = 1;
    static int Inf = 1 << 30;

    static class BinaryHeap<T extends Comparable<T>> {
        static class Node<V extends Comparable<V>> implements Comparable<Node<V>> {
            V value;
            int id;

            Node(V value, int id) {
                this.value = value;
                this.id = id;
            }

            @Override
            public int compareTo(Node<V> o) {
                return value.compareTo(o.value);
            }
        }

        int size_;
        Node<T>[] node;
        int[] index;

        BinaryHeap() {}

        BinaryHeap(int N) {
            size_ = 0;
            node = new Node[N + 1];
            index = new int[N];
        }

        int size() {
            return size_;
        }

        boolean empty() {
            return size_ == 0;
        }

        void clear() {
            while (size_ > 0) index[node[size_--].id] = 0;
        }

        T min() {
            return node[1].value;
        }

        int argmin() {
            return node[1].id;
        } // argmin ?

        T get_val(int id) {
            return node[index[id]].value;
        }

        void pop() {
            if (size_ > 0)
                pop(1);
        }

        void erase(int id) {
            if (index[id] != 0)
                pop(index[id]);
        }

        boolean has(int id) {
            return index[id] != 0;
        }

        void update(int id, T v) {
            if (!has(id)) {
                push(id, v);
                return;
            }
            boolean up = v.compareTo(node[index[id]].value) < 0;
            node[index[id]].value = v;
            if (up)
                up_heap(index[id]);
            else
                down_heap(index[id]);
        }

        void decrease_key(int id, T v) {
            if (!has(id)) {
                push(id, v);
                return;
            }
            if (v.compareTo(node[index[id]].value) < 0) {
                node[index[id]].value = v;
                up_heap(index[id]);
            }
        }

        void push(int id, T v) {
            // assert(!has(id));
            index[id] = ++size_;
            node[size_] = new Node(v, id);
            up_heap(size_);
        }

        void pop(int pos) {
            index[node[pos].id] = 0;
            if (pos == size_) {
                --size_;
                return;
            }
            boolean up = node[size_].value.compareTo(node[pos].value) < 0;
            node[pos] = node[size_--];
            index[node[pos].id] = pos;
            if (up)
                up_heap(pos);
            else
                down_heap(pos);
        }

        void swap_node(int a, int b) {
            swap(node, a, b);
            index[node[a].id] = a;
            index[node[b].id] = b;
        }

        void down_heap(int pos) {
            for (int k = pos, nk = k; 2 * k <= size_; k = nk) {
                if (node[2 * k].compareTo(node[nk]) < 0)
                    nk = 2 * k;
                if (2 * k + 1 <= size_ && node[2 * k + 1].compareTo(node[nk]) < 0)
                    nk = 2 * k + 1;
                if (nk == k)
                    break;
                swap_node(k, nk);
            }
        }

        void up_heap(int pos) {
            for (int k = pos; k > 1 && node[k].compareTo(node[k >> 1]) < 0; k >>= 1) swap_node(k, k >> 1);
        }
    }

    static class PairingHeaps<Key extends Comparable<Key>> {
        static class Node<V extends Comparable<V>> {
            V key;
            int child, next, prev;

            Node() {
                prev = -1;
            } // "prev < 0" means the node is unused.

            Node(V v) {
                key = v;
                child = 0;
                next = 0;
                prev = 0;
            }
        }

        int[] heap;
        Node<Key>[] node;

        public PairingHeaps(int H, int N) {
            // It consists of `H` Pairing heaps.
            // Each heap-node ID can appear at most 1 time(s) among heaps
            // and should be in [1, N).
            heap = new int[H];
            node = new Node[N];
            for (int i = 0; i < N; i++) {
                node[i] = new Node<>();
            }
        }

        void clear(int h) {
            if (heap[h] != 0) {
                clear_rec(heap[h]);
                heap[h] = 0;
            }
        }

        void clear_all() {
            for (int i = 0; i < heap.length; ++i) heap[i] = 0;
            for (int i = 0; i < node.length; ++i) node[i] = new Node();
        }

        boolean empty(int h) {
            return heap[h] == 0;
        }

        boolean used(int v) {
            return node[v].prev >= 0;
        }

        Key min(int h) {
            return node[heap[h]].key;
        }

        int argmin(int h) {
            return heap[h];
        }

        void pop(int h) {
            // assert(!empty(h));
            erase(h, heap[h]);
        }

        void push(int h, int v, Key key) {
            // assert(!used(v));
            node[v] = new Node(key);
            heap[h] = merge(heap[h], v);
        }

        void erase(int h, int v) {
            if (!used(v))
                return;
            int w = two_pass_pairing(node[v].child);
            if (node[v].prev == 0)
                heap[h] = w;
            else {
                cut(v);
                heap[h] = merge(heap[h], w);
            }
            node[v].prev = -1;
        }

        void decrease_key(int h, int v, Key key) {
            if (!used(v)) {
                push(h, v, key);
                return;
            }
            if (node[v].prev == 0)
                node[v].key = key;
            else {
                cut(v);
                node[v].key = key;
                heap[h] = merge(heap[h], v);
            }
        }

        void clear_rec(int v) {
            for (; v != 0; v = node[v].next) {
                if (node[v].child != 0)
                    clear_rec(node[v].child);
                node[v].prev = -1;
            }
        }

        void cut(int v) {
            Node n = node[v];
            int pv = n.prev, nv = n.next;
            Node pn = node[pv];
            if (pn.child == v)
                pn.child = nv;
            else
                pn.next = nv;
            node[nv].prev = pv;
            n.next = n.prev = 0;
        }

        int merge(int l, int r) {
            if (l != 0)
                return r;
            if (r != 0)
                return l;
            if (node[l].key.compareTo(node[r].key) > 0) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            int lc = node[r].next = node[l].child;
            node[l].child = node[lc].prev = r;
            return node[r].prev = l;
        }

        int two_pass_pairing(int root) {
            if (root == 0)
                return 0;
            int a = root;
            root = 0;
            while (a != 0) {
                int b = node[a].next, na = 0;
                node[a].prev = node[a].next = 0;
                if (b != 0) {
                    na = node[b].next;
                    node[b].prev = node[b].next = 0;
                }
                a = merge(a, b);
                node[a].next = root;
                root = a;
                a = na;
            }
            int s = node[root].next;
            node[root].next = 0;
            while (s != 0) {
                int t = node[s].next;
                node[s].next = 0;
                root = merge(root, s);
                s = t;
            }
            return root;
        }
    }

    //    static  PriorityQueue : public priority_queue<T, vector<T>, greater<T> > {
    //        PriorityQueue() {}
    //
    //        PriorityQueue(int N) { this->c.reserve(N); }
    //
    //        T min() const { return this->top(); }
    //
    //        void clear() { this->c.clear(); }
    //    };

    static class Queue {
        int qh, qt;
        int[] data;

        Queue() {}

        Queue(int N) {
            qh = 0;
            qt = 0;
            data = new int[N];
        }

        int get(int i) {
            return data[i];
        }

        void enqueue(int u) {
            data[qt++] = u;
        }

        int dequeue() {
            return data[qh++];
        }

        boolean empty() {
            return qh == qt;
        }

        void clear() {
            qh = qt = 0;
        }

        int size() {
            return qt;
        }
    }

    static class InputEdge {
        int from, to;
        int cost;

        public InputEdge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    //    template<typename T> using ModifiableHeap = BinaryHeap<T>;
    //    template<typename T> using ModifiableHeaps = PairingHeaps<T>;
    //    template<typename T> using FastHeap = PriorityQueue<T>;

    static class Edge {
        int to;
        int cost;

        public Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    static class Link {
        int from, to;

        public Link(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

    static class Node {
        static class NodeLink {
            int b, v;

            public NodeLink(int b, int v) {
                this.b = b;
                this.v = v;
            }
        }

        ;

        int parent, size;
        NodeLink[] link = new NodeLink[2];

        Node() {}

        Node(int u) {
            parent = 0;
            size = 1;
            link[0] = new NodeLink(u, u);
            link[1] = new NodeLink(u, u);
        }

        int next_v() {
            return link[0].v;
        }

        int next_b() {
            return link[0].b;
        }

        int prev_v() {
            return link[1].v;
        }

        int prev_b() {
            return link[1].b;
        }
    }

    static class Event {
        int time;
        int id;

        Event() {}

        Event(int time, int id) {
            this.time = time;
            this.id = id;
        }
        //        bool operator<(const Event &rhs) const { return time < rhs.time; }
        //        bool operator>(const Event &rhs) const { return time > rhs.time; }
    }

    static class EdgeEvent implements Comparable<EdgeEvent> {
        int time;
        int from, to;

        EdgeEvent() {}

        EdgeEvent(int time, int from, int to) {
            this.time = time;
            this.from = from;
            this.to = to;
        }

        @Override
        public int compareTo(EdgeEvent o) {
            return Integer.compare(time, o.time);
        }

        //        bool operator>(const EdgeEvent &rhs) const { return time > rhs.time; }
        //        bool operator<(const EdgeEvent &rhs) const { return time < rhs.time; }
    }

    int N, B, S; // N = |V|, B = (|V| - 1) / 2, S = N + B + 1
    int[] ofs;
    Edge[] edges;

    Queue que;
    int[] mate, surface, base;
    Link[] link;
    int[] label;
    int[] potential;

    int[] unused_bid;
    int unused_bid_idx_;
    Node[] node;

    // for O(nm log n) implementation
    int[] heavy, group;
    int[] time_created, lazy, slack;
    int[] best_from;

    int time_current_;
    Event event1;
    BinaryHeap<EdgeEvent> heap2;
    PairingHeaps<EdgeEvent> heap2s;
    PriorityQueue<EdgeEvent> heap3;
    BinaryHeap<Integer> heap4;

    public MaxGeneralWeightedMatchingEVlogV(int N, InputEdge[] in) {
        this.N = N;
        this.B = (N - 1) / 2;
        this.S = N + B + 1;
        this.ofs = new int[N + 2];
        this.edges = new Edge[in.length * 2];
        this.heap2 = new BinaryHeap<>(S);
        this.heap2s = new PairingHeaps<>(S, S);
        this.heap3 = new PriorityQueue<>(edges.length);
        this.heap4 = new BinaryHeap<>(S);

        for (InputEdge e : in) {
            ofs[e.from + 1]++;
            ofs[e.to + 1]++;
        }
        for (int i = 1; i <= N + 1; ++i) ofs[i] += ofs[i - 1];
        for (InputEdge e : in) {
            edges[ofs[e.from]++] = new Edge(e.to, e.cost * 2);
            edges[ofs[e.to]++] = new Edge(e.from, e.cost * 2);
        }
        for (int i = N + 1; i > 0; --i) ofs[i] = ofs[i - 1];
        ofs[0] = 0;
    }

    public long maximum_weighted_matching(boolean init_matching /*= false*/) {
        initialize();
        set_potential();
        if (init_matching)
            find_maximal_matching();
        for (int u = 1; u <= N; ++u)
            if (mate[u] == 0)
                do_edmonds_search(u);
        long ret = compute_optimal_value();
        return ret;
    }

    long compute_optimal_value() {
        long ret = 0;
        for (int u = 1; u <= N; ++u)
            if (mate[u] > u) {
                int max_c = 0;
                for (int eid = ofs[u]; eid < ofs[u + 1]; ++eid) {
                    if (edges[eid].to == mate[u])
                        max_c = Math.max(max_c, edges[eid].cost);
                }
                ret += max_c;
            }
        return ret >> 1;
    }

    long reduced_cost(int u, int v, Edge e) {
        return potential[u] + potential[v] - e.cost;
    }

    void rematch(int v, int w) {
        int t = mate[v];
        mate[v] = w;
        if (mate[t] != v)
            return;
        if (link[v].to == surface[link[v].to]) {
            mate[t] = link[v].from;
            rematch(mate[t], t);
        } else {
            int x = link[v].from, y = link[v].to;
            rematch(x, y);
            rematch(y, x);
        }
    }

    void fix_mate_and_base(int b) {
        if (b <= N)
            return;
        int bv = base[b], mv1 = node[bv].link[0].v, bmv1 = node[bv].link[0].b;
        int d = (node[bmv1].link[1].v == mate[mv1]) ? 0 : 1;
        while (true) {
            int mv = node[bv].link[d].v, bmv = node[bv].link[d].b;
            if (node[bmv].link[1 ^ d].v != mate[mv])
                break;
            fix_mate_and_base(bv);
            fix_mate_and_base(bmv);
            bv = node[bmv].link[d].b;
        }
        fix_mate_and_base(base[b] = bv);
        mate[b] = mate[bv];
    }

    void reset_time() {
        time_current_ = 0;
        event1 = new Event(Inf, 0);
    }

    void reset_blossom(int b) {
        label[b] = kFree;
        link[b].from = 0;
        slack[b] = Inf;
        lazy[b] = 0;
    }

    void reset_all() {
        label[0] = kFree;
        link[0].from = 0;
        for (int v = 1; v <= N; ++v) { // should be optimized for sparse graphs.
            if (label[v] == kOuter)
                potential[v] -= time_current_;
            else {
                int bv = surface[v];
                potential[v] += lazy[bv];
                if (label[bv] == kInner)
                    potential[v] += time_current_ - time_created[bv];
            }
            reset_blossom(v);
        }
        for (int b = N + 1, r = B - unused_bid_idx_; r > 0 && b < S; ++b)
            if (base[b] != b) {
                if (surface[b] == b) {
                    fix_mate_and_base(b);
                    if (label[b] == kOuter)
                        potential[b] += (time_current_ - time_created[b]) << 1;
                    else if (label[b] == kInner)
                        fix_blossom_potential(b, kInner);
                    else
                        fix_blossom_potential(b, kFree);
                }
                heap2s.clear(b);
                reset_blossom(b);
                --r;
            }

        que.clear();
        reset_time();
        heap2.clear();
        heap3.clear();
        heap4.clear();
    }

    void do_edmonds_search(int root) {
        if (potential[root] == 0)
            return;
        link_blossom(surface[root], new Link(0, 0));
        push_outer_and_fix_potentials(surface[root], 0);
        for (boolean augmented = false; !augmented;) {
            augmented = augment(root);
            if (augmented)
                break;
            augmented = adjust_dual_variables(root);
        }
        reset_all();
    }

    int fix_blossom_potential(int b, int Lab) {
        // Return the amount.
        // (If v is an atom, the potential[v] will not be changed.)
        int d = lazy[b];
        lazy[b] = 0;
        if (Lab == kInner) {
            int dt = time_current_ - time_created[b];
            if (b > N)
                potential[b] -= dt << 1;
            d += dt;
        }
        return d;
    }

    void update_heap2(int x, int y, int by, int t, int Lab) {
        if (t >= slack[y])
            return;
        slack[y] = t;
        best_from[y] = x;
        if (y == by) {
            if (Lab != kInner)
                heap2.decrease_key(y, new EdgeEvent(t + lazy[y], x, y));
        } else {
            int gy = group[y];
            if (gy != y) {
                if (t >= slack[gy])
                    return;
                slack[gy] = t;
            }
            heap2s.decrease_key(by, gy, new EdgeEvent(t, x, y));
            if (Lab == kInner)
                return;
            EdgeEvent m = heap2s.min(by);
            if (m != null)
                heap2.decrease_key(by, new EdgeEvent(m.time + lazy[by], m.from, m.to));
        }
    }

    void activate_heap2_node(int b) {
        if (b <= N) {
            if (slack[b] < Inf)
                heap2.push(b, new EdgeEvent(slack[b] + lazy[b], best_from[b], b));
        } else {
            if (heap2s.empty(b))
                return;
            EdgeEvent m = heap2s.min(b);
            heap2.push(b, new EdgeEvent(m.time + lazy[b], m.from, m.to));
        }
    }

    void swap_blossom(int a, int b) {
        // Assume that `b` is a maximal blossom.
        swap(base, a, b);
        if (base[a] == a)
            base[a] = b;
        swap(heavy, a, b);
        if (heavy[a] == a)
            heavy[a] = b;
        swap(link, a, b);
        swap(mate, a, b);
        swap(potential, a, b);
        swap(lazy, a, b);
        swap(time_created, a, b);
        for (int d = 0; d < 2; ++d) node[node[a].link[d].b].link[1 ^ d].b = b;
        swap(node, a, b);
    }

    static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    static void swap(Object[] a, int i, int j) {
        Object t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    void set_surface_and_group(int b, int sf, int g) {
        surface[b] = sf;
        group[b] = g;
        if (b <= N)
            return;
        for (int bb = base[b]; surface[bb] != sf; bb = node[bb].next_b()) {
            set_surface_and_group(bb, sf, g);
        }
    }

    void merge_smaller_blossoms(int bid) {
        int lb = bid, largest_size = 1;
        for (int beta = base[bid], b = beta;;) {
            if (node[b].size > largest_size) {
                largest_size = node[b].size;
                lb = b;
            }
            if ((b = node[b].next_b()) == beta)
                break;
        }
        for (int beta = base[bid], b = beta;;) {
            if (b != lb)
                set_surface_and_group(b, lb, b);
            if ((b = node[b].next_b()) == beta)
                break;
        }
        group[lb] = lb;
        if (largest_size > 1) {
            surface[bid] = heavy[bid] = lb;
            swap_blossom(lb, bid);
        } else
            heavy[bid] = 0;
    }

    void contract(int x, int y, int eid) {
        int bx = surface[x], by = surface[y];
        assert (bx != by);
        final int h = -(eid + 1);
        link[surface[mate[bx]]].from = link[surface[mate[by]]].from = h;

        int lca = -1;
        while (true) {
            if (mate[by] != 0) {
                int tmp = bx;
                bx = by;
                by = tmp;
            }
            bx = lca = surface[link[bx].from];
            if (link[surface[mate[bx]]].from == h)
                break;
            link[surface[mate[bx]]].from = h;
        }

        final int bid = unused_bid[--unused_bid_idx_];
        assert (unused_bid_idx_ >= 0);
        int tree_size = 0;
        for (int d = 0; d < 2; ++d) {
            for (int bv = surface[x]; bv != lca;) {
                int mv = mate[bv], bmv = surface[mv], v = mate[mv];
                int f = link[v].from, t = link[v].to;
                tree_size += node[bv].size + node[bmv].size;
                link[mv] = new Link(x, y);

                if (bv > N)
                    potential[bv] += (time_current_ - time_created[bv]) << 1;
                if (bmv > N)
                    heap4.erase(bmv);
                push_outer_and_fix_potentials(bmv, fix_blossom_potential(bmv, kInner));

                node[bv].link[d] = new Node.NodeLink(bmv, mv);
                node[bmv].link[1 ^ d] = new Node.NodeLink(bv, v);
                node[bmv].link[d] = new Node.NodeLink(bv = surface[f], f);
                node[bv].link[1 ^ d] = new Node.NodeLink(bmv, t);
            }
            node[surface[x]].link[1 ^ d] = new Node.NodeLink(surface[y], y);
            int tmp = x;
            x = y;
            y = tmp;
        }
        if (lca > N)
            potential[lca] += (time_current_ - time_created[lca]) << 1;
        node[bid].size = tree_size + node[lca].size;
        base[bid] = lca;
        link[bid] = link[lca];
        mate[bid] = mate[lca];
        label[bid] = kOuter;
        surface[bid] = bid;
        time_created[bid] = time_current_;
        potential[bid] = 0;
        lazy[bid] = 0;

        merge_smaller_blossoms(bid); // O(n log n) time / Edmonds search
    }

    void link_blossom(int v, Link l) {
        link[v] = new Link(l.from, l.to);
        if (v <= N)
            return;
        int b = base[v];
        link_blossom(b, l);
        int pb = node[b].prev_b();
        l = new Link(node[pb].next_v(), node[b].prev_v());
        for (int bv = b;;) {
            int bw = node[bv].next_b();
            if (bw == b)
                break;
            link_blossom(bw, l);
            Link nl = new Link(node[bw].prev_v(), node[bv].next_v());
            bv = node[bw].next_b();
            link_blossom(bv, nl);
        }
    }

    void push_outer_and_fix_potentials(int v, int d) {
        label[v] = kOuter;
        if (v > N) {
            for (int b = base[v]; label[b] != kOuter; b = node[b].next_b()) {
                push_outer_and_fix_potentials(b, d);
            }
        } else {
            potential[v] += time_current_ + d;
            if (potential[v] < event1.time)
                event1 = new Event(potential[v], v);
            que.enqueue(v);
        }
    }

    boolean grow(int x, int y) {
        int by = surface[y];
        boolean visited = (label[by] != kFree);
        if (!visited)
            link_blossom(by, new Link(0, 0));
        label[by] = kInner;
        time_created[by] = time_current_;
        heap2.erase(by);
        if (y != by)
            heap4.update(by, time_current_ + (potential[by] >> 1));
        int z = mate[by];
        if (z == 0) {
            rematch(x, y);
            rematch(y, x);
            return true;
        }
        int bz = surface[z];
        if (!visited)
            link_blossom(bz, new Link(x, y));
        else
            link[bz] = link[z] = new Link(x, y);
        push_outer_and_fix_potentials(bz, fix_blossom_potential(bz, kFree));
        time_created[bz] = time_current_;
        heap2.erase(bz);
        return false;
    }

    void free_blossom(int bid) {
        unused_bid[unused_bid_idx_++] = bid;
        base[bid] = bid;
    }

    int recalculate_minimum_slack(int b, int g) {
        // Return the destination of the best edge of blossom `g`.
        if (b <= N) {
            if (slack[b] >= slack[g])
                return 0;
            slack[g] = slack[b];
            best_from[g] = best_from[b];
            return b;
        }
        int v = 0;
        for (int beta = base[b], bb = beta;;) {
            int w = recalculate_minimum_slack(bb, g);
            if (w != 0)
                v = w;
            if ((bb = node[bb].next_b()) == beta)
                break;
        }
        return v;
    }

    void construct_smaller_components(int b, int sf, int g) {
        surface[b] = sf;
        group[b] = g; // `group[b] = g` is unneeded.
        if (b <= N)
            return;
        for (int bb = base[b]; surface[bb] != sf; bb = node[bb].next_b()) {
            if (bb == heavy[b]) {
                construct_smaller_components(bb, sf, g);
            } else {
                set_surface_and_group(bb, sf, bb);
                int to = 0;
                if (bb > N) {
                    slack[bb] = Inf;
                    to = recalculate_minimum_slack(bb, bb);
                } else if (slack[bb] < Inf)
                    to = bb;
                if (to > 0)
                    heap2s.push(sf, bb, new EdgeEvent(slack[bb], best_from[bb], to));
            }
        }
    }

    void move_to_largest_blossom(int bid) {
        final int h = heavy[bid];
        int d = (time_current_ - time_created[bid]) + lazy[bid];
        lazy[bid] = 0;
        for (int beta = base[bid], b = beta;;) {
            time_created[b] = time_current_;
            lazy[b] = d;
            if (b != h) {
                construct_smaller_components(b, b, b);
                heap2s.erase(bid, b);
            }
            if ((b = node[b].next_b()) == beta)
                break;
        }
        if (h > 0) {
            swap_blossom(h, bid);
            bid = h;
        }
        free_blossom(bid);
    }

    void expand(int bid) {
        int mv = mate[base[bid]];
        move_to_largest_blossom(bid); // O(n log n) time / Edmonds search
        Link old_link = link[mv];
        int old_base = surface[mate[mv]], root = surface[old_link.to];
        int d = (mate[root] == node[root].link[0].v) ? 1 : 0;
        for (int b = node[old_base].link[d ^ 1].b; b != root;) {
            label[b] = kSeparated;
            activate_heap2_node(b);
            b = node[b].link[d ^ 1].b;
            label[b] = kSeparated;
            activate_heap2_node(b);
            b = node[b].link[d ^ 1].b;
        }
        for (int b = old_base;; b = node[b].link[d].b) {
            label[b] = kInner;
            int nb = node[b].link[d].b;
            if (b == root)
                link[mate[b]] = old_link;
            else
                link[mate[b]] = new Link(node[b].link[d].v, node[nb].link[d ^ 1].v);
            link[surface[mate[b]]] = link[mate[b]]; // fix tree links
            if (b > N) {
                if (potential[b] == 0)
                    expand(b);
                else
                    heap4.push(b, time_current_ + (potential[b] >> 1));
            }
            if (b == root)
                break;
            push_outer_and_fix_potentials(nb, fix_blossom_potential(b = nb, kInner));
        }
    }

    boolean augment(int root) {
        // Return true if an augmenting path is found.
        while (!que.empty()) {
            int x = que.dequeue(), bx = surface[x];
            if (potential[x] == time_current_) {
                if (x != root)
                    rematch(x, 0);
                return true;
            }
            for (int eid = ofs[x]; eid < ofs[x + 1]; ++eid) {
                Edge e = edges[eid];
                int y = e.to, by = surface[y];
                if (bx == by)
                    continue;
                int l = label[by];
                if (l == kOuter) {
                    long t = reduced_cost(x, y, e) >> 1; // < 2 * Inf
                    if (t == time_current_) {
                        contract(x, y, eid);
                        bx = surface[x];
                    } else if (t < event1.time) {
                        heap3.add(new EdgeEvent((int) t, x, eid));
                    }
                } else {
                    long t = reduced_cost(x, y, e); // < 3 * Inf
                    if (t >= Inf)
                        continue;
                    if (l != kInner) {
                        if (t + lazy[by] == time_current_) {
                            if (grow(x, y))
                                return true;
                        } else
                            update_heap2(x, y, by, (int) t, kFree);
                    } else {
                        if (mate[x] != y)
                            update_heap2(x, y, by, (int) t, kInner);
                    }
                }
            }
        }
        return false;
    }

    boolean adjust_dual_variables(int root) {
        // delta1 : rematch
        int time1 = event1.time;

        // delta2 : grow
        int time2 = Inf;
        if (!heap2.empty())
            time2 = heap2.min().time;

        // delta3 : contract : O(m log n) time / Edmonds search [ bottleneck (?) ]
        int time3 = Inf;
        while (!heap3.isEmpty()) {
            EdgeEvent e = heap3.peek();
            int x = e.from, y = edges[e.to].to; // e.to is some edge id.
            if (surface[x] != surface[y]) {
                time3 = e.time;
                break;
            } else
                heap3.remove();
        }

        // delta4 : expand
        int time4 = Inf;
        if (!heap4.empty())
            time4 = heap4.min();

        // -- events --
        int time_next = Math.min(Math.min(time1, time2), Math.min(time3, time4));
        assert (time_current_ <= time_next && time_next < Inf);
        time_current_ = time_next;

        if (time_current_ == event1.time) {
            int x = event1.id;
            if (x != root)
                rematch(x, 0);
            return true;
        }
        while (!heap2.empty() && heap2.min().time == time_current_) {
            int x = heap2.min().from, y = heap2.min().to;
            if (grow(x, y))
                return true; // `grow` function will call `heap2.erase(by)`.
        }
        while (!heap3.isEmpty() && heap3.peek().time == time_current_) {
            int x = heap3.peek().from, eid = heap3.peek().to;
            int y = edges[eid].to;
            heap3.remove();
            if (surface[x] == surface[y])
                continue;
            contract(x, y, eid);
        }
        while (!heap4.empty() && heap4.min() == time_current_) {
            int b = heap4.argmin();
            heap4.pop();
            expand(b);
        }
        return false;
    }

    void initialize() {
        que = new Queue(N);
        mate = new int[S];
        link = new Link[S];
        for (int i = 0; i < S; i++) {
            link[i] = new Link(0, 0);
        }
        label = new int[S];
        Arrays.fill(label, kFree);
        base = new int[S];
        for (int u = 1; u < S; ++u) base[u] = u;
        surface = new int[S];
        for (int u = 1; u < S; ++u) surface[u] = u;

        potential = new int[S];
        node = new Node[S];
        for (int b = 1; b < S; ++b) node[b] = new Node(b);

        unused_bid = new int[B];
        for (int i = 0; i < B; ++i) unused_bid[i] = N + B - i;
        unused_bid_idx_ = B;

        // for O(nm log n) implementation
        reset_time();
        time_created = new int[S];
        slack = new int[S];
        for (int i = 0; i < S; ++i) slack[i] = Inf;
        best_from = new int[S];
        heavy = new int[S];
        lazy = new int[S];
        group = new int[S];
        for (int i = 0; i < S; ++i) group[i] = i;
    }

    void set_potential() {
        for (int u = 1; u <= N; ++u) {
            int max_c = 0;
            for (int eid = ofs[u]; eid < ofs[u + 1]; ++eid) {
                max_c = Math.max(max_c, edges[eid].cost);
            }
            potential[u] = max_c >> 1;
        }
    }

    void find_maximal_matching() {
        // Find a maximal matching naively.
        for (int u = 1; u <= N; ++u)
            if (mate[u] == 0) {
                for (int eid = ofs[u]; eid < ofs[u + 1]; ++eid) {
                    Edge e = edges[eid];
                    int v = e.to;
                    if (mate[v] > 0 || reduced_cost(u, v, e) > 0)
                        continue;
                    mate[u] = v;
                    mate[v] = u;
                    break;
                }
            }
    }

    // usage example
    public static void main(String[] args) {
        int n = 6;
        InputEdge[] edges = {new InputEdge(1, 4, 1), new InputEdge(1, 5, 1), new InputEdge(1, 5, 1),
            new InputEdge(2, 4, 2), new InputEdge(2, 5, 3), new InputEdge(2, 6, 1), new InputEdge(3, 4, 1),
            new InputEdge(3, 5, 1), new InputEdge(3, 6, 1)};
        MaxGeneralWeightedMatchingEVlogV mm = new MaxGeneralWeightedMatchingEVlogV(n, edges);
        System.out.println(mm.maximum_weighted_matching(false));
    }
}
