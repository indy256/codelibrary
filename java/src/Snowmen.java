import mooc.EdxIO;
import java.util.*;

public class Snowmen {
	// Usage example
	public static void main(String[] args) {
        try (EdxIO io = EdxIO.create()) {
        	int n = io.nextInt();
        	
        	List<Snowman> l = new ArrayList<Snowman>();
        	
        	Snowman s = new Snowman();
        	s.parent = -1;
        	s.top = 0;
        	
        	l.add(s);
        	
        	for (int i = 0; i < n; i++) {
        		int sm = io.nextInt();
        		int delta = io.nextInt();
        		
        		s = new Snowman();
        		s.parent = sm;
    			Snowman parent = l.get(sm);
        		int genCount = 0;
        		    			
        		if (delta == 0) {
        			while (parent.top < 0) {
        				parent = l.get(parent.parent);
        				genCount++;
        			}
        			
        			for (int j = 0; j < genCount; j++) {
        				parent = l.get(parent.parent);
        			}
        			
        			s.top = parent.top * -1;
        		} else {
        			s.top = delta;
        		}
        		
        		l.add(s);        		
        	}
        	
        	long sum = 0;
        	
        	long[] sums = new long[l.size()];
        	
        	for (int i = 1; i < l.size(); i++) {
        		s = l.get(i);
       			sums[i] = sums[s.parent] + s.top;
        	}
        	
        	for (int i = 1; i < sums.length; i++) {
        		sum += sums[i];
        	}

        	io.println(sum);
		}
	}
}

class Snowman {
	int parent;
	int top;
	int topOfRest;
}
