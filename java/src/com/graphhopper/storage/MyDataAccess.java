package com.graphhopper.storage;


public class MyDataAccess implements DataAccess {

	int[] a = new int[0];

	@Override
	public void setInt(long index, int value) {
		a[((int) index)] = value;
	}

	@Override
	public int getInt(long index) {
		return a[((int) index)];
	}

	@Override
	public void createNew(long bytes) {
		a = new int[(int) (bytes / 4)];
	}

	@Override
	public void ensureCapacity(long bytes) {
		if (a.length < bytes / 4) {
			int[] b = new int[(int) (bytes / 4)];
			System.arraycopy(a, 0, b, 0, a.length);
			a = b;
		}
	}

	@Override
	public DataAccess segmentSize(int bytes) {
		return this;
	}

	@Override
	public int segmentSize() {
		return 4;
	}

	@Override
	public int segments() {
		return a.length / 4;
	}

	@Override
	public long capacity() {
		return a.length;
	}
}
