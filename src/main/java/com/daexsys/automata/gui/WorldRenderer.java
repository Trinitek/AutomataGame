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
import java.util.List;

public class WorldRenderer implements Renderable {

    private GUI gui;
    private World worldModel;

    public WorldRenderer(GUI gui) {
        this.gui = gui;
        this.worldModel = gui.getGame().getWorld();
    }

    public GUI getGUI() {
        return gui;
    }

    @Override
    public void render(Graphics graphics) {
        ChunkManager chunkManager = worldModel.getChunkManager();

        List<Chunk> toRender = getChunksToRender(chunkManager);
        for(Chunk chunk : toRender) {
            ChunkCoordinate chunkCoordinate = chunk.getChunkCoordinate();

            for (int x = 0; x < Chunk.DEFAULT_CHUNK_SIZE; x++) {
                for (int y = 0; y < Chunk.DEFAULT_CHUNK_SIZE; y++) {
                    for (int layer = 0; layer < 2; layer++) {
                        Tile tile = chunk.getTile(layer, x, y);

                        if (tile.getTileType() != TileTypes.AIR) {
                            BufferedImage imageToRender = tile.getTileType().getImage();

                            // If it's small enough, we don't have to render the whole image
                            if (getGUI().getZoomLevel() <= 4) {
                                graphics.setColor(new Color(imageToRender.getRGB(0, 0)));
                                graphics.fillRect(chunkCoordinate.amplifyLocalX(x) *
                                                getGUI().getZoomLevel() +
                                                getGUI().getOffsets().getOffsetX(),
                                        chunkCoordinate.amplifyLocalY(y) *
                                                getGUI().getZoomLevel() +
                                                getGUI().getOffsets().getOffsetY(),
                                        getGUI().getZoomLevel(), getGUI().getZoomLevel());
                            } else {
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

//        Collection<Chunk> chunks = new ArrayList<Chunk>(chunkManager.getChunks());
//        Rectangle screen = new Rectangle(0, 0,
//                (int) getGUI().getWindowSize().getWidth(), (int) getGUI().getWindowSize().getHeight());
//
//        for(Chunk chunk : chunks) {
//            ChunkCoordinate chunkCoordinate = chunk.getChunkCoordinate();
//
//            Rectangle chunkRect = new Rectangle(
//                    (chunkCoordinate.x * Chunk.DEFAULT_CHUNK_SIZE * gui.getZoomLevel())
//                            + getGUI().getOffsets().getOffsetX() - gui.getZoomLevel(),
//                    (chunkCoordinate.y * Chunk.DEFAULT_CHUNK_SIZE * gui.getZoomLevel())
//                            + getGUI().getOffsets().getOffsetY() - gui.getZoomLevel(),
//                    80, 80
//            );
//
//            if(screen.intersects(chunkRect)) { // If chunk is on-screen
//                if(getGUI().getZoomLevel() <= 4 && chunk.isHomogenous()) {
//                    graphics.setColor(new Color(chunk.getTile(0, 0, 0).getTileType().getImage().getRGB(0, 0)));
//                    graphics.fillRect(
//                            chunkCoordinate.x * 16 *
//                                    getGUI().getZoomLevel() +
//                                    getGUI().getOffsets().getOffsetX(),
//                            chunkCoordinate.y * 16 *
//                                    getGUI().getZoomLevel() +
//                                    getGUI().getOffsets().getOffsetY(),
//
//                            getGUI().getZoomLevel() * Chunk.DEFAULT_CHUNK_SIZE, getGUI().getZoomLevel() * Chunk.DEFAULT_CHUNK_SIZE);
//                } else {
//                    for (int x = 0; x < Chunk.DEFAULT_CHUNK_SIZE; x++) {
//                        for (int y = 0; y < Chunk.DEFAULT_CHUNK_SIZE; y++) {
//                            for (int layer = 0; layer < 2; layer++) {
//                                Tile tile = chunk.getTile(layer, x, y);
//
//                                if (tile.getTileType() != TileTypes.AIR) {
//                                    BufferedImage imageToRender = tile.getTileType().getImage();
//
//                                    // If it's small enough, we don't have to render the whole image
//                                    if (getGUI().getZoomLevel() <= 4) {
//                                        graphics.setColor(new Color(imageToRender.getRGB(0, 0)));
//                                        graphics.fillRect(chunkCoordinate.amplifyLocalX(x) *
//                                                        getGUI().getZoomLevel() +
//                                                        getGUI().getOffsets().getOffsetX(),
//                                                chunkCoordinate.amplifyLocalY(y) *
//                                                        getGUI().getZoomLevel() +
//                                                        getGUI().getOffsets().getOffsetY(),
//                                                getGUI().getZoomLevel(), getGUI().getZoomLevel());
//                                    } else {
//                                        graphics.drawImage(imageToRender,
//                                                chunkCoordinate.amplifyLocalX(x) *
//                                                        getGUI().getZoomLevel() +
//                                                        getGUI().getOffsets().getOffsetX(),
//                                                chunkCoordinate.amplifyLocalY(y) *
//                                                        getGUI().getZoomLevel() +
//                                                        getGUI().getOffsets().getOffsetY(),
//                                                getGUI().getZoomLevel(), getGUI().getZoomLevel(),
//                                                null
//                                        );
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
            }}
        }

    public List<Chunk> getChunksToRender(ChunkManager chunkManager) {
        Collection<Chunk> allChunks = chunkManager.getChunks();
        List<Chunk> toRender = new ArrayList<Chunk>();

        Rectangle screen = new Rectangle(0, 0,
                (int) getGUI().getWindowSize().getWidth(), (int) getGUI().getWindowSize().getHeight());

        for(Chunk c : allChunks) {
            int sx = c.getChunkCoordinate().x * 16 * gui.getZoomLevel() + gui.getOffsets().getOffsetX();
            int sy = c.getChunkCoordinate().y * 16 * gui.getZoomLevel() + gui.getOffsets().getOffsetY();

            int xcf = 16 * gui.getZoomLevel();
            int ycf = 16 * gui.getZoomLevel();

            Rectangle chunkTangle = new Rectangle(sx, sy, xcf, ycf);

            if(screen.intersects(chunkTangle)) {
                toRender.add(c);
            }
        }

        return toRender;
    }
}
