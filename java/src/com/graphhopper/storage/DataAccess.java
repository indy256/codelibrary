package com.graphhopper.storage;

/**
 * Abstraction of the underlying datastructure with a unique id and location. To
 * ensure that the id is unique use a Directory.attach or findAttach, if you
 * don't need uniqueness call Directory.create. Current implementations are RAM
 * and memory mapped access.
 *
 * Life cycle: (1) object creation, (2) configuration (e.g. segment size), (3)
 * createNew or loadExisting, (4) usage, (5) close
 *
 * @author Peter Karich
 */
public interface DataAccess {

	/**
	 * @return the allocated storage size in bytes
	 */
	long capacity();

	/**
     * Set 4 bytes at position 'index' to the specified value
     */
    void setInt(long index, int value);

    /**
     * Get 4 bytes from position 'index'
     */
    int getInt(long index);

    /**
     * The first time you use a DataAccess object after configuring it you need
     * to call this. After that first call you have to use ensureCapacity to
     * ensure that enough space is reserved.
     */
    void createNew(long bytes);

    /**
     * Ensures the specified capacity. The first time you have to call createNew
     * instead.
     */
    void ensureCapacity(long bytes);

    /**
     * @return the size of one segment in bytes
     */
    int segmentSize();
}
