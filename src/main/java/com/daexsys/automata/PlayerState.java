package com.daexsys.automata;

import com.daexsys.automata.world.tiletypes.TileType;
import com.daexsys.automata.world.tiletypes.TileTypes;

public class PlayerState {

    private TileType inHand = TileTypes.AUTOMATA_SIMPLE;

    public TileType getInHand() {
        return inHand;
    }

    public void setInHand(TileType inHand) {
        this.inHand = inHand;
    }
}
