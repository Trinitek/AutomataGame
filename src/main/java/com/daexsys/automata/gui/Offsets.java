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

    public void moveUp() {
        offsetY+=gui.getZoomLevel() * 2;

        if(offsetY > 0)
            offsetY = 0;
    }

    public void moveDown() {
        offsetY-=gui.getZoomLevel() * 2;
    }

    public void moveLeft() {
        offsetX += gui.getZoomLevel() * 2;

        if(offsetX > 0)
            offsetX = 0;
    }

    public void moveRight() {
        offsetX -= gui.getZoomLevel() * 2;
    }

    @Override
    public String toString() {
        return offsetX + " " + offsetY;
    }
}
