#include <vector>
#include <cstdio>
#include <cstdlib>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <string>
#include <ctime>
using namespace std;

long long rnd() {
    while (true) {
        long long x = 1LL * rand() * rand() * rand() * rand() * rand();
        if (x)
            return x;
    }
}

int main() {
    freopen("D:/projects/cpp/wikialgo/bigint.txt", "r", stdin);
    freopen("D:/projects/cpp/wikialgo/bigint2.txt", "w", stdout);
    vector<bigint> va, vb, vc;
    while (1) {
        bigint a, b;
        if (!(cin >> a))
            break;
        cin >> b;
        va.push_back(a);
        vb.push_back(b);
    }

    clock_t start = clock();
    for (int i = 0; i < va.size(); i++) {
        bigint c = va[i] / vb[i];
        vc.push_back(c);
    }
    fprintf(stderr, "time=%.3lfsec\n", 0.001 * (clock() - start));

    for (int i = 0; i < vc.size(); i++) {
        cout << vc[i] << endl;
    }

    /*
     bigint a, b;
     istringstream is1("15");
     istringstream is2("8");
     is1 >> a;
     is2 >> b;
     cout << a / b << endl;
     cout << a % b << endl;
     for (int step = 0; step < 50000000; step++) {
     long long x = 1LL * rand() * rand() * rand() * rand() * rand();
     long long y = 1LL * rand() * rand() + 1;
     bigint a = x;
     bigint b = y;

     long long z1 = x / y;
     long long z2 = x % y;
     bigint c1 = a / b;
     bigint c2 = a % b;

     if (c1 != z1 || c2 != z2) {
     cout << x << " " << y << " " << z1 << " " << z2 << " " << c1 << " " << c2 << " " << endl;
     }
     }
     */
}
