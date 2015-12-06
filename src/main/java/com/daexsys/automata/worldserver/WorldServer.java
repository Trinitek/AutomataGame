package com.daexsys.automata.worldserver;

import com.daexsys.automata.Game;
import com.daexsys.automata.GameSide;
import com.daexsys.automata.Tile;
import com.daexsys.automata.event.chat.ChatMessageListener;
import com.daexsys.automata.event.game.TickListener;
import com.daexsys.automata.event.tile.StructurePlacementEvent;
import com.daexsys.automata.event.tile.StructurePlacementListener;
import com.daexsys.automata.event.tile.TileAlterListener;
import com.daexsys.automata.event.tile.TilePlacementReason;
import com.daexsys.automata.world.TileCoord;
import com.daexsys.automata.world.WorldLayer;
import com.daexsys.automata.world.structures.Structure;
import com.google.common.base.Optional;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class WorldServer {

    private Game game;
    private List<ClientConnection> clientConnectionList = new ArrayList<>();

    public WorldServer() {
        game = new Game(GameSide.SERVER);

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

            if(tileAlterEvent.getTileAlterCause() != TilePlacementReason.STRUCTURE_PLACEMENT) {
                ByteBuffer packetBuffer = ByteBuffer.allocate(10);

                Tile tile = tileAlterEvent.getTile();
                TileCoord tileCoord = tile.getCoordinate();

                packetBuffer.put(tileCoord.layer == WorldLayer.GROUND ? (byte) 0x04 : (byte) 0x08);
                packetBuffer.putShort((short) tileAlterEvent.getTile().getCoordinate().x);
                packetBuffer.putShort((short) tileAlterEvent.getTile().getCoordinate().y);
                packetBuffer.put(tileAlterEvent.getTile().getType().getID());

                broadcastPacket(packetBuffer);
            }
        });

        /* Chat listener */
        game.addListener((ChatMessageListener) chatMessageEvent -> {
            String chatMessage = chatMessageEvent.getChatMessage().getMessage();

            ByteBuffer packetBuffer = ByteBuffer.allocate(5 + chatMessage.length());
            packetBuffer.put((byte) 0x06);
            packetBuffer.put((byte) chatMessage.length());
            packetBuffer.put(chatMessage.getBytes());

            broadcastUrgentPacket(packetBuffer);
        });

        /*
            Packet structure:

            byte: packetID
            byte: length of structure name
            byte[] (string): structure name
            int: x coord to place structure
            int: y coord to place structure
            byte: length of arguments

            if arglength > 0
                send args as bytes
         */
        game.addListener((StructurePlacementListener) structurePlacementEvent -> {
            Structure structure = structurePlacementEvent.getStructure();
            String name = structure.getName();

            Optional<int[]> argsOptional = structurePlacementEvent.getArgs();

            int argsLength = 0;
            if(argsOptional.isPresent()) {
                argsLength = argsOptional.get().length;
            }

            ByteBuffer packetBuffer = ByteBuffer.allocate(name.length() + 1 + 1 + 8 + 1 + argsLength);
            packetBuffer.put((byte) 0x07);
            packetBuffer.put((byte) name.length());
            packetBuffer.put(name.getBytes());
            packetBuffer.putInt(structurePlacementEvent.getTileCoord().x);
            packetBuffer.putInt(structurePlacementEvent.getTileCoord().y);

            packetBuffer.put((byte) argsLength);
            if(argsLength != 0) {
                int[] args = argsOptional.get();

                for(int i : args) {
                    packetBuffer.put((byte) i);
                }
            }

            broadcastPacket(packetBuffer);
        });

//        /* Tick listener */
//        game.addListener((TickListener) tickEvent -> {
//            ByteBuffer byteBuffer = ByteBuffer.allocate(3);
//            byteBuffer.put((byte) 0x01);
//            byteBuffer.putShort((short) game.getTPS());
//            System.out.println(game.getTPS());
//
//            broadcastPacket(byteBuffer);
//        });

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

    public void broadcastUrgentPacket(ByteBuffer byteBuffer) {
        for(ClientConnection clientConnection : clientConnectionList) {
            clientConnection.getUrgentBuffer(byteBuffer);
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
