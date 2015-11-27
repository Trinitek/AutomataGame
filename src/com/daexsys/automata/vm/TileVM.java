package com.daexsys.automata.vm;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.TileCoordinate;
import com.daexsys.automata.world.TileType;
import com.daexsys.automata.world.WorldModel;

/**
 * Virtual machine for a single tile automaton
 */
public class TileVM {

    private Tile tile;
    private byte accumulationRegister;
    private byte instructionRegister;

    public TileVM(Tile tile) {
        this.tile = tile;

        accumulationRegister = 0;
        instructionRegister = 0;
    }

    public void processInstruction(byte instruction, VMArgument... arguments) {
        tile.lazyInit(); // Just in case

        byte[] memory = tile.getTileData();

        if(instruction == VMBytecodes.MOV) {
            byte pointer = arguments[0].getValue(); // first byte HAS to be pointer

            VMArgument argument2 = arguments[1];

            if(argument2.getVmArgumentType() == VMArgumentType.LITERAL) {
                memory[pointer] = arguments[1].getValue();
            }

            else if(argument2.getVmArgumentType() == VMArgumentType.POINTER) {
                memory[pointer] = memory[arguments[1].getValue()];
            }
        }

        else if(instruction == VMBytecodes.ADD) {
            if(arguments[0].getVmArgumentType() == VMArgumentType.LITERAL) {
                accumulationRegister += arguments[0].getValue();
            }

            else if(arguments[0].getVmArgumentType() == VMArgumentType.POINTER) {
                accumulationRegister += memory[arguments[0].getValue()];
            }
        }

        else if(instruction == VMBytecodes.SUB) {
            if(arguments[0].getVmArgumentType() == VMArgumentType.LITERAL) {
                accumulationRegister -= arguments[0].getValue();
            }

            else if(arguments[0].getVmArgumentType() == VMArgumentType.POINTER) {
                accumulationRegister -= memory[arguments[0].getValue()];
            }
        }
    }

    public byte getAccumulationRegister() {
        return accumulationRegister;
    }

    public byte getInstructionRegister() {
        return instructionRegister;
    }

    public static void main(String[] args) {
        Tile tile = new Tile(new TileCoordinate(null, 0, 0), new TileType((byte) 0, "", null, 100));
        TileVM tileVM = new TileVM(tile);

        tileVM.processInstruction(VMBytecodes.MOV,
                new VMArgument(VMArgumentType.POINTER, 100), new VMArgument(VMArgumentType.LITERAL, 6));

        tileVM.processInstruction(VMBytecodes.MOV,
                new VMArgument(VMArgumentType.POINTER, 101), new VMArgument(VMArgumentType.LITERAL, 12));

        tileVM.processInstruction(VMBytecodes.ADD, new VMArgument(VMArgumentType.POINTER, 100));
        tileVM.processInstruction(VMBytecodes.ADD, new VMArgument(VMArgumentType.POINTER, 100));
        tileVM.processInstruction(VMBytecodes.SUB, new VMArgument(VMArgumentType.POINTER, 101));

        System.out.println(tileVM.getAccumulationRegister());
    }

    public void updateVM() {
        WorldModel worldModel = tile.getCoordinate().worldModel;

        try {
            Tile left = worldModel.getTileAt(tile.getCoordinate().x + 1, tile.getCoordinate().y);
            Tile right = worldModel.getTileAt(tile.getCoordinate().x - 1, tile.getCoordinate().y);
        } catch (Exception access) {}
    }

    /**
     * atm:
     *
     * 0 - 100 is where instructions should go
     * 100 - 200 is where data goes
     *
     * 200 - 255 is 'driver' related. specifically:
     *
     * 248: block that IS above
     * 249: block that IS below
     * 250: block that IS to the left
     * 251: block that IS to the right
     * 252: block to be placed above
     * 253: block to be placed below
     * 254: block to be placed to the left
     * 255: block to be placed to the right
     */

    /**
     * Things needed:
     *
     * More registers (maybe? i mean this thing is entirely virtual)
     * Some method of detecting nearby blocks, both immediately nearby, and far away
     * A method of creating/destroying blocks near this one
     * A method of managing the internal inventory
     * A method of manipulating memory and performing math operations in a way similar to a standard computer
     */
}
