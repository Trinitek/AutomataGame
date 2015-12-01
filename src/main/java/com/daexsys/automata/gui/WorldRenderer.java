package com.daexsys.automata.gui;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.Chunk;
import com.daexsys.automata.world.ChunkCoordinate;
import com.daexsys.automata.world.ChunkManager;
import com.daexsys.automata.world.tiletypes.TileTypes;
import com.daexsys.automata.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

public class WorldRenderer implements Renderable {

    private GUI gui;
    private World worldModel;

    public WorldRenderer(GUI gui) {
        this.gui = gui;
        this.worldModel = gui.getGame().getWorldModel();
    }

    public GUI getGUI() {
        return gui;
    }

    @Override
    public void render(Graphics graphics) {
        ChunkManager chunkManager = worldModel.getChunkManager();

        Collection<Chunk> chunks = new ArrayList<Chunk>(chunkManager.getChunks());
        Rectangle screen = new Rectangle(0, 0, 1920, 1080);

        for(Chunk chunk : chunks) {
            ChunkCoordinate chunkCoordinate = chunk.getChunkCoordinate();

            Rectangle chunkRect = new Rectangle(
                    (chunkCoordinate.x * Chunk.DEFAULT_CHUNK_SIZE * gui.getZoomLevel())
                            + getGUI().getOffsets().getOffsetX(),
                    (chunkCoordinate.y * Chunk.DEFAULT_CHUNK_SIZE * gui.getZoomLevel())
                            + getGUI().getOffsets().getOffsetY(),
                    40, 40
            );

            if(screen.intersects(chunkRect)) { // If chunk is on-screen
                for (int x = 0; x < Chunk.DEFAULT_CHUNK_SIZE; x++) {
                    for (int y = 0; y < Chunk.DEFAULT_CHUNK_SIZE; y++) {
                        for (int layer = 0; layer < 2; layer++) {
                            Tile tile = chunk.getTile(layer, x, y);

                            if (tile.getTileType() != TileTypes.AIR) {
                                BufferedImage imageToRender = tile.getTileType().getImage();

                                graphics.drawImage(imageToRender,
                                        chunkCoordinate.amplifyLocalX(x) *
                                                getGUI().getZoomLevel() +
                                                getGUI().getOffsets().getOffsetX(),
                                        chunkCoordinate.amplifyLocalY(y) *
                                                getGUI().getZoomLevel() +
                                                getGUI().getOffsets().getOffsetY(),
                                        getGUI().getZoomLevel(), getGUI().getZoomLevel(),
                                        null
                                );
                            }
                        }
                    }
                }
            }
        }
    }
}
