#include <bits/stdc++.h>

using namespace std;

vector<vector<int>> children, subtreeLabels, tree, L;
vector<int> pred, mapping;
int n;

auto compare = [](int a, int b) -> bool { return subtreeLabels[a] < subtreeLabels[b]; };
auto equals = [](int a, int b) -> bool { return subtreeLabels[a] == subtreeLabels[b]; };

void generate_mapping(int r1, int r2) {
    mapping.resize(n);
    mapping[r1] = r2 - n;
    sort(children[r1].begin(), children[r1].end(), compare);
    sort(children[r2].begin(), children[r2].end(), compare);
    for (int i = 0; i < children[r1].size(); i++) {
        int u = children[r1][i];
        int v = children[r2][i];
        generate_mapping(u, v);
    }
}

vector<int> find_center(int offset = 0) {
    int cnt = n;
    vector<int> a;
    vector<int> deg(n);
    for (int i = 0; i < n; i++) {
        deg[i] = tree[i + offset].size();
        if (deg[i] <= 1) {
            a.push_back(i + offset);
            --cnt;
        }
    }
    while (cnt > 0) {
        vector<int> na;
        for (int i = 0; i < a.size(); i++) {
            int u = a[i];
            for (int j = 0; j < tree[u].size(); j++) {
                int v = tree[u][j];
                if (--deg[v - offset] == 1) {
                    na.push_back(v);
                    --cnt;
                }
            }
        }
        a = na;
    }
    return a;
}

int dfs(int u, int p = -1, int depth = 0) {
    L[depth].push_back(u);
    int h = 0;
    for (int i = 0; i < tree[u].size(); i++) {
        int v = tree[u][i];
        if (v == p)
            continue;
        pred[v] = u;
        children[u].push_back(v);
        h = max(h, dfs(v, u, depth + 1));
    }
    return h + 1;
}

bool rooted_tree_isomorphism(int r1, int r2) {
    L.assign(n, vector<int>());
    pred.assign(2 * n, -1);
    children.assign(2 * n, vector<int>());

    int h1 = dfs(r1);
    int h2 = dfs(r2);
    if (h1 != h2)
        return false;

    int h = h1 - 1;
    vector<int> label(2 * n);
    subtreeLabels.assign(2 * n, vector<int>());

    for (int i = h - 1; i >= 0; i--) {
        for (int j = 0; j < L[i + 1].size(); j++) {
            int v = L[i + 1][j];
            subtreeLabels[pred[v]].push_back(label[v]);
        }

        sort(L[i].begin(), L[i].end(), compare);

        for (int j = 0, cnt = 0; j < L[i].size(); j++) {
            if (j && !equals(L[i][j], L[i][j - 1]))
                ++cnt;
            label[L[i][j]] = cnt;
        }
    }

    if (!equals(r1, r2))
        return false;

    generate_mapping(r1, r2);
    return true;
}

bool treeIsomorphism() {
    vector<int> c1 = find_center();
    vector<int> c2 = find_center(n);
    if (c1.size() == c2.size()) {
        if (rooted_tree_isomorphism(c1[0], c2[0]))
            return true;
        else if (c1.size() > 1)
            return rooted_tree_isomorphism(c1[1], c2[0]);
    }
    return false;
}

int main() {
    n = 5;
    vector<vector<int>> t1(n);
    t1[0].emplace_back(1);
    t1[1].emplace_back(0);
    t1[1].emplace_back(2);
    t1[2].emplace_back(1);
    t1[1].emplace_back(3);
    t1[3].emplace_back(1);
    t1[0].emplace_back(4);
    t1[4].emplace_back(0);

    vector<vector<int>> t2(n);
    t2[0].emplace_back(1);
    t2[1].emplace_back(0);
    t2[0].emplace_back(4);
    t2[4].emplace_back(0);
    t2[4].emplace_back(3);
    t2[3].emplace_back(4);
    t2[4].emplace_back(2);
    t2[2].emplace_back(4);

    tree.assign(2 * n, vector<int>());
    for (int u = 0; u < n; u++) {
        for (int i = 0; i < t1[u].size(); i++) {
            int v = t1[u][i];
            tree[u].emplace_back(v);
        }
        for (int i = 0; i < t2[u].size(); i++) {
            int v = t2[u][i];
            tree[u + n].emplace_back(v + n);
        }
    }

    bool res = treeIsomorphism();
    cout << res << endl;

    if (res)
        for (int i = 0; i < n; i++)
            cout << mapping[i] << endl;
}
