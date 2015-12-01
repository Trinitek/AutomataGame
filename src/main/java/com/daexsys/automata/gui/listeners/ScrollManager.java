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
        int rotation = e.getWheelRotation() * -1;
        int newValue = gui.getZoomLevel() + rotation;

        if(newValue < 7) newValue = 7;
        if(newValue > 21) newValue = 21;

        gui.setZoomLevel(newValue);
    }
}
