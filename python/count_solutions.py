def count_solutions(a, b):
    dp = [0] * (b + 1)
    dp[0] = 1

    for i in range(len(a)):
        for j in range(a[i], b + 1):
            dp[j] += dp[j - a[i]]
    return dp[b]

print(5 == count_solutions([1, 2, 3], 5))
