#include <bits/stdc++.h>

using namespace std;

struct query {
    int l, r, index, t;
};

struct update_query {
    int pos, value, prev;
};

struct data_structure {
    int m[26];
    int cnt;

    data_structure() : m{}, cnt{0} {}

    void add(int x) {
        if (m[x]++ == 0)
            ++cnt;
    }

    void remove(int x) {
        if (--m[x] == 0)
            --cnt;
    }

    int get() { return cnt; }
};

void apply(data_structure &ds, vector<int> &a, int l, int r, int i, int x) {  // Change s[i] to x
    if (l <= i && i <= r) {
        ds.remove(a[i]);
        a[i] = x;
        ds.add(a[i]);
    } else {
        a[i] = x;
    }
}

void solve(istream &in, ostream &out) {
    string s;
    in >> s;
    int n = s.size();
    vector<int> a(n);
    for (int i = 0; i < n; ++i)
        a[i] = s[i] - 'a';
    vector<int> prev = a;

    vector<query> queries;
    vector<update_query> update_queries;

    int m;
    in >> m;

    for (int i = 0; i < m; ++i) {
        int type;
        in >> type;
        if (type == 1) {
            int pos;
            char c;
            in >> pos >> c;
            --pos;
            c -= 'a';
            update_queries.emplace_back(update_query{pos, c, prev[pos]});
            prev[pos] = c;
        } else {
            int l, r;
            in >> l >> r;
            queries.emplace_back(query{l - 1, r - 1, (int)queries.size(), (int)update_queries.size() - 1});
        }
    }

    int block = (int)pow(n, 2 / 3.);
    sort(queries.begin(), queries.end(), [block](auto &q1, auto &q2) {
        if (q1.l / block != q2.l / block)
            return q1.l < q2.l;
        if (q1.r / block != q2.r / block)
            return q1.r < q2.r;
        return q1.t < q2.t;
    });

    int l = 0;
    int r = -1;
    int t = -1;
    vector<int> ans(queries.size());
    data_structure ds;

    for (auto &q : queries) {
        while (t < q.t)
            ++t, apply(ds, a, l, r, update_queries[t].pos, update_queries[t].value);
        while (t > q.t)
            apply(ds, a, l, r, update_queries[t].pos, update_queries[t].prev), --t;

        while (r < q.r)
            ds.add(a[++r]);
        while (l > q.l)
            ds.add(a[--l]);
        while (r > q.r)
            ds.remove(a[r--]);
        while (l < q.l)
            ds.remove(a[l++]);

        ans[q.index] = ds.get();
    }

    for (size_t i = 0; i < queries.size(); i++)
        out << ans[i] << endl;
}

// usage example
int main() {}
