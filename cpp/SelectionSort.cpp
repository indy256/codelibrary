#include <iostream>
#include <vector>
using namespace std;

int main() {

	vector<int> arr = {35, 12, 94, 75, 3, 56, 81, 20};

	for (int i = 0; i < arr.size() - 1; i++) {
		int min = i;
		for (int j = i + 1; j < arr.size(); j++) {
			if (arr[j] < arr[min])
				min = j;
		}
		swap(arr[i], arr[min]);
	}

	cout << "Sorted Array" << endl;
	for (int i = 0; i < arr.size(); i++)
		cout << arr[i] << " ";

	return 0;
}
