#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/data_structures/stack_queue_modification.html

template <class T>
struct queue_min {
    stack<pair<T, T>> s1;
    stack<pair<T, T>> s2;

    T min() {
        return std::min(s1.empty() ? numeric_limits<T>::max() : s1.top().second,
                        s2.empty() ? numeric_limits<T>::max() : s2.top().second);
    }

    void add_last(T x) {
        T min_value = s1.empty() ? x : std::min(x, s1.top().second);
        s1.emplace(x, min_value);
    }

    T remove_first() {
        if (s2.empty()) {
            while (!s1.empty()) {
                T x = s1.top().first;
                s1.pop();
                T min_value = s2.empty() ? x : std::min(x, s2.top().second);
                s2.emplace(x, min_value);
            }
        }
        T x = s2.top().first;
        s2.pop();
        return x;
    }
};

// usage example
int main() {
    queue_min<int> q;
    q.add_last(2);
    q.add_last(3);
    cout << boolalpha;
    cout << (2 == q.min()) << endl;
    q.remove_first();
    cout << (3 == q.min()) << endl;
    q.add_last(1);
    cout << (1 == q.min()) << endl;
}
