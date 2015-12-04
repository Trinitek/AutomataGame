package com.daexsys.automata.gui;

import com.daexsys.automata.Game;
import com.daexsys.automata.event.tile.TileAlterCause;
import com.daexsys.automata.gui.chat.ChatMessage;
import com.daexsys.automata.gui.chat.ChatRenderer;
import com.daexsys.automata.gui.listeners.KeyboardHandler;
import com.daexsys.automata.gui.listeners.MouseHandler;
import com.daexsys.automata.gui.listeners.MouseMotionHandler;
import com.daexsys.automata.gui.listeners.ScrollManager;
import com.daexsys.automata.gui.util.ImageUtil;
import com.daexsys.automata.world.World;
import com.daexsys.automata.world.WorldLayer;
import com.daexsys.automata.world.structures.Structure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public class GUI {

    private MouseHandler mouseHandler = new MouseHandler(this);
    private MouseMotionHandler mouseMotionHandler = new MouseMotionHandler(this);
    private ScrollManager scrollManager = new ScrollManager(this);
    private KeyboardHandler keyboardHandler = new KeyboardHandler(this);

    private Offsets offsets = new Offsets(this);
    private int zoomLevel = 21;

    private Game game;
    private BufferedImage paused = ImageUtil.loadImage("images/paused.png");

    public int tps;

    private Set<String> playersOnline = new HashSet<>();

    private Dimension windowSize;

    private long lastFPSTime = System.currentTimeMillis();
    private int lastFPS = 0;
    private int fps = 0;

    public ChatRenderer chatRenderer = new ChatRenderer(this);

    public int layerBuildingOn = WorldLayer.GROUND;

    public GUI(Game game) {
        this.game = game;
    }

    private JFrame jFrame;

    public void spawnWindow() {
        jFrame = new JFrame(System.getProperty("game-name"));
        windowSize = new Dimension(1600, 900);
        jFrame.setSize(windowSize);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

        jFrame.addMouseListener(mouseHandler);
        jFrame.addMouseMotionListener(mouseMotionHandler);
        jFrame.addMouseWheelListener(scrollManager);

        jFrame.addKeyListener(keyboardHandler);

        jFrame.createBufferStrategy(3);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final GUI theGUI = this;

        lastFPSTime = System.currentTimeMillis();
        Thread renderThread = new Thread(() -> {
            WorldRenderer worldRenderer = new WorldRenderer(theGUI);

            Font theFont = new Font("Tahoma", Font.BOLD, 24);

            while(true) {
                long frameStartTime = System.currentTimeMillis();

                Graphics2D graphics2D = (Graphics2D) jFrame.getBufferStrategy().getDrawGraphics();

                graphics2D.setColor(Color.BLACK);
                graphics2D.fillRect(0, 0, 1920, 1080);

                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                worldRenderer.render(graphics2D);

                graphics2D.drawImage (game.getPlayerState().getInHand().getImage(),
                        mouseMotionHandler.getX(), mouseMotionHandler.getY(), 20, 20, null);

                chatRenderer.render(graphics2D);

                if(game.isPaused()) {
                    graphics2D.drawImage(paused, 260, 40, null);
                }

                graphics2D.setFont(theFont);
                graphics2D.setColor(Color.WHITE);
                graphics2D.drawString("Tick# " + getGame().getWorld().getTicksPulsed(), 40, 80);
                graphics2D.drawString("Tick delay: " + getGame().getTickDelayRate() + "ms", 40, 120);
                graphics2D.drawString("Zoom level: " + getZoomLevel(), 40, 160);
                graphics2D.drawString("Active chunks: " + getGame().getWorld().getChunkManager().getChunks().size(), 40, 200);
                graphics2D.drawString("FPS: " + getFPS(), 40, 240);

                if(getGame().getTickDelayRate() != 0) {
                    boolean goodTPS = false;
                    if (getGame().getTPS() > 1000 / getGame().getTickDelayRate() - 1) {
                        goodTPS = true;
                    }
                    if (goodTPS) {
                        graphics2D.setColor(Color.GREEN);
                    } else {
                        graphics2D.setColor(Color.RED);
                    }
                }

                graphics2D.drawString("TPS: " + (getGame().getTPS() == 0 ? tps : game.getTPS()), 40, 280);

                graphics2D.setColor(Color.WHITE);
                graphics2D.drawString("Item in-hand: " + getGame().getPlayerState().getInHand().getName(), 40, 320);
                graphics2D.drawString("X: " + getOffsets().getOffsetX() * -1, 40, 360);
                graphics2D.drawString("Y: " + getOffsets().getOffsetY() * -1, 40, 400);

                graphics2D.drawString("Cursor-Tile-X: " + getMouseMotionHandler().getTileX(), 40, 440);
                graphics2D.drawString("Cursor-Tile-Y: " + getMouseMotionHandler().getTileY(), 40, 480);

                Dimension windowSize1 = getWindowSize();
                graphics2D.setFont(new Font("Tahoma", Font.BOLD, 14));
                if(!playersOnline.isEmpty()) {
                    graphics2D.drawString("Players online: ", (int) windowSize1.getWidth() - 200, 80);

                    int i = 1;
                    for(String s : playersOnline) {
                        graphics2D.drawString(s, (int) windowSize1.getWidth() - 200, 80 + (i * 30));
                        i++;
                    }
                }

                jFrame.getBufferStrategy().show();

                fps++;

                if(System.currentTimeMillis() > lastFPSTime + 1000) {
                    lastFPSTime = System.currentTimeMillis();
                    lastFPS = fps;
                    fps = 0;
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long delta = System.currentTimeMillis() - frameStartTime;

                if(!getChatRenderer().isTyping()) {
                    if (KeyboardHandler.isDown(KeyEvent.VK_W)) {
                        getOffsets().moveUp(delta);
                    }
                    if (KeyboardHandler.isDown(KeyEvent.VK_S)) {
                        getOffsets().moveDown(delta);
                    }
                    if (KeyboardHandler.isDown(KeyEvent.VK_A)) {
                        getOffsets().moveLeft(delta);
                    }
                    if (KeyboardHandler.isDown(KeyEvent.VK_D)) {
                        getOffsets().moveRight(delta);
                    }
                }

                int newValue = scrollManager.retrieveScrollQueue() + getZoomLevel();
                if(newValue < 4)
                    newValue = 4;
                else if(newValue > 21)
                    newValue = 21;
                setZoomLevel(newValue);
            }
        });
        renderThread.start();
    }

    public KeyboardHandler getKeyboardHandler() {
        return keyboardHandler;
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

    public Dimension getWindowSize() {
        return jFrame.getSize();
    }

    public void playerPlaceTile(int screenX, int screenY, GUI gui) {
        Structure playerStructure = getGame().getPlayerState().getSelectedStructure();
        World worldModel = gui.getGame().getWorld();

        int tx = (screenX - gui.getOffsets().getOffsetX()) / gui.getZoomLevel();
        int ty = (screenY - gui.getOffsets().getOffsetY()) / gui.getZoomLevel();

        if(playerStructure == null) {
            worldModel.setTileTypeAt(layerBuildingOn, tx, ty, gui.getGame().getPlayerState().getInHand(), TileAlterCause.PLAYER_EDIT);
        } else {
            playerStructure.placeInWorldAt(worldModel, tx, ty);
        }
    }

    public ChatRenderer getChatRenderer() {
        return chatRenderer;
    }

    public MouseMotionHandler getMouseMotionHandler() {
        return mouseMotionHandler;
    }

    public int getFPS() {
        return lastFPS;
    }

    public void setPlayerOnline(String player, boolean state) {
        if(state) {
            playersOnline.add(player);
        } else {
            playersOnline.remove(player);
        }
    }
}
