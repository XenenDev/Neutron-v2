package com.neutron.engine.base.interfaces;

import java.awt.event.MouseEvent;

public interface MouseWindowMovement {

    default void mouseEnteredWindow(MouseEvent event, Integer x, Integer y) {}

    default void mouseExitedWindow(MouseEvent event, Integer x, Integer y) {}

}
