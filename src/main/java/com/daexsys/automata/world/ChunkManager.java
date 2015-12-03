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
    private List<Chunk> chunks = new ArrayList<Chunk>();

    public ChunkManager(World world) {
        this.world = world;
    }

    public Chunk getChunk(ChunkCoordinate chunkCoordinate) {
        return getChunk(chunkCoordinate.x, chunkCoordinate.y);
    }

    public Chunk getChunk(int x, int y) {
        for(Chunk chunk : new ArrayList<>(chunks)) {
            if(chunk.getChunkCoordinate().is(x, y)) {
                return chunk;
            }
        }

        /*
            If we have gotten this far, then the chunk hasn't been generated yet.
            But we need it! It must be generated.
         */
        return generateChunk(x, y);
    }

    private Chunk generateChunk(int x, int y) {
        Chunk chunk = new Chunk(world, x, y);
        world.getTerrainGenerator().generate(chunk);
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
//        int coreCount = Runtime.getRuntime().availableProcessors();
//        coreCount--;
//        ExecutorService executorService = Executors.newFixedThreadPool(coreCount);
//
//        for (int i = 0; i < coreCount; i++) {
//            final int segment = chunks.size() / coreCount;
//            final int ii = i;
//
//            Runnable thread = new Runnable() {
//                @Override
//                public void run() {
//                    final List<Chunk> chunks2 = new ArrayList<>(chunks.subList(segment * ii, segment * (ii + 1)));
//
//                    for(Chunk chunk : chunks2) {
//                        chunk.pulse();
//                    }
//
//                    for(Chunk chunk : chunks2) {
//                        chunk.depositQueue();
//                    }
//                }
//            };
//            executorService.execute(thread);
//        }

        final List<Chunk> chunks2 = new ArrayList<>(chunks);

        for(Chunk chunk : chunks2) {
            chunk.pulse();
        }

        for(Chunk chunk : chunks2) {
            chunk.depositQueue();
        }
    }

    public Collection<Chunk> getChunks() {
        return new ArrayList<Chunk>(chunks);
    }
}
