package com.neutron.engine.func;

import java.util.concurrent.ThreadLocalRandom;

public class Random {

    public static int randInt(int min, int max) {
        return min + (int)(ThreadLocalRandom.current().nextDouble() * ((max - min) + 1));
    }

}
