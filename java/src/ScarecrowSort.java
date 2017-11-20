import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mooc.EdxIO;

public class ScarecrowSort
{
    public static void main(String args[])
    {
        try (EdxIO io = EdxIO.create()) {
        	int n = io.nextInt();
        	int k = io.nextInt();
        	
        	int[] arr = new int[n];
        	
        	if (k == 1) {
        		io.println("YES");
        		return;
        	}

        	for (int i = 0; i < n; i++) {
        		arr[i] = io.nextInt();
        	}

        	List<Integer> list1 = new ArrayList();
        	
        	for (int i = 0; i < n; i += k) {
        		list1.add(arr[i]); 
        	}
        	
        	for (int i = 1; i < k; i++) {
            	List<Integer> list2 = new ArrayList();
        		
        		for (int j = i; j < n; j += k) {
        			list2.add(arr[j]);
        		}

        		Collections.sort(list1);
        		Collections.sort(list2);
        		
        		for (int j = 0; j < list2.size(); j++) {
        			if (list1.get(j) > list2.get(j)) {
        				io.println("NO");
        				return;
        			}
        		}
        		
        		if (list1.size() > list2.size()) {
        			if (list2.get(list2.size() - 1) > list1.get(list2.size())) {
        				io.println("NO");
        				return;
        			}
        		}
        		list1 = new ArrayList<>(list2);
        	}
        	
        	io.println("YES");
        }
   }
}