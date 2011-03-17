def pow(a, b, mod):
    res = 1
    while b > 0:
        if b & 1 != 0:
            res = res * a % mod
        a = a * a % mod
        b >>= 1
    return res

print(1024 == pow(2, 10, 1000000007))
