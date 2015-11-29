package com.daexsys.automata.vm;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.TileCoordinate;
import com.daexsys.automata.world.TileType;
import com.daexsys.automata.world.WorldModel;

import static java.lang.System.arraycopy;

/**
 * Virtual machine for a single tile automaton
 */
public class TileVM {

    private Tile tile;
    private byte accumulationRegister;
    private byte instructionRegister;

    private int regA, regB, regX, regS, regF, regP, regTS, regTD;

    private byte[] ram;

    public TileVM(Tile tile) {
        this.tile = tile;

        accumulationRegister = 0;
        instructionRegister = 0;

        this.regA = this.regB = this.regX = this.regF = this.regP = 0;
        this.regS = 255;

        this.ram = tile.getTileData();
    }

    private int getA() {
        return this.regA & 0xFF;
    }

    private void setA(int x) {
        this.regA = x & 0xFF;
    }

    private int getB() {
        return this.regB & 0xFF;
    }

    private void setB(int x) {
        this.regB = x & 0xFF;
    }

    private int getX() {
        return this.regX & 0xFF;
    }

    private void setX(int x) {
        this.regX = x & 0xFF;
    }

    private int getS() {
        return this.regS & 0xFF;
    }

    private void setS(int x) {
        this.regS = x & 0xFF;
    }

    private int getF() {
        return this.regF & 0xFF;
    }

    private void setF(int x) {
        this.regF = x & 0xFF;
    }

    private int getP() {
        return this.regP & 0xFF;
    }

    private void setP(int x) {
        this.regP = x & 0xFF;
    }

    private int getTS() {
        return this.regTS & 0xFF;
    }

    private void setTS(int x) {
        this.regTS = x & 0xFF;
    }

    private int getTD() {
        return this.regTD & 0xFF;
    }

    private void setTD(int x) {
        this.regTD = x & 0xFF;
    }

    private byte[] getRam() {
        return this.ram;
    }

