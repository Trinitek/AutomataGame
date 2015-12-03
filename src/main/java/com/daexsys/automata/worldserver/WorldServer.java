package com.daexsys.automata.worldserver;

import com.daexsys.automata.Game;
import com.daexsys.automata.Tile;
import com.daexsys.automata.world.Chunk;
import com.sun.corba.se.spi.orbutil.fsm.Input;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

public class WorldServer {

    private Game game;

    public WorldServer() {
        game = new Game();

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                game.getWorld().getChunkManager().getChunk(i, j);
            }
        }
    }

    public void bindAndStart() {

        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ServerSocket serverSocket = new ServerSocket(33433);
                        System.out.println("Started server");

                        Socket socket = serverSocket.accept();
                        System.out.println("Someone connected");

                        InputStream inputStream = socket.getInputStream();
                        OutputStream outputStream = socket.getOutputStream();
                        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                        Collection<Chunk> chunks = game.getWorld().getChunkManager().getChunks();

                        for (Chunk chunk : chunks) {
                            outputStream.write(0x05);
                            dataOutputStream.writeInt(chunk.getChunkCoordinate().x);
                            dataOutputStream.writeInt(chunk.getChunkCoordinate().y);

                            for (int i = 0; i < 16; i++) {
                                for (int j = 0; j < 16; j++) {
                                    Tile t = chunk.getTile(0, i, j);
                                    outputStream.write(t.getType().getID());
                                }
                            }

                            System.out.println("Sent chunk");
                        }
                    } catch (Exception e) {

                    }
                }
            });
            thread.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new WorldServer().bindAndStart();
    }
}
