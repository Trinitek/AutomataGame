package com.daexsys.automata.worldserver;

import com.daexsys.automata.Tile;
import com.daexsys.automata.event.chat.ChatMessageEvent;
import com.daexsys.automata.gui.chat.ChatMessage;
import com.daexsys.automata.world.Chunk;
import com.daexsys.automata.world.structures.Structure;
import com.daexsys.automata.world.tiletypes.TileType;
import com.daexsys.automata.worldserver.WorldServer;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;

public class ClientConnection {

    private WorldServer worldServer;
    private Socket socket;
    private String username;

    private List<ByteBuffer> packetsToSend = new ArrayList<>();

    public ClientConnection(WorldServer worldServer, Socket socket) {
        this.worldServer = worldServer;
        this.socket = socket;
    }

    public void start() throws IOException {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        Thread theThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        byte packetID = dataInputStream.readByte();

                        if(packetID == 0x00) {
                            byte length = dataInputStream.readByte();
                            byte[] b = new byte[length];

                            for (int i = 0; i < length; i++) {
                                b[i] = dataInputStream.readByte();
                            }
                            username = new String(b);
                            worldServer.getGame().fireEvent(new ChatMessageEvent(new ChatMessage(username, Color.YELLOW)));
                        }

                        if(packetID == 0x04) {
                            int x = dataInputStream.readInt();
                            int y = dataInputStream.readInt();
                            byte b = (byte) inputStream.read();

                            worldServer.getGame().getWorld().setTileTypeAt(0, x, y, TileType.getTileFromId(b));
                        }

                        if(packetID == 0x06) {
                            byte length = dataInputStream.readByte();
                            byte[] b = new byte[length];

                            for (int i = 0; i < length; i++) {
                                b[i] = dataInputStream.readByte();
                            }
                            String chat = new String(b);
                            worldServer.getGame().fireEvent(new ChatMessageEvent(new ChatMessage(chat, Color.WHITE)));
                        }
                    }
                }catch (Exception e) {

                }
            }
        });
        theThread.start();

        Collection<Chunk> chunks = worldServer.getGame().getWorld().getChunkManager().getChunks();

        for (Chunk chunk : chunks) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(265);
            byteBuffer.put((byte) 0x05);
            byteBuffer.putInt(chunk.getChunkCoordinate().x);
            byteBuffer.putInt(chunk.getChunkCoordinate().y);

            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    Tile t = chunk.getTile(0, i, j);
                    byteBuffer.put(t.getType().getID());
                }
            }
            giveByteBuffer(byteBuffer);
        }

        String chatMessage = "World fully loaded. Welcome to the server!";
        ByteBuffer chatPacket = ByteBuffer.allocate(2 + chatMessage.length());
        chatPacket.put((byte) 0x06);
        chatPacket.put((byte) chatMessage.length());
        chatPacket.put(chatMessage.getBytes());
        giveByteBuffer(chatPacket);
//
//        worldServer.getGame().getWorld().setTileTypeAt(0, 50, 50, TileType.GREEDY_VIRUS);
//        worldServer.getGame().getWorld().setTileTypeAt(0, 25, 25, TileType.GREEDY_VIRUS);
//
//        Structure structure = worldServer.getGame().getStructures().getStructureByName("cgol_glider");
//        for (int i = 0; i < 20; i++) {
//            for (int j = 0; j < 20; j++) {
//                structure.placeInWorldAt(worldServer.getGame().getWorld(), i * 10, j * 10);
//            }
//        }

        Thread sendPackets = new Thread(() -> {
            while(true) {
                if(!packetsToSend.isEmpty()) {
                    ByteBuffer a = packetsToSend.remove(0);

                    try {
                        dataOutputStream.write(a.array());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        sendPackets.start();
    }

    public void giveByteBuffer(ByteBuffer byteBuffer) {
        packetsToSend.add(byteBuffer);
    }

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }
}
