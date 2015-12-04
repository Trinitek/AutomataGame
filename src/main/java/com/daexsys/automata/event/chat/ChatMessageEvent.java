package com.daexsys.automata.event.chat;

import com.daexsys.automata.event.Event;
import com.daexsys.automata.gui.chat.ChatMessage;

public class ChatMessageEvent implements Event {

    private ChatMessage chatMessage;

    public ChatMessageEvent(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }
}
