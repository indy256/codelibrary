#pragma GCC optimize("Ofast")

#include <bits/stdc++.h>

using namespace std;

// Fast Fourier transform, simple implementation
// https://cp-algorithms.com/algebra/fft.html

using cpx = complex<double>;
const double PI = acos(-1);

void fft(vector<cpx> &z, bool inverse) {
    size_t n = z.size();
    assert((n & (n - 1)) == 0);
    if (n == 1)
        return;
    vector<cpx> z0(n / 2);
    vector<cpx> z1(n / 2);
    for (int i = 0; i < n / 2; i++) {
        z0[i] = z[2 * i];
        z1[i] = z[2 * i + 1];
    }
    fft(z0, inverse);
    fft(z1, inverse);
    for (int i = 0; i < n / 2; ++i) {
        double ang = 2 * PI * i / n * (inverse ? -1 : 1);
        cpx w(cos(ang), sin(ang));
        z[i] = z0[i] + w * z1[i];
        z[i + n / 2] = z0[i] - w * z1[i];
        if (inverse) {
            z[i] /= 2;
            z[i + n / 2] /= 2;
        }
    }
}

vector<int> multiply_bigint(const vector<int> &a, const vector<int> &b) {
    int need = a.size() + b.size();
    int n = 1;
    while (n < need)
        n <<= 1;
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
        result[i] = (int)(fa[i].real() + 0.5) + carry;
        carry = result[i] / 10;
        result[i] %= 10;
    }
    return result;
}

// usage example
int main() {
    vector<int> a{5, 1};
    vector<int> b{2, 1};
    vector<int> res = multiply_bigint(a, b);

    for (int x : res)
        cout << x << " ";
    cout << endl;
}
