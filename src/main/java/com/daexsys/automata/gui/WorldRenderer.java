package com.daexsys.automata.gui;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.Chunk;
import com.daexsys.automata.world.ChunkCoord;
import com.daexsys.automata.world.ChunkManager;
import com.daexsys.automata.world.structures.Structure;
import com.daexsys.automata.world.structures.StructureElement;
import com.daexsys.automata.world.World;
import com.daexsys.automata.world.tiletypes.TileType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WorldRenderer implements Renderable {

    private GUI gui;
    private World world;

    public WorldRenderer(GUI gui) {
        this.gui = gui;
        this.world = gui.getGame().getWorld();
    }

    public GUI getGUI() {
        return gui;
    }

    @Override
    public void render(Graphics graphics) {
        ChunkManager chunkManager = world.getChunkManager();

        List<Chunk> toRender = getChunksToRender(chunkManager);
        for(Chunk chunk : toRender) {
            ChunkCoord chunkCoordinate = chunk.getChunkCoordinate();

            for (int x = 0; x < Chunk.DEFAULT_CHUNK_SIZE; x++) {
                for (int y = 0; y < Chunk.DEFAULT_CHUNK_SIZE; y++) {
                    for (int layer = 0; layer < 2; layer++) {
                        Tile tile = chunk.getTile(layer, x, y);

                        if (tile.getType() != TileType.AIR) {
                            BufferedImage imageToRender = tile.getType().getImage();

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
            }
        }

        graphics.setColor(new Color(50, 50, 50, 155));
        Structure structure = getGUI().getGame().getPlayerState().getSelectedStructure();

        if(structure == null) {
            graphics.fillRect(
                    getGUI().getMouseMotionHandler().getTileX() * getGUI().getZoomLevel(),
                    getGUI().getMouseMotionHandler().getTileY() * getGUI().getZoomLevel(),
                    getGUI().getZoomLevel(),
                    getGUI().getZoomLevel()
            );
        } else {
            for (StructureElement structureElement : structure.getStructureElements()) {
                graphics.fillRect(
                        (getGUI().getMouseMotionHandler().getTileX() + structureElement.getX()) * getGUI().getZoomLevel(),
                        (getGUI().getMouseMotionHandler().getTileY() + structureElement.getY()) * getGUI().getZoomLevel(),
                        getGUI().getZoomLevel(),
                        getGUI().getZoomLevel());

            }
        }
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
