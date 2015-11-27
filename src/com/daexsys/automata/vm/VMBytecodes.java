package com.daexsys.automata.vm;

public class VMBytecodes {

    // Standard PC stuff
    public static final byte INT = 0x01;
    public static final byte MOV = 0x02;
    public static final byte JMP = 0x03;
    public static final byte ADD = 0x04;
    public static final byte SUB = 0x05;
    public static final byte MUL = 0x06;
    public static final byte DIV = 0x07;

    // World stuff
    public static final byte PLC = 0x4F;
}
