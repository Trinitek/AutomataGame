package com.daexsys.automata.gui.listeners;

import com.daexsys.automata.gui.GUI;
import com.daexsys.automata.world.tiletypes.TileTypes;

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
            gui.getGame().getPlayerState().setInHand(TileTypes.CGOL);
        }

        else if(e.getKeyCode() == KeyEvent.VK_2) {
            gui.getGame().getPlayerState().setInHand(TileTypes.VIRUS);
        }

        else if(e.getKeyCode() == KeyEvent.VK_3) {
            gui.getGame().getPlayerState().setInHand(TileTypes.VM_255_BYTE_RAM);
        }

        else if(e.getKeyCode() == KeyEvent.VK_4) {
            gui.getGame().getPlayerState().setInHand(TileTypes.MINER);
        }

        else if(e.getKeyCode() == KeyEvent.VK_5) {
            gui.getGame().getPlayerState().setInHand(TileTypes.EQUAL_VIRUS);
        }

        else if(e.getKeyCode() == KeyEvent.VK_E) {
            gui.getGame().setTickDelayRate(gui.getGame().getTickDelayRate() - 10);
        }

        else if(e.getKeyCode() == KeyEvent.VK_Q) {
            gui.getGame().setTickDelayRate(gui.getGame().getTickDelayRate() + 10);
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
