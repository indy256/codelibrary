def generate_permutations(p, depth):
    n = len(p)
    if depth == n:
        yield p
    for i in range(n):
        if p[i] == -1:
            p[i] = depth
            yield from generate_permutations(p, depth + 1)
            p[i] = -1


def test():
    for p in generate_permutations([-1] * 3, 0):
        print(p)


test()
