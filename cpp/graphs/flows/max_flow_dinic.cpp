#include "max_flow_dinic.h"

#include <bits/stdc++.h>

using namespace std;

// usage example
int main() {
    int capacity[][3] = {{0, 3, 2}, {0, 0, 2}, {0, 0, 0}};
    int n = 3;
    max_flow_dinic flow(n);
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            if (capacity[i][j] != 0)
                flow.add_bidi_edge(i, j, capacity[i][j]);

    cout << (4 == flow.max_flow(0, 2)) << endl;
}
