package misc

// BinarySearchFirstTrue finds the first index where predicate returns true
// in range [fromInclusive, toExclusive)
func BinarySearchFirstTrue(f func(int) bool, fromInclusive, toExclusive int) int {
	lo := fromInclusive
	hi := toExclusive
	for lo < hi {
		mid := (lo & hi) + ((lo ^ hi) >> 1)
		if !f(mid) {
			lo = mid + 1
		} else {
			hi = mid
		}
	}
	return hi
}

// BinarySearchFloat performs binary search on floating point range
func BinarySearchFloat(f func(float64) bool, lo, hi float64) float64 {
	for step := 0; step < 1000; step++ {
		mid := (lo + hi) / 2
		if !f(mid) {
			lo = mid
		} else {
			hi = mid
		}
	}
	return hi
}
