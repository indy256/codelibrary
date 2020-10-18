#include <bits/stdc++.h>

using namespace std;

//to merge the sort of the array
void merge_sort(vector<int> &a, int low, int high) {
    if (high - low < 2)
        return;
    int mid = (low + high) >> 1;
    merge_sort(a, low, mid);
    merge_sort(a, mid, high);
    vector<int> b;
    copy(a.begin() + low, a.begin() + mid, back_inserter(b));
    for (int i = low, j = mid, k = 0; k < b.size(); i++) {
        if (j == high || b[k] <= a[j]) {
            a[i] = b[k++];
        } else {
            a[i] = a[j++];
        }
    }
}

//works as a counter for array
void counting_sort(vector<int> &a) {
    int max = *max_element(a.begin(), a.end());
    vector<int> cnt(max + 1);
    for (int x : a) {
        ++cnt[x];
    }
    for (int i = 1; i < cnt.size(); i++) {
        cnt[i] += cnt[i - 1];
    }
    int n = a.size();
    vector<int> b(n);
    for (int i = 0; i < n; i++) {
        b[--cnt[a[i]]] = a[i];
    }
    a = b;
}

// usage example
int main() {
    vector<int> a{4, 1, 2, 3};
    merge_sort(a, 0, a.size());
    for (int x : a)
        cout << x << " ";
    cout << endl;

    a = {4, 1, 2, 3};
    counting_sort(a);
    for (int x : a)
        cout << x << " ";
    cout << endl;
}
