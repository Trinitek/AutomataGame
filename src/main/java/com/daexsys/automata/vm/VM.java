package com.daexsys.automata.vm;

public interface VM {

    void initialize();
    byte[] getRam();
    void step();
    String regsToString();
}
