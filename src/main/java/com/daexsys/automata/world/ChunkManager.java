package com.daexsys.automata.world;

import com.daexsys.automata.Pulsable;
import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;

/**
 * Worth noting this class is going to get a LOT more complex.
 * So hold on to your hat.
 */
public class ChunkManager implements Pulsable {

    private World world;
    private List<Chunk> chunks = new ArrayList<Chunk>();

    public ChunkManager(World world) {
        this.world = world;
    }

    public Optional<Chunk> getChunk(int x, int y) {
        for(Chunk chunk : chunks) {
            if(chunk.getChunkCoordinate().is(x, y)) {
                return Optional.of(chunk);
            }
        }

        /*
            If we have gotten this far, then the chunk hasn't been generated yet.
            But we need it! It must be generated.
         */
        return Optional.of(generateChunk(x, y));
    }

    private Chunk generateChunk(int x, int y) {
        Chunk chunk = new Chunk(world, x, y);
        addChunk(chunk);
        return chunk;
    }

    private void addChunk(Chunk chunk) {
        chunks.add(chunk);
    }

    private void removeChunk(Chunk chunk) {
        chunks.remove(chunk);
    }

    public World getWorld() {
        return world;
    }

    public void pulse() {
        for(Chunk chunk : chunks) {
            chunk.pulse();
        }
    }
}
