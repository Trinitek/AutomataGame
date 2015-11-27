package com.daexsys.automata.gui.listeners;

import com.daexsys.automata.gui.GUI;
import com.daexsys.automata.world.TileTypes;
import com.daexsys.automata.world.WorldModel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMotionHandler implements MouseMotionListener {

    private GUI gui;
    private int x;
    private int y;

    public MouseMotionHandler(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();

        if(gui.isMouseDown()) {
            WorldModel worldModel = gui.getGame().getWorldModel();

            int tx = e.getX() / gui.getZoomLevel();
            int ty = e.getY() / gui.getZoomLevel();

            worldModel.setTileTypeAt(tx, ty, gui.getGame().getPlayerState().getInHand());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();

        if(gui.isMouseDown()) {
            WorldModel worldModel = gui.getGame().getWorldModel();

            int tx = e.getX() / gui.getZoomLevel();
            int ty = e.getY() / gui.getZoomLevel();

            worldModel.setTileTypeAt(tx, ty, gui.getGame().getPlayerState().getInHand());
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
