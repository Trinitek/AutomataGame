package com.daexsys.automata.worldserver;

import com.daexsys.automata.Game;
import com.daexsys.automata.event.chat.ChatMessageListener;
import com.daexsys.automata.event.tile.TileAlterListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class WorldServer {

    private Game game;
    private List<ClientConnection> clientConnectionList = new ArrayList<>();

    public WorldServer() {
        game = new Game();

        int worldSize = 15;
        for (int i = 0; i < worldSize; i++) {
            for (int j = 0; j < worldSize; j++) {
                game.getWorld().getChunkManager().getChunk(i, j);
            }
        }
    }

    public void bindAndStart() {

        /* Block alter listener */
        game.addListener((TileAlterListener) tileAlterEvent -> {
            ByteBuffer packetBuffer = ByteBuffer.allocate(10);

            packetBuffer.put((byte) 0x04);
            packetBuffer.putInt(tileAlterEvent.getTile().getCoordinate().x);
            packetBuffer.putInt(tileAlterEvent.getTile().getCoordinate().y);
            packetBuffer.put(tileAlterEvent.getTile().getType().getID());

            broadcastPacket(packetBuffer);
        });

        /* Chat listener */
        game.addListener((ChatMessageListener) chatMessageEvent -> {
            String chatMessage = "GLOBAL: " + chatMessageEvent.getChatMessage().getMessage();

            ByteBuffer packetBuffer = ByteBuffer.allocate(5 + chatMessage.length());
            packetBuffer.put((byte) 0x06);
            packetBuffer.put((byte) chatMessage.length());
            packetBuffer.put(chatMessage.getBytes());

            broadcastPacket(packetBuffer);
        });

        final WorldServer theServer = this;
        Thread thread = new Thread(() -> {
            try {
                int port = 33433;
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Started server @" + port);

                while(true) {
                    Socket socket = serverSocket.accept();

                    ClientConnection clientConnection = new ClientConnection(theServer, socket);
                    clientConnectionList.add(clientConnection);
                    clientConnection.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void broadcastPacket(ByteBuffer byteBuffer) {
        for(ClientConnection clientConnection : clientConnectionList) {
            clientConnection.giveByteBuffer(byteBuffer);
        }
    }

    public Game getGame() {
        return game;
    }

    public static void main(String[] args) {
        new WorldServer().bindAndStart();
    }

    public List<ClientConnection> getClientConnectionList() {
        return clientConnectionList;
    }
}
