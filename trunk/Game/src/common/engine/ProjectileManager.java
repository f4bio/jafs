/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

import client.anim.UpdateLoop;
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

    public static void addProjectile(CProjectile p) {
        projectiles.add(p);
    }

    public static void removeProjectile(CProjectile p) {
        projectiles.remove(p);
    }

    public static void checkProjectiles(UpdateLoop u, CPlayer[] p, CMap map) {
        synchronized (projectiles) {
            Iterator<CProjectile> i = projectiles.iterator();
            while (i.hasNext()) {
                CProjectile pr = i.next();
                if (pr != null) {
                    int retC;
                    if ((retC = pr.move(map, pr.getDirection().resize_cpy(1.0d),
                            u.getSpeedfactor(), false, p)) != -2) {
                        if (game != null && retC > -1) {
                            game.hitPlayer(retC, pr);
                        }
                        i.remove();
                    }
                }
            }
        }
    }

    public static Vector<CProjectile> getProjectiles() {
        return projectiles;
    }

    public static void setGame(Game g) {
        game = g;
    }
}
