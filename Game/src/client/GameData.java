package client;

import client.render.Viewport;
import common.engine.UpdateLoop;
import common.engine.UpdateObject;
import client.resource.MapLoader;
import common.CVector2;
import common.engine.CMap;
import common.engine.CPlayer;
import common.engine.CProjectile;
import common.engine.ProjectileManager;
import common.engine.UpdateCountdown;
import common.engine.UpdateCountdownObject;
import common.net.Network;
import common.net.ProtocolCmd;

import java.util.ArrayList;
import static common.net.ProtocolCmdArgument.*;

/**
 *
 * @author J.A.F.S
 */
public class GameData implements UpdateObject, UpdateCountdownObject {
    public static final int maxEvents = 7;

    private MapLoader loader = new MapLoader(null, null);
    private String name;
    private CPlayer[] player;
    private ArrayList<Event> gameEvents;
    private ArrayList<Event> chatEvents;
    private int selfId;
    private Input input;
    private boolean loaded;
    private long roundTime;
    private long respawnTime;
    private int scoreRed;
    private int scoreBlue;

    /**
     * Constructs a GameData object
     * @param input
     */
    public GameData(Input input) {
        selfId = -1;
        this.input = input;
        this.gameEvents = new ArrayList<Event>();
        this.chatEvents = new ArrayList<Event>();
    }

    public void reset() {
        scoreRed = 0;
        scoreBlue = 0;
        roundTime = 0;
        respawnTime = 0;
        
        for(int i=0; i<player.length; ++i) {
            player[i] = null;
        }

        gameEvents.clear();
        chatEvents.clear();
    }

    /**
     *
     * @param map mapname
     * @return specified map
     */
    public boolean loadMap(String map) {
        reset();
        loader.setMap(map);

        loaded = loader.load(map);
        return loaded;
    }

    public void setScoreRed(int score) {
        scoreRed = score;
    }

    public int getScoreRed() {
        return scoreRed;
    }

    public void setScoreBlue(int score) {
        scoreBlue = score;
    }

    public int getScoreBlue() {
        return scoreBlue;
    }

    public void setRoundTime(long time) {
        roundTime = time;
    }

    public long getRoundTime() {
        return roundTime;
    }

    public void setRespawnTime(long time) {
        respawnTime = time;
    }

    public long getRespawnTime() {
        return respawnTime;
    }

    private void checkPlayerInput(UpdateLoop u) {
        CPlayer self = getSelf();
        Viewport port = Main.getScreen().getGameScene();
        Network net = Main.getNetwork();

        if(self == null || !net.isReallyConnected() || port == null)
            return;

        if(input.isKeyNPressed())
            port.setTeamSelectionVisible(true);

        if(input.isKeyBPressed() && port.isTeamSelectionVisible()) {
            port.setTeamSelectionVisible(false);
            net.send(ProtocolCmd.CLIENT_SERVER_JOINTEAM, argInt(CPlayer.TEAM_RED));
        }

        if(input.isKeyMPressed() && port.isTeamSelectionVisible()) {
            port.setTeamSelectionVisible(false);
            net.send(ProtocolCmd.CLIENT_SERVER_JOINTEAM, argInt(CPlayer.TEAM_BLUE));
        }

        if(input.isKeyEPressed())
            port.setStatsVisible(true);
        else
            port.setStatsVisible(false);
        
        if(self.isDead())
            return;

        if(input.wasMouseWheelMoveUp()) {
            int cur = self.getCurrentWeapon();
            if(cur == 0)
                self.setCurrentWeapon(2);
            else
                self.setCurrentWeapon(self.getCurrentWeapon() - 1);
            port.setWeaponSelectionVisible(true);
            u.resetCountdown(Main.getWeaponCountdown());
            self.setLockedShoot(true);
        }

        if(input.wasMouseWheelMoveDown()) {
            int cur = self.getCurrentWeapon();
            if(cur == 2)
                self.setCurrentWeapon(0);
            else
                self.setCurrentWeapon(self.getCurrentWeapon() + 1);
            port.setWeaponSelectionVisible(true);
            u.resetCountdown(Main.getWeaponCountdown());
            self.setLockedShoot(true);
        }
        
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

        net.send(ProtocolCmd.CLIENT_SERVER_PLAYER_INFO, argInt(id),
                argInt(weapon), argDouble(posX), argDouble(posY), argDouble(dirX),
                argDouble(dirY));

        if(input.isKeyM1Pressed()) {
            CProjectile c = null;
            if((c = self.shoot(u)) != null) {
                Main.getProjectileManager().addProjectile(c);

                net.send(ProtocolCmd.CLIENT_SERVER_SHOOT, argInt(c.getId()),
                        argInt(self.getCurrentWeapon()), argDouble(c.getDirection().getX()),
                        argDouble(c.getDirection().getY()), argDouble(c.getOrigin().getX()),
                        argDouble(c.getOrigin().getY()));
            }
        }


    }

