/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import client.anim.UpdateLoop;
import client.anim.UpdateObject;
import client.resource.MapLoader;
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
        
        if(input.isKeyWPressed()) {
            self.move(CPlayer.VEC_UP, u.getSpeedfactor());
        } else if(input.isKeyAPressed()) {
            self.move(CPlayer.VEC_LEFT, u.getSpeedfactor());
        } else if(input.isKeySPressed()) {
            self.move(CPlayer.VEC_DOWN, u.getSpeedfactor());
        } else if(input.isKeyDPressed()) {
            self.move(CPlayer.VEC_RIGHT, u.getSpeedfactor());
        }
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
