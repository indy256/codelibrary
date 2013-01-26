package com.graphhopper.util;

/**
 * Make simple speed measurements possible.
 *
 * @author Peter Karich,
 */
public class StopWatch {

    private long lastTime;
    private long time;
    private String name = "";

    public StopWatch() {
    }

    public StopWatch start() {
        lastTime = System.currentTimeMillis();
        return this;
    }

    public StopWatch stop() {
        if (lastTime < 0)
            return this;
        time += System.currentTimeMillis() - lastTime;
        lastTime = -1;
        return this;
    }

    public float getSeconds() {
        return time / 1000f;
    }
}
