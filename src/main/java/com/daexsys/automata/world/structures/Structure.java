package com.daexsys.automata.world.structures;

import com.daexsys.automata.event.tile.TileAlterCause;
import com.daexsys.automata.world.World;
import com.daexsys.automata.world.WorldLayer;
import com.daexsys.automata.world.tiletypes.TileType;

import java.util.ArrayList;
import java.util.List;

public class Structure {

    private List<StructureElement> structureElements = new ArrayList<>();
    private int rowLength;

    public Structure() {}

    public Structure(List<StructureElement> elements) {
        structureElements.addAll(elements);
    }

    public Structure(int rowLength, int[] elements, TileType... trueValue) {
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
                    TileAlterCause.PLAYER_EDIT
            );
        }
    }
}
