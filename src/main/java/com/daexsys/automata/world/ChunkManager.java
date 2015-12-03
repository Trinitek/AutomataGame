package com.daexsys.automata.world;

import com.daexsys.automata.Pulsable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Worth noting this class is going to get a LOT more complex.
 * So hold on to your hat.
 */
public class ChunkManager implements Pulsable {

    private World world;
    private List<Chunk> chunks = new ArrayList<>();
    private Chunk[][] chunkArray = new Chunk[15][15];

    public ChunkManager(World world) {
        this.world = world;
    }

    public Chunk getChunk(ChunkCoordinate chunkCoordinate) {
        return getChunk(chunkCoordinate.x, chunkCoordinate.y);
    }

    public Chunk getChunk(int x, int y) {
        if(x < 15 && y < 15 && x >= 0 && y >= 0) {
            Chunk c = chunkArray[x][y];

            if (c == null) {
                return generateChunk(x, y);
            } else {
                return c;
            }
        } return null;
//        for(Chunk chunk : new ArrayList<>(chunks)) {
//            if(chunk.getChunkCoordinate().is(x, y)) {
//                return chunk;
//            }
//        }

        /*
            If we have gotten this far, then the chunk hasn't been generated yet.
            But we need it! It must be generated.
         */
//        return generateChunk(x, y);
    }

    private Chunk generateChunk(int x, int y) {
        Chunk chunk = new Chunk(world, x, y);
        world.getTerrainGenerator().generate(chunk);
        addChunk(chunk);
        chunkArray[x][y] = chunk;

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

    private List<Chunk> chunks2 = new ArrayList<>(chunks);
    public void pulse() {
        chunks2.clear();
        chunks2.addAll(chunks);

        for(Chunk chunk : chunks2) {
            chunk.pulse();
        }

        for(Chunk chunk : chunks2) {
            chunk.depositQueue();
        }
    }

    public boolean doesChunkExist(int x, int y) {
        for(Chunk c : getChunks()) {
            if(c.getChunkCoordinate().is(x, y)) {
                return true;
            }
        }

        return false;
    }

    public Collection<Chunk> getChunks() {
        return new ArrayList<Chunk>(chunks);
    }
}