    private void setRam(byte[] newRam) {
        this.ram = newRam;
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

    private void push(int x) {
        this.ram[getS()] = (byte) (x & 0xFF);
        setS(getS() - 1);
    }

    private int pop() {
        setS(getS() + 1);
        return ((int) this.ram[getS()]) & 0xFF;
    }

    // Returns the pointer to the next instruction
    public void step() {
        int ptr = this.regP & 0xFF;
        int iLength = 1;
        VMArg src, dest;
        int temp = 0;
        switch (ram[ptr] & 0xF0) {
            case 0x00:          // add dest, src ; 0x0r
                src = parseArg(VMArgType.SRC, ram[ptr] & 0x03);
                dest = parseArg(VMArgType.DEST, (ram[ptr] >> 2) & 0x03);
                switch (dest) {
                    case A:
                        temp = getA() + getTS();
                        setA(temp);
                        break;
                    case B:
                        temp = getB() + getTS();
                        setB(temp);
                        break;
                    case X:
                        temp = getX() + getTS();
                        setX(temp);
                        break;
                    case MEM:
                        temp = this.ram[getX()] + getTS();
                        this.ram[getX()] = (byte) (temp & 0xFF);
                        break;
                }
                if (temp > 0xFF || temp < 0)
                    setF(getF() | 0x08);
                if (src == VMArg.IMM) iLength = 2;
                else iLength = 1;
                break;

            case 0x10:          // sub dest, src ; 0x1r
                src = parseArg(VMArgType.SRC, ram[ptr] & 0x03);
                dest = parseArg(VMArgType.DEST, (ram[ptr] >> 2) & 0x03);
                switch (dest) {
                    case A:
                        temp = getA() - getTS();
                        setA(temp);
                        break;
                    case B:
                        temp = getB() - getTS();
                        setB(temp);
                        break;
                    case X:
                        temp = getX() - getTS();
                        setX(temp);
                        break;
                    case MEM:
                        temp = this.ram[getX()] - getTS();
                        this.ram[getX()] = (byte) (temp & 0xFF);
                        break;
                }
                if (temp > 0xFF || temp < 0)
                    setF(getF() | 0x08);
                if (src == VMArg.IMM) iLength = 2;
                else iLength = 1;
                break;

            case 0x20:          // mul
                break;

            case 0x30:          // div
                break;

            case 0x40:          // mod
                break;

            case 0x50:          // mov dest, src ; 0x5:dd(dest):dd(src)
                parseArg(VMArgType.DEST, ram[ptr] & 0x03);
                setTS(getTD());
                dest = parseArg(VMArgType.DEST, (ram[ptr] >> 2) & 0x03);
                switch (dest) {
                    case A:
                        setA(getTS());
                        break;
                    case B:
                        setB(getTS());
                        break;
                    case X:
                        setX(getTS());
                        break;
                    case MEM:
                        this.ram[getX()] = (byte) getTS();
                        break;
                }
                iLength = 1;
                break;

            case 0x60:          // (special)
                break;

            case 0x70:          // in
                break;

            case (byte) 0x80:   // out
                break;

            case (byte) 0x90:   // (special)
                break;

            case (byte) 0xA0:   // cmp
                break;

            case (byte) 0xB0:   // (special)
                break;

            case (byte) 0xC0:   // (special)
                break;

            case (byte) 0xD0:   // (special)
                break;

            case (byte) 0xE0:   // and dest, src ; 0xEr
                src = parseArg(VMArgType.SRC, ram[ptr] & 0x03);
                dest = parseArg(VMArgType.DEST, (ram[ptr] >> 2) & 0x03);
                switch (dest) {
                    case A:
                        setA(getA() & getTS());
                        break;
                    case B:
                        setB(getB() & getTS());
                        break;
                    case X:
                        setX(getX() & getTS());
                        break;
                    case MEM:
                        this.ram[getX()] = (byte) (this.ram[getX()] & getTS());
                        break;
                }
                if (src == VMArg.IMM) iLength = 2;
                else iLength = 1;
                break;

            case (byte) 0xF0:   // or dest, src ; 0xFr
                src = parseArg(VMArgType.SRC, ram[ptr] & 0x03);
                dest = parseArg(VMArgType.DEST, (ram[ptr] >> 2) & 0x03);
                switch (dest) {
                    case A:
                        setA(getA() | getTS());
                        break;
                    case B:
                        setB(getB() | getTS());
                        break;
                    case X:
                        setX(getX() | getTS());
                        break;
                    case MEM:
                        this.ram[getX()] = (byte) (this.ram[getX()] | getTS());
                        break;
                }
                if (src == VMArg.IMM) iLength = 2;
                else iLength = 1;
                break;
        }
       setP(ptr + iLength);
    }

    public VMArg parseArg(VMArgType type, int arg) {
        switch (arg) {
            case 0:
                if (type == VMArgType.SRC) setTS(getA());
                else setTD(getA());
                return VMArg.A;
            case 1:
                if (type == VMArgType.SRC) setTS(getB());
                else setTD(getB());
                return VMArg.B;
            case 2:
                if (type == VMArgType.SRC) setTS(getX());
                else setTD(getX());
                return VMArg.X;
            case 3:
                if (type == VMArgType.SRC) {
                    setTS(this.ram[(getP() + 1) & 0xFF]);
                    return VMArg.IMM;
                } else {
                    setTS(this.ram[getX()]);
                    return VMArg.MEM;
                }
            default:
                return VMArg.A;
        }
    }

    public static void main(String[] args) {
        Tile tile = new Tile(new TileCoordinate(null, 0, 0), new TileType((byte) 0, "", null, 100, 1));
        TileVM tileVM = new TileVM(tile);

        tileVM.processInstruction(VMBytecodes.MOV,
                new VMArgument(VMArgumentType.POINTER, 100), new VMArgument(VMArgumentType.LITERAL, 6));

        tileVM.processInstruction(VMBytecodes.MOV,
                new VMArgument(VMArgumentType.POINTER, 101), new VMArgument(VMArgumentType.LITERAL, 12));

        tileVM.processInstruction(VMBytecodes.ADD, new VMArgument(VMArgumentType.POINTER, 100));
        tileVM.processInstruction(VMBytecodes.ADD, new VMArgument(VMArgumentType.POINTER, 100));
        tileVM.processInstruction(VMBytecodes.SUB, new VMArgument(VMArgumentType.POINTER, 101));

        //System.out.println(tileVM.getAccumulationRegister());

        byte[] newRam = new byte[256];
        byte[] program = {
                0x03, (byte) 0xA0,      /* add a, 0xA0 */
                0x13, (byte) 0x90,      /* sub a, 0x90 */
                0x54,                   /* mov b, a */
                0x53,                   /* mov a, x */
                0x03 | (0x02 << 2), (byte) 0xFE, /* mov x, 0xFE */
                0x03, 0x03,             /* add a, 0x03 */
                0x02,                   /* add a, x */
        };
        arraycopy(program, 0, newRam, 0, program.length);
        tileVM.setRam(newRam);
        System.out.println(tileVM.regsToString());
        while (tileVM.getP() < program.length) {
            tileVM.step();
            System.out.println(tileVM.regsToString());
        }
    }

    public String regsToString() {
        return
            String.format("@$%02X | ", getP()) +
            String.format("A=$%02X  ", getA()) +
            String.format("B=$%02X  ", getB()) +
            String.format("X=$%02X  ", getX()) +
            String.format("S=$%02X  ", getS()) +
            String.format("F=$%02X", getF());
    }

    public void updateVM() {
        WorldModel worldModel = tile.getCoordinate().worldModel;

        try {
            Tile left = worldModel.getTileAt(tile.getCoordinate().x + 1, tile.getCoordinate().y);
            Tile right = worldModel.getTileAt(tile.getCoordinate().x - 1, tile.getCoordinate().y);
        } catch (Exception access) {}
    }

    public Tile getTile() {
        return tile;
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
