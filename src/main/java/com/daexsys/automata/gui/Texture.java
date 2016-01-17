package com.daexsys.automata.gui;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    private int textureId;
    private ByteBuffer buffer;
    private BufferedImage baseImage;

    public Texture(int textureId, BufferedImage baseImage, ByteBuffer data) {
        this.textureId = textureId;
        this.baseImage = baseImage;
        this.buffer = data;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.textureId);
    }

    public void load() {
        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                this.getWidth(),
                this.getHeight(),
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                this.getData());
    }

    public ByteBuffer getData() {
        return this.buffer;
    }

    public int getWidth() {
        return this.baseImage.getWidth();
    }

    public int getHeight() {
        return this.baseImage.getHeight();
    }
}
