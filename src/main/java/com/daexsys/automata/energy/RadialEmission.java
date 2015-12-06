package com.daexsys.automata.energy;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.TileCoord;

public class RadialEmission implements Emission {

    private EnergyTransfer energyTransfer;

    private int range;

    private TileCoord origin;

    private boolean isEmitted;

    public RadialEmission(EnergyTransfer energyTransfer, TileCoord origin, int range) {
        this.energyTransfer = energyTransfer;
        this.origin = origin;
        this.range = range;

        isEmitted = false;
    }

    public int getRange() {
        return range;
    }

    public TileCoord getOrigin() {
        return origin;
    }

    public EnergyTransfer getEnergyTransfer() {
        return energyTransfer;
    }

    public void emit() {
        int range = getRange();
        int startX = getOrigin().x;
        int startY = getOrigin().y;

        for (int fx = startX - range; fx < startX + range; fx++) {
            for (int fy = startY - range; fy < startY + range; fy++) {
                int xx = fx - startX;
                int yy = fy - startY;

                if((xx * xx + yy * yy) < range * range) {
                    Tile tile = getOrigin().getChunk().getChunkCoordinate().getWorld().getTileAt(getOrigin().layer, xx, yy);
                    tile.setEnergy(tile.getEnergy() + getEnergyTransfer().getAmount());
                }
            }
        }

        isEmitted = true;
    }

    @Override
    public boolean isInRange(TileCoord tileCoord) {
        if(tileCoord.distance(getOrigin()) < getRange()) {
            Tile tile = tileCoord.getTile().get();
        }
        return false;
    }

    public boolean isEmitted() {
        return isEmitted;
    }

    @Override
    public String toString() {
        return "{emission: range=" + range + ", transfer=" + getEnergyTransfer() + ", around=" +  getOrigin() + "}";
    }
}
