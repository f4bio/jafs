/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.resource;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author miracle
 */
public class CImage {
    public static final Color transparency = new Color(255, 0, 255);

    private BufferedImage image;
    private String path;
    private CArchive archive;
    private byte[] data;

    public CImage(CArchive archive, String path) {
        this.data = null;
        this.image = null;
        this.archive = archive;
        this.path = path;
        init();
    }

    public void init() {
        data = archive.getFileData(path);

        if(data != null) {
            load(data);
        }
    }

    private GraphicsConfiguration getGC() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration();
    }

    public void load(byte[] data) {
        BufferedImage tmp = null;
        try {
             tmp = ImageIO.read(new ByteArrayInputStream(data));
        } catch(Exception e) {

        }

        if(tmp == null)
            return;

        image = new BufferedImage(tmp.getWidth(), tmp.getHeight(), BufferedImage.TYPE_INT_ARGB);

        int mask = transparency.getRGB() | 0xFF000000;

        for(int x=0; x<image.getWidth(); ++x) {
            for(int y=0; y<image.getHeight(); ++y) {
                int rgb = tmp.getRGB(x, y);

                if((rgb | 0xFF000000) == mask) {
                    image.setRGB(x, y, (rgb & 0x00FFFFFF));
                } else {
                    image.setRGB(x, y, rgb);
                }
            }
        }

        data = null;
        archive = null;
    }

    public BufferedImage getImage() {
        return image;
    }
}
