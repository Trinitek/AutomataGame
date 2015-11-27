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
            e.printStackTrace();
        }

        return null;
    }
}
