import mooc.EdxIO;

import java.util.*;

public class Main {
	// Usage example
	public static void main(String[] args) {
        try (EdxIO io = EdxIO.create()) {
        	int n = io.nextInt();
        	
        	for (int i = 0; i < n; i++) {
        		char[] c = io.next().toCharArray();
        		Deque<Character> s = new ArrayDeque<Character>(); 

        		boolean matching = true;
        		
        		for (int j = 0; j < c.length; j++) {
        			if (c[j] == '[' || c[j] == '(') {
        				s.push(c[j]);
        			} else {
        				Character ch = s.poll();
        				if (c[j] == ']' && (ch == null || ch != '[') ||
        					c[j] == ')' && (ch == null || ch != '(')) {
        					matching = false;
        					break;
        				}
        			}
        		}

        		if (!matching || s.size() > 0) {
					io.println("NO");
        		} else {
					io.println("YES");
        		}
        	}
		}
	}
}
