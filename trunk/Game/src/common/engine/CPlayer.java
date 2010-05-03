package common.engine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author miracle
 */
public class CPlayer extends CEntity {
    /**
     *
     */
    public static final int TEAM_NONE = 0;
    /**
     *
     */
    public static final int TEAM_RED = 1;
    /**
     *
     */
    public static final int TEAM_BLUE = 2;

    private int team;
    private CWeapon[] weapon;
    private int currentWeapon;
    private int health;
    private int kills;
    private int deaths;
    private BufferedImage[] body;

    /**
     *
     */
    public CPlayer() {
        team = TEAM_NONE;
        weapon = new CWeapon[3];
        weapon[0] = new CWeaponPistol();
        weapon[1] = new CWeaponRifle();
        weapon[2] = new CWeaponRailgun();
        currentWeapon = 0;
        size = new Dimension(50, 50);
        speed = 3.0d;
        health = 0;
        setPosition(50, 50);
        setDirection(1, 0);
        kills = 0;
        deaths = 0;
        body = new BufferedImage[2];
        body[0] = new BufferedImage(50, 26, BufferedImage.TYPE_INT_ARGB);
        body[1] = new BufferedImage(50, 26, BufferedImage.TYPE_INT_ARGB);
        try {
             body[0] = ImageIO.read(getClass().getResource("/common/resource/player_team_red.png"));
             body[1] = ImageIO.read(getClass().getResource("/common/resource/player_team_blue.png"));
        } catch(Exception e) { }
    }

    /**
     *
     * @return
     */
    public BufferedImage getBody(int team){
        return body[team-1];
    }

    /**
     *
     * @param team
     */
    public void setTeam(final int team) {
        this.team = team;
    }

    /**
     *
     * @return
     */
    public int getTeam() {
        return team;
    }

    /**
     *
     * @param health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     *
     * @return
     */
    public int getHealth() {
        return health;
    }

    /**
     *
     * @return
     */
    public boolean isDead() {
        if(health <= 0)
            return true;
        return false;
    }

    /**
     *
     * @param i
     */
    public void setCurrentWeapon(int i) {
        currentWeapon = i;
    }

    /**
     *
     * @return
     */
    public int getCurrentWeapon() {
        return currentWeapon;
    }

    /**
     *
     * @param idx
     * @return
     */
    public CWeapon getWeapon(int idx) {
        if(idx > -1 && idx < weapon.length)
            return weapon[idx];
        return null;
    }

    /**
     *
     * @param g
     */
    @Override
    public void render(Graphics2D g) {

    }

    /**
     *
     * @param ent
     */
    public void hit(CEntity ent) {

    }
    
    /**
     *
     * @param u
     * @return
     */
    public CProjectile shoot(UpdateLoop u) {
        return weapon[currentWeapon].shoot(this, u);
    }

    public void setKills(int k) {
        kills = k;
    }

    public int getKills() {
        return kills;
    }

    public void setDeaths(int d) {
        deaths = d;
    }

    public int getDeaths() {
        return deaths;
    }

    public void moveToSpawn(CMap map) {
        Point p = (team == CPlayer.TEAM_BLUE) ? map.getSpawnBlue() :
            map.getSpawnRed();
        int x = p.x * map.getTileSize().width + map.getTileSize().width/2;
        int y = p.y * map.getTileSize().height + map.getTileSize().height/2;
        setPosition(x, y);
    }
}
