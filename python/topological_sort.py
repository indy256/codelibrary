# https://en.wikipedia.org/wiki/Topological_sorting


def topological_sort(graph):
    def dfs(graph, used, order, u):
        used[u] = True
        for v in graph[u]:
            if not used[v]:
                dfs(graph, used, order, v)
        order.append(u)

    n = len(graph)
    used = [False] * n
    order = []
    for i in range(n):
        if not used[i]:
            dfs(graph, used, order, i)

    return order[::-1]


def test():
    g = [[] for _ in range(3)]
    g[2].append(0)
    g[2].append(1)
    g[0].append(1)

    assert topological_sort(g) == [2, 0, 1]


test()
