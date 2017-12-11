#include <iostream>
using namespace std;

void merge(int *arr, int left, int middle, int right) {
	int leftArrayLength = middle - left + 1;
	int rightArrayLength = right - middle;
	int leftArray[leftArrayLength], rightArray[rightArrayLength];

	for (int i = 0; i < leftArrayLength; i++)
		leftArray[i] = arr[left + i];
	for (int i = 0; i < rightArrayLength; i++)
		rightArray[i] = arr[middle + 1 + i];
	int leftArrayIndex = 0, rightArrayIndex = 0, mergedArrayIndex = left;

	while (leftArrayIndex < leftArrayLength && rightArrayIndex < rightArrayLength) {
		if (leftArray[leftArrayIndex] <= rightArray[rightArrayIndex])
			arr[mergedArrayIndex++] = leftArray[leftArrayIndex++];
		else
			arr[mergedArrayIndex++] = rightArray[rightArrayIndex++];
	}

	while (leftArrayIndex < leftArrayLength)
		arr[mergedArrayIndex++] = leftArray[leftArrayIndex++];

	while (rightArrayIndex < rightArrayLength)
		arr[mergedArrayIndex++] = rightArray[rightArrayIndex++];
}

void merge_sort(int *arr, int left, int right) {
	if (left < right) {
		int mid = (left + right) / 2;
		merge_sort(arr, left, mid);
		merge_sort(arr, mid + 1, right);
		merge(arr, left, mid, right);
	}
}

int main() {

	int arr[] = {65, 20, 37, 98, 1, 12, 44, 19};
	int size = sizeof(arr) / sizeof(arr[0]);
	merge_sort(arr, 0, size - 1);

	cout << "Sorted Array" << endl;
	for (int i = 0; i < size; i++)
		cout << arr[i] << " ";

	return 0;
}
