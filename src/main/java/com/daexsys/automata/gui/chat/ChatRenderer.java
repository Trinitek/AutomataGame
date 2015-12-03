package com.daexsys.automata.gui.chat;

import com.daexsys.automata.gui.GUI;
import com.daexsys.automata.gui.Renderable;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ChatRenderer implements Renderable {

    private GUI gui;

    public ChatRenderer(GUI gui) {
        this.gui = gui;
    }

    public boolean typingState = false;

    @Override
    public void render(Graphics graphics) {
        graphics.setFont(new Font("Tahoma", Font.BOLD, 16));

        List<ChatMessage> newList =  gui.getGame().getChatManager().getChatMessages();

        int i = 0;
        Collections.reverse(newList);
        for(ChatMessage chatMessage : newList) {
            graphics.setColor(chatMessage.getColor());
            graphics.drawString(chatMessage.getMessage(), 120, 800 - i * 40);
            i++;
            if(i > 5) break;
        }
        Collections.reverse(newList);

        if(typingState) {
            graphics.setColor(Color.GRAY);
            graphics.fillRect(120, 820, 600, 50);

            graphics.setColor(Color.WHITE);
            graphics.drawString(gui.getKeyboardHandler().getCache(), 145, 840);
        }
    }

    public boolean isTyping() {
        return typingState;
    }
}
