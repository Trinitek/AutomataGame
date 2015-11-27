package com.daexsys.automata.gui.listeners;

import com.daexsys.automata.gui.GUI;
import com.daexsys.automata.world.TileTypes;
import com.daexsys.automata.world.WorldModel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMotionHandler implements MouseMotionListener {

    private GUI gui;

    public MouseMotionHandler(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(gui.isMouseDown()) {
            WorldModel worldModel = gui.getGame().getWorldModel();

            int tx = e.getX() / 40;
            int ty = e.getY() / 40;

            worldModel.setTileTypeAt(tx, ty, TileTypes.AUTOMATA_SIMPLE);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(gui.isMouseDown()) {
            WorldModel worldModel = gui.getGame().getWorldModel();

            int tx = e.getX() / 40;
            int ty = e.getY() / 40;

            worldModel.setTileTypeAt(tx, ty, TileTypes.AUTOMATA_SIMPLE);
        }
    }
}
