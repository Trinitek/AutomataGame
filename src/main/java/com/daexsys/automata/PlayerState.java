package com.daexsys.automata;

import com.daexsys.automata.world.structures.Structure;
import com.daexsys.automata.world.structures.Structures;
import com.daexsys.automata.world.tiletypes.TileType;
import com.daexsys.automata.world.tiletypes.TileTypes;

public class PlayerState {

    private Game game;
    private Structure selectedStructure;
    private TileType inHand = TileTypes.CGOL;

    public PlayerState(Game game) {
        this.game = game;
        selectedStructure = game.getStructures().getStructureByName("cgol_glider");
    }

    public Game getGame() {
        return game;
    }

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
