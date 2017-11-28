with open("input.txt", "rb") as inf, open("output.txt", "wt") as ouf:
    n = int(inf.readline())
    result = [i for i in range(1, n + 1)]
    for i in range(2, n):
        result[i // 2], result[i] = result[i], result[i // 2]
    ouf.write(' '.join(map(str, result)))