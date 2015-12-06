package com.daexsys.automata.world.structures;

import com.daexsys.automata.event.tile.StructurePlacementEvent;
import com.daexsys.automata.event.tile.TilePlacementReason;
import com.daexsys.automata.world.TileCoord;
import com.daexsys.automata.world.World;
import com.daexsys.automata.world.WorldLayer;
import com.daexsys.automata.world.tiletypes.TileType;

public class ExplosionStructure extends Structure {

    public ExplosionStructure() {
        super("vanilla:explosion");
    }

    @Override
    public void placeInWorldAt(World world, int x, int y, int... args) {
        int lakeSize = args[0];
        int lakeX = x;
        int lakeY = y;

        for (int fx = lakeX - lakeSize; fx < lakeX + lakeSize; fx++) {
            for (int fy = lakeY - lakeSize; fy < lakeY + lakeSize; fy++) {
                int xx = fx - lakeX;
                int yy = fy - lakeY;

                if((xx * xx + yy * yy) < lakeSize * lakeSize) {
                    world.setTileTypeAt(WorldLayer.GROUND, fx, fy, TileType.DIRT, TilePlacementReason.STRUCTURE_PLACEMENT);
                    world.setTileTypeAt(WorldLayer.ABOVE_GROUND, fx, fy, TileType.SMOKE, TilePlacementReason.STRUCTURE_PLACEMENT);
                }
            }
        }

        world.getGame().fireEvent(new StructurePlacementEvent(this, TileCoord.of(0, world, x, y), args));
    }
}
