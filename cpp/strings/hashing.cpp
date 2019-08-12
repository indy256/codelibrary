#include <bits/stdc++.h>

using namespace std;

struct hashing {
    static const long long multiplier = 131;
    static int mod1, mod2;
    vector<int> hash1, hash2, p1, p2;

    hashing(const string &s) : hash1(s.size() + 1), hash2(s.size() + 1), p1(s.size() + 1), p2(s.size() + 1) {
        p1[0] = 1;
        p2[0] = 1;
        for (size_t i = 0; i < s.size(); i++) {
            hash1[i + 1] = (hash1[i] * multiplier + s[i]) % mod1;
            hash2[i + 1] = (hash2[i] * multiplier + s[i]) % mod2;
            p1[i + 1] = p1[i] * multiplier % mod1;
            p2[i + 1] = p2[i] * multiplier % mod2;
        }
    }

    long long get_hash(int i, int len) {
        return (((hash1[i + len] + (long long) hash1[i] * (mod1 - p1[len])) % mod1) << 32)
               + (hash2[i + len] + (long long) hash2[i] * (mod2 - p2[len])) % mod2;
    }
};

int next_prime(int x) {
    for (int i;; x++) {
        for (i = 2; i * i <= x && x % i != 0; i++);
        if (i * i > x)
            return x;
    }
}

mt19937 rng(chrono::steady_clock::now().time_since_epoch().count());
int hashing::mod1 = next_prime(uniform_int_distribution<int>((int) 1e9, (int) 2e9)(rng));
int hashing::mod2 = next_prime(uniform_int_distribution<int>((int) 1e9, (int) 2e9)(rng));

// usage example
int main() {
    string s = "abc123abc";
    auto h = hashing(s);
    cout << hashing::mod1 << " " << hashing::mod2 << endl;
    cout << h.get_hash(0, 3) << " " << h.get_hash(6, 3) << " " << h.get_hash(3, 3) << endl;
}
