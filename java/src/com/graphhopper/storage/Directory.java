package com.graphhopper.storage;

/**
 * Maintains a collection of DataAccess objects stored at the same location. One GraphStorage per
 * Directory as we need one to maintain one DataAccess object for nodes, edges and location2id
 * index.
 *
 * @author Peter Karich
 */
public interface Directory {

    /**
     * Tries to find the object with that name if not existent it creates one and associates the
     * location with it. A name is unique in one Directory.
     */
    DataAccess findCreate(String name);
}
