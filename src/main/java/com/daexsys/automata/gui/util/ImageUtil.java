package com.daexsys.automata.gui.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    public static BufferedImage loadImage(String location) {
        try {
            return ImageIO.read(new File(location));
        } catch (IOException e) {
            System.err.println("Can't find image file '" + location + "'");
        }

        return null;
    }
}
