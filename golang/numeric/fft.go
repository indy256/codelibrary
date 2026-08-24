package numeric

import (
	"math"
	"math/cmplx"
)

// FFT computes the Fast Fourier Transform of a complex array
// https://en.wikipedia.org/wiki/Fast_Fourier_transform
func FFT(a []complex128, invert bool) {
	n := len(a)
	if n == 1 {
		return
	}

	for i, j := 1, 0; i < n; i++ {
		bit := n >> 1
		for ; j&bit != 0; bit >>= 1 {
			j ^= bit
		}
		j ^= bit
		if i < j {
			a[i], a[j] = a[j], a[i]
		}
	}

	for length := 2; length <= n; length <<= 1 {
		angle := 2 * math.Pi / float64(length)
		if invert {
			angle = -angle
		}
		wn := cmplx.Exp(complex(0, angle))
		for i := 0; i < n; i += length {
			w := complex(1, 0)
			for j := 0; j < length/2; j++ {
				u := a[i+j]
				v := a[i+j+length/2] * w
				a[i+j] = u + v
				a[i+j+length/2] = u - v
				w *= wn
			}
		}
	}

	if invert {
		for i := range a {
			a[i] /= complex(float64(n), 0)
		}
	}
}

// Multiply multiplies two polynomials using FFT
func Multiply(a, b []int) []int {
	resultLen := len(a) + len(b) - 1
	n := 1
	for n < resultLen {
		n <<= 1
	}

	fa := make([]complex128, n)
	fb := make([]complex128, n)
	for i, v := range a {
		fa[i] = complex(float64(v), 0)
	}
	for i, v := range b {
		fb[i] = complex(float64(v), 0)
	}

	FFT(fa, false)
	FFT(fb, false)

	for i := range fa {
		fa[i] *= fb[i]
	}

	FFT(fa, true)

	result := make([]int, resultLen)
	for i := range result {
		result[i] = int(math.Round(real(fa[i])))
	}
	return result
}
