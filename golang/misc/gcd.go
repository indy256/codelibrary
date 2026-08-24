package misc

// Gcd computes the greatest common divisor using Euclidean algorithm
func Gcd(a, b int) int {
	for b != 0 {
		a, b = b, a%b
	}
	if a < 0 {
		return -a
	}
	return a
}

// Lcm computes the least common multiple
func Lcm(a, b int) int {
	return a / Gcd(a, b) * b
}

// ExtGcd computes extended GCD: returns gcd, x, y such that a*x + b*y = gcd(a,b)
func ExtGcd(a, b int) (int, int, int) {
	if b == 0 {
		return a, 1, 0
	}
	g, x, y := ExtGcd(b, a%b)
	return g, y, x - (a/b)*y
}
