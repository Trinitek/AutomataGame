package com.daexsys.automata.world.structures;

import com.daexsys.automata.world.tiletypes.TileType;

import java.util.HashMap;
import java.util.Map;

public class StructureRegistry {

    private Map<String, Structure> structureMap = new HashMap<>();

    public StructureRegistry() {
        /* Add default structures */

        structureMap.put("cgol_glider", new Structure(3, new int[] {
                0, 1, 0,
                0, 0, 1,
                1, 1, 1
        }, TileType.CGOL));

        structureMap.put("cgol_lwss", new Structure(5, new int[] {
                1, 0, 0, 1, 0,
                0, 0, 0, 0, 1,
                1, 0, 0, 0, 1,
                0, 1, 1, 1, 1,
        }, TileType.CGOL));

        structureMap.put("cgol_r-pentomino", new Structure(3, new int[] {
                0, 1, 1,
                1, 1, 0,
                0, 1, 0
        }, TileType.CGOL));

        structureMap.put("vanilla:stone-pointer", new Structure(7, new int[] {
                0, 1, 1, 0, 0, 0, 0,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 0, 0, 0,
                0, 1, 1, 0, 0, 0, 0
        }, TileType.STONE));

        structureMap.put("vanilla:bush", new Structure(4, new int[] {
                0, 1, 1, 0,
                1, 1, 1, 1,
                0, 1, 1, 0,
                0, 2, 2, 0
        }, TileType.LEAVES, TileType.WOOD));

        structureMap.put("vanilla:pine_tree", new Structure(5, new int[] {
                0, 0, 1, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                1, 1, 1, 1, 1,
                0, 0, 2, 0, 0
        }, TileType.LEAVES, TileType.WOOD));
    }

    public Structure getStructureByName(String name) {
        return structureMap.get(name);
    }

    /**
     * Registers a structure.
     *
     * @return whether or not the structure was successfully registered.
     * If it wasn't, it was because something was already there of the same name.
     */
    public boolean registerStructure(String structureName, Structure structure) {
        if(!structureMap.containsKey(structureName)) {
            this.structureMap.put(structureName, structure);
            return true;
        }

        return false;
    }

    public int structuresRegistered() {
        return structureMap.size();
    }
}
