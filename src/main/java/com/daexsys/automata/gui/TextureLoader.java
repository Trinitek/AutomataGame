package com.daexsys.automata.gui;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class TextureLoader {
    private static ColorModel glColorModel =
        new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
            new int[] {8, 8, 8, 0},
            false,
            false,
            ComponentColorModel.OPAQUE,
            DataBuffer.TYPE_BYTE
        );

    private static IntBuffer idBuffer = BufferUtils.createIntBuffer(1);

    /**
     * Create a new texture ID
     * @return a new texture ID
     */
    private static int newID() {
        glGenTextures(idBuffer);
        return idBuffer.get(0);
    }

    /**
     * Resize a given image to be 64 x 64 pixels
     * @param image image to resize
     * @return resized image
     */
    public static BufferedImage sizeImage(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
        Graphics2D new2d = newImage.createGraphics();
        new2d.drawImage(image.getScaledInstance(64, 64, Image.SCALE_DEFAULT), 0, 0, null);
        new2d.dispose();
        return newImage;
    }

    /**
     * Generates a new texture from the given image for use in OpenGL.
     * The resolution must be a multiple of 2 in each dimension.
     * @param imageURL image from which to generate a new texture
     * @return the new texture
     */
    public static Texture generateTexture(String imageURL) {
        try {
            return generateTexture(new FileInputStream(imageURL));
        } catch (IOException e) {
            System.err.println("IOException when trying to load '" + imageURL + "' when creating texture!");
        }
        return null;
    }

    /**
     * Generates a new texture from the given image for use in OpenGL.
     * The resolution must be a multiple of 2 in each dimension.
     * @param image image from which to generate a new texture
     * @return the new texture
     */
    public static Texture generateTexture(InputStream image) throws IOException {
        PNGDecoder decoder = new PNGDecoder(image);
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        try {
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            image.close();
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace(System.err);
            return null;
        } catch (UnsupportedOperationException e) {
            System.err.println("The image '" + image.toString() + "' can't be decoded into RGBA for use as a texture!");
            return null;
        }
        return new Texture(decoder.getWidth(), decoder.getHeight(), buffer);
    }
}
