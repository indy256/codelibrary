import mooc.EdxIO;

public class ScarecrowSort
{
    void bubbleSort(int arr[], int k)
    {
        int n = arr.length;
        
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-k; j++)
                if (arr[j] > arr[j+k])
                {
                    // swap temp and arr[i]
                    int temp = arr[j];
                    arr[j] = arr[j+k];
                    arr[j+k] = temp;
                }
        }
    }
 
    boolean testArray(int arr[])
    {
        int n = arr.length; 
        for (int i=0; i<n-1; ++i) {
        	if (arr[i] > arr[i+1]) {
        		return false;
        	}
        }
        return true;
    }
 
    // Driver method to test above
    public static void main(String args[])
    {
        ScarecrowSort ob = new ScarecrowSort();
        try (EdxIO io = EdxIO.create()) {
        	int n = io.nextInt();
        	int k = io.nextInt();
        	
        	if (k == 1) {
        		io.println("YES");
        		return;
        	}
        	int[] arr = new int[n];
        	
        	for (int i = 0; i < n; i++) {
        		arr[i] = io.nextInt();
        	}
        	
            ob.bubbleSort(arr, k);
            io.println(ob.testArray(arr) ? "YES" : "NO");
             	
        }
   }
}