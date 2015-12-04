package com.daexsys.automata.worldclient;

import com.daexsys.automata.Game;
import com.daexsys.automata.event.chat.ChatMessageEvent;
import com.daexsys.automata.event.chat.ChatMessageListener;
import com.daexsys.automata.event.tile.TileAlterCause;
import com.daexsys.automata.event.tile.TileAlterEvent;
import com.daexsys.automata.event.tile.TileAlterListener;
import com.daexsys.automata.gui.GUI;
import com.daexsys.automata.gui.chat.ChatMessage;
import com.daexsys.automata.world.Chunk;
import com.daexsys.automata.world.tiletypes.TileType;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class WorldClient {

    private Game game;

    public static void main(String[] args) {
        new WorldClient();
    }

    public WorldClient() {
        game = new Game(true);

        GUI gameGUI = new GUI(game);
        gameGUI.spawnWindow();

        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socket = new Socket("localhost", 33433);
                        InputStream inputStream = socket.getInputStream();
                        DataInputStream dataInputStream = new DataInputStream(inputStream);
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                        String playerName = "MangoViolin";
                        socket.getOutputStream().write(0x00);
                        socket.getOutputStream().write(playerName.length());
                        dataOutputStream.writeBytes(playerName);

                        game.addListener((TileAlterListener) tileAlterEvent -> {
                            if(tileAlterEvent.getTileAlterCause() == TileAlterCause.PLAYER_EDIT) {
                                try {
                                    socket.getOutputStream().write(0x04);
                                    dataOutputStream.writeInt(tileAlterEvent.getTile().getCoordinate().x);
                                    dataOutputStream.writeInt(tileAlterEvent.getTile().getCoordinate().y);
                                    socket.getOutputStream().write(tileAlterEvent.getTile().getType().getID());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        game.addListener((ChatMessageListener) chatMessageEvent -> {
                            try {
                                String s = chatMessageEvent.getChatMessage().getMessage();
                                socket.getOutputStream().write(0x06);
                                socket.getOutputStream().write(s.length());
                                dataOutputStream.writeBytes(s);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        while (true) {
                            byte packetID = (byte) inputStream.read();


                            // Chunk load packet
                            if(packetID == 0x04) {
                                int x = dataInputStream.readInt();
                                int y = dataInputStream.readInt();
                                byte b = (byte) inputStream.read();

                                game.getWorld().setTileTypeAt(0, x, y, TileType.getTileFromId(b));
                            }

                            if (packetID == 0x05) {
                                int x = dataInputStream.readInt();
                                int y = dataInputStream.readInt();

                                Chunk chunk = new Chunk(game.getWorld(), x, y);

                                for (int i = 0; i < 16; i++) {
                                    for (int j = 0; j < 16; j++) {
                                        byte tileID = (byte) inputStream.read();
                                        TileType tileType = TileType.getTileFromId(tileID);
                                        chunk.flashWithNewType(0, i, j, tileType, TileAlterCause.GENERATION);
                                    }
                                }

                                System.out.println("{" + x + ", " + y + "}");
                                System.out.println(chunk.toString());

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
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });
            thread.start();

        } catch (Exception e){}
    }
}
