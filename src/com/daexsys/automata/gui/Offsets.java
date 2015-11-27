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
        offsetY+=gui.getZoomLevel();
    }

    public void moveDown() {
        offsetY-=gui.getZoomLevel();
    }

    public void moveLeft() {
        offsetX+=gui.getZoomLevel();
    }

    public void moveRight() {
        offsetX-=gui.getZoomLevel();
    }
}
