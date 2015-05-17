function maxFlow(cap, s, t) {
    var flow = 0, df;
    while (true) {
        df = findPath(cap, [], s, t, 1e9);
        if (df === 0) {
            return flow;
        }
        flow += df;
    }
}

function findPath(cap, vis, u, t, f) {
    var v, df;
    if (u === t) {
        return f;
    }
    vis[u] = true;
    for (v = 0; v < cap.length; v += 1) {
        if (!vis[v] && cap[u][v] > 0) {
            df = findPath(cap, vis, v, t, Math.min(f, cap[u][v]));
            if (df > 0) {
                cap[u][v] -= df;
                cap[v][u] += df;
                return df;
            }
        }
    }
    return 0;
}

// tests
var capacity = [
    [ 0, 3, 2 ],
    [ 0, 0, 2 ],
    [ 0, 0, 0 ]
];
console.log(4 === maxFlow(capacity, 0, 2));
