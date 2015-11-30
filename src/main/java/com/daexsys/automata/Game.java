package com.daexsys.automata;

import com.daexsys.automata.world.tiletypes.TileTypes;
import com.daexsys.automata.world.World;

public class Game {

    private boolean isPaused = false;
    private World worldModel;
    private PlayerState playerState = new PlayerState();

    private volatile int tickDelayRate = 250; // 500 default

    public Game() {
        worldModel = new World(100);

        Thread worldPule = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(tickDelayRate);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!isPaused) worldModel.pulse();

                }
            }
        });
        worldPule.start();
    }

    public World getWorldModel() {
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
