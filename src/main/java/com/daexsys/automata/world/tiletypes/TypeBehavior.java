package com.daexsys.automata.world.tiletypes;

public enum TypeBehavior {
    /**
     * Predictable by the client without having to receive updates from the server.
     */
    DETERMINISTIC,

    /**
     * All updates absolutely have to come from the server, because behavior is random.
     */
    NON_DETERMINISTIC
}
