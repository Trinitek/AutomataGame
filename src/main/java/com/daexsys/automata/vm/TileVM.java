package com.daexsys.automata.vm;

import com.daexsys.automata.Tile;
import com.daexsys.automata.world.TileCoordinate;
import com.daexsys.automata.world.tiletypes.TileType;

import static java.lang.System.arraycopy;

/**
 * Virtual machine for a single tile automaton.
 */
public class TileVM implements VM {

    private int regA, regB, regX, regS, regF, regP, regTS, regTD;
    private byte[] ram;

    private Tile tile;

    public TileVM(Tile tile) {
        this.tile = tile;

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

    public byte[] getRam() {
        return this.ram;
    }

    private void setRam(byte[] newRam) {
        this.ram = newRam;
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
        int ptr = getP();
        int iLength = 1;
        VMArg src, dest;
        int iTemp = 0;
        switch (this.ram[ptr] & 0xF0) {
            case 0x00:          // add dest, src ; 0x0r
                src = parseArg(VMArgType.SRC, ram[ptr] & 0x03);
                dest = parseArg(VMArgType.DEST, (ram[ptr] >> 2) & 0x03);
                switch (dest) {
                    case A:
                        iTemp = getA() + getTS();
                        setA(iTemp);
                        break;
                    case B:
                        iTemp = getB() + getTS();
                        setB(iTemp);
                        break;
                    case X:
                        iTemp = getX() + getTS();
                        setX(iTemp);
                        break;
                    case MEM:
                        iTemp = this.ram[getX()] + getTS();
                        this.ram[getX()] = (byte) (iTemp & 0xFF);
                        break;
                }
                if (iTemp > 0xFF || iTemp < 0)
                    setF(getF() | 0x08);
                if (src == VMArg.IMM) iLength = 2;
                else iLength = 1;
                break;

            case 0x10:          // sub dest, src ; 0x1r
                src = parseArg(VMArgType.SRC, ram[ptr] & 0x03);
                dest = parseArg(VMArgType.DEST, (ram[ptr] >> 2) & 0x03);
                switch (dest) {
                    case A:
                        iTemp = getA() - getTS();
                        setA(iTemp);
                        break;
                    case B:
                        iTemp = getB() - getTS();
                        setB(iTemp);
                        break;
                    case X:
                        iTemp = getX() - getTS();
                        setX(iTemp);
                        break;
                    case MEM:
                        iTemp = this.ram[getX()] - getTS();
                        this.ram[getX()] = (byte) (iTemp & 0xFF);
                        break;
                }
                if (iTemp > 0xFF || iTemp < 0)
                    setF(getF() | 0x08);
                if (src == VMArg.IMM) iLength = 2;
                else iLength = 1;
                break;

            case 0x20:          // mul
                src = parseArg(VMArgType.SRC, ram[ptr] & 0x03);
                dest = parseArg(VMArgType.DEST, (ram[ptr] >> 2) & 0x03);
                if ((getF() & 0x0F) != 0) {         // unsigned multiply
                    iTemp = getTD() * getTS();
                } else {                            // signed multiply
                    iTemp = (byte) getTD() * (byte) getTS();
                }
                push((iTemp >> 0x0F) & 0xFF);
                switch (dest) {
                    case A:
                        setA(iTemp);
                        break;
                    case B:
                        setB(iTemp);
                        break;
                    case X:
                        setX(iTemp);
                        break;
                    case MEM:
                        this.ram[getX()] = (byte) iTemp;
                        break;
                }
                if (src == VMArg.IMM) iLength = 2;
                else iLength = 1;
                break;

            case 0x30:          // div
                src = parseArg(VMArgType.SRC, ram[ptr] & 0x03);
                dest = parseArg(VMArgType.DEST, (ram[ptr] >> 2) & 0x03);
                setTS(pop());
                iTemp = (getTS() << 0x0F) | getTS();
                if ((getF() & 0x0F) != 0) {         // unsigned divide
                    iTemp = getTD() / iTemp;
                } else {                            // signed divide
                    iTemp = (byte) getTD() / iTemp * (getTS() >= 0x80 ? -1 : 1);
                }
                switch (dest) {
                    case A:
                        setA(iTemp);
                        break;
                    case B:
                        setB(iTemp);
                        break;
                    case X:
                        setX(iTemp);
                        break;
                    case MEM:
                        this.ram[getX()] = (byte) iTemp;
                        break;
                }
                if (src == VMArg.IMM) iLength = 2;
                else iLength = 1;
                break;

            case 0x40:          // TODO mod
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
                dest = parseArg(VMArgType.DEST, ram[ptr] & 0x03);
                setTS(ram[(ptr + 1) & 0xFF]);
                switch (this.ram[getP()] & 0x0C) {
                    case 0x00:  // mov dest, imm
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
                        iLength = 2;
                        break;
                    case 0x04:  // push imm
                        push(getTS());
                        iLength = 2;
                        break;
                    case 0x08:  // push dest
                        switch (dest) {
                            case A:
                                push(getA());
                                break;
                            case B:
                                push(getB());
                                break;
                            case X:
                                push(getX());
                                break;
                            case MEM:
                                push(this.ram[getX()] & 0xFF);
                                break;
                        }
                        iLength = 1;
                        break;
                    case 0x0C:  // pop dest
                        switch (dest) {
                            case A:
                                setA(pop());
                                break;
                            case B:
                                setB(pop());
                                break;
                            case X:
                                setX(pop());
                                break;
                            case MEM:
                                this.ram[getX()] = (byte) pop();
                                break;
                        }
                        iLength = 1;
                        break;
                }
                break;

            case 0x70:          // TODO in
                break;

            case 0x80:          // TODO out
                break;

            case 0x90:          // (special) function calling and returning
                switch (this.ram[ptr] & 0x0C) {
                    case 0x00:  // call dest
                        push(getP() + 1);
                        dest = parseArg(VMArgType.DEST, ram[ptr] & 0x03);
                        switch (dest) {
                            case A:
                                setP(getA());
                                break;
                            case B:
                                setP(getB());
                                break;
                            case X:
                                setP(getX());
                                break;
                            case MEM:
                                setP(this.ram[getX()]);
                                break;
                        }
                        break;
                    case 0x04:  // call imm
                        push(getP() + 2);
                        setP(this.ram[(getP() + 1) & 0xFF]);
                        break;
                    case 0x08:  // ret src
                        int i = 0;
                        src = parseArg(VMArgType.SRC, ram[ptr] & 0x03);
                        switch (src) {
                            case A:
                                i = getA();
                                break;
                            case B:
                                i = getB();
                                break;
                            case X:
                                i = getX();
                                break;
                            case IMM:
                                i = this.ram[(getP() + 1) & 0xFF];
                                break;
                        }
                        setP(pop());
                        setS(getS() + i);
                        break;
                    case 0x0C:  // ret
                        setP(pop());
                        break;
                }
                iLength = 0;
                break;

            case 0xA0:          // cmp
                src = parseArg(VMArgType.SRC, ram[ptr] & 0x03);
                dest = parseArg(VMArgType.DEST, (ram[ptr] >> 2) & 0x03);
                switch (dest) {
                    case A:
                        iTemp = getA();
                        break;
                    case B:
                        iTemp = getB();
                        break;
                    case X:
                        iTemp = getX();
                        break;
                    case MEM:
                        iTemp = this.ram[getX()];
                        break;
                }
                if (iTemp == getTS()) setF((getF() & 0xF8) | 0x04);
                else if ((getF() & 0x0F) != 0) {    // unsigned comparison
                    if (iTemp < getTS())
                        setF((getF() & 0xF8) | 0x01);
                    else if (iTemp > getTS())
                        setF((getF() & 0xF8) | 0x02);
                } else {                            // signed comparison
                    if ((byte) iTemp < (byte) getTS()) setF((getF() & 0xF8) | 0x01);
                    else if ((byte) iTemp > (byte) getTS()) setF((getF() & 0xF8) | 0x02);
                }
                if (src == VMArg.IMM) iLength = 2;
                else iLength = 1;
                break;

            case 0xB0:          // (special) conditional jumps
                src = parseArg(VMArgType.SRC, ram[ptr] & 0x03);
                iTemp = 0;
                switch (this.ram[ptr] & 0x0C) {
                    case 0x00:  // jo
                        if ((getF() & 0x08) != 0) {
                            setP(getTS());
                            iTemp = 1;
                        }
                        break;
                    case 0x04:  // jz
                        if ((getF() & 0x04) != 0) {
                            setP(getTS());
                            iTemp = 1;
                        }
                        break;
                    case 0x08:  // jg
                        if ((getF() & 0x02) != 0) {
                            setP(getTS());
                            iTemp = 1;
                        }
                        break;
                    case 0x0C:  // jl
                        if ((getF() & 0x01) != 0) {
                            setP(getTS());
                            iTemp = 1;
                        }
                        break;
                }
                if (iTemp == 0) {
                    if (src == VMArg.IMM) iLength = 2;
                    else iLength = 1;
                } else {
                    iLength = 0;
                }
                break;

            case 0xC0:          // (special)
                dest = parseArg(VMArgType.DEST, ram[ptr] & 0x03);
                switch (this.ram[ptr] & 0x0C) {
                    case 0x00:  // inc
                        switch (dest) {
                            case A:
                                setA(getA() + 1);
                                break;
                            case B:
                                setB(getB() + 1);
                                break;
                            case X:
                                setX(getX() + 1);
                                break;
                            case MEM:
                                this.ram[getX()] = (byte) (this.ram[getX()] + 1);
                                break;
                        }
                        iLength = 1;
                        break;
                    case 0x04:  // dec
                        switch (dest) {
                            case A:
                                setA(getA() - 1);
                                break;
                            case B:
                                setB(getB() - 1);
                                break;
                            case X:
                                setX(getX() - 1);
                                break;
                            case MEM:
                                this.ram[getX()] = (byte) (this.ram[getX()] - 1);
                                break;
                        }
                        iLength = 1;
                        break;
                    case 0x08:  // jmp dest
                        switch (dest) {
                            case A:
                                setP(getA());
                                break;
                            case B:
                                setP(getB());
                                break;
                            case X:
                                setP(getX());
                                break;
                            case MEM:
                                setP(this.ram[getX()]);
                                break;
                        }
                        iLength = 0;
                        break;
                    case 0x0C:  // jmp imm
                        setP(this.ram[(ptr + 1) & 0xFF]);
                        iLength = 0;
                        break;
                }
                break;

            case 0xD0:          // (special)
                if ((this.ram[ptr] & 0x0C) == 0x04) {   // not dest
                    dest = parseArg(VMArgType.DEST, ram[ptr] & 0x03);
                    switch (dest) {
                        case A:
                            setA(~getA());
                            break;
                        case B:
                            setB(~getB());
                            break;
                        case X:
                            setX(~getX());
                            break;
                        case MEM:
                            this.ram[getX()] = (byte) ~this.ram[getX()];
                            break;
                    }
                    iLength = 2;
                } else {
                    switch (this.ram[ptr] & 0x0F) {
                        case 0x00:  // cls
                            setF(getF() & (~0x10));
                            break;
                        case 0x01:  // sts
                            setF(getF() | 0x10);
                            break;
                        case 0x02:  // push f
                            push(getF());
                            break;
                        case 0x03:  // pop f
                            setF(pop());
                            break;
                        case 0x09:  // mov a, s
                            setA(getS());
                            break;
                        case 0x0A:  // mov s, a
                            setS(getA());
                            break;
                    }
                    iLength = 1;
                }
                break;

            case 0xE0:          // and dest, src ; 0xEr
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

            case 0xF0:   // or dest, src ; 0xFr
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
        if (iLength != 0) setP(ptr + iLength);
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
        Tile tile = new Tile(new TileCoordinate(null, 0, 0),
                new TileType((byte) 0, "", null, 100, 1));
        TileVM tileVM = new TileVM(tile);

        byte[] newRam = new byte[256];
        byte[] program = {
                0x03, (byte) 0xA0,      /* 00 | add a, 0xA0 */
                0x13, (byte) 0x90,      /* 02 | sub a, 0x90 */
                0x54,                   /* 04 | mov b, a */
                0x53,                   /* 05 | mov a, x */
                0x03 | (0x02 << 2), (byte) 0xFE, /* 06 | mov x, 0xFE */
                0x03, 0x03,             /* 08 | add a, 0x03 */
                0x02,                   /* 0A | add a, x */
                (byte) 0xE0 | (0x02 << 2), /* 0B | and x, a */
                (byte) 0xF0 | (0x01 << 2), /* 0C | or b, a */
                (byte) 0xA3, 0x00,      /* 0D | cmp a, 0 */
                (byte) 0xA0,            /* 0F | cmp a, a */
                (byte) 0xA1,            /* 10 | cmp a, b */
                (byte) 0xBF, 0x11       /* 11 | jl 0x11 */
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

    public Tile getTile() {
        return tile;
    }
}
