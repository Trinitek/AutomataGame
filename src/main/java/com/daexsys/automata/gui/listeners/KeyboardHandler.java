package com.daexsys.automata.gui.listeners;

import com.daexsys.automata.PlayerState;
import com.daexsys.automata.gui.GUI;
import com.daexsys.automata.gui.chat.ChatMessage;

import java.awt.*;
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
    private String cache = "";

    public KeyboardHandler(GUI gui) {
        this.gui = gui;
    }

    public String getCache() {
        return cache;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        PlayerState playerState = gui.getGame().getPlayerState();

        if(gui.getChatRenderer().typingState) {

            if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                cache = cache.substring(0, cache.length() - 1);
            } else {
                cache += e.getKeyChar();
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_P) {
                gui.getGame().setPaused(!gui.getGame().isPaused());
            } else if (e.getKeyCode() == KeyEvent.VK_1) {
                playerState.getInventory().selectSlot(1);
                gui.getGame().getPlayerState().setSelectedStructure(null);
            } else if (e.getKeyCode() == KeyEvent.VK_2) {
                playerState.getInventory().selectSlot(2);
                gui.getGame().getPlayerState().setSelectedStructure(null);
            }
//
            else if (e.getKeyCode() == KeyEvent.VK_3) {
                playerState.getInventory().selectSlot(3);
                gui.getGame().getPlayerState().setSelectedStructure(null);
            } else if (e.getKeyCode() == KeyEvent.VK_4) {
                playerState.getInventory().selectSlot(4);
                gui.getGame().getPlayerState().setSelectedStructure(null);
            } else if (e.getKeyCode() == KeyEvent.VK_5) {
                playerState.getInventory().selectSlot(5);
                gui.getGame().getPlayerState().setSelectedStructure(null);
            } else if (e.getKeyCode() == KeyEvent.VK_6) {
                playerState.getInventory().selectSlot(6);
                gui.getGame().getPlayerState().setSelectedStructure(null);
            } else if (e.getKeyCode() == KeyEvent.VK_7) {
                playerState.getInventory().selectSlot(7);
                gui.getGame().getPlayerState().setSelectedStructure(null);
            } else if (e.getKeyCode() == KeyEvent.VK_8) {
                playerState.getInventory().selectSlot(8);
                gui.getGame().getPlayerState().setSelectedStructure(null);
            } else if (e.getKeyCode() == KeyEvent.VK_9) {
                playerState.getInventory().selectSlot(9);
                gui.getGame().getPlayerState().setSelectedStructure(null);
            } else if (e.getKeyCode() == KeyEvent.VK_E) {
                if (gui.getGame().getTickDelayRate() <= 10) {
                    gui.getGame().setTickDelayRate(gui.getGame().getTickDelayRate() - 1);
                } else {
                    gui.getGame().setTickDelayRate(gui.getGame().getTickDelayRate() - 10);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                gui.getGame().setTickDelayRate(gui.getGame().getTickDelayRate() + 10);
            } else if (e.getKeyCode() == KeyEvent.VK_G) {
                gui.getGame().getPlayerState().setSelectedStructure(gui.getGame().getStructures().getStructureByName("cgol_glider"));
            } else if (e.getKeyCode() == KeyEvent.VK_L) {
                gui.getGame().getPlayerState().setSelectedStructure(gui.getGame().getStructures().getStructureByName("cgol_lwss"));
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            gui.getChatRenderer().typingState = !gui.getChatRenderer().typingState;

            if(!gui.getChatRenderer().typingState && !cache.equals("")) {
                gui.getGame().getChatManager().addChatMessage(new ChatMessage("Player: " + cache, Color.WHITE));
                cache = "";
            }
        }

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
