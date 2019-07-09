#include <bits/stdc++.h>
#include "fft.h"

using namespace std;

// usage example
int main() {
    vector<int> a{9, 9};
    vector<int> b{8, 9};
    vector<int> mul = a * b;
    for (int x:mul) cout << x << " ";
    cout << endl;
}
