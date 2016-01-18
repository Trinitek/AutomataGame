package com.daexsys.automata.gui;

import com.daexsys.automata.Game;
import com.daexsys.automata.Tile;
import com.daexsys.automata.event.tile.TilePlacementReason;
import com.daexsys.automata.gui.chat.ChatRenderer;
import com.daexsys.automata.gui.listeners.KeyboardHandler;
import com.daexsys.automata.gui.listeners.MouseHandler;
import com.daexsys.automata.gui.listeners.MouseMotionHandler;
import com.daexsys.automata.gui.listeners.ScrollManager;
import com.daexsys.automata.gui.util.ImageUtil;
import com.daexsys.automata.world.World;
import com.daexsys.automata.world.WorldLayer;
import com.daexsys.automata.world.structures.Structure;

import static org.lwjgl.glfw.GLFW.*;

import com.daexsys.automata.world.tiletypes.TileType;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Hashtable;
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

    /////
    private GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
    private long window;
    /////

    public static void main(String args[]) {
        System.setProperty("game-name", "Automata");
        System.setProperty("org.lwjgl.librarypath", "natives");

        // Initialize GLFW
        GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
        long window;
        glfwSetErrorCallback(errorCallback);
        if (glfwInit() != GLFW_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Build window
        window = glfwCreateWindow(640, 480, System.getProperty("game-name"), 0, 0);
        if (window == 0) {
            glfwTerminate();
            throw new RuntimeException("Unable to create GLFW window");
        }

        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        // Create OpenGL context
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        // Setup projection matrix
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity(); // reset previous projection matrices
        glOrtho(0, 640, 480, 0, 1, -1);

        // Create texture
        /*BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
        Graphics2D image2d = image.createGraphics();
        image2d.drawImage(TileType.DIRT.getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT), 0, 0, null);
        image2d.dispose();

        ByteBuffer imageBuffer;
        WritableRaster raster;
        BufferedImage texImage;
        raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, 64, 64, 3, null);
        texImage = new BufferedImage(
                new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                    new int[] {8, 8, 8, 0},
                    false,
                    false,
                    ComponentColorModel.OPAQUE,
                    DataBuffer.TYPE_BYTE),
                raster,
                false,
                new Hashtable<>()
        );
        Graphics g = texImage.getGraphics();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, 64, 64);
        g.drawImage(image, 0, 0, null);
        byte[] textureData = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();
        imageBuffer = ByteBuffer.allocateDirect(textureData.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(textureData, 0, textureData.length);
        imageBuffer.flip();

        IntBuffer textureIDBuffer = BufferUtils.createIntBuffer(1);
        glGenTextures(textureIDBuffer);
        int textureID = textureIDBuffer.get(0);
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, 64, 64, 0, GL_RGB, GL_UNSIGNED_BYTE, imageBuffer);
        glBindTexture(GL_TEXTURE_2D, textureID);
*/
        /////
        /*glEnable(GL_TEXTURE_2D);
        BufferedImage i = TextureLoader.sizeImage(TileType.DIRT.getImage());
        Texture t = TextureLoader.generateTexture(i);
        t.bind();
        t.load();
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);*/
        /////

        /*glEnable(GL_TEXTURE_2D);
        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        byte[] data = {
                (byte) 255, 0, 0,
                (byte) 255, 0, 0,
                (byte) 255, 0, 0,
                (byte) 255, 0, 0
        };
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
        buffer.order(ByteOrder.nativeOrder());
        buffer.put(data, 0, data.length);
        buffer.flip();
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, 4, 4, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);*/

        glEnable(GL_TEXTURE_2D);

        /*
        PNGDecoder decoder;
        ByteBuffer buffer;
        try {
            //String url = TileType.DIRT.getImageURL();
            String url = "images/red_cgol.png";
            System.out.println("Using " + url);
            InputStream in = new FileInputStream(url);
            decoder = new PNGDecoder(in);
            buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            in.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return;
        }

        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        */

        Texture texture;
        try {
            //texture = new Texture("images/red_cgol.png");
            texture = new Texture("");
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return;
        }
        texture.initialize();
        texture.bind();

        // Render
        while (glfwWindowShouldClose(window) == GLFW_FALSE) {
            glClear(GL_COLOR_BUFFER_BIT);
            glBegin(GL_TRIANGLES); // start rendering triangles

            glTexCoord2f(0, 1); // top left of texture
            glVertex2i(0, 0);   // top left of triangle
            glTexCoord2f(0, 0); // bottom left of texture
            glVertex2i(128, 0); // bottom left of triangle
            glTexCoord2f(1, 1); // top right of texture
            glVertex2i(0, 128); // top right of triangle

            glTexCoord2f(1, 1); // top right of texture
            glVertex2i(0, 128); // top right of triangle
            glTexCoord2f(0, 0); // bottom left of texture
            glVertex2i(128, 0); // bottom left of triangle
            glTexCoord2f(1, 0); // bottom right of texture
            glVertex2i(128, 128); // bottom right of triangle

            glEnd();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public void spawnWindow() {

        jFrame = new JFrame(System.getProperty("game-name"));
        windowSize = new Dimension(640, 480);
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

                //int time = getGame().getTime();
                //graphics2D.setColor(new Color(0, 0, 0, time));

                graphics2D.setColor(new Color(0, 0, 0, 0));

                graphics2D.fillRect(0, 0, (int) getWindowSize().getWidth(), (int) getWindowSize().getHeight());

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
            worldModel.setTileTypeAt(layerBuildingOn, tx, ty, gui.getGame().getPlayerState().getInHand(), TilePlacementReason.PLAYER_EDIT);
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
