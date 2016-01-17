package com.daexsys.automata;

import com.daexsys.automata.gui.GUI;
import com.daexsys.automata.world.Chunk;
import com.daexsys.automata.world.tiletypes.TileType;

public class Main {

    public static void main(String[] args) {

        System.setProperty("game-name", "Automata");
        System.setProperty("org.lwjgl.librarypath", "natives");

        System.out.println("Using LWJGL version " + org.lwjgl.Version.getVersion());

//        TileType.getTileFromID((byte) 0);

        Game game = new Game(GameSide.BOTH);
        game.getWorld().getChunkManager();

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Chunk c = game.getWorld().getChunkManager().getChunk(i, j);
//                c.fillLayerWith(0, TileType.TALL_GRASS);
//                c.fillLayerWith(1, TileType.AIR);
            }
        }

        GUI gameGUI = new GUI(game);
        gameGUI.spawnWindow();
    }
}
