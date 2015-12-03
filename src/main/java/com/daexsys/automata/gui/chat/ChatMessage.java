package com.daexsys.automata.gui.chat;

import java.awt.*;

public class ChatMessage {

    private String message;
    private Color color;

    public ChatMessage(String message, Color color) {
        this.message = message;
        this.color = color;
    }

    public String getMessage() {
        return message;
    }

    public Color getColor() {
        return color;
    }
}
