#include <iostream>
#include <vector>
using namespace std;

int main() {

	vector<int> arr = {65, 78, 12, 3, 87, 63, 40, 1, 97};

	while (true) {
		bool sorted = true;
		for (int i = 0; i < arr.size(); i++) {
			if (arr[i] > arr[i + 1]) {
				swap(arr[i], arr[i + 1]);
				sorted = false;
			}
		}
		if (sorted)
			break;
	}

	cout << "Sorted array" << endl;
	for (int i = 0; i < arr.size(); i++)
		cout << arr[i] << " ";

	return 0;
}
