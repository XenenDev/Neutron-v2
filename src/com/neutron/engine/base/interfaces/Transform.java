package com.neutron.engine.base.interfaces;

public interface Transform {

    Integer getX();

    Integer getY();

    default Double getScale() {
        return 1d;
    }

    default Double getRotation() {
        return 0d;
    }

}
