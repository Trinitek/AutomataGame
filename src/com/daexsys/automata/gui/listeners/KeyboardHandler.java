package com.daexsys.automata.gui.listeners;

import com.daexsys.automata.gui.GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardHandler implements KeyListener {

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
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public GUI getGui() {
        return gui;
    }
}
