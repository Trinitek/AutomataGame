package com.daexsys.automata.worldserver.serverevent;

import com.daexsys.automata.worldserver.ClientConnection;

public class PlayerJoinEvent {

    private ClientConnection clientConnection;

    public PlayerJoinEvent(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }
}
