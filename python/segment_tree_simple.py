def get(t, i):
    return t[i + len(t) // 2]


def add(t, i, value):
    i += len(t) // 2
    t[i] += value
    while i > 1:
        t[i >> 1] = max(t[i], t[i ^ 1])
        i >>= 1


def max_value(t, a, b):
    res = float('-inf')
    a += len(t) // 2
    b += len(t) // 2
    while a <= b:
        if (a & 1) != 0:
            res = max(res, t[a])
        if (b & 1) == 0:
            res = max(res, t[b])
        a = (a + 1) >> 1
        b = (b - 1) >> 1
    return res


def test():
    n = 10
    t = [0] * (2 * n)
    add(t, 0, 1)
    add(t, 9, 2)
    assert 2 == max_value(t, 0, 9)


test()
