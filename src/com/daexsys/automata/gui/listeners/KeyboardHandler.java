package com.daexsys.automata.gui.listeners;

import com.daexsys.automata.gui.GUI;

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
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_P) {
            gui.getGame().setPaused(!gui.getGame().isPaused());
        }

        keysDown.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysDown.put(e.getKeyCode(), false);

    }

    public GUI getGui() {
        return gui;
    }

    public static boolean isDown(Integer i) {
        return keysDown.get(i);
    }
}
