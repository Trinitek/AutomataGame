package com.daexsys.automata.gui;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.WorldModel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WorldRenderer implements Renderable {

    public static final int TILE_SIZE = 40;

    private GUI gui;
    private WorldModel worldModel;
    private Offsets offsets = new Offsets();

    public WorldRenderer(GUI gui, WorldModel worldModel) {
        this.gui = gui;
        this.worldModel = worldModel;
    }

    public GUI getGui() {
        return gui;
    }

    @Override
    public void render(Graphics graphics) {
        for (int x = 0; x < worldModel.getTiles().length; x++) {
            for (int y = 0; y < worldModel.getTiles()[x].length; y++) {
                try {
                    Tile tile = worldModel.getTileAt(x, y);

                    BufferedImage imageToRender = tile.getTileType().getImage();

                    graphics.drawImage(imageToRender,
                            x *  getGui().getZoomLevel()  + offsets.getOffsetX(),
                            y *  getGui().getZoomLevel()  + offsets.getOffsetY(),
                            getGui().getZoomLevel() ,  getGui().getZoomLevel() ,
                            null
                    );

//                    graphics.setColor(Color.WHITE);
//                    graphics.drawString(tile.getEnergy() + "", x * getGui().getZoomLevel() + offsets.getOffsetX() + 20, y * getGui().getZoomLevel() + offsets.getOffsetY() + 20);
                } catch (Exception ignore) {}
            }
        }
    }
}
