#include <bits/stdc++.h>

using namespace std;

// Fast Fourier transform
// https://cp-algorithms.com/algebra/fft.html
// https://github.com/indy256/olymp-docs/blob/master/adamant/fft_eng.pdf

using cpx = complex<double>;
const double PI = acos(-1);

unsigned reverse_bits(unsigned i) {
    i = (i & 0x55555555) << 1 | ((i >> 1) & 0x55555555);
    i = (i & 0x33333333) << 2 | ((i >> 2) & 0x33333333);
    i = (i & 0x0f0f0f0f) << 4 | ((i >> 4) & 0x0f0f0f0f);
    i = (i << 24) | ((i & 0xff00) << 8) | ((i >> 8) & 0xff00) | (i >> 24);
    return i;
}

void fft(vector<cpx> &z, bool inverse) {
    size_t n = z.size();
    assert((n & (n - 1)) == 0);
    int shift = 32 - __builtin_ctz(n);
    for (unsigned i = 1; i < n; i++) {
        unsigned j = reverse_bits(i << shift);
        if (i < j) {
            swap(z[i], z[j]);
        }
    }
    vector<cpx> w;
    for (int len = 2; len <= n; len <<= 1) {
        int halfLen = len >> 1;
        w.resize(len);
        for (int i = halfLen >> 1; i < halfLen; i++) {
            w[i << 1] = w[i];
            double angle = 2 * PI * ((i << 1) - halfLen + 1) / len * (inverse ? -1 : 1);
            w[(i << 1) + 1] = cpx(cos(angle), sin(angle));
        }
        for (int i = 0; i < n; i += len) {
            for (int j = 0; j < halfLen; j++) {
                cpx u = z[i + j];
                cpx v = z[i + j + halfLen] * w[j + halfLen];
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

vector<int> operator*(const vector<int> &a, const vector<int> &b) {
    int need = a.size() + b.size() - 1;
    int n = 1;
    while (n < need) n <<= 1;
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

vector<int> &operator*=(vector<int> &a, const vector<int> &b) {
    a = a * b;
    return a;
}

vector<int> multiply_mod(const vector<int> &a, const vector<int> &b, int m) {
    int need = a.size() + b.size() - 1;
    int n = 1;
    while (n < need) n <<= 1;
    vector<cpx> A(n);
    for (int i = 0; i < a.size(); i++) {
        int x = (a[i] % m + m) % m;
        A[i] = cpx(x & ((1 << 15) - 1), x >> 15);
    }
    fft(A, false);

    vector<cpx> B(n);
    for (int i = 0; i < b.size(); i++) {
        int x = (b[i] % m + m) % m;
        B[i] = cpx(x & ((1 << 15) - 1), x >> 15);
    }
    fft(B, false);

    vector<cpx> fa(n);
    vector<cpx> fb(n);
    for (int i = 0, j = 0; i < n; i++, j = n - i) {
        cpx a1 = (A[i] + conj(A[j])) * cpx(0.5, 0);
        cpx a2 = (A[i] - conj(A[j])) * cpx(0, -0.5);
        cpx b1 = (B[i] + conj(B[j])) * cpx(0.5, 0);
        cpx b2 = (B[i] - conj(B[j])) * cpx(0, -0.5);
        fa[i] = a1 * b1 + a2 * b2 * cpx(0, 1);
        fb[i] = a1 * b2 + a2 * b1;
    }

    fft(fa, true);
    fft(fb, true);
    vector<int> res(need);
    for (int i = 0; i < need; i++) {
        long long aa = (long long) (fa[i].real() + 0.5);
        long long bb = (long long) (fb[i].real() + 0.5);
        long long cc = (long long) (fa[i].imag() + 0.5);
        res[i] = (aa % m + (bb % m << 15) + (cc % m << 30)) % m;
    }
    return res;
}
