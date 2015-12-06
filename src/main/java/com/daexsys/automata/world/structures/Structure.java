package com.daexsys.automata.world.structures;

import com.daexsys.automata.event.tile.TilePlacementReason;
import com.daexsys.automata.world.World;
import com.daexsys.automata.world.WorldLayer;
import com.daexsys.automata.world.tiletypes.TileType;

import java.util.ArrayList;
import java.util.List;

public class Structure {

    protected String name;
    private List<StructureElement> structureElements = new ArrayList<>();
    private int rowLength;

    public Structure(String name) { this.name = name; }

    public Structure(String name, List<StructureElement> elements) {
        structureElements.addAll(elements);
    }

    public Structure(String name, int rowLength, int[] elements, TileType... trueValue) {
        this.name = name;
        this.rowLength = rowLength;
        for (int i = 0; i < elements.length; i++) {
            int column = i % rowLength;
            int row = i / rowLength;

            if(elements[i] > 0)
                structureElements.add(new StructureElement(trueValue[elements[i] - 1], column, row));
        }
    }

    public List<StructureElement> getStructureElements() {
        return structureElements;
    }

    public void placeInWorldAt(World world, int x, int y, int... args) {
        for(StructureElement structureElement : structureElements) {
            world.setTileTypeAt(WorldLayer.GROUND,
                    x + structureElement.getX(),
                    y + structureElement.getY(),
                    structureElement.getType(),
                    TilePlacementReason.STRUCTURE_PLACEMENT
            );
        }
    }

    public String getName() {
        return name;
    }
}
