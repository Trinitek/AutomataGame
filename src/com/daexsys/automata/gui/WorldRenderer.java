package com.daexsys.automata.gui;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.WorldModel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WorldRenderer implements Renderable {

    private GUI gui;
    private WorldModel worldModel;

    public WorldRenderer(GUI gui) {
        this.gui = gui;
        this.worldModel = gui.getGame().getWorldModel();
    }

    public GUI getGui() {
        return gui;
    }

    public Offsets getOffsets() {
        return getGui().getOffsets();
    }

    @Override
    public void render(Graphics graphics) {
        for (int x = 0; x < worldModel.getTiles().length; x++) {
            for (int y = 0; y < worldModel.getTiles()[x].length; y++) {
                try {
                    Tile tile = worldModel.getTileAt(x, y);

                    BufferedImage imageToRender = tile.getTileType().getImage();

                    graphics.drawImage(imageToRender,
                            x *  getGui().getZoomLevel() + getOffsets().getOffsetX(),
                            y *  getGui().getZoomLevel() + getOffsets().getOffsetY(),
                            getGui().getZoomLevel() ,  getGui().getZoomLevel(),
                            null
                    );

                    if(getGui().getGame().isPaused()) {
                        graphics.setColor(Color.WHITE);
                        graphics.drawString(tile.getEnergy() + "", x * getGui().getZoomLevel() + getGui().getOffsets().getOffsetX() + 20
                                , y * getGui().getZoomLevel() + getGui().getOffsets().getOffsetY() + 20);
                    }
                } catch (Exception ignore) {}
            }
        }
    }
}
