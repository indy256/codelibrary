#include <bits/stdc++.h>

using namespace std;

// Fast Fourier transform
// https://e-maxx-eng.appspot.com/algebra/fft.html

typedef complex<double> cpx;

// precondition: a.size() is a power of 2
void fft(vector<cpx> &a, bool inverse) {
    size_t n = a.size();
    if (n == 1) return;

    vector<cpx> a0(n / 2);
    vector<cpx> a1(n / 2);
    for (int i = 0; i < n / 2; i++) {
        a0[i] = a[2 * i];
        a1[i] = a[2 * i + 1];
    }
    fft(a0, inverse);
    fft(a1, inverse);

    double ang = 2 * M_PI / n * (inverse ? -1 : 1);
    cpx w(1);
    cpx wn(cos(ang), sin(ang));
    for (int i = 0; i < n / 2; ++i) {
        a[i] = a0[i] + w * a1[i];
        a[i + n / 2] = a0[i] - w * a1[i];
        if (inverse)
            a[i] /= 2, a[i + n / 2] /= 2;
        w *= wn;
    }
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
