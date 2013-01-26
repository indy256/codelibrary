package com.graphhopper.storage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements some common methods for the subclasses.
 *
 * @author Peter Karich
 */
public abstract class AbstractDirectory implements Directory {

	protected Map<String, DataAccess> map = new HashMap<String, DataAccess>();
	protected final String location;

	public AbstractDirectory(String _location) {
		if (_location == null || _location.isEmpty())
			_location = new File("").getAbsolutePath();
		if (!_location.endsWith("/"))
			_location += "/";
		location = _location;
		File dir = new File(location);
		if (dir.exists() && !dir.isDirectory())
			throw new RuntimeException("file '" + dir + "' exists but is not a directory");
	}

	protected abstract DataAccess create(String id, String location);

	@Override
	public DataAccess findCreate(String name) {
		DataAccess da = map.get(name);
		if (da != null)
			return da;

		da = create(name, location);
		map.put(name, da);
		return da;
	}

	protected void mkdirs() {
		new File(location).mkdirs();
	}

	@Override
	public String toString() {
		return location();
	}

	@Override
	public String location() {
		return location;
	}
}
