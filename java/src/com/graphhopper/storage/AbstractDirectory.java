package com.graphhopper.storage;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements some common methods for the subclasses.
 *
 * @author Peter Karich
 */
public abstract class AbstractDirectory implements Directory {

	protected Map<String, DataAccess> map = new HashMap<String, DataAccess>();

	protected abstract DataAccess create(String id);

	@Override
	public DataAccess findCreate(String name) {
		DataAccess da = map.get(name);
		if (da != null)
			return da;

		da = create(name);
		map.put(name, da);
		return da;
	}
}
