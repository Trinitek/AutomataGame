package com.daexsys.automata.gui.util;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    private int textureId;

    public Texture(int textureId) {
        this.textureId = textureId;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.textureId);
    }
}
