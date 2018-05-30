#include <bits/stdc++.h>

using namespace std;

// Fast Fourier transform
// https://e-maxx-eng.appspot.com/algebra/fft.html

typedef complex<double> cpx;

// precondition: a.size() is a power of 2
void fft(vector<cpx> &a, bool inverse) {
    size_t n = a.size();
    for (int i = 1, j = 0; i < n; ++i) {
        int bit = n >> 1;
        for (; j >= bit; bit >>= 1)
            j -= bit;
        j += bit;
        if (i < j)
            swap(a[i], a[j]);
    }
    for (int len = 2; len <= n; len *= 2) {
        double angle = 2 * M_PI / len * (inverse ? -1 : 1);
        cpx wlen(cos(angle), sin(angle));
        for (int i = 0; i < n; i += len) {
            cpx w(1);
            for (int j = 0; j < len / 2; j++) {
                cpx u = a[i + j];
                cpx v = a[i + j + len / 2] * w;
                a[i + j] = u + v;
                a[i + j + len / 2] = u - v;
                w *= wlen;
            }
        }
    }
    if (inverse)
        for (int i = 0; i < n; i++)
            a[i] /= n;
}

vector<int> multiply(const vector<int> &a, const vector<int> &b) {
    vector<cpx> fa(a.begin(), a.end());
    vector<cpx> fb(b.begin(), b.end());
    size_t n = 1;
    while (n < max(a.size(), b.size())) n *= 2;
    n *= 2;
    fa.resize(n), fb.resize(n);

    fft(fa, false);
    fft(fb, false);
    for (size_t i = 0; i < n; i++)
        fa[i] *= fb[i];
    fft(fa, true);

    vector<int> res(n);
    int carry = 0;
    for (size_t i = 0; i < n; i++) {
        res[i] = lround(fa[i].real()) + carry;
        carry = res[i] / 10;
        res[i] %= 10;
    }
    return res;
}

int main() {
    vector<int> a{5, 1};
    vector<int> b{2, 1};
    vector<int> res = multiply(a, b);

    copy(res.begin(), res.end(), ostream_iterator<int>(cout, " "));
    cout << endl;
}
