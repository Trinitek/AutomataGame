package com.daexsys.automata.world.structures;

import com.daexsys.automata.world.tiletypes.TileTypes;

public class Structures {

    public static final Structure CGOL_GLIDER = new Structure(3, new int[] {
            0, 1, 0,
            0, 0, 1,
            1, 1, 1
    }, TileTypes.CGOL);

    public static final Structure CGOL_LWSS = new Structure(5, new int[] {
            1, 0, 0, 1, 0,
            0, 0, 0, 0, 1,
            1, 0, 0, 0, 1,
            0, 1, 1, 1, 1,
    }, TileTypes.CGOL);

    public static void main(String[] args) {
        for(StructureElement structureElement : CGOL_GLIDER.getStructureElements()) {
            System.out.println(structureElement);
        }
    }
}
