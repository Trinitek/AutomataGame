package com.daexsys.automata.energy;

public enum EnergyType {

    /* Broadcast by the sun. Only fertile tile types and solar panels can harvest this. */
    SOLAR,

    /* Can be harvested by putting geothermal energy collectors near tiles that emit this. */
    GEOTHERMAL,

    /* Produced by coal power planets */
    CHEMICAL,

    /* Produced by lights */
    LIGHT,

    /* Sent through player energy hardware */
    WIRED,

    /* Emitted by energy towers. Has a specific frequency per-player. */
    TOWER,

    /* Destructive excessive heat released by cells. */
    FREE_HEAT
}
