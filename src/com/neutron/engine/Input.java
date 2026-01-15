package com.neutron.engine;

import com.neutron.engine.base.interfaces.*;
import com.neutron.engine.base.interfaces.WindowFocusListener;
import com.neutron.engine.base.interfaces.ui.UIObject;

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

    // Key constants
    public final static int A = KeyEvent.VK_A;
    public final static int B = KeyEvent.VK_B;
    public final static int C = KeyEvent.VK_C;
    public final static int D = KeyEvent.VK_D;
    public final static int E = KeyEvent.VK_E;
    public final static int F = KeyEvent.VK_F;
    public final static int G = KeyEvent.VK_G;
    public final static int H = KeyEvent.VK_H;
    public final static int I = KeyEvent.VK_I;
    public final static int J = KeyEvent.VK_J;
    public final static int K = KeyEvent.VK_K;
    public final static int L = KeyEvent.VK_L;
    public final static int M = KeyEvent.VK_M;
    public final static int N = KeyEvent.VK_N;
    public final static int O = KeyEvent.VK_O;
    public final static int P = KeyEvent.VK_P;
    public final static int Q = KeyEvent.VK_Q;
    public final static int R = KeyEvent.VK_R;
    public final static int S = KeyEvent.VK_S;
    public final static int T = KeyEvent.VK_T;
    public final static int U = KeyEvent.VK_U;
    public final static int V = KeyEvent.VK_V;
    public final static int W = KeyEvent.VK_W;
    public final static int X = KeyEvent.VK_X;
    public final static int Y = KeyEvent.VK_Y;
    public final static int Z = KeyEvent.VK_Z;

    public final static int NUM_0 = KeyEvent.VK_0;
    public final static int NUM_1 = KeyEvent.VK_1;
    public final static int NUM_2 = KeyEvent.VK_2;
    public final static int NUM_3 = KeyEvent.VK_3;
    public final static int NUM_4 = KeyEvent.VK_4;
    public final static int NUM_5 = KeyEvent.VK_5;
    public final static int NUM_6 = KeyEvent.VK_6;
    public final static int NUM_7 = KeyEvent.VK_7;
    public final static int NUM_8 = KeyEvent.VK_8;
    public final static int NUM_9 = KeyEvent.VK_9;

    public final static int SPACE = KeyEvent.VK_SPACE;
    public final static int ENTER = KeyEvent.VK_ENTER;
    public final static int ESCAPE = KeyEvent.VK_ESCAPE;
    public final static int BACKSPACE = KeyEvent.VK_BACK_SPACE;
    public final static int TAB = KeyEvent.VK_TAB;
    public final static int SHIFT = KeyEvent.VK_SHIFT;
    public final static int CONTROL = KeyEvent.VK_CONTROL;
    public final static int ALT = KeyEvent.VK_ALT;

    public final static int UP = KeyEvent.VK_UP;
    public final static int DOWN = KeyEvent.VK_DOWN;
    public final static int LEFT = KeyEvent.VK_LEFT;
    public final static int RIGHT = KeyEvent.VK_RIGHT;

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
        return (int) (((mouseX - gameCore.getRenderer().getCenterX()) / gameCore.getRenderer().getCameraZoom()) + gameCore.getRenderer().getCameraX());
    }

    public static int getMouseYinWorld() {
        return (int) (((mouseY - gameCore.getRenderer().getCenterY()) / gameCore.getRenderer().getCameraZoom()) + gameCore.getRenderer().getCameraY());
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

    public void keyTyped(KeyEvent e) {
        UIObject focused = ObjectHandler.getFocusedUIObject();
        if (focused != null) {
            focused.onKeyTyped(e.getKeyChar());
        }
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
        UIObject focused = ObjectHandler.getFocusedUIObject();
        if (focused != null) focused.onRelease();
    }

    public void mouseDragged(MouseEvent e) {
        mouseX = Math.max(0, Math.min(gameCore.WIDTH, e.getX()));
        mouseY = Math.max(0, Math.min(gameCore.HEIGHT, e.getY()));
        ObjectHandler.queueInterfaceUpdate(MouseMovement.class, "mouseMoved", e, Math.max(0, Math.min(gameCore.WIDTH, e.getX())), Math.max(0, Math.min(gameCore.HEIGHT, e.getY())), e.getX() > gameCore.WIDTH || e.getX() < 0 || e.getY() > gameCore.HEIGHT || e.getY() < 0);
        ObjectHandler.queueInterfaceUpdate(MouseMovement.class, "mouseDragged", e, Math.max(0, Math.min(gameCore.WIDTH, e.getX())), Math.max(0, Math.min(gameCore.HEIGHT, e.getY())), e.getX() > gameCore.WIDTH || e.getX() < 0 || e.getY() > gameCore.HEIGHT || e.getY() < 0);
        UIObject focused = ObjectHandler.getFocusedUIObject();
        if (focused != null) focused.onDrag(mouseX, mouseY);
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
