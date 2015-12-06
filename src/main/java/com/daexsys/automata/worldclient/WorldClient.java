package com.daexsys.automata.worldclient;

import com.daexsys.automata.Game;
import com.daexsys.automata.event.chat.ChatMessageListener;
import com.daexsys.automata.event.tile.TilePlacementReason;
import com.daexsys.automata.event.tile.TileAlterListener;
import com.daexsys.automata.gui.GUI;
import com.daexsys.automata.gui.chat.ChatMessage;
import com.daexsys.automata.world.Chunk;
import com.daexsys.automata.world.structures.Structure;
import com.daexsys.automata.world.tiletypes.TileType;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class WorldClient {

    private Game game;

    /**
     * Gets IP address to connect to from args
     * @param args
     */
    public static void main(String[] args) {
        if(args.length == 0) {
//            new WorldClient(NameGenerator.getName(), "104.196.35.63");
            new WorldClient(NameGenerator.getName(), "127.0.0.1");
        } else {
            new WorldClient(args[0], args[1]);
        }
    }

    private List<ByteBuffer> toSend = new ArrayList<ByteBuffer>();
    public WorldClient(String username, String address) {
        game = new Game(true);

        GUI gameGUI = new GUI(game);
        gameGUI.spawnWindow();

        try {
            Thread thread = new Thread(() -> {
                try {
                    Socket socket = new Socket(address, 33433);
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    String playerName = username;
                    ByteBuffer byteBuffer = ByteBuffer.allocate(2 + playerName.length());
                    byteBuffer.put((byte) 0x00);
                    byteBuffer.put((byte) playerName.length());
                    byteBuffer.put(playerName.getBytes());
                    toSend.add(byteBuffer);

                    game.addListener((TileAlterListener) tileAlterEvent -> {
                        if(tileAlterEvent.getTileAlterCause() == TilePlacementReason.PLAYER_EDIT) {
                            ByteBuffer packetBuffer = ByteBuffer.allocate(10);
                            packetBuffer.put((byte) 0x04);
                            packetBuffer.putInt(tileAlterEvent.getTile().getCoordinate().x);
                            packetBuffer.putInt(tileAlterEvent.getTile().getCoordinate().y);
                            packetBuffer.put(tileAlterEvent.getTile().getType().getID());
                            toSend.add(packetBuffer);
                        }
                    });

                    game.addListener((ChatMessageListener) chatMessageEvent -> {
                        String s = chatMessageEvent.getChatMessage().getMessage();
                        ByteBuffer packetBuffer = ByteBuffer.allocate(2 + s.length());
                        packetBuffer.put((byte) 0x06);
                        packetBuffer.put((byte) s.length());
                        packetBuffer.put(s.getBytes());
                        toSend.add(packetBuffer);
                    });

                    Thread sendThread = new Thread(() -> {
                        while(true) {
                            if(!toSend.isEmpty()) {
                                ByteBuffer packetBuffer = toSend.remove(0);
                                try {
                                    dataOutputStream.write(packetBuffer.array());
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
                    sendThread.start();

                    while (true) {
                        byte packetID = (byte) inputStream.read();

                        if(packetID == 0x01) {
                            short tps = dataInputStream.readShort();
                            System.out.println("TPS: " + tps);
                            gameGUI.tps = tps;
                        }

                        if(packetID == 0x02) {
                            byte length = dataInputStream.readByte();

                            byte[] str = new byte[length];
                            for (int i = 0; i < length; i++) {
                                str[i] = dataInputStream.readByte();
                            }
                            String theString = new String(str);
                            gameGUI.setPlayerOnline(theString, true);
                        }

                        if(packetID == 0x03) {
                            byte length = dataInputStream.readByte();

                            byte[] str = new byte[length];
                            for (int i = 0; i < length; i++) {
                                str[i] = dataInputStream.readByte();
                            }
                            String theString = new String(str);
                            gameGUI.setPlayerOnline(theString, false);
                        }

                        if(packetID == 0x04) {
                            int x = dataInputStream.readShort();
                            int y = dataInputStream.readShort();
                            byte b = (byte) inputStream.read();

                            game.getWorld().setTileTypeAt(0, x, y, TileType.getTileFromId(b));
                        }

                        // Chunk load packet
                        if (packetID == 0x05) {
                            int x = dataInputStream.readInt();
                            int y = dataInputStream.readInt();

                            Chunk chunk = new Chunk(game.getWorld(), x, y);

                            for (int i = 0; i < 16; i++) {
                                for (int j = 0; j < 16; j++) {
                                    byte tileID = (byte) inputStream.read();
                                    TileType tileType = TileType.getTileFromId(tileID);
                                    chunk.flashWithNewType(0, i, j, tileType, TilePlacementReason.GENERATION);
                                }
                            }

                            game.getWorld().getChunkManager().putChunk(x, y, chunk);
                        }

                        if(packetID == 0x06) {
                            byte length = dataInputStream.readByte();

                            byte[] str = new byte[length];
                            for (int i = 0; i < length; i++) {
                                str[i] = dataInputStream.readByte();
                            }
                            String theString = new String(str);
                            System.out.println(theString);
                            game.getChatManager().addChatMessage(new ChatMessage(theString, Color.WHITE));
                        }

                        if(packetID == 0x07) {
                            byte length = dataInputStream.readByte();

                            byte[] str = new byte[length];
                            for (int i = 0; i < length; i++) {
                                str[i] = dataInputStream.readByte();
                            }
                            String structureName = new String(str);

                            Structure theStructure = game.getStructures().getStructureByName(structureName);

                            int x = dataInputStream.readInt();
                            int y = dataInputStream.readInt();

                            byte argLength = dataInputStream.readByte();

                            int[] args = new int[argLength];
                            if(argLength > 0) {
                                for (int i = 0; i < argLength; i++) {
                                    args[i] = dataInputStream.readByte();
                                }
                            }

                            theStructure.placeInWorldAt(game.getWorld(), x, y, args);
                        }

                        if(packetID == 0x08) {
                            int x = dataInputStream.readShort();
                            int y = dataInputStream.readShort();
                            byte b = (byte) inputStream.read();

                            game.getWorld().setTileTypeAt(1, x, y, TileType.getTileFromId(b));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            });
            thread.start();

        } catch (Exception e){}
    }
}
