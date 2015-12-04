package com.daexsys.automata.event.chat;

import com.daexsys.automata.event.Listener;

public interface ChatMessageListener extends Listener {

    public void chatMessage(ChatMessageEvent chatMessageEvent);
}
