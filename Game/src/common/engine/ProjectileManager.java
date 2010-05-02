/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import server.Game;

/**
 *
 * @author miracle
 */
public class ProjectileManager {
    private static Vector<CProjectile> projectiles = new Vector<CProjectile>();
    private static Game game = null;

    /**
     *
     * @param p
     */
    public static void addProjectile(CProjectile p) {
        projectiles.add(p);
    }

    /**
     *
     * @param p
     */
    public static void removeProjectile(CProjectile p) {
        projectiles.remove(p);
    }

    /**
     *
     * @param u
     * @param p
     * @param map
     */
    public static void checkProjectiles(UpdateLoop u, CPlayer[] p, CMap map) {
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
    public static Vector<CProjectile> getProjectiles() {
        return projectiles;
    }

    /**
     *
     * @param g
     */
    public static void setGame(Game g) {
        game = g;
    }
}
