package com.neutron.engine.base.interfaces.ui;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;

import java.awt.Color;
import java.util.Objects;


public class UIButton extends UIObject {

    protected String text;

    protected Color backgroundColor = new Color(60, 60, 60);
    protected Color borderColor = Color.BLACK;
    protected Color textColor = Color.WHITE;

    protected Color hoverColor = new Color(80, 80, 80);
    protected Color pressedColor = new Color(40, 40, 40);

    protected boolean hovered = false;
    protected boolean pressed = false;

    protected Runnable onClick;

    public UIButton(int x, int y, int width, int height, String text, Runnable onClick) {
        super(x, y, width, height);
        this.text = Objects.requireNonNull(text);
        this.onClick = onClick;
    }

    // ---- Rendering ----

    @Override
    protected void renderSelf(GameCore gameCore, Renderer r) {
        r.setUseScreenCoordinates(true);

        Color bg = backgroundColor;
        if (pressed) {
            bg = pressedColor;
        } else if (hovered) {
            bg = hoverColor;
        }

        r.fillRect(x, y, width, height, bg);
        r.drawRect(x, y, width, height, borderColor);

        // Simple centered text (baseline approximation)
        int textX = x + width / 2 - (text.length() * 4);
        int textY = y + height / 2 + 4;

        r.drawText(text, textX, textY, textColor);
    }

    // ---- Input handling ----

    public void updateHover(int mouseX, int mouseY) {
        hovered = isBeingPressed(mouseX, mouseY);
    }

    public void press(int mouseX, int mouseY) {
        if (!enabled || !visible) return;

        if (isBeingPressed(mouseX, mouseY)) {
            pressed = true;
        }
    }

    public void release(int mouseX, int mouseY) {
        if (!enabled || !visible) return;

        if (pressed && isBeingPressed(mouseX, mouseY)) {
            onPress();
        }
        pressed = false;
    }

    @Override
    public void onPress() {
        if (onClick != null) {
            onClick.run();
        }
    }
}
