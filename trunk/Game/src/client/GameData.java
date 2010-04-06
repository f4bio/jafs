/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import client.anim.UpdateLoop;
import client.anim.UpdateObject;
import client.resource.MapLoader;
import common.CVector2;
import common.engine.CMap;
import common.engine.CPlayer;

/**
 *
 * @author miracle
 */
public class GameData implements UpdateObject {
    private MapLoader loader = new MapLoader(null, null);
    private CPlayer[] player;
    private int selfId;
    private Input input;

    public GameData(Input input) {
        this.input = input;
    }

    public void loadMap(String map) {
        player = new CPlayer[1];

        player[0] = new CPlayer();
        player[0].setTeam(CPlayer.TEAM_BLUE);
        setSelfId(0);

        loader.setMap(map);
        loader.load(map);
    }
    
    private void checkPlayerInput(UpdateLoop u) {
        CPlayer self = getSelf();
        
        if(self == null)
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
            self.move(mov, u.getSpeedfactor());

        self.setDirection(input.getDirection());
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
        if(idx > 0 && idx < player.length)
            player[idx] = p;
    }

    public CPlayer getPlayer(int i) {
        if(player != null && i < player.length)
            return player[i];
        return null;
    }

    public CPlayer[] getPlayers() {
        return player;
    }

    public void setSelfId(int id) {
        selfId = id;
    }

    public int getSelfId() {
        return selfId;
    }
}
