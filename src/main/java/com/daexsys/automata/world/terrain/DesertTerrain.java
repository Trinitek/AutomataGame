package com.daexsys.automata.world.terrain;

import com.daexsys.automata.world.Chunk;
import com.daexsys.automata.world.World;
import com.daexsys.automata.world.WorldLayers;
import com.daexsys.automata.world.tiletypes.TileTypes;

public class DesertTerrain extends TerrainGenerator {

    public DesertTerrain(World world) {
        super(world);
    }

    @Override
    public void generate(Chunk chunk) {
        super.generate(chunk);

        chunk.fillLayerWith(WorldLayers.ABOVE_GROUND, TileTypes.AIR);
        chunk.fillLayerWith(WorldLayers.GROUND, TileTypes.SAND);

        chunk.homogenous = true;
    }
}
