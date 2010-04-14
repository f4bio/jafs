package client;

import client.anim.UpdateLoop;
import client.anim.UpdateObject;
import client.resource.MapLoader;
import common.CVector2;
import common.engine.CMap;
import common.engine.CPlayer;
import common.net.ProtocolCmd;

import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author miracle
 */
public class GameData implements UpdateObject {
    private MapLoader loader = new MapLoader(null, null);
    private String name;
    private CPlayer[] player;
    private int selfId;
    private Input input;
    private boolean loaded;

    public GameData(Input input) {
        this.input = input;
    }

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
            self.move(getMap(), mov, u.getSpeedfactor());

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
    }

    public void update(UpdateLoop u) {
        checkPlayerInput(u);
    }

    public CMap getMap() {
        return loader.getMap();
    }

    public CPlayer getSelf() {
        return getPlayer(selfId);
    }

    public void addPlayer(CPlayer p) {
        int idx = p.getId();
        if(idx >= 0 && idx < player.length)
            player[idx] = p;
    }

    public void removePlayer(CPlayer p) {
        int idx = p.getId();
        if(idx >= 0 && idx < player.length)
            player[idx] = null;
    }

    public CPlayer getPlayer(int i) {
        if(player != null && i < player.length)
            return player[i];
        return null;
    }

    public CPlayer[] getPlayers() {
        return player;
    }

    public void setMaxPlayers(int max) {
        player = new CPlayer[max];
    }

    public void setSelfId(int id) {
        selfId = id;
    }

    public int getSelfId() {
        return selfId;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;

        try {
            player[selfId].setName(n);
        } catch(Exception e) {

        }
    }

    public boolean isLoaded() {
        return loaded;
    }
}
