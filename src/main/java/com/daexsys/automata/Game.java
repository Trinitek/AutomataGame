package com.daexsys.automata;

import com.daexsys.automata.world.structures.StructureRegistry;
import com.daexsys.automata.world.World;
import com.daexsys.automata.world.tiletypes.TileRegistry;

public class Game {

    private boolean isPaused = false;
    private World world;

    private TileRegistry tileRegistry;
    private StructureRegistry structures;

    private ChatManager chatManager;

    private PlayerState playerState;
    private volatile int tickDelayRate = 250; // 500 default

    private int lastTPS = 0;
    private long lastTPSTime = System.currentTimeMillis();
    private int tps = 0;

    public Game() {
        world = new World(this);

        tileRegistry = new TileRegistry();
        structures = new StructureRegistry();

        chatManager = new ChatManager(this);

        playerState = new PlayerState(this);

        Thread worldPule = new Thread(new Runnable() {
            @Override
            public void run() {
            while(true) {
                try {
                    Thread.sleep(tickDelayRate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(!isPaused) {
                    world.pulse();
                    tps++;

                    if(System.currentTimeMillis() > lastTPSTime + 1000) {
                        lastTPSTime = System.currentTimeMillis();
                        lastTPS = tps;
                        tps = 0;
                    }
                }

            }
            }
        });
        worldPule.start();
    }

    /**
     * Client constructor
     */
    public Game(boolean t) {
        world = new World(this);

        tileRegistry = new TileRegistry();
        structures = new StructureRegistry();
        chatManager = new ChatManager(this);
        playerState = new PlayerState(this);
    }

    public int getTPS() {
        return lastTPS;
    }

    public World getWorld() {
        return world;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public TileRegistry getTileRegistry() {
        return tileRegistry;
    }

    public StructureRegistry getStructures() {
        return structures;
    }

    public int getTickDelayRate() {
        return tickDelayRate;
    }

    public void setTickDelayRate(int tickDelayRate) {
        if(tickDelayRate >= 0) {
            this.tickDelayRate = tickDelayRate;
        }
    }

    public ChatManager getChatManager() {
        return chatManager;
    }
}
