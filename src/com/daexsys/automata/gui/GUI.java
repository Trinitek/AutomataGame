package com.daexsys.automata.gui;

import com.daexsys.automata.Game;
import com.daexsys.automata.gui.listeners.KeyboardHandler;
import com.daexsys.automata.gui.listeners.MouseHandler;
import com.daexsys.automata.gui.listeners.MouseMotionHandler;
import com.daexsys.automata.world.WorldModel;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private MouseHandler mouseHandler = new MouseHandler(this);
    private MouseMotionHandler mouseMotionHandler = new MouseMotionHandler(this);
    private Game game;

    public GUI(Game game) {
        this.game = game;
    }

    public void spawnWindow() {

        final JFrame jFrame = new JFrame(System.getProperty("game-name"));
        jFrame.setSize(800, 600);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

        jFrame.addMouseListener(mouseHandler);
        jFrame.addMouseMotionListener(mouseMotionHandler);

        jFrame.addKeyListener(new KeyboardHandler(this));

        jFrame.createBufferStrategy(2);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Thread renderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                WorldModel worldModel = game.getWorldModel();
                WorldRenderer worldRenderer = new WorldRenderer(worldModel);

                while(true) {
                    Graphics2D graphics2D = (Graphics2D) jFrame.getBufferStrategy().getDrawGraphics();

                    graphics2D.setColor(Color.BLACK);
                    graphics2D.fillRect(0, 0, 800, 600);

                    worldRenderer.render(graphics2D);

                    graphics2D.drawImage(game.getPlayerState().getInHand().getImage(),
                            mouseMotionHandler.getX(), mouseMotionHandler.getY(), 20, 20, null);

                    jFrame.getBufferStrategy().show();

                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        renderThread.start();
    }

    public Game getGame() {
        return game;
    }

    public boolean isMouseDown() {
        return mouseHandler.isMouseDown();
    }
}
