package client.render;

import client.Event;
import client.GameData;
import client.Main;
import common.CVector2;
import common.engine.CMap;
import common.engine.CPlayer;
import common.engine.CProjectile;
import common.engine.ProjectileManager;
import common.engine.Tile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author miracle
 */
public class Viewport {
    /**
     *
     */
    public static final Dimension size = new Dimension(1024, 768);
    public static final Color background = new Color(128, 128, 128, 64);
    public static final Color backgroundSelected = new Color(128, 128, 128, 192);

    private VolatileImage buffer;
    private GraphicsConfiguration gc;
    private float aspectRatio = (float)size.width / size.height;
    private boolean statsVisible;
    private boolean lockStatsVisible;
    private boolean teamSelectionVisible;
    private boolean wepSelectionVisible;

    /**
     *
     * @param gc
     */
    public Viewport(GraphicsConfiguration gc) {
        this.gc = gc;
        createBuffer();
        statsVisible = false;
        teamSelectionVisible = false;
        wepSelectionVisible = false;
        lockStatsVisible = false;
    }

    /**
     *
     * @param g
     */
    public void clear(Graphics2D g) {
        if(buffer != null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, size.width, size.height);
        }
    }

    private void createBuffer() {
        buffer = gc.createCompatibleVolatileImage(size.width, size.height);
    }

    /**
     *
     * @return
     */
    public VolatileImage getBuffer() {
        return buffer;
    }

    /**
     * Rotiert das übergebene Bild um den Winkel angle und gibt das bearbeitete Bild
     * wieder.
     *
     * @param img Ursprungsbild.
     * @param angle Drehwinkel.
     * @return Bearbeitetes Bild.
     */
    public static BufferedImage rotateImage(BufferedImage img, double angle) {
        int w = img.getWidth(), h = img.getWidth();
        BufferedImage ret = new BufferedImage(w, h, Transparency.TRANSLUCENT);
        Graphics2D g = null;
        try {
            g = ret.createGraphics();
            g.rotate(Math.toRadians(angle), w / 2, h / 2);
            g.drawImage(img, 0, 0, null);
        } finally {
            g.dispose();
        }
        return ret;
    }

    public double getAngleTo(double fromX, double fromY, double x, double y) {
        //int diff = buffer.getHeight()/2;

        if (fromX < x && fromY > y) {
            return Math.toDegrees((Math.atan(x - fromX) / (y - fromY)));
        } else if (fromX < x && fromY < y) {
            return 180 - Math.toDegrees(Math.atan((x - fromX) / (fromY - y)));
        } else if (fromX > x && fromY > y) {
            return 360 - Math.toDegrees((Math.atan((fromX - x) / (y - fromY))));
        } else if (fromX > x && fromY < y) {
            return 180 + Math.toDegrees(Math.atan(((fromX - x) / (fromY - y))));
        }
        
        return 0;
    }

    /**
     *
     * @param g2
     */
    public void renderScene(Graphics2D g2) {
        do {
            Graphics2D g = buffer.createGraphics();
            try {
                int result = buffer.validate(gc);

                if(result == VolatileImage.IMAGE_INCOMPATIBLE)
                    createBuffer();

                clear(g);
                render(g);
                renderHud(g);

                int sizeX = Main.getScreen().getSize().width;
                int sizeY = Main.getScreen().getSize().height;

                int sY = Math.round((float) sizeX / aspectRatio);
                int sX = Math.round((float) sizeY * aspectRatio);

                if (sY <= sizeY) {
                    int posY = Math.round((float) (sizeY - sY) / 2.0f);
                    g2.drawImage(buffer, 0, posY, sizeX, sY, null);
                } else {
                    int posX = Math.round((float) (sizeX - sX) / 2.0f);
                    g2.drawImage(buffer, posX, 0, sX, sizeY, null);
                 }
            } catch(Exception e) {

            } finally {
                g.dispose();
            }
        } while(buffer.contentsLost());
    }

    /**
     *
     * @param g
     */
    public void render(Graphics2D g) {
        CMap map = Main.getGameData().getMap();

        if(map == null)
            return;

        Point focus = Main.getGameData().getSelf().getPosition().get();
        int width = size.width;
        int height = size.height;

        int left = focus.x - width/2;
        int right = focus.x + width/2;
        int up = focus.y - height/2;
        int down = focus.y + height/2;

        Point upperLeft = map.getTileByCoords(left, up);
        Point bottomRight = map.getTileByCoords(right, down);
        Point pFocus = map.getTileByCoords(focus.x, focus.y);

        int dX = focus.x % map.getTileSize().width;
        int dY = focus.y % map.getTileSize().height;

        int initCntX = (width/2) - ((pFocus.x - upperLeft.x) *
                map.getTileSize().width + dX);
        int initCntY = (height/2) - ((pFocus.y - upperLeft.y) *
                map.getTileSize().height + dY);

        int xCnt = initCntX;

        for(int x=upperLeft.x; x<=bottomRight.x; x++) {
            int yCnt = initCntY;

            for(int y=upperLeft.y; y<=bottomRight.y; y++) {
                Tile t = map.getTile(x, y);
                if(t != null) {
                    g.drawImage(t.getTexture(), xCnt, yCnt, map.getTileSize().width,
                            map.getTileSize().height, null);
                }

                yCnt += map.getTileSize().height;
            }
            xCnt += map.getTileSize().width;
        }

        // ----- Players

        CPlayer[] player = Main.getGameData().getPlayers();

        for(int i=0; i<player.length; ++i) {
            if(player[i] != null && player[i].getTeam() != CPlayer.TEAM_NONE) {
                Point pos = player[i].getPosition().get();
                Point loc = map.getTileByCoords(pos);
                int pX = pos.x % map.getTileSize().width;
                int pY = pos.y % map.getTileSize().height;

                if(loc.x >= upperLeft.x && loc.x <= bottomRight.x &&
                        loc.y >= upperLeft.y && loc.y <= bottomRight.y) {
                    int posX = initCntX + pX +
                            (loc.x - upperLeft.x) * map.getTileSize().width;
                    int posY = initCntY + pY +
                            (loc.y - upperLeft.y) * map.getTileSize().height;
                    Dimension pSize = player[i].getSize();

                    if(player[i].getTeam() == CPlayer.TEAM_BLUE)
                        g.setColor(Color.BLUE);
                    else
                        g.setColor(Color.RED);

                    g.fillOval(posX - (player[i].getSize().width/2),
                            posY - (player[i].getSize().height/2),
                            pSize.width, pSize.height);

                    g.setColor(Color.BLACK);
                    g.drawOval(posX - (player[i].getSize().width/2),
                            posY - (player[i].getSize().height/2),
                            pSize.width, pSize.height);

                    g.setColor(player[i].getWeapon(player[i].getCurrentWeapon()).getColor());
                    CVector2 dir = player[i].getDirection().resize_cpy(25);
                    g.drawLine(posX, posY, posX + (int)dir.getX(),
                            posY + (int)dir.getY());

                    /*CVector2 direction = player[i].getDirection();
                    double ang = getAngleTo(0, -1, direction.getX(), direction.getY());
                    BufferedImage img = rotateImage(player[i].getBody(player[i].getTeam()), -ang);
                    g.drawImage(img, null, posX - (player[i].getSize().width/2),
                            posY - (player[i].getSize().height/2));*/

                    g.setColor(Color.BLACK);
                    if(player[i].isDead()) {
                        //g.setColor(player[i].getWeapon(player[i].getCurrentWeapon()).getColor());
                        //g.fillOval(posX - (player[i].getSize().width/2), posY - (player[i].getSize().height/2), 50, 50);
                        g.drawLine(posX - player[i].getSize().width/2, posY,
                                posX + player[i].getSize().width/2, posY);

                        g.drawLine(posX, posY + player[i].getSize().height/2,
                                posX, posY - player[i].getSize().height/2);
                    }
                }
            }
        }

        // ----- Projectiles
        Vector<CProjectile> list = ProjectileManager.getProjectiles();

        for (int i = 0; i < list.size(); ++i) {
            CProjectile cp = list.get(i);

            Point pos = cp.getPosition().get();
            Point loc = map.getTileByCoords(pos);

            double dist = cp.getPosition().getDistanceTo(cp.getOrigin());
            if (dist > 300) {
                dist = 300;
            }

            CVector2 tmp = cp.getDirection().invert_cpy();
            tmp.resize(dist);
            Point end = tmp.get();

            int pXS = pos.x % map.getTileSize().width;
            int pYS = pos.y % map.getTileSize().height;

            if (loc.x >= upperLeft.x && loc.x <= bottomRight.x && loc.y >= upperLeft.y && loc.y <= bottomRight.y) {

                int posX = initCntX + pXS + (loc.x - upperLeft.x) * map.getTileSize().width;
                int posY = initCntY + pYS + (loc.y - upperLeft.y) * map.getTileSize().height;

                g.setColor(Main.getGameData().getSelf().getWeapon(cp.getWeaponId()).getColor());
                g.drawLine(posX, posY, posX + end.x, posY + end.y);

                if(cp.isCollided())
                    g.fillOval(posX - 10, posY - 10, 20, 20);
            }
        }
    }

    public static String getTime(long millis) {
        long time = millis / 1000;

        String seconds = Integer.toString((int) (time % 60));
        String minutes = Integer.toString((int) ((time % 3600) / 60));
        String hours = Integer.toString((int) (time / 3600));

        for (int i = 0; i < 2; i++) {
            if (seconds.length() < 2) {
                seconds = "0" + seconds;
            }
            if (minutes.length() < 2) {
                minutes = "0" + minutes;
            }
            if (hours.length() < 2) {
                hours = "0" + hours;
            }
        }

        return hours + ":" + minutes + ":" + seconds;
    }

    public static void drawCenteredString(Graphics2D g, String s, int x, int y) {
        FontMetrics fm = g.getFontMetrics(g.getFont());

        int textWidth = (int)Math.round(fm.getStringBounds(s, g).getWidth());
        int textHeight = (int)Math.round(fm.getStringBounds(s, g).getHeight());
        int textAscent = fm.getAscent();

        int textX = x - (textWidth / 2);
        int textY = y - (textHeight / 2) + textAscent;

        g.drawString(s, textX, textY);
    }

    public static void drawCenteredStringY(Graphics2D g, String s, int x, int y) {
        FontMetrics fm = g.getFontMetrics(g.getFont());

        int textHeight = (int)Math.round(fm.getStringBounds(s, g).getHeight());
        int textAscent = fm.getAscent();

        int textY = y - (textHeight / 2) + textAscent;

        g.drawString(s, x, textY);
    }
    
    public Rectangle2D getStringMetrics(String s, Graphics2D g) {
        FontMetrics fm = g.getFontMetrics(g.getFont());
        
        return fm.getStringBounds(s, g);
    }
    
    public void renderBox(int x, int y, int width, int height, Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.setColor(background);
        g.fillRect(x+1, y+1, width-1, height-1);
    }

    public void renderBox(int x, int y, int width, int height, Graphics2D g, Color c) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.setColor(c);
        g.fillRect(x+1, y+1, width-1, height-1);
    }
    
    public void renderHealthBox(Graphics2D g) {
        int w = 100;
        int h = 50;
        CPlayer self = Main.getGameData().getSelf();
        renderBox(buffer.getWidth() - 5 - w, buffer.getHeight() - 5 - h, w, h, g);

        if(self != null) {
            Color c = Color.WHITE;
            int health = self.getHealth();
            if(health <= 33)
                c = Color.RED;
            else if(health <= 66)
                c = Color.YELLOW;

            g.setColor(c);
            
            drawCenteredString(g, "" + self.getHealth(), buffer.getWidth() - (w+5)/2,
                    buffer.getHeight() - (h+5)/2);
        }
    }

    public void renderGameEvents(Graphics2D g) {
        ArrayList<Event> events = Main.getGameData().getGameEvents();

        synchronized(events) {
            int y = 0;
            for(int i=0; i<events.size(); ++i) {
                Event e = events.get(i);
                y += (int)getStringMetrics(e.getMsg(), g).getHeight() + 5;
                g.setColor(e.getColor());
                g.drawString(e.getMsg(), 5, y);
            }
        }
    }

    public void renderChatEvents(Graphics2D g) {
        ArrayList<Event> events = Main.getGameData().getChatEvents();

        synchronized(events) {
            int y = buffer.getHeight();

            for(int i=events.size()-1; i>=0; --i) {
                Event e = events.get(i);
                y = y - 5 - (int)getStringMetrics(e.getMsg(), g).getHeight();

                g.setColor(e.getColor());
                g.drawString(e.getMsg(), 5, y);
            }
        }
    }

    public void renderRoundTime(Graphics2D g) {
        long time = Main.getGameData().getRoundTime();

        int x = buffer.getWidth()/2;
        int y = 0;
        int w = 100;
        int h = 50;

        renderBox(x - 5 - w, y + 5, w, h, g);

        g.setColor(Color.WHITE);
        drawCenteredString(g, getTime(time), x - 5 - w/2, y + 5 + h/2);
    }

    public void renderRespawnTime(Graphics2D g) {
        long time = Main.getGameData().getRespawnTime();

        int x = buffer.getWidth()/2;
        int y = 0;
        int w = 100;
        int h = 50;

        renderBox(x + 5, y + 5, w, h, g);

        g.setColor(Color.WHITE);
        drawCenteredString(g, getTime(time), x + 5 + w/2, y + 5 + h/2);
    }

    public CPlayer[] sortByKills(int team) {
        CPlayer[] p = new CPlayer[Main.getGameData().getPlayers().length];
        boolean[] gone = new boolean[p.length];
        CPlayer[] l = Main.getGameData().getPlayers();

        int idx = 0;
        int gIdx = 0;
        
        for(int i=0; i<l.length; ++i) {
            if(l[i] != null && l[i].getTeam() == team && !gone[i]) {
                p[idx] = l[i];
                gIdx = i;
                for(int j = 0; j<l.length; ++j) {
                    if(l[j] != null && l[j].getKills() > p[idx].getKills()
                            && l[j].getTeam() == team && !gone[j]) {
                        p[idx] = l[j];
                        gIdx = j;
                    }
                }
                gone[gIdx] = true;
                idx++;
            }
        }

        return p;
    }

    public void renderStats(Graphics2D g) {
        GameData d = Main.getGameData();
        CPlayer[] pRed = sortByKills(CPlayer.TEAM_RED);
        CPlayer[] pBlue = sortByKills(CPlayer.TEAM_BLUE);
        CPlayer[][] players = {
            pRed, pBlue
        };

        int w = 640;
        int h = 30;
        int x = buffer.getWidth()/2;
        int y = 70;

        int w1 = (int)((4.0d/5.0d) * w);
        int w2 = (int)((0.5d/5.0d) * w);

        for(int i=0; i<2; ++i) {
            CPlayer[] p = players[i];
            String team = (i==0)?"Team Red (" + d.getScoreRed() + ")":
                "Team Blue (" + d.getScoreBlue() + ")";
            Color c = (i==0)?Color.RED:Color.CYAN;

            renderBox(x - w/2, y, w, h, g);
            g.setColor(c);
            drawCenteredString(g, team, x, y + h/2);
            y += h;

            for(int j=0; j<p.length + 1; ++j) {
                if(j > 0 && p[j-1] != null &&
                        p[j-1].getId() == Main.getGameData().getSelfId()) {
                    renderBox(x - w/2, y, w1, h, g, backgroundSelected);
                    renderBox(x - w/2 + w1, y, w2, h, g, backgroundSelected);
                    renderBox(x - w/2 + w1 + w2, y, w2, h, g, backgroundSelected);
                } else if(j==0 || (j > 0 && p[j-1] != null)) {
                    renderBox(x - w/2, y, w1, h, g);
                    renderBox(x - w/2 + w1, y, w2, h, g);
                    renderBox(x - w/2 + w1 + w2, y, w2, h, g);
                }

                g.setColor(c);
                if(j == 0) {
                    drawCenteredStringY(g, "Name", x - w/2 + 5, y + h/2);
                    drawCenteredStringY(g, "Kills", x - w/2 + 5 + w1, y + h/2);
                    drawCenteredStringY(g, "Deaths", x - w/2 + 5 + w1 + w2, y + h/2);
                } else {
                    CPlayer o = p[j - 1];
                    if(o != null) {
                        String name = o.getName();
                        int kills = o.getKills();
                        int deaths = o.getDeaths();

                        drawCenteredStringY(g, name, x - w/2 + 5, y + h/2);
                        drawCenteredStringY(g, "" + kills, x - w/2 + 5 + w1, y + h/2);
                        drawCenteredStringY(g, "" + deaths, x - w/2 + 5 + w1 + w2, y + h/2);
                    }
                }

                if(j==0 || (j > 0 && p[j-1] != null))
                    y += h;
            }
        }
    }

    public void renderTeamSelection(Graphics2D g) {
        int w = 320;
        int h = 240;
        int x = buffer.getWidth()/2;
        int y = buffer.getHeight()/2;

        renderBox(x - w/2, y - h/2 - 40, w, 40, g);
        g.setColor(Color.WHITE);
        drawCenteredString(g, "Pick a team", x, y - h/2 - 20);

        renderBox(x - w/2, y - h/2, w, h, g);
        g.setColor(Color.RED);
        g.fillRect(x - w/2 + 5, y - h/2 + 5, w/2 - 10, h - 40);
        drawCenteredStringY(g, "Press \"B\"", x - w/2 + 5, y + h/2 - 20);
        g.setColor(Color.BLUE);
        g.fillRect(x + 5, y - h/2 + 5, w/2 - 10, h - 40);
        drawCenteredStringY(g, "Press \"M\"", x + 5, y + h/2 - 20);
    }

    public void renderWeaponSelection(Graphics2D g) {
        CPlayer self = Main.getGameData().getSelf();
        int w = 100;
        int h = 100;
        int x = buffer.getWidth() - w - 5;
        int y = buffer.getHeight()/2 - (h*3)/2;

        for(int i=0; i<3; ++i) {
            if(self.getCurrentWeapon() == i)
                renderBox(x, y, w, h, g, backgroundSelected);
            else
                renderBox(x, y, w, h, g);
            g.setColor(self.getWeapon(i).getColor());
            g.fillRect(x + 5, y + 5, 90, 90);
            y += 100;
        }
    }

    public void renderHud(Graphics2D g) {
        if(statsVisible || lockStatsVisible)
            renderStats(g);
        if(teamSelectionVisible)
            renderTeamSelection(g);
        if(wepSelectionVisible)
            renderWeaponSelection(g);
        
        renderHealthBox(g);
        renderRoundTime(g);
        renderRespawnTime(g);
        renderGameEvents(g);
        renderChatEvents(g);
    }

    public void setStatsVisible(boolean vis) {
        statsVisible = vis;
    }

    public boolean areStatsVisible() {
        return statsVisible;
    }

    public void setLockStatsVisible(boolean vis) {
        lockStatsVisible = vis;
    }

    public boolean isLockStatsVisible() {
        return lockStatsVisible;
    }

    public void setTeamSelectionVisible(boolean vis) {
        teamSelectionVisible = vis;
    }

    public boolean isTeamSelectionVisible() {
        return teamSelectionVisible;
    }

    public void setWeaponSelectionVisible(boolean vis) {
        wepSelectionVisible = vis;
    }

    public boolean isWeaponSelectionVisible() {
        return wepSelectionVisible;
    }
}
