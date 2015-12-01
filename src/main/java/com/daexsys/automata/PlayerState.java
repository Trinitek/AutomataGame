package com.daexsys.automata;

import com.daexsys.automata.world.structures.Structure;
import com.daexsys.automata.world.structures.Structures;
import com.daexsys.automata.world.tiletypes.TileType;
import com.daexsys.automata.world.tiletypes.TileTypes;

public class PlayerState {

    private Structure selectedStructure = Structures.CGOL_R_PENTOMINO;
    private TileType inHand = TileTypes.CGOL;

    public TileType getInHand() {
        return inHand;
    }

    public void setInHand(TileType inHand) {
        this.inHand = inHand;
    }

    public Structure getSelectedStructure() {
        return selectedStructure;
    }
}
