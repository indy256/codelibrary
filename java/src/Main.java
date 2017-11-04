import mooc.EdxIO;
import java.util.*;

class Main
{
     
    public static void main(String[] args) 
    {
        try (EdxIO io = EdxIO.create()) {
        	int n = io.nextInt();
        	int k1 = io.nextInt();
        	int k2 = io.nextInt();
        	
        	int A = io.nextInt();
        	int B = io.nextInt();
        	int C = io.nextInt();

        	int[] arr = new int[n];
        	
        	arr[0] = io.nextInt();
        	arr[1] = io.nextInt();
        	
        	for (int i = 2; i < n; i++) {
        		arr[i] = A * arr[i-2] + B * arr[i-1] + C;
        	}
        	
		}
    }
} 
