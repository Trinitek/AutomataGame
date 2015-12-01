package com.daexsys.automata.gui.listeners;

import com.daexsys.automata.gui.GUI;
import org.omg.CORBA.NVList;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class ScrollManager implements MouseWheelListener {

    private GUI gui;

    public ScrollManager(GUI gui) {
        this.gui = gui;
    }

    private int scrollQueue = 0;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scrollQueue = e.getWheelRotation() * -1;
    }

    public int retrieveScrollQueue() {
        int cache = scrollQueue;
        scrollQueue = 0;
        return cache;
    }
}
