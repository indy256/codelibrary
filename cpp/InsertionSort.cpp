#include <iostream>
#include <vector>
using namespace std;

int main() {

	vector<int> arr = {65, 79, 12, 92, 36, 42, 7, 1, 25};

	for (int i = 1; i < arr.size(); i++) {
		int key = arr[i];
		int j = i - 1;
		while (j >= 0 && arr[j] > key) {
			arr[j + 1] = arr[j];
			j--;
		}
		arr[j + 1] = key;
	}

	cout << "Sorted Array" << endl;
	for (int i = 0; i < arr.size(); i++)
		cout << arr[i] << " ";

	return 0;
}
