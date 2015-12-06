package com.daexsys.automata.event.tile;

import com.daexsys.automata.event.Listener;

public interface StructurePlacementListener extends Listener {

    public void structurePlace(StructurePlacementEvent structurePlacementEvent);
}
