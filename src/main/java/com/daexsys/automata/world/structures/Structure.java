package com.daexsys.automata.world.structures;

import com.daexsys.automata.world.World;
import com.daexsys.automata.world.WorldLayers;
import com.daexsys.automata.world.tiletypes.TileType;

import java.util.ArrayList;
import java.util.List;

public class Structure {

    private List<StructureElement> structureElements = new ArrayList<>();

    public Structure() {}

    public Structure(List<StructureElement> elements) {
        structureElements.addAll(elements);
    }

    public Structure(int rowLength, int[] elements, TileType trueValue) {
        for (int i = 0; i < elements.length; i++) {
            int column = i % rowLength;
            int row = i / rowLength;

            if(elements[i] == 1)
                structureElements.add(new StructureElement(trueValue, column, row));
        }
    }

    public List<StructureElement> getStructureElements() {
        return structureElements;
    }

    public void placeInWorldAt(World world, int x, int y) {
        for(StructureElement structureElement : structureElements) {
            world.setTileTypeAt(WorldLayers.GROUND,
                    x + structureElement.getX(),
                    y + structureElement.getY(),
                    structureElement.getType()
            );
        }
    }
}
