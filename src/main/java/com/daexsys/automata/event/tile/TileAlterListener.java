package com.daexsys.automata.event.tile;

import com.daexsys.automata.event.Listener;

public interface TileAlterListener extends Listener {

    public void tileAlter(TileAlterEvent tileAlterEvent);
}
