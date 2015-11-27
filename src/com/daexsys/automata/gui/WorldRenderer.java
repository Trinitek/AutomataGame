package com.daexsys.automata.gui;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.WorldModel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WorldRenderer implements Renderable {

    public static final int TILE_SIZE = 40;

    private WorldModel worldModel;
    private Offsets offsets = new Offsets();

    public WorldRenderer(WorldModel worldModel) {
        this.worldModel = worldModel;
    }

    @Override
    public void render(Graphics graphics) {
        for (int x = 0; x < worldModel.getTiles().length; x++) {
            for (int y = 0; y < worldModel.getTiles()[x].length; y++) {
                try {
                    Tile tile = worldModel.getTileAt(x, y);

                    BufferedImage imageToRender = tile.getTileType().getImage();

                    graphics.drawImage(imageToRender,
                            x * TILE_SIZE + offsets.getOffsetX(),
                            y * TILE_SIZE + offsets.getOffsetY(),
                            TILE_SIZE, TILE_SIZE,
                            null
                    );
                } catch (Exception ignore) {}
            }
        }
    }
}
