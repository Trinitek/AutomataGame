package com.daexsys.automata;

import com.daexsys.automata.world.structures.Structure;
import com.daexsys.automata.world.tiletypes.TileType;

public class PlayerState {

    private Game game;
    private Structure selectedStructure = null;
    private PlayerInventory inventory;

    public PlayerState(Game game) {
        this.game = game;
        inventory = new PlayerInventory(this, 0);
        selectedStructure = game.getStructures().getStructureByName("vanilla:stone_pointer");
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    public Game getGame() {
        return game;
    }

    public TileType getInHand() {
        return getInventory().getInHand();
    }

    public Structure getSelectedStructure() {
        return selectedStructure;
    }

    public void setSelectedStructure(Structure structure) {
        this.selectedStructure = structure;
    }
}
