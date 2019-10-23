# include <iostream>
using namespace std;

// function to swap elements at the given index values
void swap(int arr[], int firstIndex, int secondIndex) 
{   
    int temp;
    temp = arr[firstIndex];
    arr[firstIndex] = arr[secondIndex];
    arr[secondIndex] = temp;
}

// function to look for smallest element in the given subarray
int indexOfMinimum(int arr[], int startIndex, int n) 
{
    int minValue = arr[startIndex];
    int minIndex = startIndex;

    for(int i = minIndex + 1; i < n; i++) {
        if(arr[i] < minValue) 
        {
            minIndex = i;
            minValue = arr[i];
        }
    } 
    return minIndex;
}

void selectionSort(int arr[], int n) 
{
    for(int i = 0; i < n; i++) 
    {
        int index = indexOfMinimum(arr, i, n);
        swap(arr, i, index);
    }
    
}

void printArray(int arr[], int size)
{
    int i;
    for(i = 0; i < size; i++)
    {
        cout<<arr[i];
    }
    cout<<"\n";
}

int main()
{   int n;
    cout<<"enter no. of elements";
    cin>>n;
    int arr[n];
    for(int i=0;i<n;i++)
    cin>>arr[i];
    selectionSort(arr, n);
    cout<<"Sorted array: \n";
    printArray(arr, n);
    return 0;
}
