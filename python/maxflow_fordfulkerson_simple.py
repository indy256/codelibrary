# https://en.wikipedia.org/wiki/Ford–Fulkerson_algorithm in O(V^2 * flow)


def max_flow(cap, s, t):
    def augment_path(cap, visited, i, t):
        if i == t:
            return True
        visited[i] = True
        for j in range(len(cap)):
            if not visited[j] and cap[i][j] > 0 and augment_path(cap, visited, j, t):
                cap[i][j] -= 1
                cap[j][i] += 1
                return True
        return False

    flow = 0
    while True:
        if not augment_path(cap, [False] * len(cap), s, t):
            return flow
        flow += 1


def test():
    capacity = [[0, 1, 1, 0], [1, 0, 1, 1], [1, 1, 0, 1], [0, 1, 1, 0]]
    assert max_flow(capacity, 0, 3) == 2


test()
