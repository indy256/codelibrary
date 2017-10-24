def pow(a, b, mod)
  res = 1
  while b > 0
    if b & 1 != 0
      res = res * a % mod
    end
    a = a * a % mod
    b >>= 1
  end
  res
end

puts 1024 == pow(2, 10, 1000000007)
