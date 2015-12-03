package com.daexsys.automata.gui;

import com.daexsys.automata.gui.listeners.KeyboardHandler;

import java.awt.event.KeyEvent;

public class Offsets {

    private int offsetX = 0;
    private int offsetY = 0;

    private GUI gui;

    public Offsets(GUI gui) {
        this.gui = gui;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    private double speed = .25;

    public double getSpeed() {
        return KeyboardHandler.isDown(KeyEvent.VK_SHIFT) ? speed * 4 : speed;
    }

    public void moveUp(double delta) {
        delta /= 20;
        offsetY+=gui.getZoomLevel() * getSpeed() * delta;

        if(offsetY > 0)
            offsetY = 0;
    }

    public void moveDown(double delta) {
        delta /= 20;
        offsetY-=gui.getZoomLevel() * getSpeed() * delta;
    }

    public void moveLeft(double delta) {
        delta /= 20;
        offsetX += gui.getZoomLevel() * getSpeed() * delta;

        if(offsetX > 0)
            offsetX = 0;
    }

    public void moveRight(double delta) {
        delta /= 20;
        offsetX -= gui.getZoomLevel() * getSpeed() * delta;
    }

    @Override
    public String toString() {
        return offsetX + " " + offsetY;
    }
}
