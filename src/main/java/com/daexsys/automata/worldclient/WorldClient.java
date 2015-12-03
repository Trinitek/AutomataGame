package com.daexsys.automata.worldclient;

import com.daexsys.automata.Game;
import com.daexsys.automata.gui.GUI;
import com.daexsys.automata.world.Chunk;
import com.daexsys.automata.world.tiletypes.TileType;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

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

                        while (true) {
                            byte packetID = (byte) inputStream.read();

                            System.out.println(packetID);


                            // Chunk load packet

                            if(packetID == 0x04) {
                                System.out.println("Block placement");
                                int x = dataInputStream.readInt();
                                int y = dataInputStream.readInt();
                                byte b = (byte) inputStream.read();

                                game.getWorld().setTileTypeAt(0, x, y, TileType.getTileFromId(b));
                            }

                            if (packetID == 0x05) {
                                int x = dataInputStream.readInt();
                                int y = dataInputStream.readInt();

                                Chunk chunk = new Chunk(game.getWorld(), x, y);

                                System.out.println("Chunk");

                                for (int i = 0; i < 16; i++) {
                                    for (int j = 0; j < 16; j++) {
                                        byte tileID = (byte) inputStream.read();
                                        TileType tileType = TileType.getTileFromId(tileID);
                                        chunk.flashWithNewType(0, i, j, tileType);
                                    }
                                }

                                System.out.println("{" + x + ", " + y + "}");
                                System.out.println(chunk.toString());

                                game.getWorld().getChunkManager().putChunk(x, y, chunk);
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
