package com.daexsys.automata.world.tiletypes;

import com.daexsys.automata.Tile;

import java.util.ArrayList;
import java.util.List;

public class CGoLTileType extends TileType {

    public CGoLTileType(byte id, String blockName, String imageUrl, int defaultEnergy, int defaultDecayRate) {
        super(id, blockName, imageUrl, defaultEnergy, defaultDecayRate);
    }

    @Override
    public void pulse(Tile tile) {
        List<Tile> neighborOptional = new ArrayList<>(tile.getNeighbors(0));
        int number = 0;

        for (Tile t : neighborOptional) {
            if (t != null) {
                if (t.getType() == TileType.CGOL) {
                    number++;
                }

                else {
                    List<Tile> neighborOptional2 = t.getNeighbors(0);
                    int number2 = 0;

                    for (Tile t2 : neighborOptional2) {
                        if (t2 != null) {
                            if (t2.getType() == TileType.CGOL) {
                                number2++;
                            }
                        }
                    }

                    if(number2 == 3) {
                        t.getCoordinate().queueChange(TileType.CGOL);
                    }
                }
            }
        }

        if(number != 2 && number != 3) {
            tile.getCoordinate().queueChange(TileType.DIRT);
        }
    }
}
