#include <iostream>
 
using namespace std;
 
//member functions declaration
void insertionSort(int arr[], int length);
void printArray(int array[], int size);
 
// main function
int main() 
{
	int n;
  cout<<"enter no. of elements";
  cin>>n;
  int array[n];
  for(int i-0;i<n;i++)
  cin>>array[i];
	// calling insertion sort function to sort the array
	insertionSort(array, 6);
	return 0;
}
 
void insertionSort(int arr[], int length) 
{
	int i, j, key;
	for (i = 1; i < length; i++) 
	{
		j = i;
 		while (j > 0 && arr[j - 1] > arr[j]) 
 		{
 			key = arr[j];
 			arr[j] = arr[j - 1];
 			arr[j - 1] = key;
 			j--;
 		}
	}
	cout << "Sorted Array: ";
	// print the sorted array
	printArray(arr, length);
}

// function to print the given array 
void printArray(int array[], int size)
{ 
 	int j;
	for (j = 0; j < size; j++)
	{
 		cout <<" "<< array[j];
 	}
 	cout << endl;
}
