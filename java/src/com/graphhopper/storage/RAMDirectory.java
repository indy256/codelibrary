package com.graphhopper.storage;

/**
 * Manages in-memory DataAccess objects.
 *
 * @see RAMDataAccess
 * @author Peter Karich
 */
public class RAMDirectory extends AbstractDirectory {

    @Override
    protected DataAccess create(String id) {
        //return new RAMDataAccess(id, location);
		return new MyDataAccess();
    }
}
