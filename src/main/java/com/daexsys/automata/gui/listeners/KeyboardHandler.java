package com.daexsys.automata.gui.listeners;

import com.daexsys.automata.gui.GUI;
import com.daexsys.automata.world.tiletypes.TileType;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class KeyboardHandler implements KeyListener {

    private static Map<Integer, Boolean> keysDown = new HashMap<Integer, Boolean>();
    static {
        for (int i = 0; i < 1000; i++) {
            keysDown.put(i, false);
        }
    }

    private GUI gui;

    public KeyboardHandler(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_P) {
            gui.getGame().setPaused(!gui.getGame().isPaused());
        }

        else if(e.getKeyCode() == KeyEvent.VK_1) {
            gui.getGame().getPlayerState().setInHand(TileType.CGOL);
            gui.getGame().getPlayerState().setSelectedStructure(null);
        }

        else if(e.getKeyCode() == KeyEvent.VK_2) {
            gui.getGame().getPlayerState().setInHand(TileType.GREEDY_VIRUS);
            gui.getGame().getPlayerState().setSelectedStructure(null);
        }

        else if(e.getKeyCode() == KeyEvent.VK_3) {
            gui.getGame().getPlayerState().setInHand(TileType.VM_255_BYTE_RAM);
            gui.getGame().getPlayerState().setSelectedStructure(null);
        }

        else if(e.getKeyCode() == KeyEvent.VK_4) {
            gui.getGame().getPlayerState().setInHand(TileType.MINER);
            gui.getGame().getPlayerState().setSelectedStructure(null);
        }

        else if(e.getKeyCode() == KeyEvent.VK_5) {
            gui.getGame().getPlayerState().setInHand(TileType.SHOCKWAVE_VIRUS);
            gui.getGame().getPlayerState().setSelectedStructure(null);
        }

        else if(e.getKeyCode() == KeyEvent.VK_6) {
            gui.getGame().getPlayerState().setInHand(TileType.GRASS);
            gui.getGame().getPlayerState().setSelectedStructure(null);
        }

        else if(e.getKeyCode() == KeyEvent.VK_7) {
            gui.getGame().getPlayerState().setInHand(TileType.WATER);
            gui.getGame().getPlayerState().setSelectedStructure(null);
        }

        else if(e.getKeyCode() == KeyEvent.VK_8) {
            gui.getGame().getPlayerState().setInHand(TileType.BOMB);
            gui.getGame().getPlayerState().setSelectedStructure(null);
        }

        else if(e.getKeyCode() == KeyEvent.VK_E) {
            gui.getGame().setTickDelayRate(gui.getGame().getTickDelayRate() - 10);
        }

        else if(e.getKeyCode() == KeyEvent.VK_Q) {
            gui.getGame().setTickDelayRate(gui.getGame().getTickDelayRate() + 10);
        }

        else if(e.getKeyCode() == KeyEvent.VK_G) {
            gui.getGame().getPlayerState().setSelectedStructure(gui.getGame().getStructures().getStructureByName("cgol_glider"));
        }

        else if(e.getKeyCode() == KeyEvent.VK_L) {
            gui.getGame().getPlayerState().setSelectedStructure(gui.getGame().getStructures().getStructureByName("cgol_lwss"));
        }
//
//        if(e.getKeyCode() == KeyEvent.VK_D) {
//            gui.getOffsets().moveRight();
//        }
//
//        if(e.getKeyCode() == KeyEvent.VK_A) {
//            gui.getOffsets().moveLeft();
//        }
//
//        if(e.getKeyCode() == KeyEvent.VK_W) {
//            gui.getOffsets().moveUp();
//        }
//
//        if(e.getKeyCode() == KeyEvent.VK_S) {
//            gui.getOffsets().moveDown();
//        }

        keysDown.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysDown.put(e.getKeyCode(), false);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public GUI getGui() {
        return gui;
    }

    public static boolean isDown(Integer i) {
        return keysDown.get(i);
    }
}
