package com.daexsys.automata;

import com.daexsys.automata.world.tiletypes.TileType;

public class PlayerInventory {

    private int selectedSlot = 1;

    public PlayerInventory(int size) {

    }

    public void selectSlot(int slotNum) {
        selectedSlot = slotNum;
    }

    public TileType getInHand() {
        if(selectedSlot == 1)
            return TileType.CGOL;
        if(selectedSlot == 2)
            return TileType.GREEDY_VIRUS;
        if(selectedSlot == 3)
            return TileType.HIGH_LIFE;
        if(selectedSlot == 4)
            return TileType.AMOEBA;
        if(selectedSlot == 5)
            return TileType.SHOCKWAVE_VIRUS;
        if(selectedSlot == 6)
            return TileType.GRASS;
        if(selectedSlot == 7)
            return TileType.WATER;
        if(selectedSlot == 8)
            return TileType.BOMB;
        if(selectedSlot == 9)
            return TileType.BOT;

        return null;
    }
}
