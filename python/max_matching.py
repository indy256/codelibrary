def max_matching(graph):
    n1 = len(graph)
    n2 = 0 if n1 == 0 else len(graph[0])
    matching = [-1] * n2
    matches = 0
    for u in range(n1):
        if find_path(graph, u, matching, [False] * n1):
            matches += 1
    return matches


def find_path(graph, u1, matching, vis):
    vis[u1] = True
    for v in range(len(matching)):
        u2 = matching[v]
        if graph[u1][v] and (u2 == -1 or not vis[u2] and find_path(graph, u2, matching, vis)):
            matching[v] = u1
            return True
    return False


def test():
    graph = [[False] * 2 for _ in range(2)]
    graph[0][1] = True
    graph[1][0] = True
    graph[1][1] = True
    assert 2 == max_matching(graph)


test()
