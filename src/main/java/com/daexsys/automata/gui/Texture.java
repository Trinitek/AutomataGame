package com.daexsys.automata.gui;

import de.matthiasmann.twl.utils.PNGDecoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    private int textureId;
    private ByteBuffer imageData;
    private int width;
    private int height;
    private static BufferedImage defaultImage;
    private boolean usesDefault;

    static {
        // The default texture image placeholder is a magenta and blue checkerboard.
        defaultImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        defaultImage.setRGB(0, 0, Color.BLUE.getRGB());
        defaultImage.setRGB(1, 1, Color.BLUE.getRGB());
        defaultImage.setRGB(1, 0, Color.MAGENTA.getRGB());
        defaultImage.setRGB(0, 1, Color.MAGENTA.getRGB());
    }

    /**
     * Create an uninitialized Texture object
     * @param imageURL    the filesystem path of the PNG image to load
     * @throws IOException if accessing the InputStream fails
     */
    public Texture(String imageURL) throws IOException {
        InputStream image;
        BufferedImage bufferedImage;
        try {
            // Try to read the image from the given filepath as usual.
            image = new FileInputStream(imageURL);
            bufferedImage = ImageIO.read(image);
            image.close();
            image = new FileInputStream(imageURL);

            // Mark that this object does not use the default texture.
            this.usesDefault = false;
        } catch (IOException e) {
            // If there is a problem reading from the file, then use a placeholder instead.
            System.err.println("Error loading texture '" + imageURL + "'; using default placeholder instead.");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(defaultImage, "png", outputStream);

            // Instead of writing the PNG stream to a file, use a byte array.
            bufferedImage = ImageIO.read(new ByteArrayInputStream(outputStream.toByteArray()));
            image = new ByteArrayInputStream(outputStream.toByteArray());

            // Mark that this object does use the default texture.
            this.usesDefault = true;
        }

        // Convert PNG file to a usable buffer of bytes for OpenGL to use
        PNGDecoder decoder = new PNGDecoder(image);
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buffer.flip();
        image.close();

        // Set some private fields
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
        this.imageData = buffer;
    }

    /**
     * Create an uninitialized Texture object
     * @param width        the width of the image used
     * @param height       the height of the image used
     * @param imageData    the raw image data
     */
    public Texture(int width, int height, ByteBuffer imageData) {
        this.width = width;
        this.height = height;
        this.imageData = imageData;
        this.usesDefault = false;
    }

    /**
     * Initializes the texture for use by OpenGL
     * @throws NullPointerException if the image data is null
     */
    public void initialize() throws NullPointerException {
        if (this.imageData == null) {
            throw new NullPointerException("Texture image data is null!");
        }
        this.textureId = glGenTextures();
        this.bind();
        this.load();
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        // Wrap mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // Don't fuzz the texture if it's using the default
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, this.usesDefault ? GL_NEAREST : GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, this.usesDefault ? GL_NEAREST : GL_LINEAR);
    }

    /**
     * Sets the current working OpenGL texture to this
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.textureId);
    }

    /**
     * Loads the image data of this texture into video memory
     */
    public void load() {
        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                this.width,
                this.height,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                this.imageData
        );
    }

    /**
     * Unloads the texture from video memory. The texture must be reinitialized in order to be used again.
     */
    public void unload() {
        glDeleteTextures(this.textureId);
    }

    /**
     * Get the image data used by this texture
     * @return image data
     */
    public ByteBuffer getData() {
        return this.imageData;
    }

    /**
     * Get the width of the image
     * @return width
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Get the height of the image
     * @return height
     */
    public int getHeight() {
        return this.height;
    }
}
