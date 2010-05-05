/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

import java.util.Iterator;
import java.util.Vector;
import server.Game;

/**
 *
 * @author miracle
 */
public class ProjectileManager {
    private Vector<CProjectile> projectiles = new Vector<CProjectile>();
    private Game game = null;

    public ProjectileManager() {
        projectiles = new Vector<CProjectile>();
    }

    /**
     *
     * @param p
     */
    public void addProjectile(CProjectile p) {
        projectiles.add(p);
    }

    /**
     *
     * @param p
     */
    public void removeProjectile(CProjectile p) {
        projectiles.remove(p);
    }

    /**
     *
     * @param u
     * @param p
     * @param map
     */
    public void checkProjectiles(UpdateLoop u, CPlayer[] p, CMap map) {
        synchronized (projectiles) {
            Iterator<CProjectile> i = projectiles.iterator();
            while (i.hasNext()) {
                CProjectile pr = i.next();
                if (pr != null) {
                    if(pr.isCollided()) {
                        i.remove();
                        continue;
                    }

                    int retC;
                    if ((retC = pr.move(map, pr.getDirection(),
                            u.getSpeedfactor(), true, p)) != -2) {
                        if (game != null && retC > -1) {
                            game.hitPlayer(retC, pr);
                        }
                        pr.setCollided(true);
                    }
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public Vector<CProjectile> getProjectiles() {
        return projectiles;
    }

    /**
     *
     * @param g
     */
    public void setGame(Game g) {
        game = g;
    }
}
