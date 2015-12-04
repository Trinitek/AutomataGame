package com.daexsys.automata.event.game;

import com.daexsys.automata.Game;
import com.daexsys.automata.event.Event;

public class TickEvent implements Event {

    private Game game;

    public TickEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
