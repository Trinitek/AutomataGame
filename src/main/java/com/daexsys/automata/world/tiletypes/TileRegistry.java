package com.daexsys.automata.world.tiletypes;

import java.util.HashMap;
import java.util.Map;

public final class TileRegistry {

    private Map<Byte, TileType> tileIDMap = new HashMap<>();
    private Map<String, TileType> tileNameMap = new HashMap<>();

    public void registerType(TileType tileType) {
        tileIDMap.put(tileType.getID(), tileType);
        tileNameMap.put(tileType.getName(), tileType);
    }

    public void unregisterType(TileType tileType) {
        tileIDMap.remove(tileType.getID());
        tileNameMap.remove(tileType.getName());
    }

    public TileType getByID(byte id) {
        return tileIDMap.get(id);
    }

    public TileType getByName(String name) {
        return tileNameMap.get(name);
    }

    public int getTilesRegistered() {
        return tileNameMap.size();
    }
}
