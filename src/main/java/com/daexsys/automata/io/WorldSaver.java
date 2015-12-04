package com.daexsys.automata.io;

import com.daexsys.automata.world.Chunk;
import com.daexsys.automata.world.World;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

public class WorldSaver {

    public static void save(String location, World world) throws IOException {
        System.out.println("Saving world to " + location);

        try {
            Collection<Chunk> chunk = world.getChunkManager().getChunks();

            for (Chunk c : chunk) {
                File f = new File(location + "/" + c.getChunkCoordinate().x + ", " + c.getChunkCoordinate().y + ".automata");
                System.out.println(f.getAbsoluteFile());
                new File(location).mkdir();
                f.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(f);

                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        fileOutputStream.write(c.getTile(0, i, j).getType().getID());
                    }
                }
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
