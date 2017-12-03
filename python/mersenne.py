import gmpy2 as mp

def isqrt(n):
    if n < 0:
        raise ValueError
    elif n < 2:
        return n
    else:
        a = 1 << ((1 + n.bit_length()) >> 1)
        while True:
            b = (a + n // a) >> 1
            if b >= a:
                return a
            a = b
 
def isprime(n):
    if n < 5:
        return n == 2 or n == 3
    elif n%2 == 0:
        return False
    else:
        r = isqrt(n)
        k = 3
        while k <= r:
            if n%k == 0:
                return False
            k += 2
        return True
 
def lucas_lehmer(n):
    if n == 2:
        return True
    if not mp.is_prime(n):
        return False
    two = mp.mpz(2)
    m = two**n - 1
    s = two*two
    for i in range(2, n):
        sqr = s*s
        s = (sqr & m) + (sqr >> n)
        if s >= m:
            s -= m
        s -= two
    return mp.is_zero(s)
