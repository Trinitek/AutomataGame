package com.daexsys.automata.gui;

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

    private int speed = 1;

    public void moveUp(double delta) {
        delta /= 20;
        offsetY+=gui.getZoomLevel() * speed * delta;

        if(offsetY > 0)
            offsetY = 0;
    }

    public void moveDown(double delta) {
        delta /= 20;
        offsetY-=gui.getZoomLevel() * speed * delta;
    }

    public void moveLeft(double delta) {
        delta /= 20;
        offsetX += gui.getZoomLevel() * speed * delta;

        if(offsetX > 0)
            offsetX = 0;
    }

    public void moveRight(double delta) {
        delta /= 20;
        offsetX -= gui.getZoomLevel() * speed * delta;
    }

    @Override
    public String toString() {
        return offsetX + " " + offsetY;
    }
}
