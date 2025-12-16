package com.neutron.engine;

import com.neutron.engine.base.interfaces.*;
import com.neutron.engine.base.interfaces.WindowFocusListener;

import java.awt.event.*;
import java.util.ArrayList;

public class Input extends KeyAdapter implements MouseMotionListener, MouseWheelListener, MouseListener, FocusListener {

    private static GameCore gameCore;

    private static final ArrayList<Integer> currentPressedKeys = new ArrayList<>();
    private static final boolean[] currentPressedMouseButtons = new boolean[3];
    private static int mouseX = 0;
    private static int mouseY = 0;
    private static boolean isMouseOnScreen = false;
    private static boolean isWindowFocused = false;

    public final static int MOUSE_L_BUTTON = 1;
    public final static int MOUSE_M_BUTTON = 2;
    public final static int MOUSE_R_BUTTON = 3;

    public Input(GameCore gameCore) {
        Input.gameCore = gameCore;
    }

    public static boolean isWindowFocused() {
        return isWindowFocused;
    }

    public static boolean isMouseDown(int button) {
        return currentPressedMouseButtons[button - 1];
    }

    public static boolean isKeyDown(int key) {
        return currentPressedKeys.contains(key);
    }

    public static int getMouseX() {
        return mouseX;
    }

    public static int getMouseY() {
        return mouseY;
    }

    public static int getMouseXinWorld() {
        return (int) ((mouseX / gameCore.getRenderer().getCameraZoom()) + gameCore.getRenderer().getCameraX());
    }

    public static int getMouseYinWorld() {
        //FIXME: fix this, the formula doesn't give mouse pos in world accurately
        return (int) ((mouseY / gameCore.getRenderer().getCameraZoom()) + gameCore.getRenderer().getCameraY());
    }

    public static boolean isMouseOnScreen() {
        return isMouseOnScreen;
    }


    public void keyPressed(KeyEvent e) {
        if (!currentPressedKeys.contains(e.getKeyCode())) {
            currentPressedKeys.add(e.getKeyCode());
            ObjectHandler.queueInterfaceUpdate(KeyboardInput.class, "keyPressed", this, e, e.getKeyCode());
        }
    }

    public void keyReleased(KeyEvent e) {
        currentPressedKeys.remove((Object) e.getKeyCode());
        ObjectHandler.queueInterfaceUpdate(KeyboardInput.class, "keyReleased", this, e, e.getKeyCode());
    }

    public void mouseClicked(MouseEvent e) {
        //Useless
    }

    public void mousePressed(MouseEvent e) {
        currentPressedMouseButtons[e.getButton()-1] = true;
        ObjectHandler.queueInterfaceUpdate(MouseButtonInput.class, "mousePressed", e, Math.max(0, Math.min(gameCore.WIDTH, e.getX())), Math.max(0, Math.min(gameCore.HEIGHT, e.getY())), e.getX() > gameCore.WIDTH || e.getX() < 0 || e.getY() > gameCore.HEIGHT || e.getY() < 0);
        ObjectHandler.sendUIObjectUpdates(Math.max(0, Math.min(gameCore.WIDTH, e.getX())), Math.max(0, Math.min(gameCore.HEIGHT, e.getY())));
    }

    public void mouseReleased(MouseEvent e) {
        currentPressedMouseButtons[e.getButton()-1] = false;
        ObjectHandler.queueInterfaceUpdate(MouseButtonInput.class, "mouseReleased", e, Math.max(0, Math.min(gameCore.WIDTH, e.getX())), Math.max(0, Math.min(gameCore.HEIGHT, e.getY())), e.getX() > gameCore.WIDTH || e.getX() < 0 || e.getY() > gameCore.HEIGHT || e.getY() < 0);
    }

    public void mouseDragged(MouseEvent e) {
        mouseX = Math.max(0, Math.min(gameCore.WIDTH, e.getX()));
        mouseY = Math.max(0, Math.min(gameCore.HEIGHT, e.getY()));
        ObjectHandler.queueInterfaceUpdate(MouseMovement.class, "mouseMoved", e, Math.max(0, Math.min(gameCore.WIDTH, e.getX())), Math.max(0, Math.min(gameCore.HEIGHT, e.getY())), e.getX() > gameCore.WIDTH || e.getX() < 0 || e.getY() > gameCore.HEIGHT || e.getY() < 0);
        ObjectHandler.queueInterfaceUpdate(MouseMovement.class, "mouseDragged", e, Math.max(0, Math.min(gameCore.WIDTH, e.getX())), Math.max(0, Math.min(gameCore.HEIGHT, e.getY())), e.getX() > gameCore.WIDTH || e.getX() < 0 || e.getY() > gameCore.HEIGHT || e.getY() < 0);
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = Math.max(0, Math.min(gameCore.WIDTH, e.getX()));
        mouseY = Math.max(0, Math.min(gameCore.HEIGHT, e.getY()));
        ObjectHandler.queueInterfaceUpdate(MouseMovement.class, "mouseMoved", e, Math.max(0, Math.min(gameCore.WIDTH, e.getX())), Math.max(0, Math.min(gameCore.HEIGHT, e.getY())), e.getX() > gameCore.WIDTH || e.getX() < 0 || e.getY() > gameCore.HEIGHT || e.getY() < 0);
    }

    public void mouseEntered(MouseEvent e) {
        isMouseOnScreen = true;
        ObjectHandler.queueInterfaceUpdate(MouseWindowMovement.class, "mouseEnteredWindow", e, e.getX(), e.getY());
    }

    public void mouseExited(MouseEvent e) {
        isMouseOnScreen = false;
        ObjectHandler.queueInterfaceUpdate(MouseWindowMovement.class, "mouseExitedWindow", e, e.getX(), e.getY());
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        ObjectHandler.queueInterfaceUpdate(MouseWheelInput.class, "mouseWheelMoved", e, e.getUnitsToScroll());
    }

    public void focusGained(FocusEvent e) {
        isWindowFocused = true;
        ObjectHandler.queueInterfaceUpdate(WindowFocusListener.class, "focusGained");
    }

    public void focusLost(FocusEvent e) {
        isWindowFocused = false;
        ObjectHandler.queueInterfaceUpdate(WindowFocusListener.class, "focusLost");
    }
}
