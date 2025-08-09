package com.neutron.engine.base.interfaces;

public interface WindowFocusListener {

    default void focusGained() {}
    default void focusLost() {}

}
