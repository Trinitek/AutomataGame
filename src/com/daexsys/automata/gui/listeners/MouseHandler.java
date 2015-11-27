package com.daexsys.automata.gui.listeners;

import com.daexsys.automata.gui.GUI;
import com.daexsys.automata.world.TileTypes;
import com.daexsys.automata.world.WorldModel;

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

            gui.getGame().getPlayerState().placeBlock(e.getX(), e.getY(), gui);

        mouseDown = true;
//        System.out.println("mouse down");

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
//        System.out.println("mouse up");

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public boolean isMouseDown() {
        return mouseDown;
    }

    public GUI getGui() {
        return gui;
    }
}
