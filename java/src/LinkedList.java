public class LinkedList {

	int[] next;
	int[] prev;

	void init(int n) {
		next = new int[n + 2];
		prev = new int[n + 2];
	}

	void insert(int x, int p) {
		prev[x] = p;
		next[x] = next[p];
		prev[next[x]] = x;
		next[prev[x]] = x;
	}

	void remove(int i) {
		next[prev[i]] = next[i];
		prev[next[i]] = prev[i];
	}

	// Usage example
	public static void main(String[] args) {

	}
}
