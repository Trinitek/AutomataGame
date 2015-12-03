package com.daexsys.automata.gui.listeners;

import com.daexsys.automata.gui.GUI;
import com.daexsys.automata.world.WorldLayer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {

    private GUI gui;
    private boolean mouseDown = false;

    public MouseHandler(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            gui.layerBuildingOn = WorldLayer.GROUND;
        } else {
            gui.layerBuildingOn = WorldLayer.ABOVE_GROUND;
        }

        gui.playerPlaceTile(e.getX(), e.getY(), gui);

        mouseDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public boolean isMouseDown() {
        return mouseDown;
    }

    public GUI getGui() {
        return gui;
    }
}
