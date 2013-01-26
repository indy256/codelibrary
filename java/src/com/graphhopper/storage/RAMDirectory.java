package com.graphhopper.storage;

/**
 * Manages in-memory DataAccess objects.
 *
 * @see RAMDataAccess
 * @author Peter Karich
 */
public class RAMDirectory extends AbstractDirectory {

    private boolean store;

    /**
     * @param store true if you want that the RAMDirectory can be loaded or saved on demand, false
     * if it should be entirely in RAM
     */
    public RAMDirectory(String _location, boolean store) {
        super(_location);
        this.store = store;
        mkdirs();
    }

    @Override
    protected void mkdirs() {
        if (store)
            super.mkdirs();
    }

    @Override
    protected DataAccess create(String id, String location) {
        return new RAMDataAccess(id, location, store);
    }
}
