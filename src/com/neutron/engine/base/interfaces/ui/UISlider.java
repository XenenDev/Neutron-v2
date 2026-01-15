package com.neutron.engine.base.interfaces.ui;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;

import java.awt.Color;

public class UISlider extends UIObject {

    private float value; // 0 to 1
    private Color backgroundColor = Color.GRAY;
    private Color knobColor = Color.WHITE;
    private int knobWidth = 10;
    private boolean dragging = false;

    public UISlider(int x, int y, int width, int height, float initialValue) {
        super(x, y, width, height);
        this.value = Math.max(0, Math.min(1, initialValue));
    }

    @Override
    protected void renderSelf(GameCore gameCore, Renderer r) {
        r.setUseScreenCoordinates(true);
        r.fillRect(x, y, width, height, backgroundColor);
        int knobX = x + (int) ((width - knobWidth) * value);
        r.fillRect(knobX, y, knobWidth, height, knobColor);
    }

    @Override
    public void onPress() {
        dragging = true;
    }

    @Override
    public void onDrag(int mouseX, int mouseY) {
        if (dragging) {
            float relativeX = mouseX - x;
            value = Math.max(0, Math.min(1, relativeX / (width - knobWidth)));
        }
    }

    @Override
    public void onRelease() {
        dragging = false;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = Math.max(0, Math.min(1, value));
    }
}