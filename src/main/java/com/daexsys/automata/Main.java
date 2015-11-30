package com.daexsys.automata;

import com.daexsys.automata.gui.GUI;

public class Main {

    public static void main(String[] args) {

        System.setProperty("game-name", "IDK");

        Game game = new Game();

        GUI gameGUI = new GUI(game);
        gameGUI.spawnWindow();
    }
}
