#include <bits/stdc++.h>

using namespace std;

// see https://codeforces.com/blog/entry/60442 for analysis

struct hashing {
    static constexpr int dimensions = 4;
    static constexpr int mod = (1u << 31) - 1;
    vector<vector<int>> hashes, p;

    static const vector<int> &get_bases() {
        static mt19937 rng(chrono::steady_clock::now().time_since_epoch().count());
        static vector<int> bases;
        while (bases.size() < dimensions) {
            bases.emplace_back(uniform_int_distribution<int>((int)1e9, mod - 1)(rng));
        }
        return bases;
    }

    hashing(const string &s) : hashes(dimensions), p(dimensions) {
        int n = s.size();
        const vector<int> &bases = get_bases();
        for (int d = 0; d < dimensions; ++d) {
            hashes[d].resize(n + 1);
            p[d].resize(n + 1);
            p[d][0] = 1;
            long long base = bases[d];
            for (int i = 0; i < n; i++) {
                hashes[d][i + 1] = (hashes[d][i] * base + s[i]) % mod;
                p[d][i + 1] = p[d][i] * base % mod;
            }
        }
    }

    vector<int> get_hash(int i, int len) {
        vector<int> res;
        for (int d = 0; d < dimensions; ++d) {
            int hash = (int)((hashes[d][i + len] + (long long)hashes[d][i] * (mod - p[d][len])) % mod);
            res.emplace_back(hash);
        }
        return res;
    }
};

// usage example
int main() {
    string a = "abc123";
    string b = "abc";
    auto ha = hashing(a);
    auto hb = hashing(b);
    vector<int> ha1 = ha.get_hash(0, 3);
    vector<int> ha2 = ha.get_hash(3, 3);
    vector<int> hb1 = ha.get_hash(0, 3);
    cout << (ha1 == hb1) << " " << (ha1 == ha2) << endl;
    cout << hashing::mod << endl;
}
