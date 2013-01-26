package com.graphhopper.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Several utility classes which are compatible with Java6 on Android.
 *
 * @author Peter Karich,
 * @see Helper7 for none-Android compatible methods.
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
