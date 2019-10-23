#include <iostream>
using namespace std;
void bubbleSort(int arr[], int n)
{
    int i, j, temp;
    for(i = 0; i < n; i++)
    {
        for(j = 0; j < n-i-1; j++)
        {
            if( arr[j] > arr[j+1])
            {
                // swap the elements
                temp = arr[j];
                arr[j] = arr[j+1];
                arr[j+1] = temp;
            } 
        }
    }
    
    // print the sorted array
    printf("Sorted Array: ");
    for(i = 0; i < n; i++)
    {
        cout<<" "<<arr[i]);
    }
}

int main()
{
    int arr[100], i, n, step, temp;
    // ask user for number of elements to be sorted
    printf("Enter the number of elements to be sorted: ");
    cin>>n;
    // input elements if the array
    for(i = 0; i < n; i++)
    {
        printf("Enter element no. %d: ", i+1);
        cin>>arr[i];
    }
    // call the function bubbleSort
    bubbleSort(arr, n);
    
    return 0;
}
