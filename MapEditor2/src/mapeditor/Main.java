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
    static int sizeX;
    static int sizeY;
    static int tileSizeX;
    static int tileSizeY;

    /**
     * @param args the command line arguments
     */
    public static void generateIni() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream("about.ini"));

        } catch (IOException e) {
            System.out.println(" Error in opening output file");
            System.exit(1);
        }
        writer.println("[Main]");
        writer.println("name = Test");
        writer.println("texture0 = textures/tile.jpg");
        writer.println("texture1 = textures/stone.png");
        writer.println("texture2 = textures/kp.jpg");
        writer.println("texture3 = textures/grass.jpg");
        writer.println("texture4 = textures/water.jpg");
        writer.println("texture5 = textures/red.png");
        writer.println("texture6 = textures/blue.png");
        writer.println("texture7 = textures/bridge.jpg");
        writer.println();

        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[i].length; j++) {
                if (p[i][j].getImage().equals("red.png")) {
                    writer.println("[SpaxnRed]");
                    writer.println("tileX = " + i);
                    writer.println("tileY = " + j);
                    writer.println();
                }

            }
        }

        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[i].length; j++) {
                if (p[i][j].getImage().equals("blue.png")) {
                    writer.println("[SpawnBlue]");
                    writer.println("tileX = " + i);
                    writer.println("tileY = " + j);
                    writer.println();
                }

            }
        }

        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[i].length; j++) {
                if (p[i][j].getImage().equals("tile.jpg")) {
                    writer.println("[Tile " + i + " " + j + "]");
                    writer.println("typeId = 0");
                    writer.println("textureId = 0");
                    writer.println("itemId = 0");
                } else if (p[i][j].getImage().equals("kp.jpg")) {
                    writer.println("[Tile " + i + " " + j + "]");
                    writer.println("typeId = 0");
                    writer.println("textureId = 2");
                    writer.println("itemId = 0");
                } else if (p[i][j].getImage().equals("stone.png")) {
                    writer.println("[Tile " + i + " " + j + "]");
                    writer.println("typeId = 1");
                    writer.println("textureId = 1");
                    writer.println("itemId = 0");
                } else if (p[i][j].getImage().equals("grass.jpg")) {
                    writer.println("[Tile " + i + " " + j + "]");
                    writer.println("typeId = 0");
                    writer.println("textureId = 3");
                    writer.println("itemId = 0");
                } else if (p[i][j].getImage().equals("water.jpg")) {
                    writer.println("[Tile " + i + " " + j + "]");
                    writer.println("typeId = 1");
                    writer.println("textureId = 4");
                    writer.println("itemId = 0");
                } else if (p[i][j].getImage().equals("bridge.jpg")) {
                    writer.println("[Tile " + i + " " + j + "]");
                    writer.println("typeId = 0");
                    writer.println("textureId = 7");
                    writer.println("itemId = 0");
                } else if (p[i][j].getImage().equals("red.png")) {
                    writer.println("[Tile " + i + " " + j + "]");
                    writer.println("typeId = 0");
                    writer.println("textureId = 5");
                    writer.println("itemId = 0");
                } else if (p[i][j].getImage().equals("blue.png")) {
                    writer.println("[Tile " + i + " " + j + "]");
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
        if (args.length == 0) {
            setSizeX(16);
            setSizeY(26);
            setTileSizeX(150);
            setTileSizeY(150);
        } else if (args.length == 2) {
            setSizeX(Integer.parseInt(args[0]));
            setSizeY(Integer.parseInt(args[1]));
        } else if (args.length == 4) {
            setSizeX(Integer.parseInt(args[0]));
            setSizeY(Integer.parseInt(args[1]));
            setTileSizeX(Integer.parseInt(args[2]));
            setTileSizeY(Integer.parseInt(args[3]));
        } else {
            System.out.println("Argument error!");
            System.exit(0);
        }
        frame = new JFrame("MapEditor");
        JPanel panel = new JPanel(null);
        JPanel panelLeft = new JPanel(new GridLayout(sizeX, sizeY));
        panelLeft.setBounds(0, 0, 1300, 850);

        JPanel panelRight = new JPanel(new GridLayout(4, 2));
        panelRight.setBounds(1300, 0, 300, 850);
        msR = new MouseListener() {

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

            String h = "";
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
                if (!b) {
                    ((ShowImage) e.getSource()).setImage(h);
                }


            }
        };



        pR = new ShowImage[8][1];
        for (int i = 0; i < pR.length; i++) {
            for (int j = 0; j < 1; j++) {
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
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                generateIni();
            }
        });
        panelRight.add(button);

        p = new ShowImage[sizeX][sizeY];
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[i].length; j++) {
                p[i][j] = new ShowImage();
                p[i][j].setImage("black.jpg");
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

    public static void setSizeX(int x) {
        sizeX = x;
    }

    public static void setSizeY(int y) {
        sizeY = y;
    }

    public static int getSizeX() {
        return sizeX;
    }

    public static int getSizeY() {
        return sizeY;
    }

    public static void setTileSizeX(int x) {
        tileSizeX = x;
    }

    public static void setTileSizeY(int y) {
        tileSizeY = y;
    }

    public static int getTileSizeX() {
        return tileSizeX;
    }

    public static int getTileSizeY() {
        return tileSizeY;
    }
}
