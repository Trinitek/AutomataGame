package com.daexsys.automata.event.tile;

import com.daexsys.automata.event.Event;
import com.daexsys.automata.world.TileCoord;
import com.daexsys.automata.world.structures.Structure;
import com.google.common.base.Optional;

public class StructurePlacementEvent implements Event {

    private Structure structure;
    private TileCoord tileCoord;
    private int[] args;

    public StructurePlacementEvent(Structure structure, TileCoord tileCoord) {
        this.structure = structure;
        this.tileCoord = tileCoord;
    }

    public StructurePlacementEvent(Structure structure, TileCoord tileCoord, int[] args) {
        this.structure = structure;
        this.tileCoord = tileCoord;
        this.args = args;
    }

    public Optional<int[]> getArgs() {
        if(args != null) {
            return Optional.of(args);
        }

        return Optional.absent();
    }

    public Structure getStructure() {
        return structure;
    }

    public TileCoord getTileCoord() {
        return tileCoord;
    }
}
