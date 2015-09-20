getAllDivisors n = [d | d <- [1..n], n `mod` d == 0]
  
-- Usage example
main = print $ getAllDivisors 12
