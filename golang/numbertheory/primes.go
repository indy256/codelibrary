package numbertheory

// SieveOfEratosthenes returns all primes up to n
func SieveOfEratosthenes(n int) []int {
	if n < 2 {
		return nil
	}
	composite := make([]bool, n+1)
	var primes []int
	for i := 2; i <= n; i++ {
		if !composite[i] {
			primes = append(primes, i)
			for j := i * i; j <= n; j += i {
				composite[j] = true
			}
		}
	}
	return primes
}

// IsPrime checks if n is prime using trial division
func IsPrime(n int) bool {
	if n < 2 {
		return false
	}
	for i := 2; i*i <= n; i++ {
		if n%i == 0 {
			return false
		}
	}
	return true
}
