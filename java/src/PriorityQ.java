import java.util.Comparator;
import java.util.PriorityQueue;

import mooc.EdxIO;

public class PriorityQ {

	public static void main(String[] args) {
		try (EdxIO io = EdxIO.create()) {
			int n = io.nextInt();
			
			PriorityQueue<Node> q = 
					new PriorityQueue<Node>(n);
			int[] inserts = new int[n+1];
			Node node;
			
			for (int i = 1; i <= n; i++) {
				char cmd = io.nextChar();
				int num, index;
				
				switch (cmd) {
				case 'A':
					num = io.nextInt();
					node = new Node();
					node.id = i;
					node.dist  = num;
					q.add(node);
					break;
				case 'X':
					if (q.isEmpty()) 
						io.println("*");
					else
						io.println(q.remove().dist);
					break;
				case 'D':
					index = io.nextInt();
					num = io.nextInt();
					node = new Node();
					node.id = index;
					q.remove(node);
					node.dist = num;
					q.add(node);
					break;
				}
			}
		}
	}
}

class Node implements Comparable<Node>{
    public int id;   // store unique id to distinguish elements in the set
    public int dist; // store distance estimates in the Node itself
    public int compareTo(Node other) {
        if (this.dist == other.dist) {
            return Integer.valueOf(this.id).compareTo(other.id);
        } else {
            return Integer.valueOf(this.dist).compareTo(other.dist);
        }
    }
    public boolean equals(Object other) {
    	if (other instanceof Node) {
    		Node o = (Node)other;
        	return (this.id == o.id);
    	} else {
    		return false;
    	}
    }
}
