package com.daexsys.automata;

import com.daexsys.automata.gui.GUI;
import com.daexsys.automata.world.TileType;
import com.daexsys.automata.world.TileTypes;
import com.daexsys.automata.world.WorldModel;

public class PlayerState {

    private TileType inHand = TileTypes.AUTOMATA_SIMPLE;

    public TileType getInHand() {
        return inHand;
    }

    public void setInHand(TileType inHand) {
        this.inHand = inHand;
    }

    public void placeBlock(int screenX, int screenY, GUI gui) {
        WorldModel worldModel = gui.getGame().getWorldModel();

        int tx = (screenX - gui.getOffsets().getOffsetX()) / gui.getZoomLevel();
        int ty = (screenY - gui.getOffsets().getOffsetY()) / gui.getZoomLevel();

        worldModel.setTileTypeAt(tx, ty, gui.getGame().getPlayerState().getInHand());
    }
}
