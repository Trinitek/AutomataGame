package com.daexsys.automata;

import com.daexsys.automata.gui.GUI;
import com.daexsys.automata.world.tiletypes.TileType;

public class Main {

    public static void main(String[] args) {

        System.setProperty("game-name", "IDK");

//        TileType.getTileFromId((byte) 0);

        Game game = new Game();

        GUI gameGUI = new GUI(game);
        gameGUI.spawnWindow();
    }
}
