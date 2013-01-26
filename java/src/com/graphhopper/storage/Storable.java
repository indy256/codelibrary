package com.graphhopper.storage;

import java.io.Closeable;

/**
 * Interface for a storage abstraction.
 *
 * @author Peter Karich
 */
public interface Storable {

    /**
     * @return the allocated storage size in bytes
     */
    long capacity();
}
