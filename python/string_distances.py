# https://en.wikipedia.org/wiki/Levenshtein_distance
import time
import random


def levenstein_distance(a, b):
    m = len(a)
    n = len(b)
    dp = [[0] * (n + 1) for i in range(m + 1)]
    for i in range(m + 1):
        dp[i][0] = i
    for j in range(n + 1):
        dp[0][j] = j
    for i in range(m):
        for j in range(n):
            if a[i] == b[j]:
                dp[i + 1][j + 1] = dp[i][j]
            else:
                dp[i + 1][j + 1] = 1 + min(dp[i][j], dp[i + 1][j], dp[i][j + 1])
    return dp[m][n]


def test():
    start = time.time()
    print(levenstein_distance('abc', 'ab'))
    print(levenstein_distance(random_string(1000), random_string(1000)))
    print(time.time() - start)


def random_string(len):
    return ''.join(random.choice('ab') for _ in range(len))


test()
