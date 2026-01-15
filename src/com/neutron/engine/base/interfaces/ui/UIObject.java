package com.neutron.engine.base.interfaces.ui;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;

public abstract class UIObject {

    protected final int x, y, width, height;
    protected boolean visible;
    protected boolean enabled;

    protected UIObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = true;
        this.enabled = true;
    }

    protected UIObject(int x, int y, int width, int height,
                       boolean visible, boolean enabled) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = visible;
        this.enabled = enabled;
    }

    public final void renderUI(GameCore gameCore, Renderer r) {
        if (!visible) return;
        renderSelf(gameCore, r);
    }

    protected abstract void renderSelf(GameCore gameCore, Renderer r);

    public boolean isBeingPressed(int mouseX, int mouseY) {
        if (!visible || !enabled) return false;

        return mouseX >= x && mouseY >= y &&
                mouseX <= x + width &&
                mouseY <= y + height;
    }

    public void onPress() { }

    public void onDrag(int mouseX, int mouseY) { }

    public void onRelease() { }

    public void onKeyTyped(char c) { }

    public boolean canFocus() { return false; }
}


