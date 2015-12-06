package com.daexsys.automata.energy;

import com.daexsys.automata.world.TileCoord;

public interface Emission {

    void emit();

    boolean isInRange(TileCoord tileCoord);

    EnergyTransfer getEnergyTransfer();
}
