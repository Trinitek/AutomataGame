package com.daexsys.automata.world.terrain;

import com.daexsys.automata.world.Chunk;
import com.daexsys.automata.world.World;
import com.daexsys.automata.world.WorldLayer;
import com.daexsys.automata.world.tiletypes.TileType;

public class TemperateTerrain extends TerrainGenerator {

    public TemperateTerrain(World world) {
        super(world);
    }

    @Override
    public void generate(Chunk chunk) {
        super.generate(chunk);

        chunk.fillLayerWith(WorldLayer.ABOVE_GROUND, TileType.AIR);
        chunk.fillLayerWith(WorldLayer.GROUND, TileType.TALL_GRASS);

        chunk.homogenous = true;
    }
}
