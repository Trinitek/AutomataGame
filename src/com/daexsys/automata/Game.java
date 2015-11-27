package com.daexsys.automata;

import com.daexsys.automata.world.AccessOutOfWorldException;
import com.daexsys.automata.world.TileTypes;
import com.daexsys.automata.world.WorldModel;

public class Game {

    private WorldModel worldModel;

    public Game() {
        worldModel = new WorldModel(50);

        placeGliderAt(worldModel, 5, 5);
        placeGliderAt(worldModel, 10, 5);

        placeGliderAt(worldModel, 5, 10);
        placeGliderAt(worldModel, 10, 10);

        Thread worldPule = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    worldModel.pulse();

                }
            }
        });
        worldPule.start();
//
//        worldModel.queueChangeAt(5, 4, Tiles.AUTOMATA_SIMPLE);
//        worldModel.queueChangeAt(5, 6, Tiles.AUTOMATA_SIMPLE);

        Tile tile = null;
        try {
            tile = worldModel.getTileAt(30, 30);
            System.out.println(tile.getTileType().getBlockName());
        } catch (AccessOutOfWorldException e) {
            e.printStackTrace();
        }
    }

    public WorldModel getWorldModel() {
        return worldModel;
    }

    public void placeGliderAt(WorldModel worldModel, int x, int y) {
        worldModel.setTileTypeAt(x - 1, y, TileTypes.AUTOMATA_SIMPLE);
        worldModel.setTileTypeAt(x, y, TileTypes.AUTOMATA_SIMPLE);
        worldModel.setTileTypeAt(x + 1, y, TileTypes.AUTOMATA_SIMPLE);
        worldModel.setTileTypeAt(x + 1, y - 1, TileTypes.AUTOMATA_SIMPLE);
        worldModel.setTileTypeAt(x, y - 2, TileTypes.AUTOMATA_SIMPLE);
    }
}
