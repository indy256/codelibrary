Tarjan's oine LCA algorithm. (Based on DFS and union-nd structure.)
DFS(x):
ancestor[Find(x)] = x
for all children y of x:
DFS(y); Union(x, y); ancestor[Find(x)] = x
seen[x] = true
for all queries {x, y}:
if seen[y] then output "LCA(x, y) is ancestor[Find(y)]"