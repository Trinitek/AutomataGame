package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.World;
import com.daexsys.automata.world.WorldLayer;

import java.awt.image.BufferedImage;

public class BombTileType extends TileType {

    public BombTileType(byte id, String blockName, BufferedImage image, int defaultEnergy, int defaultDecayRate) {
        super(id, blockName, image, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void destruct(Tile tile) {
        World world = tile.getWorld();

        int size = world.getRandom().nextInt(5);

        for (int i = 0; i < size; i++) {
            int lakeX = world.getRandom().nextInt(size) + tile.getCoordinate().x;
            int lakeY = world.getRandom().nextInt(size) + tile.getCoordinate().y;
            int lakeSize = world.getRandom().nextInt(15) + 3;

            for (int fx = lakeX - lakeSize; fx < lakeX + lakeSize; fx++) {
                for (int fy = lakeY - lakeSize; fy < lakeY + lakeSize; fy++) {
                    int x = fx - lakeX;
                    int y = fy - lakeY;

                    if((x * x + y * y) < lakeSize * lakeSize) {
                        world.setTileTypeAt(WorldLayer.GROUND, fx, fy, TileType.DIRT);
                        world.setTileTypeAt(WorldLayer.ABOVE_GROUND, fx, fy, TileType.SMOKE);
                    }
                }
            }
        }
    }
}
