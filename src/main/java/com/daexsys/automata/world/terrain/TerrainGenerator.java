package com.daexsys.automata.world.terrain;

import com.daexsys.automata.world.Chunk;
import com.daexsys.automata.world.World;

public class TerrainGenerator {

    private World world;

    public TerrainGenerator(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public void generate(Chunk chunk) {

    }
}
