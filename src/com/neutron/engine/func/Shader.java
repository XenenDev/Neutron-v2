package com.neutron.engine.func;

import java.awt.*;

public interface Shader {
    // x, y are pixel points, between 0 to w and 0 to h respectively.
    // u and v are normalized co-ordinates for. u is horizontal unit and v is vertical unit.
    Color shade(int x, int y, float u, float v, int w, int h);

}
