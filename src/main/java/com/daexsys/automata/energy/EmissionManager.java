package com.daexsys.automata.energy;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.TileCoord;

import java.util.ArrayList;
import java.util.List;

public class EmissionManager {

    private List<Emission> activeEmissions = new ArrayList<>();
    { // Default emissions
        activeEmissions.add(new SolarEmission());
    }

    public void addEmission(Emission emmission) {
        activeEmissions.add(emmission);
    }

    public void removeEmission(Emission emission) {
        activeEmissions.remove(emission);
    }

    public void collectFor(Tile tile) {
        for(Emission emission : activeEmissions) {
            if(emission.isApplicableFor(tile)) {
                emission.apply(tile);
            }
        }
    }
}
