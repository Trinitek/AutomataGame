package com.daexsys.automata;

import com.daexsys.automata.world.structures.Structures;
import com.daexsys.automata.world.tiletypes.TileTypes;
import com.daexsys.automata.world.World;

public class Game {

    private boolean isPaused = false;
    private World worldModel;
    private Structures structures;
    private PlayerState playerState;

    private volatile int tickDelayRate = 250; // 500 default

    private int lastTPS = 0;
    private long lastTPSTime = System.currentTimeMillis();
    private int tps = 0;

    public Game() {
        worldModel = new World(100);
        structures = new Structures();
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
                    worldModel.pulse();
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

    public int getTPS() {
        return lastTPS;
    }

    public World getWorld() {
        return worldModel;
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

    public Structures getStructures() {
        return structures;
    }

    public int getTickDelayRate() {
        return tickDelayRate;
    }

    public void setTickDelayRate(int tickDelayRate) {
        if(tickDelayRate > 0) {
            this.tickDelayRate = tickDelayRate;
        }

        System.out.println(tickDelayRate);
    }
}
