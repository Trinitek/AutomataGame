package com.daexsys.automata;

import com.daexsys.automata.world.tiletypes.TileType;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private Map<TileType, Integer> amounts = new HashMap<TileType, Integer>();
    private int amountStored = 0;

    public Inventory() {}

    public void addItem(TileType tileType) {
        int currentAmount = amounts.getOrDefault(tileType, 0);
        amounts.put(tileType, currentAmount + 1);
        amountStored++;
    }

    public void removeItem(TileType tileType) {
        int currentAmount = amounts.getOrDefault(tileType, 0);
        if(currentAmount > 0) {
            amounts.put(tileType, currentAmount - 1);
            amountStored--;
        }
    }

    public int getAmountStoredOfItem(TileType tileType) {
        return amounts.getOrDefault(tileType, 0);
    }

    public int getAmountStored() {
        return amountStored;
    }
}
