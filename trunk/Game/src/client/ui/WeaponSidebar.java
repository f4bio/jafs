package client.ui;

import client.anim.UpdateLoop;
import client.anim.UpdateObject;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author Julian Sanio
 */
public class WeaponSidebar extends UiWindow implements UpdateObject {

    private BufferedImage[] img_weapon;
    private int height;
    private int width;
    private int focus_width;
    private int focus_height;
    private int aktiv_weapon;
    private int nWeapons;
    private int slot_position = 0;
    private int focus_x;
    private int focus_y1;
    private int focus_y2;
    private int focus_border;
    private Color focus_color;

    public WeaponSidebar(int width, int focus_height){
        URL[] url = new URL[3];
        url[0] = getClass().getResource("/common/resource/weapon_glock18.png");
        url[1] = getClass().getResource("/common/resource/weapon_mp5.png");
        url[2] = getClass().getResource("/common/resource/weapon_m4a1.png");

        nWeapons = url.length;
        focus_width = width;
        this.focus_height = focus_height;
        this.width = width;
        height = (nWeapons-1)*focus_height+(nWeapons)*focus_height;
        focus_x = 0;
        focus_y1 = (height/2)-(focus_height/2);
        focus_y2 = (height/2)+(focus_height/2);
        focus_border = 4;
        focus_color = new Color(128, 128, 128, 128);
        aktiv_weapon = 0;

        img_weapon = new BufferedImage[nWeapons];
        for(int i=0; i<img_weapon.length; i++){
            img_weapon[i] = new BufferedImage(width, focus_height, BufferedImage.TYPE_INT_ARGB);
            try {
                img_weapon[i] = ImageIO.read(url[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setBackground(new Color(0, 0, 0, 128));
        setSize(this.width, height);
        setUndecorated(true);
        setMoveable(false);
//        setOpaque(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        // Weapons
        g.setColor(new Color(255, 255, 255, 128));
        for(int i=0; i<img_weapon.length; i++){
            g.drawImage(img_weapon[i], focus_x, i*focus_height + focus_y1 - slot_position, null);
        }
        // Focus
        g.setColor(focus_color);
        g.fillRect(focus_x, focus_y1-focus_border, focus_width, focus_border);      // top
        g.fillRect(focus_x, focus_y1, focus_border, focus_height);                  // left
        g.fillRect(focus_width-focus_border, focus_y1, focus_border, focus_height); // right
        g.fillRect(focus_x, focus_y2, focus_width, focus_border);                   // bottom
    }

    public void setAktiveWeapon(int n){
        if(n < nWeapons && n >= 0)
            aktiv_weapon = n;
//        System.out.println("Aktive Weapon: "+aktiv_weapon);
    }

    public int getAktiveWeapon(){
        return aktiv_weapon;
    }

    @Override
    public void addActionListener(ActionListener a) {  }

    public void update(UpdateLoop u) {
        if(slot_position < aktiv_weapon*focus_height) {
            slot_position += 10;
        } else if(slot_position > aktiv_weapon*focus_height) {
            slot_position -= 10;
        }
    }
}
