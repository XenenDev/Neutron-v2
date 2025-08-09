package com.neutron.engine.base.interfaces;

import java.awt.event.MouseEvent;

public interface MouseButtonInput {

    int MOUSE_L_BUTTON = 1;
    int MOUSE_M_BUTTON = 2;
    int MOUSE_R_BUTTON = 3;

    default void mousePressed(MouseEvent event, Integer x, Integer y, Boolean isOffWindow) {}

    default void mouseReleased(MouseEvent event, Integer x, Integer y, Boolean isOffWindow) {}

}
