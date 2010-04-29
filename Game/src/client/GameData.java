package client;

import client.anim.UpdateLoop;
import client.anim.UpdateObject;
import client.resource.MapLoader;
import common.CVector2;
import common.engine.CMap;
import common.engine.CPlayer;
import common.engine.CProjectile;
import common.engine.ProjectileManager;
import common.net.ProtocolCmd;

import java.util.ArrayList;
import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author miracle
 */
public class GameData implements UpdateObject {
    private MapLoader loader = new MapLoader(null, null);
    private String name;
    private CPlayer[] player;
    private ArrayList<CProjectile> projectiles;
    private int selfId;
    private Input input;
    private boolean loaded;

    /**
     *
     * @param input
     */
    public GameData(Input input) {
        this.input = input;
        this.projectiles = new ArrayList<CProjectile>(100);
    }

    /**
     *
     * @param map
     * @return
     */
    public boolean loadMap(String map) {
        loader.setMap(map);

        loaded = loader.load(map);
        return loaded;
    }

    private void checkPlayerInput(UpdateLoop u) {
        CPlayer self = getSelf();
        
        if(self == null || !Main.getNetwork().isReallyConnected())
            return;
        
        CVector2 mov = null;

        if(input.isKeyWPressed()) {
            if(input.isKeyAPressed())
                mov = CPlayer.VEC_UP_LEFT;
            else if(input.isKeyDPressed())
                mov = CPlayer.VEC_UP_RIGHT;
            else
                mov = CPlayer.VEC_UP;
        }
        else if(input.isKeySPressed()) {
            if(input.isKeyAPressed())
                mov = CPlayer.VEC_DOWN_LEFT;
            else if(input.isKeyDPressed())
                mov = CPlayer.VEC_DOWN_RIGHT;
            else
                mov = CPlayer.VEC_DOWN;
        }
        else if(input.isKeyAPressed()) {
            mov = CPlayer.VEC_LEFT;
        }
        else if(input.isKeyDPressed()) {
            mov = CPlayer.VEC_RIGHT;
        }

        if(mov != null)
            self.move(getMap(), mov, u.getSpeedfactor(), false, player);

        self.setDirection(input.getDirection());

        //Send player info
        int id = self.getId();
        int weapon = self.getCurrentWeapon();
        double posX = self.getPosition().getX();
        double posY = self.getPosition().getY();
        double dirX = self.getDirection().getX();
        double dirY = self.getDirection().getY();

        Main.getNetwork().send(ProtocolCmd.CLIENT_SERVER_PLAYER_INFO, argInt(id),
                argInt(weapon), argDouble(posX), argDouble(posY), argDouble(dirX),
                argDouble(dirY));

        if(input.isKeyM1Pressed()) {
            CProjectile c = null;
            if((c = self.shoot(u)) != null) {
                ProjectileManager.addProjectile(c);
                Main.getNetwork().send(ProtocolCmd.CLIENT_SERVER_SHOOT, argInt(c.getId()),
                        argInt(self.getCurrentWeapon()), argInt((int)c.getDirection().getX()),
                        argInt((int)c.getDirection().getY()), argDouble(c.getOrigin().getX()),
                        argDouble(c.getOrigin().getY()));
            }
        }
    }

    /**
     *
     * @param u
     */
    public void update(UpdateLoop u) {
        checkPlayerInput(u);
        ProjectileManager.checkProjectiles(u, player, getMap());
    }

    /**
     *
     * @return
     */
    public CMap getMap() {
        return loader.getMap();
    }

    /**
     *
     * @return
     */
    public CPlayer getSelf() {
        return getPlayer(selfId);
    }

    /**
     *
     * @param p
     */
    public void addPlayer(CPlayer p) {
        int idx = p.getId();
        if(idx >= 0 && idx < player.length)
            player[idx] = p;
    }

    /**
     *
     * @param p
     */
    public void removePlayer(CPlayer p) {
        int idx = p.getId();
        if(idx >= 0 && idx < player.length)
            player[idx] = null;
    }

    /**
     *
     * @param i
     * @return
     */
    public CPlayer getPlayer(int i) {
        if(player != null && i < player.length)
            return player[i];
        return null;
    }

    /**
     *
     * @return
     */
    public CPlayer[] getPlayers() {
        return player;
    }

    /**
     *
     * @param max
     */
    public void setMaxPlayers(int max) {
        player = new CPlayer[max];
    }

    /**
     *
     * @param id
     */
    public void setSelfId(int id) {
        selfId = id;
    }

    /**
     *
     * @return
     */
    public int getSelfId() {
        return selfId;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param n
     */
    public void setName(String n) {
        name = n;

        try {
            player[selfId].setName(n);
        } catch(Exception e) {

        }
    }

    /**
     *
     * @return
     */
    public boolean isLoaded() {
        return loaded;
    }
}
