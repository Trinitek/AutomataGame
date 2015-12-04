package com.daexsys.automata.worldserver;

import com.daexsys.automata.Tile;
import com.daexsys.automata.event.chat.ChatMessageEvent;
import com.daexsys.automata.gui.chat.ChatMessage;
import com.daexsys.automata.io.WorldSaver;
import com.daexsys.automata.world.Chunk;
import com.daexsys.automata.world.tiletypes.TileType;

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

        for(ClientConnection clientConnection : worldServer.getClientConnectionList()) {
            if(clientConnection != this) {
                String theirUsername = clientConnection.getUsername();

                ByteBuffer byteBuffer = ByteBuffer.allocate(theirUsername.length() + 2);
                byteBuffer.put((byte) 0x02);
                byteBuffer.put((byte) theirUsername.length());
                byteBuffer.put(theirUsername.getBytes());
                worldServer.broadcastPacket(byteBuffer);
            }
        }

        Thread theThread = new Thread(() -> {
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
                        System.out.println(username + " has connected");
                        worldServer.getGame().fireEvent(new ChatMessageEvent(new ChatMessage(username + " has joined the server", Color.YELLOW)));

                        ByteBuffer byteBuffer = ByteBuffer.allocate(length + 2);
                        byteBuffer.put((byte) 0x02);
                        byteBuffer.put(length);
                        byteBuffer.put(b);
                        worldServer.broadcastPacket(byteBuffer);
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

                        if(chat.charAt(0) == '/') {
                            System.out.println("command!");

                            String command = chat.substring(1, chat.length());
                            String commandName = command.split("\\s+")[0];
                            System.out.println(commandName);

                            if(commandName.equalsIgnoreCase("pause")) {
                                worldServer.getGame().setPaused(!worldServer.getGame().isPaused());
                                worldServer.getGame().fireEvent(new ChatMessageEvent(new ChatMessage("Paused state: " +worldServer.getGame().isPaused(), Color.WHITE)));
                            }
                            else if(commandName.equalsIgnoreCase("tickrate")) {
                                worldServer.getGame().setTickDelayRate(Integer.parseInt(command.split("\\s+")[1]));
                            }

                            else if(commandName.equalsIgnoreCase("save")) {
                                WorldSaver.save("serverworld", worldServer.getGame().getWorld());
                            }
                            else {
                                worldServer.getGame().fireEvent(new ChatMessageEvent(new ChatMessage("Unknown command '" + commandName + "'", Color.WHITE)));
                            }

                        } else {
                            String message = "<" + username + "> " + chat;
                            worldServer.getGame().fireEvent(new ChatMessageEvent(new ChatMessage(message, Color.WHITE)));
                        }
                    }
                }
            }catch (Exception e) {
                System.out.println(username + " has disconnected");
                return;
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

        Thread sendPackets = new Thread(() -> {
            while(true) {
                try {
                    if (!packetsToSend.isEmpty()) {
                        ByteBuffer a = packetsToSend.remove(0);

                        try {
                            dataOutputStream.write(a.array());
                        } catch (IOException e) {
                            disconnect();
                            return;
                        }
                    }

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {}
            }
        });
        sendPackets.start();
    }

    public void giveByteBuffer(ByteBuffer byteBuffer) {
        packetsToSend.add(byteBuffer);
    }

    public void disconnect() {
        System.out.println(username + " has disconnected");

        ByteBuffer byteBuffer = ByteBuffer.allocate(username.length() + 2);
        byteBuffer.put((byte) 0x03);
        byteBuffer.put((byte) username.length());
        byteBuffer.put(username.getBytes());
        worldServer.broadcastPacket(byteBuffer);

        worldServer.getClientConnectionList().remove(this);
    }

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }
}