    /**
     *
     * @param u UpdateLoop
     */
    public void update(UpdateLoop u) {
        checkPlayerInput(u);
        Main.getProjectileManager().checkProjectiles(u, player, getMap());
    }

    public void countdown(UpdateCountdown u) {
        Viewport port = Main.getScreen().getGameScene();

        if(u.getName().equals("weapon")) {
            if(port != null)
                port.setWeaponSelectionVisible(false);
            GameData g = Main.getGameData();
            if(g != null) {
                CPlayer p = g.getSelf();
                if(p != null)
                    p.setLockedShoot(false);
            }
        }
    }

    /**
     * Get Map
     * @return CMap
     */
    public CMap getMap() {
        return loader.getMap();
    }

    /**
     * Get self
     * @return CPlayer
     */
    public CPlayer getSelf() {
        return getPlayer(selfId);
    }

    /**
     * Add a Player
     * @param p CPlayer
     */
    public void addPlayer(CPlayer p) {
        int idx = p.getId();
        if(idx >= 0 && idx < player.length)
            player[idx] = p;
    }

    /**
     * Remove a player
     * @param p CPlayer
     */
    public void removePlayer(CPlayer p) {
        int idx = p.getId();
        if(idx >= 0 && idx < player.length)
            player[idx] = null;
    }

    /**
     * Get player
     * @param i player id
     * @return CPlayer
     */
    public CPlayer getPlayer(int i) {
        if(player != null && i < player.length)
            return player[i];
        return null;
    }

    /**
     * Get all players
     * @return CPlayer (array)
     */
    public CPlayer[] getPlayers() {
        return player;
    }

    /**
     * Set maximum players
     * @param max maximum amount of players
     */
    public void setMaxPlayers(int max) {
        player = new CPlayer[max];
    }

    /**
     * Set self id
     * @param id
     */
    public void setSelfId(int id) {
        selfId = id;
    }

    /**
     * Get self id
     * @return selfId (int)
     */
    public int getSelfId() {
        return selfId;
    }

    /**
     * Get name
     * @return name (String)
     */
    public String getName() {
        return name;
    }

    /**
     * Set name
     * @param n (String)
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
     * @return true: Game data was loaded, false: Game data was not loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    public void addGameEvent(Event e) {
        synchronized(gameEvents) {
            if(gameEvents.size() >= 7)
                gameEvents.remove(0);
            gameEvents.add(e);
        }
    }

    public void removeGameEvent() {
        synchronized(gameEvents) {
            if(gameEvents.size() > 0)
                gameEvents.remove(0);
        }
    }

    public ArrayList<Event> getGameEvents() {
        return gameEvents;
    }

    public void addChatEvent(Event e) {
        synchronized(chatEvents) {
            if(chatEvents.size() >= 7)
                chatEvents.remove(0);
            chatEvents.add(e);
        }
    }

    public void removeChatEvent() {
        synchronized(chatEvents) {
            if(chatEvents.size() > 0)
                chatEvents.remove(0);
        }
    }

    public ArrayList<Event> getChatEvents() {
        return chatEvents;
    }
}
