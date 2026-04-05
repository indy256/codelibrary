package linearalgebra

import "math"

// Gauss performs Gaussian elimination to solve Ax = b
// Returns the solution vector x
func Gauss(A [][]float64, B []float64) []float64 {
	n := len(A)
	a := make([][]float64, n)
	for i := range A {
		a[i] = make([]float64, n)
		copy(a[i], A[i])
	}
	b := make([]float64, n)
	copy(b, B)

	for row := 0; row < n; row++ {
		best := row
		for i := row + 1; i < n; i++ {
			if math.Abs(a[best][row]) < math.Abs(a[i][row]) {
				best = i
			}
		}
		a[row], a[best] = a[best], a[row]
		b[row], b[best] = b[best], b[row]

		for i := row + 1; i < n; i++ {
			a[row][i] /= a[row][row]
		}
		b[row] /= a[row][row]

		for i := 0; i < n; i++ {
			if i != row && a[i][row] != 0 {
				z := a[i][row]
				for j := row + 1; j < n; j++ {
					a[i][j] -= a[row][j] * z
				}
				b[i] -= b[row] * z
			}
		}
	}
	return b
}

// MatrixMultiply multiplies two matrices
func MatrixMultiply(a, b [][]float64) [][]float64 {
	n := len(a)
	m := len(b[0])
	k := len(b)
	c := make([][]float64, n)
	for i := range c {
		c[i] = make([]float64, m)
		for j := 0; j < m; j++ {
			for p := 0; p < k; p++ {
				c[i][j] += a[i][p] * b[p][j]
			}
		}
	}
	return c
}

// Determinant computes the determinant of a square matrix using Gaussian elimination
func Determinant(A [][]float64) float64 {
	n := len(A)
	a := make([][]float64, n)
	for i := range A {
		a[i] = make([]float64, n)
		copy(a[i], A[i])
	}

	det := 1.0
	for col := 0; col < n; col++ {
		best := col
		for i := col + 1; i < n; i++ {
			if math.Abs(a[best][col]) < math.Abs(a[i][col]) {
				best = i
			}
		}
		if best != col {
			a[col], a[best] = a[best], a[col]
			det = -det
		}
		if math.Abs(a[col][col]) < 1e-12 {
			return 0
		}
		det *= a[col][col]
		for i := col + 1; i < n; i++ {
			factor := a[i][col] / a[col][col]
			for j := col + 1; j < n; j++ {
				a[i][j] -= factor * a[col][j]
			}
		}
	}
	return det
}
