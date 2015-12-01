package com.daexsys.automata.world.structures;

import com.daexsys.automata.world.tiletypes.TileTypes;

import java.util.HashMap;
import java.util.Map;

public class Structures {

    private Map<String, Structure> structureMap = new HashMap<>();

    public Structures() {
        structureMap.put("cgol_glider", new Structure(3, new int[] {
                0, 1, 0,
                0, 0, 1,
                1, 1, 1
        }, TileTypes.CGOL));

        structureMap.put("cgol_lwss", new Structure(5, new int[] {
                1, 0, 0, 1, 0,
                0, 0, 0, 0, 1,
                1, 0, 0, 0, 1,
                0, 1, 1, 1, 1,
        }, TileTypes.CGOL));

        structureMap.put("cgol_r-pentomino", new Structure(3, new int[] {
                0, 1, 1,
                1, 1, 0,
                0, 1, 0
        }, TileTypes.CGOL));
    }

    public Structure getStructureByName(String name) {
        return structureMap.get(name);
    }
}
