/*
 *  Copyright 2011 Peter Karich 
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.util;

//import com.graphhopper.routing.AStar;
//import com.graphhopper.routing.AStarBidirection;
import com.graphhopper.routing.DijkstraBidirectionRef;
import com.graphhopper.routing.DijkstraSimple;
import com.graphhopper.routing.RoutingAlgorithm;
import com.graphhopper.routing.util.AlgorithmPreparation;
import com.graphhopper.routing.util.NoOpAlgorithmPreparation;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.shapes.BBox;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Several utility classes which are compatible with Java6 on Android.
 *
 * @see Helper7 for none-Android compatible methods.
 * @author Peter Karich,
 */
public class Helper {

    public static final int MB = 1 << 20;

    private Helper() {
    }

    public static List<String> readFile(Reader simpleReader) throws IOException {
        BufferedReader reader = new BufferedReader(simpleReader);
        try {
            List<String> res = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null) {
                res.add(line);
            }
            return res;
        } finally {
            reader.close();
        }
    }

    public static String getMemInfo() {
        return "totalMB:" + Runtime.getRuntime().totalMemory() / MB
                + ", usedMB:" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / MB;
    }
    /**
     * The version without the snapshot string
     */
    public static final String VERSION;
    public static final boolean SNAPSHOT;

    static {
        String version = "0.0";
        try {
            List<String> v = readFile(new InputStreamReader(Helper.class.getResourceAsStream("/version"), "UTF-8"));
            version = v.get(0);
        } catch (Exception ex) {
            System.err.println("GraphHopper Initialization ERROR: cannot read version!? " + ex.getMessage());
        }
        int indexM = version.indexOf("-");
        int indexP = version.indexOf(".");
        if ("${project.version}".equals(version)) {
            VERSION = "0.0";
            SNAPSHOT = true;
            System.err.println("GraphHopper Initialization WARNING: maven did not preprocess the version file!?");
        } else if ("0.0".equals(version) || indexM < 0 || indexP >= indexM) {
            VERSION = "0.0";
            SNAPSHOT = true;
            System.err.println("GraphHopper Initialization WARNING: cannot get version!?");
        } else {
            // throw away the "-SNAPSHOT"
            int major = -1, minor = -1;
            try {
                major = Integer.parseInt(version.substring(0, indexP));
                minor = Integer.parseInt(version.substring(indexP + 1, indexM));
            } catch (Exception ex) {
                System.err.println("GraphHopper Initialization WARNING: cannot parse version!? " + ex.getMessage());
            }
            SNAPSHOT = version.toLowerCase().contains("-snapshot");
            VERSION = major + "." + minor;
        }
    }
}
