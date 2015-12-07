package com.daexsys.automata.energy;

import com.daexsys.automata.Game;
import com.daexsys.automata.Tile;
import com.daexsys.automata.world.tiletypes.TileType;

public class SolarEmission implements Emission {

    private int sunStrength = 0;
    private EnergyTransfer theSun = new EnergyTransfer(EnergyType.SOLAR, 0) {
        @Override
        public int getAmount() {
            return sunStrength;
        }
    };

    @Override
    public boolean isApplicableFor(Tile tile) {
        return tile.getType() == TileType.GRASS
                || tile.getType() == TileType.DIRT
                || tile.getType() == TileType.TALL_GRASS
                || tile.getType() == TileType.SOLAR_PANEL;
    }

    @Override
    public void apply(Tile tile) {
        Game game = tile.getWorld().getGame();
        sunStrength = game.getTime();
        tile.giveEnergy(theSun);
    }

    @Override
    public EnergyTransfer getEnergyTransfer() {
        return theSun;
    }
}
