package sort

import "math/rand"

// QuickSort sorts array in-place using randomized quicksort
func QuickSort(a []int, low, high int) {
	if low >= high {
		return
	}
	separator := a[low+rand.Intn(high-low+1)]
	i, j := low, high
	for i <= j {
		for a[i] < separator {
			i++
		}
		for a[j] > separator {
			j--
		}
		if i <= j {
			a[i], a[j] = a[j], a[i]
			i++
			j--
		}
	}
	QuickSort(a, low, j)
	QuickSort(a, i, high)
}
