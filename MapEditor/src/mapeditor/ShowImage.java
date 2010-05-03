/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package mapeditor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class ShowImage extends Panel {

    BufferedImage image;
    String imageName;
    int position;

    public void setImage(String s) {
        try {
            imageName = s;
            File input = new File(imageName);
            image = ImageIO.read(input);
        } catch (IOException ie) {
            System.out.println("Error:" + ie.getMessage());
        }
    }

    public String getImage(){
        return imageName;
    }

    public void setPosition(int i){
        position = i;
    }

    public int getPosition(){
        return position;
    }

    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

}
