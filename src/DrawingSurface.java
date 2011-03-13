/*
 * Copyright 2009 Entero Corporation. All Rights Reserved.
 * www.entero.com
 */

import javax.swing.*;
import java.awt.*;

public class DrawingSurface extends JFrame {
    private static final int FRAME_WIDTH = 700;
    private static final int FRAME_HEIGHT = 500;

    public static void main(String[] args){
        DrawingSurface drawingSurface = new DrawingSurface();
        drawingSurface.setVisible(true);
    }

    public DrawingSurface(){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createGUI();

        disableFrameFocusability();

        pack();
        setVisible(true);
        repaint();
    }

    private void createGUI(){
        Painter painter = new Painter();
        add(painter, BorderLayout.CENTER);
    }

    private void disableFrameFocusability() {
        setFocusable(false);
        getRootPane().setFocusable(false);
        getLayeredPane().setFocusable(false);
        getContentPane().setFocusable(false);
        getGlassPane().setFocusable(false);
    }
}
