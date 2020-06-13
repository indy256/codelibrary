#include <bits/stdc++.h>

using namespace std;

constexpr int INSERTION_SORT_THRESHOLD = 47;

int MIN_VALUE;
int MAX_VALUE;
constexpr int NO_VALUE = -1;
constexpr int MAX_SIZE = 300'000;
int a[MAX_SIZE];
int p[MAX_SIZE];
int s[MAX_SIZE];

mt19937 rng(1);

inline bool LESS(int a, int b) {
    if (a != NO_VALUE && b != NO_VALUE) {
        return a < b;
    }
    if (a == NO_VALUE) {
        return b > MAX_VALUE;
    }
    return a < MIN_VALUE;
}

inline bool GREATER(int a, int b) {
    if (a != NO_VALUE && b != NO_VALUE) {
        return a > b;
    }
    if (a == NO_VALUE) {
        return b < MIN_VALUE;
    }
    return a > MAX_VALUE;
}

void hackedSort(int left, int right, bool leftmost) {
    int length = right - left + 1;

    // Use insertion sort on tiny arrays
    if (length < INSERTION_SORT_THRESHOLD) {
        for (int i = right; i >= left; i--) {
            if (a[i] == NO_VALUE)
                a[i] = MIN_VALUE++;
        }
        shuffle(a + left, a + right + 1, rng);  // why not?

        if (leftmost) {
            for (int i = left, j = i; i < right; j = ++i) {
                int ai = a[i + 1];
                int pi = p[i + 1];
                while (ai < a[j]) {
                    a[j + 1] = a[j];
                    p[j + 1] = p[j];
                    if (j-- == left) {
                        break;
                    }
                }
                a[j + 1] = ai;
                p[j + 1] = pi;
            }
        } else {
            do {
                if (left >= right) {
                    return;
                }
            } while (a[++left] >= a[left - 1]);
            for (int k = left; ++left <= right; k = ++left) {
                int a1 = a[k], a2 = a[left];
                int p1 = p[k], p2 = p[left];

                if (a1 < a2) {
                    a2 = a1;
                    a1 = a[left];
                    p2 = p1;
                    p1 = p[left];
                }
                while (a1 < a[--k]) {
                    a[k + 2] = a[k];
                    p[k + 2] = p[k];
                }
                ++k;
                a[k + 1] = a1;
                p[k + 1] = p1;

                while (a2 < a[--k]) {
                    a[k + 1] = a[k];
                    p[k + 1] = p[k];
                }
                a[k + 1] = a2;
                p[k + 1] = p2;
            }
            int last = a[right];
            int plast = p[right];

            while (last < a[--right]) {
                a[right + 1] = a[right];
                p[right + 1] = p[right];
            }
            a[right + 1] = last;
            p[right + 1] = plast;
        }
        return;
    }
    int seventh = (length >> 3) + (length >> 6) + 1;
    int e3 = (left + right) >> 1;
    int e2 = e3 - seventh;
    int e1 = e2 - seventh;
    int e4 = e3 + seventh;
    int e5 = e4 + seventh;

    if (a[e5] == NO_VALUE)
        a[e5] = MIN_VALUE++;
    if (a[e4] == NO_VALUE)
        a[e4] = MIN_VALUE++;

    if (a[e1] == NO_VALUE)
        a[e1] = MAX_VALUE--;
    if (a[e2] == NO_VALUE)
        a[e2] = MAX_VALUE--;
    if (LESS(a[e2], a[e1])) {
        int t = a[e2];
        a[e2] = a[e1];
        a[e1] = t;
        int s = p[e2];
        p[e2] = p[e1];
        p[e1] = s;
    }

    if (LESS(a[e3], a[e2])) {
        int t = a[e3];
        a[e3] = a[e2];
        a[e2] = t;
        int s = p[e3];
        p[e3] = p[e2];
        p[e2] = s;
        if (LESS(t, a[e1])) {
            a[e2] = a[e1];
            a[e1] = t;
            p[e2] = p[e1];
            p[e1] = s;
        }
    }
    if (LESS(a[e4], a[e3])) {
        int t = a[e4];
        a[e4] = a[e3];
        a[e3] = t;
        int s = p[e4];
        p[e4] = p[e3];
        p[e3] = s;
        if (LESS(t, a[e2])) {
            a[e3] = a[e2];
            a[e2] = t;
            p[e3] = p[e2];
            p[e2] = s;
            if (LESS(t, a[e1])) {
                a[e2] = a[e1];
                a[e1] = t;
                p[e2] = p[e1];
                p[e1] = s;
            }
        }
    }
    if (LESS(a[e5], a[e4])) {
        int t = a[e5];
        a[e5] = a[e4];
        a[e4] = t;
        int s = p[e5];
        p[e5] = p[e4];
        p[e4] = s;
        if (LESS(t, a[e3])) {
            a[e4] = a[e3];
            a[e3] = t;
            p[e4] = p[e3];
            p[e3] = s;
            if (LESS(t, a[e2])) {
                a[e3] = a[e2];
                a[e2] = t;
                p[e3] = p[e2];
                p[e2] = s;
                if (LESS(t, a[e1])) {
                    a[e2] = a[e1];
                    a[e1] = t;
                    p[e2] = p[e1];
                    p[e1] = s;
                }
            }
        }
    }

    int less = left;
    int great = right;
    if (a[e1] != a[e2] && a[e2] != a[e3] && a[e3] != a[e4] && a[e4] != a[e5]) {
        int pivot1 = a[e2];
        int pivot2 = a[e4];
        int ppivot1 = p[e2];
        int ppivot2 = p[e4];
        a[e2] = a[left];
        a[e4] = a[right];
        p[e2] = p[left];
        p[e4] = p[right];
        while (LESS(a[++less], pivot1))
            ;
        while (GREATER(a[--great], pivot2))
            ;
        for (int k = less - 1; ++k <= great;) {
            int ak = a[k];
            int pk = p[k];
            if (LESS(ak, pivot1)) {
                a[k] = a[less];
                p[k] = p[less];
                a[less] = ak;
                p[less] = pk;
                ++less;
            } else if (GREATER(ak, pivot2)) {
                while (GREATER(a[great], pivot2)) {
                    if (great-- == k) {
                        goto m1;
                    }
                }
                if (LESS(a[great], pivot1)) {
                    a[k] = a[less];
                    p[k] = p[less];
                    a[less] = a[great];
                    p[less] = p[great];
                    ++less;
                } else {
                    a[k] = a[great];
                    p[k] = p[great];
                }
                a[great] = ak;
                p[great] = pk;
                --great;
            }
        }
    m1:
        a[left] = a[less - 1];
        a[less - 1] = pivot1;
        a[right] = a[great + 1];
        a[great + 1] = pivot2;
        p[left] = p[less - 1];
        p[less - 1] = ppivot1;
        p[right] = p[great + 1];
        p[great + 1] = ppivot2;
        hackedSort(left, less - 2, leftmost);
        hackedSort(great + 2, right, false);
        if (less < e1 && e5 < great) {
            for (int k = less - 1; ++k <= great;) {
                int ak = a[k];
                int pk = p[k];
                if (ak == pivot1) {  // Move a[k] to left part
                    a[k] = a[less];
                    p[k] = p[less];
                    a[less] = ak;
                    p[less] = pk;
                    ++less;
                } else if (ak == pivot2) {  // Move a[k] to right part
                    while (a[great] == pivot2) {
                        if (great-- == k) {
                            goto m2;
                        }
                    }
                    if (a[great] == pivot1) {
                        a[k] = a[less];
                        p[k] = p[less];
                        a[less] = pivot1;
                        p[less] = ppivot1;
                        ++less;
                    } else {
                        a[k] = a[great];
                        p[k] = p[great];
                    }
                    a[great] = ak;
                    p[great] = pk;
                    --great;
                }
            }
        m2:;
        }
        hackedSort(less, great, false);
    }
}

int main() {
    int n = 100'000;

    for (int i = 0; i < n; i++) {
        a[i] = NO_VALUE;
        p[i] = i;
    }
    MIN_VALUE = 1;
    MAX_VALUE = n;

    hackedSort(0, n - 1, true);

    for (int i = 0; i < n; i++) {
        s[p[i]] = a[i];
    }

    cout << n << endl;
    copy(s, s + n, ostream_iterator<int>(cout, " "));
    cout << endl;
}
