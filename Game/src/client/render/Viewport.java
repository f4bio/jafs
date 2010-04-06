/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.render;

import client.Main;
import common.CVector2;
import common.engine.CMap;
import common.engine.CPlayer;
import common.engine.Tile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.image.VolatileImage;

/**
 *
 * @author miracle
 */
public class Viewport {
    public static final Dimension size = new Dimension(1024, 768);

    private VolatileImage buffer;
    private GraphicsConfiguration gc;
    private float aspectRatio = (float)size.width / size.height;

    public Viewport(GraphicsConfiguration gc) {
        this.gc = gc;
        createBuffer();
    }

    public void clear(Graphics2D g) {
        if(buffer != null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, size.width, size.height);
        }
    }

    private void createBuffer() {
        buffer = gc.createCompatibleVolatileImage(size.width, size.height);
    }

    public VolatileImage getBuffer() {
        return buffer;
    }

    public void renderScene(Graphics2D g2) {
        do {
            Graphics2D g = buffer.createGraphics();
            try {
                int result = buffer.validate(gc);

                if(result == VolatileImage.IMAGE_INCOMPATIBLE)
                    createBuffer();

                clear(g);
                render(g);

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

                    g.drawOval(posX - (player[i].getSize().width/2),
                            posY - (player[i].getSize().height/2),
                            pSize.width, pSize.height);

                    CVector2 dir = player[i].getDirection().trim_cpy(50);
                    g.drawLine(posX, posY, posX + (int)dir.getX(),
                            posY + (int)dir.getY());
                }
            }
        }

        // ----- Projectiles


    }
}
