package com.daexsys.automata.energy;

import com.daexsys.automata.Tile;

public interface Emission {

    void apply(Tile tile);

    boolean isApplicableFor(Tile tileCoord);

    EnergyTransfer getEnergyTransfer();
}
