package com.daexsys.automata.world.structures;

import com.daexsys.automata.world.tiletypes.TileType;

import java.util.HashMap;
import java.util.Map;

public class StructureRegistry {

    private Map<String, Structure> structureMap = new HashMap<>();

    public StructureRegistry() {
        /* Add default structures */
        registerStructure(new ExplosionStructure());

        registerStructure(new Structure("cgol_glider", 3, new int[] {
                0, 1, 0,
                0, 0, 1,
                1, 1, 1
        }, TileType.CGOL));

        registerStructure(new Structure("cgol_glider", 5, new int[]{
                1, 0, 0, 1, 0,
                0, 0, 0, 0, 1,
                1, 0, 0, 0, 1,
                0, 1, 1, 1, 1,
        }, TileType.CGOL));

        registerStructure(new Structure("cgol_r-pentomino", 3, new int[]{
                0, 1, 1,
                1, 1, 0,
                0, 1, 0
        }, TileType.CGOL));

        registerStructure(new Structure("vanilla:stone-pointer", 7, new int[] {
                0, 1, 1, 0, 0, 0, 0,
                1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 0, 0, 0,
                0, 1, 1, 0, 0, 0, 0
        }, TileType.STONE));

        registerStructure(new Structure("vanilla:bush", 4, new int[] {
                0, 1, 1, 0,
                1, 1, 1, 1,
                0, 1, 1, 0,
                0, 2, 2, 0
        }, TileType.LEAVES, TileType.WOOD));

        registerStructure(new Structure("vanilla:pine_tree", 5, new int[]{
                0, 0, 1, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                1, 1, 1, 1, 1,
                0, 0, 2, 0, 0
        }, TileType.LEAVES, TileType.WOOD));

//        TileType cottonCandy = new TileType.Builder().setID((byte) 245).setName("cotton candy")
//                .setEnergy(5000).setDecayRate(2).setImageLocation("images/cottencandy.png").build();
//        structureMap.put("vanilla:cotton_candy", new Structure(5, new int[] {
//                0, 0, 1, 0, 0,
//                0, 1, 1, 1, 1,
//                0, 1, 1, 1, 0,
//                1, 1, 1, 1, 0,
//                0, 0, 1, 0, 0
//        }, cottonCandy));
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
    public boolean registerStructure(Structure structure) {
        if(!structureMap.containsKey(structure.getName())) {
            this.structureMap.put(structure.getName(), structure);
            return true;
        }

        return false;
    }

    public int structuresRegistered() {
        return structureMap.size();
    }
}
