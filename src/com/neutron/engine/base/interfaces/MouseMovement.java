package com.neutron.engine.base.interfaces;

import java.awt.event.MouseEvent;

public interface MouseMovement {

    default void mouseDragged(MouseEvent event, Integer x, Integer y, Boolean isOffWindow) {}

    default void mouseMoved(MouseEvent event, Integer x, Integer y, Boolean isOffWindow) {}

}
