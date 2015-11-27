package com.daexsys.automata.vm;

public class VMArgument {

    private VMArgumentType vmArgumentType;
    private int value;

    public VMArgument(VMArgumentType vmArgumentType, int value) {
        this.vmArgumentType = vmArgumentType;
        this.value = value;
    }

    public VMArgumentType getVmArgumentType() {
        return vmArgumentType;
    }

    public byte getValue() {
        return (byte) value;
    }
}
