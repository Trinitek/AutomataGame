package com.daexsys.automata.world.structures;

import com.daexsys.automata.world.tiletypes.TileType;

public class StructureElement {

    private TileType type;
    private int x;
    private int y;

    public StructureElement(TileType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public TileType getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return getX() + " " + getY();
    }
}
