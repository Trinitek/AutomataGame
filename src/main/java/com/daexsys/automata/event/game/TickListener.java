package com.daexsys.automata.event.game;

import com.daexsys.automata.event.Listener;

public interface TickListener extends Listener {

    public void tickOccur(TickEvent tickEvent);
}
