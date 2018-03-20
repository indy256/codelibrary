#include <iostream>
using namespace std;

void heapify(int arr[], int root, int heapSize) {
	int maximum = root;
	int leftChild = 2 * root + 1;
	int rightChild = 2 * root + 2;

	if (leftChild < heapSize && arr[leftChild] > arr[root])
		maximum = leftChild;
	if (rightChild < heapSize && arr[rightChild] > arr[maximum])
		maximum = rightChild;

	if (root != maximum) {
		swap(arr[root], arr[maximum]);
		heapify(arr, maximum, heapSize);
	}
}

void heap_sort(int arr[], int heapSize) {
	for (int i = heapSize / 2 - 1; i >= 0; i--)
		heapify(arr, i, heapSize);

	for (int i = heapSize - 1; i > 0; i--) {
		swap(arr[i], arr[0]);
		heapSize--;
		heapify(arr, 0, heapSize);
	}
}

int main() {

	int arr[] = {65, 87, 12, 3, 94, 53, 44, 36, 27};
	int size = sizeof(arr) / sizeof(arr[0]);

	heap_sort(arr, size);

	cout << "Sorted Array" << endl;

	for (int i = 0; i < size; i++)
		cout << arr[i] << " ";

	return 0;
}
