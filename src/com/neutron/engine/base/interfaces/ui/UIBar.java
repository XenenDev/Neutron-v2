package com.neutron.engine.base.interfaces.ui;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;

import java.awt.Color;

public class UIBar extends UIObject {

    private float value; // 0 to 1
    private Color backgroundColor = Color.GRAY;
    private Color fillColor = Color.GREEN;

    public UIBar(int x, int y, int width, int height, float value) {
        super(x, y, width, height);
        this.value = Math.max(0, Math.min(1, value));
    }

    @Override
    protected void renderSelf(GameCore gameCore, Renderer r) {
        r.setUseScreenCoordinates(true);
        r.fillRect(x, y, width, height, backgroundColor);
        int fillWidth = (int) (width * value);
        r.fillRect(x, y, fillWidth, height, fillColor);
    }

    public void setValue(float value) {
        this.value = Math.max(0, Math.min(1, value));
    }

    public float getValue() {
        return value;
    }
}