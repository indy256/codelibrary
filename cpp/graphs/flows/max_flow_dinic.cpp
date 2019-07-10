#include <bits/stdc++.h>
#include "max_flow_dinic.h"

using namespace std;

// usage example
int main() {
    int capacity[][3] = {{0, 3, 2},
                         {0, 0, 2},
                         {0, 0, 0}};
    int n = 3;
    nodes = n;
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            if (capacity[i][j] != 0)
                add_edge(i, j, capacity[i][j]);

    cout << (4 == max_flow(0, 2)) << endl;
}
