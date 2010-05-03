/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package mapeditor;


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
import java.util.Random;

import javax.swing.JTextArea;

/**
 *
 * @author Sezer Güler
 */
public class Main extends JFrame implements ActionListener{

    static JFrame frame;
    static BufferedImage image;
    static ShowImage panelImage;
    static MouseListener ms;
    /**
     * @param args the command line arguments
     */
     public static void generateIni(String[] s){
                 PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream(" outputfile. ini"));
            
        } catch (IOException e) {
            System.out.println(" Error in opening output file");
            System.exit(1);
        }
                 int y = 0;
         for(int i = 0; i < s.length; i++){
             if(i% 26 == 0){
                 y=0;
             }
             if(s[i].equals("tile")){
                writer.println("[Tile " + i/26 + " " + y + "]");
                writer.println("typeId = 0");
                writer.println("textureId = 0");
                writer.println("itemId = 0");
             } else if(s[i].equals("kp")){
                   writer.println("[Tile " + i/26 + " " + y + "]");
                   writer.println("typeId = 0");
                   writer.println("textureId = 2");
                   writer.println("itemId = 0");
             } else if(s[i].equals("stone")){
                writer.println("[Tile " + i/26 + " " + y + "]");
                writer.println("typeId = 1");
                writer.println("textureId = 1");
                writer.println("itemId = 0");
             } else if(s[i].equals("ground")){
                writer.println("[Tile " + i/26 + " " + y + "]");
                writer.println("typeId = 0");
                writer.println("textureId = 1");
                writer.println("itemId = 0");
             }
             writer.println("");
             y++;

             
         }
                 writer.close();
     }
    public static void main(String[] args) {
        // TODO code application logic here
        frame = new JFrame("MapEditor");
        JPanel panel = new JPanel(null);
        JPanel panelLeft = new JPanel(new GridLayout(16, 26));
        panelLeft.setBounds(0, 0, 1300,900);
 
        JPanel panelRight = new JPanel(new GridLayout(4, 2));
        panelRight.setBounds(1300, 0, 300, 900);

        ms = new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                System.out.println( ((ShowImage)e.getSource()).getPosition() );
            }

            public void mousePressed(MouseEvent e) {
              //  throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseReleased(MouseEvent e) {
              //  throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseEntered(MouseEvent e) {
                System.out.println(((ShowImage)e.getSource()).getPosition());
            }

            public void mouseExited(MouseEvent e) {
              //  throw new UnsupportedOperationException("Not supported yet.");
            }
        };

       // panelRight.set(100, 500);
        panelImage = new ShowImage();
      //  panelImage.setPreferredSize(new Dimension(100,100));
        panelImage.setImage("stone.png");
        panelRight.add(panelImage);
         panelImage = new ShowImage();
         panelImage.setImage("tile.jpg");
        panelRight.add(panelImage);
         panelImage = new ShowImage();
         panelImage.setImage("kp.jpg");
        panelRight.add(panelImage);
        panelImage = new ShowImage();
         panelImage.setImage("ground.jpg");
        panelRight.add(panelImage);
       // frame.add(panelLeft);
      //  frame.add(panelRight);
        Random random = new Random();
        double d;
        String[] s = new String[16*26];
       for(int i = 0; i < 16*26; i++){
         d = random.nextDouble();
         panelImage = new ShowImage();
         panelImage.addMouseListener(ms);
         panelImage.setPosition(i);
           if(d <= 0.5){
            
            panelImage.setImage("tile.jpg");
            s[i] = "tile";
         } else if(0.5 < d && d < 0.7){
             
            panelImage.setImage("kp.jpg");
            s[i] = "kp";
         } else if(0.7 < d && d < 0.9){
             
            panelImage.setImage("ground.jpg");
            s[i] = "ground";
         }
         else{
         panelImage = new ShowImage();
         panelImage.setImage("stone.png");
         s[i] = "stone";
         }
        panelLeft.add(panelImage);
       }

        generateIni(s);


 /*       panelImage = new ShowImage("stone.png");
        panelLeft.add(panelImage);

        ShowImage[]  p = new ShowImage[16*26];
for(int i = 0; i < 16*26; i++){
   p[i] = new ShowImage();
   panelLeft.add(p[i]);
}
        
    p[0].setImage("grass.jpg");
    p[50].setImage("stone.png");

*/
        panel.add(panelLeft);
        panel.add(panelRight);
        frame.add(panel);

        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
