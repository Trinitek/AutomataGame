package com.daexsys.automata.client;

import com.daexsys.automata.Game;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class Client {

    private Game game;
    private boolean isConnected;

    public Client() {}
    public Client(Game game) { this.game = game; }

    public IOPair connectToRemote(SocketAddress socketAddress) throws IOException {
        Socket socket = new Socket();
        socket.connect(socketAddress);
        this.isConnected = true;
        return new IOPair(socket.getInputStream(), socket.getOutputStream());
    }

    public IOPair connectToLocal() {
        this.isConnected = true;
        return game.localConnection();
    }

    public boolean isConnected() {
        return isConnected;
    }
}
