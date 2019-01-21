#pragma GCC optimize ("Ofast")

#include <bits/stdc++.h>

using namespace std;

// Fast Fourier transform
// https://cp-algorithms.com/algebra/fft.html
// https://github.com/indy256/olymp-docs/blob/master/adamant/fft_eng.pdf

using cpx = complex<double>;
const double PI = acos(-1);

int reverse(unsigned i) {
    i = (i & 0x55555555) << 1 | ((i >> 1) & 0x55555555);
    i = (i & 0x33333333) << 2 | ((i >> 2) & 0x33333333);
    i = (i & 0x0f0f0f0f) << 4 | ((i >> 4) & 0x0f0f0f0f);
    i = (i << 24) | ((i & 0xff00) << 8) | ((i >> 8) & 0xff00) | (i >> 24);
    return i;
}

void fft(vector<cpx> &z, bool inverse) {
    int n = z.size();
    assert((n & (n - 1)) == 0);
    int shift = 32 - __builtin_ctz(n);
    for (int i = 1; i < n; i++) {
        int j = reverse((unsigned) i << shift);
        if (i < j) {
            swap(z[i], z[j]);
        }
    }
    for (int len = 2; len <= n; len <<= 1) {
        int halfLen = len >> 1;
        vector<cpx> w(halfLen);
        for (int i = 0; i < halfLen; i++) {
            double angle = 2 * PI * i / len * (inverse ? -1 : 1);
            w[i] = cpx(cos(angle), sin(angle));
        }
        for (int i = 0; i < n; i += len) {
            for (int j = 0; j < halfLen; j++) {
                cpx u = z[i + j];
                cpx v = z[i + j + halfLen] * w[j];
                z[i + j] = u + v;
                z[i + j + halfLen] = u - v;
            }
        }
    }
    if (inverse) {
        for (int i = 0; i < n; i++) {
            z[i] /= n;
        }
    }
}

void fft_slow(vector<cpx> &a, bool inverse) {
    size_t n = a.size();
    if (n == 1) return;
    vector<cpx> a0(n / 2);
    vector<cpx> a1(n / 2);
    for (int i = 0; i < n / 2; i++) {
        a0[i] = a[2 * i];
        a1[i] = a[2 * i + 1];
    }
    fft_slow(a0, inverse);
    fft_slow(a1, inverse);
    for (int i = 0; i < n / 2; ++i) {
        double ang = 2 * PI * i / n * (inverse ? -1 : 1);
        cpx w(cos(ang), sin(ang));
        a[i] = a0[i] + w * a1[i];
        a[i + n / 2] = a0[i] - w * a1[i];
        if (inverse) {
            a[i] /= 2;
            a[i + n / 2] /= 2;
        }
    }
}

vector<int> multiply(const vector<int> &a, const vector<int> &b) {
    int need = a.size() + b.size();
    int n = 1 << (32 - __builtin_clz(need - 1));
    vector<cpx> p(n);
    for (int i = 0; i < n; i++) {
        p[i] = cpx(i < a.size() ? a[i] : 0, i < b.size() ? b[i] : 0);
    }
    fft(p, false);
    // a[w[k]] = (p[w[k]] + conj(p[w[n-k]])) / 2
    // b[w[k]] = (p[w[k]] - conj(p[w[n-k]])) / (2*i)
    vector<cpx> ab(n);
    cpx r(0, -0.25);
    for (int i = 0; i < n; i++) {
        int j = (n - i) & (n - 1);
        ab[i] = (p[i] * p[i] - conj(p[j] * p[j])) * r;
    }
    fft(ab, true);
    vector<int> result(need);
    for (int i = 0, carry = 0; i < need; i++) {
        result[i] = (int) (ab[i].real() + 0.5) + carry;
        carry = result[i] / 10;
        result[i] %= 10;
    }
    return result;
}

vector<int> multiply_slow(const vector<int> &a, const vector<int> &b) {
    int need = a.size() + b.size();
    int n = 1 << (32 - __builtin_clz(need - 1));
    vector<cpx> fa(a.begin(), a.end());
    vector<cpx> fb(b.begin(), b.end());
    fa.resize(n);
    fb.resize(n);
    fft(fa, false);
    fft(fb, false);
    for (int i = 0; i < n; i++) {
        fa[i] *= fb[i];
    }
    fft(fa, true);
    vector<int> result(need);
    for (int i = 0, carry = 0; i < need; i++) {
        result[i] = (int) (fa[i].real() + 0.5) + carry;
        carry = result[i] / 10;
        result[i] %= 10;
    }
    return result;
}

vector<int> multiply_mod(const vector<int> &a, const vector<int> &b, int mod) {
    int need = a.size() + b.size() - 1;
    int n = max(2, 1 << (32 - (need == 1 ? 0 : __builtin_clz(need - 1))));

    vector<cpx> A(n);
    for (int i = 0; i < a.size(); i++) {
        int x = (a[i] % mod + mod) % mod;
        A[i] = cpx(x & ((1 << 15) - 1), x >> 15);
    }
    fft(A, false);

    vector<cpx> B(n);
    for (int i = 0; i < b.size(); i++) {
        int x = (b[i] % mod + mod) % mod;
        B[i] = cpx(x & ((1 << 15) - 1), x >> 15);
    }
    fft(B, false);

    vector<cpx> fa(n);
    vector<cpx> fb(n);
    cpx r2(0, -1), r3(0.25, 0), r4(0, -0.25), r5(0, 1);
    for (int i = 0; i < n; i++) {
        int j = (n - i) & (n - 1);
        cpx a1 = (A[i] + conj(A[j]));
        cpx a2 = (A[i] - conj(A[j])) * r2;
        cpx b1 = (B[i] + conj(B[j])) * r3;
        cpx b2 = (B[i] - conj(B[j])) * r4;
        fa[i] = a1 * b1 + a2 * b2 * r5;
        fb[i] = a1 * b2 + a2 * b1;
    }

    fft(fa, true);
    fft(fb, true);
    vector<int> res(need);
    for (int i = 0; i < need; i++) {
        long long aa = (long long) (fa[i].real() + 0.5);
        long long bb = (long long) (fb[i].real() + 0.5);
        long long cc = (long long) (fa[i].imag() + 0.5);
        res[i] = (int) ((aa + ((bb % mod) << 15) + ((cc % mod) << 30)) % mod);
    }
    return res;
}

// usage example
int main() {
    vector<int> a{5, 1};
    vector<int> b{2, 1};
    vector<int> res = multiply(a, b);

    copy(res.begin(), res.end(), ostream_iterator<int>(cout, " "));
    cout << endl;
}
