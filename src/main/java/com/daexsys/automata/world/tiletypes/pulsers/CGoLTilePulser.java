package com.daexsys.automata.world.tiletypes.pulsers;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.tiletypes.TilePulser;
import com.daexsys.automata.world.tiletypes.TileType;

import java.util.ArrayList;
import java.util.List;

public class CGoLTilePulser implements TilePulser {

    private List<Integer> birthNums = new ArrayList<>();
    private List<Integer> stayAliveNums = new ArrayList<>();

    public CGoLTilePulser(List<Integer> birthNums, List<Integer> stayAliveNums) {
        this.birthNums = birthNums;
        this.stayAliveNums = stayAliveNums;
    }

    @Override
    public void pulse(Tile tile) {
        List<Tile> neighbors = tile.getMooreNeighborhood(tile.getCoordinate().layer);
        int number = 0;

        for (Tile iteratedTile : neighbors) {
            if (iteratedTile.getType() == tile.getType()) {
                number++;
            } else {
                int amount = iteratedTile.getMooreNeighborhoodEqualTo(tile.getCoordinate().layer, tile.getType());

                for(Integer i : birthNums) {
                    if(i == amount) {
                        iteratedTile.getCoordinate().queueChange(tile.getType());
                    }
                }
            }
        }

        boolean found = false;
        for(Integer i : stayAliveNums) {
            if(i == number) {
                found = true;
            }
        }

        if(!found) {
            tile.getCoordinate().queueChange(TileType.DIRT);
        }
    }
}
