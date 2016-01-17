package com.daexsys.automata.gui;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Hashtable;

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
     * @param sourceImage image from which to generate a new texture
     * @return the new texture
     */
    public static Texture generateTexture(BufferedImage sourceImage) {
        ByteBuffer buffer;
        WritableRaster raster;
        BufferedImage image;

        raster = Raster.createInterleavedRaster(
                DataBuffer.TYPE_BYTE,
                sourceImage.getWidth(),
                sourceImage.getHeight(),
                3,
                null
        );

        image = new BufferedImage(glColorModel, raster, false, new Hashtable<>());

        Graphics g = image.getGraphics();
        g.drawImage(sourceImage, 0, 0, null);

        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        buffer = ByteBuffer.allocateDirect(data.length);
        buffer.order(ByteOrder.nativeOrder());
        buffer.put(data, 0, data.length);
        buffer.flip();

        return new Texture(newID(), image, buffer);
    }
}
