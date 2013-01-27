package com.graphhopper.storage;


public class MyDataAccess {

	int[] a = new int[0];

	public void setInt(long index, int value) {
		a[((int) index)] = value;
	}

	public int getInt(long index) {
		return a[((int) index)];
	}

	public void createNew(long bytes) {
		a = new int[(int) (bytes / 4)];
	}

	public void ensureCapacity(long bytes) {
		if (a.length < bytes / 4) {
			int[] b = new int[(int) (bytes / 4)];
			System.arraycopy(a, 0, b, 0, a.length);
			a = b;
		}
	}

	public long capacity() {
		return a.length;
	}
}
