package com.daexsys.automata.gui.listeners;

import com.daexsys.automata.gui.GUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMotionHandler implements MouseMotionListener {

    private GUI gui;
    private int x;
    private int y;

    public MouseMotionHandler(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();

        if(gui.isMouseDown()) {
            gui.playerPlaceTile(x, y, gui);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();

        if(gui.isMouseDown()) {
            gui.playerPlaceTile(x, y, gui);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
