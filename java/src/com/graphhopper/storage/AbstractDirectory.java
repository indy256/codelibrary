/*
 *  Licensed to Peter Karich under one or more contributor license 
 *  agreements. See the NOTICE file distributed with this work for 
 *  additional information regarding copyright ownership.
 * 
 *  Peter Karich licenses this file to you under the Apache License, 
 *  Version 2.0 (the "License"); you may not use this file except 
 *  in compliance with the License. You may obtain a copy of the 
 *  License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.storage;

import com.graphhopper.util.Helper;
import java.io.File;
import java.util.Collection;
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
