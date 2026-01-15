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

    default Double getPivotX() {
        return 0d;
    }

    default Double getPivotY() {
        return 0d;
    }

}
