package com.neutron.engine.base.interfaces.ui;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;

import java.awt.Color;

public class UITextBox extends UIObject {

    private StringBuilder text = new StringBuilder();
    private Color backgroundColor = Color.WHITE;
    private Color textColor = Color.BLACK;
    private Color borderColor = Color.BLACK;

    public UITextBox(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    protected void renderSelf(GameCore gameCore, Renderer r) {
        r.setUseScreenCoordinates(true);
        r.fillRect(x, y, width, height, backgroundColor);
        r.drawRect(x, y, width, height, borderColor);
        String t = text.toString();
        int textX = x + 5;
        int textY = y + height / 2 + 4;
        r.drawText(t, textX, textY, textColor);
    }

    @Override
    public void onKeyTyped(char c) {
        if (c == '\b') {
            if (text.length() > 0) text.deleteCharAt(text.length() - 1);
        } else if (c >= 32 && c <= 126) { // printable ASCII
            text.append(c);
        }
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    public String getText() {
        return text.toString();
    }

    public void setText(String text) {
        this.text = new StringBuilder(text);
    }
}