package com.daexsys.automata.gui;

import com.daexsys.automata.Game;
import com.daexsys.automata.gui.listeners.KeyboardHandler;
import com.daexsys.automata.gui.listeners.MouseHandler;
import com.daexsys.automata.gui.listeners.MouseMotionHandler;
import com.daexsys.automata.gui.listeners.ScrollManager;
import com.daexsys.automata.gui.util.ImageUtil;
import com.daexsys.automata.world.WorldModel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GUI {

    private MouseHandler mouseHandler = new MouseHandler(this);
    private MouseMotionHandler mouseMotionHandler = new MouseMotionHandler(this);
    private ScrollManager scrollManager = new ScrollManager(this);

    private Offsets offsets = new Offsets(this);
    private int zoomLevel = 40;

    private Game game;
    private BufferedImage paused = ImageUtil.loadImage("images/paused.png");

    public GUI(Game game) {
        this.game = game;
    }

    public void spawnWindow() {
        final JFrame jFrame = new JFrame(System.getProperty("game-name"));
        jFrame.setSize(1600, 900);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

        jFrame.addMouseListener(mouseHandler);
        jFrame.addMouseMotionListener(mouseMotionHandler);
        jFrame.addMouseWheelListener(scrollManager);

        jFrame.addKeyListener(new KeyboardHandler(this));

        jFrame.createBufferStrategy(2);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final GUI theGUI = this;

        Thread renderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                WorldRenderer worldRenderer = new WorldRenderer(theGUI);

                while(true) {
                    Graphics2D graphics2D = (Graphics2D) jFrame.getBufferStrategy().getDrawGraphics();

                    graphics2D.setColor(Color.BLACK);
                    graphics2D.fillRect(0, 0, 1920, 1080);

                    worldRenderer.render(graphics2D);

                    graphics2D.drawImage(game.getPlayerState().getInHand().getImage(),
                            mouseMotionHandler.getX(), mouseMotionHandler.getY(), 20, 20, null);

                    if(game.isPaused()) {
                        graphics2D.drawImage(paused, 40, 40, null);
                    }

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

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public Offsets getOffsets() {
        return offsets;
    }

    public boolean isMouseDown() {
        return mouseHandler.isMouseDown();
    }
}
