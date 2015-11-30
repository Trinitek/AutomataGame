package com.daexsys.automata.gui.listeners;

import com.daexsys.automata.gui.GUI;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class ScrollManager implements MouseWheelListener {

    private GUI gui;

    public ScrollManager(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        gui.setZoomLevel(gui.getZoomLevel() + e.getWheelRotation() * -1);
    }
}
