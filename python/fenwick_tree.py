import time


# T[i] += value
def add(t, i, value):
    while i < len(t):
        t[i] += value
        i |= i + 1


# sum[0..i]
def sum(t, i):
    res = 0
    while i >= 0:
        res += t[i]
        i = (i & (i + 1)) - 1
    return res


def test():
    start = time.time()
    n = 100000
    t = [0] * n
    for i in range(n):
        add(t, i, 1)
    assert n == sum(t, n - 1)
    print(time.time() - start)


test()
