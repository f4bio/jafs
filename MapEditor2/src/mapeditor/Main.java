/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeditor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;


/**
 *
 * @author Sezer Güler
 */
public class Main extends JFrame implements ActionListener {

    static JFrame frame;
    static BufferedImage image;
    static ShowImage panelImage;
    static MouseListener ms;
    static MouseListener msR;
    static ShowImage[][] p, pR;
    static String img = "";
    /**
     * @param args the command line arguments
     */
    public static void generateIni() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream(" outputfile. ini"));

        } catch (IOException e) {
            System.out.println(" Error in opening output file");
            System.exit(1);
        }

        for (int i = 0; i < p.length; i++) {
           for(int j = 0; j < p[i].length; j++){
            if (p[i][j].getImage().equals("tile.jpg")) {
                writer.println("[Tile " + i + " " + j + "]");
                writer.println("typeId = 0");
                writer.println("textureId = 0");
                writer.println("itemId = 0");
            } else if (p[i][j].getImage().equals("kp.jpg")) {
                writer.println("[Tile " + i  + " " + j + "]");
                writer.println("typeId = 0");
                writer.println("textureId = 2");
                writer.println("itemId = 0");
            } else if (p[i][j].getImage().equals("stone.png")) {
                writer.println("[Tile " + i  + " " + j + "]");
                writer.println("typeId = 1");
                writer.println("textureId = 1");
                writer.println("itemId = 0");
            } else if (p[i][j].getImage().equals("grass.jpg")) {
                writer.println("[Tile " + i  + " " + j + "]");
                writer.println("typeId = 0");
                writer.println("textureId = 3");
                writer.println("itemId = 0");
            } else if (p[i][j].getImage().equals("water.jpg")) {
                writer.println("[Tile " + i  + " " + j + "]");
                writer.println("typeId = 1");
                writer.println("textureId = 4");
                writer.println("itemId = 0");
            } else if (p[i][j].getImage().equals("bridge.jpg")) {
                writer.println("[Tile " + i  + " " + j + "]");
                writer.println("typeId = 0");
                writer.println("textureId = 7");
                writer.println("itemId = 0");
            } else if (p[i][j].getImage().equals("red.png")) {
                writer.println("[Tile " + i  + " " + j + "]");
                writer.println("typeId = 0");
                writer.println("textureId = 5");
                writer.println("itemId = 0");
            }  else if (p[i][j].getImage().equals("blue.png")) {
                writer.println("[Tile " + i  + " " + j + "]");
                writer.println("typeId = 0");
                writer.println("textureId = 6");
                writer.println("itemId = 0");
            }

            writer.println("");
            
           }

        }
        writer.close();
    }

    public static void main(String[] args) {
       
        frame = new JFrame("MapEditor");
        JPanel panel = new JPanel(null);
        JPanel panelLeft = new JPanel(new GridLayout(16, 26));
        panelLeft.setBounds(0, 0, 1300, 850);

        JPanel panelRight = new JPanel(new GridLayout(4, 2));
        panelRight.setBounds(1300, 0, 300, 850);
        msR = new MouseListener(){

            public void mouseClicked(MouseEvent e) {
                img = ((ShowImage) e.getSource()).getImage();

            }

            public void mousePressed(MouseEvent e) {
               // throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseReleased(MouseEvent e) {
               // throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseEntered(MouseEvent e) {
               // throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseExited(MouseEvent e) {
               // throw new UnsupportedOperationException("Not supported yet.");
            }

        };
        ms = new MouseListener() {
        String h = "stone.png";
        boolean b = false;
            public void mouseClicked(MouseEvent e) {
                b = true;
            }

            public void mousePressed(MouseEvent e) {
                //  throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseReleased(MouseEvent e) {
                //  throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseEntered(MouseEvent e) {
                b = false;              
                h = (((ShowImage) e.getSource()).getImage());
                p[((ShowImage) e.getSource()).getX()][((ShowImage) e.getSource()).getY()].setImage(img);
            }

            public void mouseExited(MouseEvent e) {
                //  throw new UnsupportedOperationException("Not supported yet.");
                if(!b){
                     ((ShowImage) e.getSource()).setImage(h);
                }

              
            }
        };

        pR = new ShowImage[8][1];
        for (int i = 0; i < pR.length; i++){
            for(int j = 0; j < 1; j++){
                pR[i][j] = new ShowImage();
                pR[i][j].addMouseListener(msR);
                panelRight.add(pR[i][j]);
            }
        }

        pR[0][0].setImage("stone.png");
        pR[1][0].setImage("tile.jpg");
        pR[2][0].setImage("kp.jpg");
        pR[3][0].setImage("grass.jpg");
        pR[4][0].setImage("water.jpg");
        pR[5][0].setImage("bridge.jpg");
        pR[6][0].setImage("red.png");
        pR[7][0].setImage("blue.png");

        JButton button = new JButton("Save Map");
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                generateIni();
            }
        });
        panelRight.add(button);
        
        p = new ShowImage[16][26];
        for (int i = 0; i < 16; i++) {
            for(int j = 0; j < 26; j++){
                p[i][j] = new ShowImage();
                p[i][j].setImage("kp.jpg");
                p[i][j].addMouseListener(ms);
                p[i][j].setX(i);
                p[i][j].setY(j);
                panelLeft.add(p[i][j]);
            }
        }


        panel.add(panelLeft);
        panel.add(panelRight);
        frame.add(panel);

        frame.setSize(1600, 850);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
