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
    public void moveUp() {
        offsetY+=gui.getZoomLevel() * speed;

        if(offsetY > 0)
            offsetY = 0;
    }

    public void moveDown() {
        offsetY-=gui.getZoomLevel() * speed;
    }

    public void moveLeft() {
        offsetX += gui.getZoomLevel() * speed;

        if(offsetX > 0)
            offsetX = 0;
    }

    public void moveRight() {
        offsetX -= gui.getZoomLevel() * speed;
    }

    @Override
    public String toString() {
        return offsetX + " " + offsetY;
    }
}
