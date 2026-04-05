package sort

// MergeSort sorts array using merge sort algorithm
func MergeSort(a []int) []int {
	if len(a) <= 1 {
		return a
	}
	mid := len(a) / 2
	left := MergeSort(append([]int{}, a[:mid]...))
	right := MergeSort(append([]int{}, a[mid:]...))
	return merge(left, right)
}

func merge(left, right []int) []int {
	result := make([]int, 0, len(left)+len(right))
	i, j := 0, 0
	for i < len(left) && j < len(right) {
		if left[i] <= right[j] {
			result = append(result, left[i])
			i++
		} else {
			result = append(result, right[j])
			j++
		}
	}
	result = append(result, left[i:]...)
	result = append(result, right[j:]...)
	return result
}
