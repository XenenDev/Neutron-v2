package com.neutron.engine.func;

public class uniqueID {
    private static long nextGameObjectId = 0;
    private static long nextResourceId = 0;
    public static synchronized long generateGameObjectId() {
        return nextGameObjectId++;
    }

    public static synchronized long generateResourceId() {
        return nextResourceId++;
    }
}
