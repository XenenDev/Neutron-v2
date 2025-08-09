package com.neutron.engine.base.interfaces;

import com.neutron.engine.Input;

import java.awt.event.KeyEvent;

public interface KeyboardInput {

    default void keyPressed(Input input, KeyEvent event, Integer key) {}

    default void keyReleased(Input input, KeyEvent event, Integer key) {}

}
