package com.neutron.engine;

import javax.swing.*;
import java.awt.*;

public class Window extends Frame {

    private final Canvas canvas;

    public Window(String windowTitle, int w, int h, String iconPath, Input input) {
        JFrame frame = new JFrame(windowTitle);
        canvas = new Canvas();

        Dimension dimension = new Dimension(w, h);

        //setup window
        canvas.setMinimumSize(dimension);
        canvas.setMaximumSize(dimension);
        canvas.setPreferredSize(dimension);

        frame.setMinimumSize(dimension);
        frame.setMaximumSize(dimension);
        frame.setPreferredSize(dimension);

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //add icon image
        frame.setIconImage(ResManager.img(iconPath));

        //add canvas to frame
        frame.add(canvas);
        frame.pack();
        frame.setLocationRelativeTo(null);

        //add listeners
        canvas.addKeyListener(input);
        canvas.addMouseListener(input);
        canvas.addMouseMotionListener(input);
        canvas.addMouseWheelListener(input);
        canvas.addFocusListener(input);
        canvas.requestFocus();

        //set window visible
        frame.setVisible(true);
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
