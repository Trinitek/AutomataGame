package com.daexsys.automata;

import com.daexsys.automata.event.Event;
import com.daexsys.automata.event.Listener;
import com.daexsys.automata.event.chat.ChatMessageEvent;
import com.daexsys.automata.event.chat.ChatMessageListener;
import com.daexsys.automata.event.game.TickEvent;
import com.daexsys.automata.event.game.TickListener;
import com.daexsys.automata.event.tile.TileAlterEvent;
import com.daexsys.automata.event.tile.TileAlterListener;
import com.daexsys.automata.world.structures.StructureRegistry;
import com.daexsys.automata.world.World;
import com.daexsys.automata.world.tiletypes.TileRegistry;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private boolean isPaused = false;
    private World world;

    private TileRegistry tileRegistry;
    private StructureRegistry structures;

    private ChatManager chatManager;

    private PlayerState playerState;
    private volatile int tickDelayRate = 1000; // 500 default

    private int lastTPS = 0;
    private long lastTPSTime = System.currentTimeMillis();
    private int tps = 0;

    public Game() {
        world = new World(this);

        tileRegistry = new TileRegistry();
        structures = new StructureRegistry();

        chatManager = new ChatManager(this);

        playerState = new PlayerState(this);

        final Game thisGame = this;

        Thread worldPule = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(tickDelayRate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(!isPaused) {
                    world.pulse();

                    TickEvent tickEvent = new TickEvent(thisGame);
                    fireEvent(tickEvent);
                    tps++;

                    if(System.currentTimeMillis() > lastTPSTime + 1000) {
                        lastTPSTime = System.currentTimeMillis();
                        lastTPS = tps;
                        tps = 0;
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

    private List<TileAlterListener> tileAlterListenerList = new ArrayList<>();
    private List<ChatMessageListener> chatMessageListenerList = new ArrayList<>();
    private List<TickListener> tickListeners = new ArrayList<>();
    public void fireEvent(Event event) {
        if(event instanceof TileAlterEvent) {
            for(TileAlterListener tileAlterListener : tileAlterListenerList) {
                tileAlterListener.tileAlter((TileAlterEvent) event);
            }
        } else if(event instanceof ChatMessageEvent) {
            for(ChatMessageListener chatMessageListener : chatMessageListenerList) {
                chatMessageListener.chatMessage((ChatMessageEvent) event);
            }
        } else if(event instanceof TickEvent) {
            for(TickListener tickListener : tickListeners) {
                tickListener.tickOccur((TickEvent) event);
            }
        }
    }

    public void addListener(Listener listener) {
        if(listener instanceof TileAlterListener) {
            tileAlterListenerList.add((TileAlterListener) listener);
        } else if(listener instanceof ChatMessageListener) {
            chatMessageListenerList.add((ChatMessageListener) listener);
        } else if(listener instanceof TickListener) {
            tickListeners.add((TickListener) listener);
        }
    }

    public ChatManager getChatManager() {
        return chatManager;
    }
}
