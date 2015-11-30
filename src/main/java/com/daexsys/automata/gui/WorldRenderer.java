package com.daexsys.automata.gui;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.tiletypes.TileTypes;
import com.daexsys.automata.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WorldRenderer implements Renderable {

    private GUI gui;
    private World worldModel;

    public WorldRenderer(GUI gui) {
        this.gui = gui;
        this.worldModel = gui.getGame().getWorldModel();
    }

    public GUI getGui() {
        return gui;
    }

    @Override
    public void render(Graphics graphics) {
        Tile[][] tiles = worldModel.getTiles()[0];

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                for (int i = 0; i < 2; i++) {
                    try {
                        Tile tile = worldModel.getTileAt(i, x, y);

                        if(tile.getTileType() != TileTypes.AIR) {
                            BufferedImage imageToRender = tile.getTileType().getImage();

                            graphics.drawImage(imageToRender,
                                    x * getGui().getZoomLevel() + getGui().getOffsets().getOffsetX(),
                                    y * getGui().getZoomLevel() + getGui().getOffsets().getOffsetY(),
                                    getGui().getZoomLevel(), getGui().getZoomLevel(),
                                    null
                            );
                        }

                        if(i == 1 && gui.getGame().isPaused()) {
//                            graphics.setColor(Color.WHITE);
//                            graphics.drawString(tile.getEnergy() + "", x * getGui().getZoomLevel() + getGui()..getOffsetX() + 20, y * getGui() + offsets.getOffsetY() + 20);
                        }
                    } catch (Exception ignore) {ignore.printStackTrace();}
                }
            }
        }
    }
}
