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
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Peter Karich
 */
public abstract class AbstractDataAccess implements DataAccess {

    protected static final int SEGMENT_SIZE_MIN = 1 << 7;
    private static final int SEGMENT_SIZE_DEFAULT = 1 << 20;
    // reserve some space for downstream usage (in classes using/exting this)
    protected static final int HEADER_OFFSET = 20 * 4 + 20;
    protected static final byte[] EMPTY = new byte[1024];
    private final String location;
    protected int segmentSizeInBytes = SEGMENT_SIZE_DEFAULT;
    protected String name;    

    public AbstractDataAccess(String name, String location) {
        this.name = name;
        if (!location.isEmpty() && !location.endsWith("/"))
            throw new IllegalArgumentException("Create DataAccess object via its corresponding Directory!");
        this.location = location;
    }

    @Override
    public String name() {
        return name;
    }

    protected String fullName() {
        return location + name;
    }

    @Override
    public DataAccess segmentSize(int bytes) {
        // segment size should be a power of 2
        int tmp = (int) (Math.log(bytes) / Math.log(2));
        segmentSizeInBytes = Math.max((int) Math.pow(2, tmp), SEGMENT_SIZE_MIN);        
        return this;
    }

    @Override
    public int segmentSize() {
        return segmentSizeInBytes;
    }

    @Override
    public String toString() {
        return fullName();
    }
}
