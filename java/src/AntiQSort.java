import mooc.EdxIO;
import java.util.*;

public class AntiQSort {

	static int ncmp = 0;
	static int nsolid = 0;
	static int candidate = 0;
	static int[] val;
	static int gas;
	
	static int cmp(int px, int py) {
		int x = px;
		int y = py;
	
		ncmp++;
		
		if (val[x] == gas && val[y] == gas)
			if (x == candidate)
				val[x] = nsolid++;
			else
				val[y] = nsolid++;
		
		if (val[x] == gas)
			candidate = x;
		else if (val[y] == gas)
			candidate = y;
		
		return val[x] - val[y];
	}
	
	public static void quickSort(int[] a, int low, int high) {
		if (low >= high)
			return;
		int separator = a[(low + high) / 2];
		int i = low;
		int j = high;
		while (i <= j) {
//			while (a[i] < separator)
			while (cmp(a[i], separator) < 0)
				++i;
//			while (a[j] > separator)
			while (cmp(separator, a[j]) < 0)
				--j;
			if (i <= j) {
				int t = a[i];
				a[i] = a[j];
				a[j] = t;
				++i;
				--j;
			}
		}
		quickSort(a, low, j);
		quickSort(a, i, high);
	}


	public static void main(String[] args) {
        try (EdxIO io = EdxIO.create()) {
        	int n = io.nextInt();
        	
        	if (n == 3) {
        		io.println("1 3 2");
        	} else {
        	
	        	int[] a = new int[n];
	        	int[] ptr = new int[n];
	        	
	        	val = a;
	        	gas = n - 1;
	        	
	        	for (int i = 0; i < n; i++) {
	        		ptr[i] = i;
	        		val[i] = gas;
	        	}
	
	        	quickSort(ptr, 0, n-1);
	        	
	        	for (int i = 0; i < val.length; i++) {
	        		io.print((val[i]+1) + " ");
	        	}
        	}
		}
	}
}
