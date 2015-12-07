package com.daexsys.automata.world.tiletypes.pulsers;

import com.daexsys.automata.Tile;
import com.daexsys.automata.energy.Emission;
import com.daexsys.automata.energy.EnergyTransfer;
import com.daexsys.automata.energy.EnergyType;
import com.daexsys.automata.energy.RadialEmission;
import com.daexsys.automata.world.TileCoord;
import com.daexsys.automata.world.tiletypes.TilePulser;
import com.daexsys.automata.world.tiletypes.TileType;

import java.util.List;

public class EnergyTowerPulser implements TilePulser {

    @Override
    public void pulse(Tile t) {
        int energyHarvest = harvestEnergyInRange(t, 5);

        Emission emission = new RadialEmission(
                new EnergyTransfer(EnergyType.TOWER, energyHarvest), t.getCoordinate(), 50);

        t.getWorld().getGame().getEmissionManager().addEmission(emission);
    }

    private int harvestEnergyInRange(Tile tile, int range) {
        int energyHarvested = 0;

        TileCoord coord = tile.getCoordinate();

        List<Tile> tiles = coord.world.getTilesInRange(tile.getCoordinate(), range);
        for(Tile t : tiles) {
            if(t.getType() == TileType.SOLAR_PANEL) {
                energyHarvested += t.getEnergy();
                t.setEnergy(0);
            }
        }

        return energyHarvested;
    }
}
