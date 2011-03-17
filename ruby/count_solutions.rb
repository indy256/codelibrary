def count_solutions(a, b)
  dp = [0] * (b + 1)
  dp[0] = 1

  0.upto(a.length-1) do |i|
    a[i].upto(b) do |j|
      dp[j] += dp[j - a[i]]
    end
  end

  dp[b]
end

puts 5 == count_solutions([1,2,3], 5)
