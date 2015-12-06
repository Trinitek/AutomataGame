package com.daexsys.automata.energy;

/**
 * A representation of a transfer of energy.
 *
 * This encompasses every type of energy transfer in the game,
 * from solar energy hitting the ground, to geothermal energy
 * bubbling in geysers, to energy emitted from energy towers.
 *
 * Note that one energy transfer can be applied multiple times.
 * It only represents the concept of a transfer, not one specific
 * block-to-block exchange.
 */
public class EnergyTransfer {

    /**
     * The amount of energy transferred
     */
    private int amount;

    /**
     * The type of energy transferred
     */
    private EnergyType energyType;


    public EnergyTransfer(EnergyType energyType, int amount) {
        this.amount = amount;
        this.energyType = energyType;
    }

    public int getAmount() {
        return amount;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    @Override
    public String toString() {
        return "{energy transfer: amount=" + getAmount() + ", type=" + getEnergyType() + "}";
    }
}
