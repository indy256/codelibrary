#include <bits/stdc++.h>

using namespace std;

// https://cp-algorithms.com/data_structures/stack_queue_modification.html

stack<pair<int, int>> s1;
stack<pair<int, int>> s2;

int min() {
    return min(s1.empty() ? INT_MAX : s1.top().second, s2.empty() ? INT_MAX : s2.top().second);
}

void add_last(int x) {
    int min_value = s1.empty() ? x : min(x, s1.top().second);
    s1.push({x, min_value});
}

int remove_first() {
    if (s2.empty()) {
        while (!s1.empty()) {
            int x = s1.top().first;
            s1.pop();
            int min_value = s2.empty() ? x : min(x, s2.top().second);
            s2.push({x, min_value});
        }
    }
    int x = s2.top().first;;
    s2.pop();
    return x;
}

// usage example
int main() {
    add_last(2);
    add_last(3);
    cout << (2 == min()) << endl;
    remove_first();
    cout << (3 == min()) << endl;
    add_last(1);
    cout << (1 == min()) << endl;
}
