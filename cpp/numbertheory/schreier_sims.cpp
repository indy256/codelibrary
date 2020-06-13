#include <bits/stdc++.h>

using namespace std;

// https://en.wikipedia.org/wiki/Schreier%E2%80%93Sims_algorithm

// time: O(n^2 lg^3 |G| + t n lg |G|)
// mem : O(n^2 lg |G| + tn)
// t : number of generator
namespace SchreierSimsAlgorithm {
using vi = vector<int>;
using pii = pair<int, int>;

vi inv(const vi &p) {
    vi res(p.size());
    for (int i = 0; i < p.size(); i++) {
        res[p[i]] = i;
    }
    return res;
}

vi operator*(const vi &a, const vi &b) {
    vi res(a.size());
    for (int i = 0; i < a.size(); i++)
        res[i] = b[a[i]];
    return res;
}

int n, m;
vector<vector<vector<int>>> bkts, bktsInv;
vector<vector<int>> lookup;

int fast_filter(const vi &g, bool addToG = 1) {
    n = bkts.size();
    vi p = g;
    for (int i = 0; i < n; i++) {
        int res = lookup[i][p[i]];
        if (res == -1) {
            if (addToG) {
                bkts[i].push_back(p);
                bktsInv[i].push_back(inv(p));
                lookup[i][p[i]] = (int)bkts[i].size() - 1;
            }
            return i;
        }
        p = p * bktsInv[i][res];
    }
    return -1;
}

long long calc_total_size() {
    long long ret = 1;
    for (int i = 0; i < n; i++)
        ret *= bkts[i].size();
    return ret;
}

bool in_group(const vi &g) {
    return fast_filter(g, false) == -1;
}

void solve(const vector<vector<int>> &perms, int _n) {
    n = _n;
    m = perms.size();
    bkts.clear();
    bktsInv.clear();
    lookup.clear();
    lookup.resize(n);
    for (int i = 0; i < n; i++) {
        lookup[i].resize(n);
        fill(lookup[i].begin(), lookup[i].end(), -1);
    }
    vi id(n);
    iota(id.begin(), id.end(), 0);
    bkts.resize(n);
    bktsInv.resize(n);
    for (int i = 0; i < n; i++) {
        bkts[i].push_back(id);
        bktsInv[i].push_back(id);
        lookup[i][i] = 0;
    }
    for (int i = 0; i < m; i++)
        fast_filter(perms[i]);
    queue<pair<pii, pii>> toUpd;
    for (int i = 0; i < n; i++)
        for (int j = i; j < n; j++)
            for (int k = 0; k < bkts[i].size(); k++)
                for (int l = 0; l < bkts[j].size(); l++)
                    toUpd.push({pii(i, k), pii(j, l)});
    while (!toUpd.empty()) {
        pii a = toUpd.front().first;
        pii b = toUpd.front().second;
        toUpd.pop();
        int res = fast_filter(bkts[a.first][a.second] * bkts[b.first][b.second]);
        if (res == -1)
            continue;
        pii newPair(res, (int)bkts[res].size() - 1);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < bkts[i].size(); ++j) {
                if (i <= res)
                    toUpd.push({pii(i, j), newPair});
                if (res <= i)
                    toUpd.push({newPair, pii(i, j)});
            }
    }
}
}  // namespace SchreierSimsAlgorithm

// usage example
int main() {
    vector<vector<int>> perms{{0, 1, 3, 2}, {1, 0, 2, 3}};

    SchreierSimsAlgorithm::solve(perms, 4);
    cout << SchreierSimsAlgorithm::in_group({0, 1, 2, 3}) << endl;
    cout << SchreierSimsAlgorithm::in_group({1, 0, 2, 3}) << endl;
    cout << SchreierSimsAlgorithm::in_group({0, 1, 3, 2}) << endl;
    cout << SchreierSimsAlgorithm::in_group({1, 0, 3, 2}) << endl;
    cout << SchreierSimsAlgorithm::in_group({2, 3, 0, 1}) << endl;
}
