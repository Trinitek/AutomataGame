
;
; ATM8 VM-within-VM emulator
; Emulates a Brainfuck-derived bytecode processor
;
; To write a VM bytecode application, include this file at the top of your
; main assembly file, and use the macroinstructions specified at the bottom
; of this file. Refer to the brief list of instructions as follows.
;
; INSTRUCTION   BYTECODE    DESCRIPTION
; inc x         00          increment X pointer
; dec x         01          decrement X pointer
; inc [x]       02          increment value at X
; dec [x]       03          decrement value at X
; out           04          output value at X to port A
; in            05          input value into X from port A
; jxz imm       06 imm      goto label if value at X is zero
; jxnz imm      07 imm      goto label if value at X is not zero
; inc a         08          increment A register
; dec a         09          decrement A register
;

include "atm8.inc"

vm_start:
    mov a, 0xBF     ; relocate stack pointer
    mov s, a

;
; Main loop
;
    
vm_doForever:
    mov x, regP
    mov a, [x]
    cmp a, 0x00     ; inc x
    jz vm_incX         
    cmp a, 0x01     ; dec x
    jz vm_decX         
    cmp a, 0x02     ; inc [x]
    jz vm_incXval      
    cmp a, 0x03     ; dec [x]
    jz vm_decXval
    cmp a, 0x04     ; out [x], a
    jz vm_outX
    cmp a, 0x05     ; in [x], a
    jz vm_inX
    cmp a, 0x06     ; jxz imm
    jz vm_doJxz
    cmp a, 0x07     ; jxnz imm
    jz vm_doJxnz
    cmp a, 0x08     ; inc a
    jz vm_incA
    cmp a, 0x09     ; dec a
    jz vm_decA

vm_incX:               ; regX++
    mov x, regX
    inc [x]
    jmp nextP

vm_decX:               ; regX--
    mov x, regX
    dec [x]
    jmp nextP

vm_incXval:            ; [regX]++
    mov x, regX
    mov x, [x]
    inc [x]
    jmp nextP

vm_decXval:            ; [regX]--
    mov x, regX
    mov x, [x]
    dec [x]
    jmp nextP

vm_outX:               ; outport(regA, [regX])
    mov x, regA
    mov a, [x]
    mov x, regX
    mov x, [x]
    out [x], a
    jmp nextP

vm_inX:                ; [regX] = inport(regA)
    mov x, regA
    mov a, [x]
    in b, a
    mov x, regX
    mov x, [x]
    mov [x], b
    jmp nextP

vm_doJxz:              ; if ([regX] == 0) goto imm
    mov x, regX
    mov x, [x]
    mov a, [x]
    cmp a, 0
    jz doImmJump
    mov x, regP
    add [x], 2
    or [x], 0xC0
    jmp vm_doForever

vm_doJxnz:             ; if ([regX] != 0) goto imm
    mov x, regX
    mov x, [x]
    mov a, [x]
    cmp a, 0
    jz .continue
    jmp doImmJump
    .continue:
    mov x, regP
    add [x], 2
    or [x], 0xC0
    jmp vm_doForever

doImmJump:          ; regP = [regP + 1]
    mov x, regP
    inc x
    mov a, [x]
    mov x, regP
    mov [x], a
    jmp vm_doForever

vm_incA:               ; regA--
    mov x, regA
    inc [x]
    jmp nextP

vm_decA:               ; regA--
    mov x, regA
    dec [x]
    jmp nextP

nextP:              ; regP = (regP + 1) | 0xC0
    mov x, regP
    inc [x]
    or [x], 0xC0
    jmp vm_doForever

;
; EOF data and alignment
;

    regA db 0
    regX db 0
    regP db 0x80
    
    virtual
        align 0x10
        _buf = $-$$
    end virtual
    db _buf dup 0

;
; Macroinstructions for the VM emulator
;

macro _inc reg* {
    if reg in <x>
        db 0x00
    else if reg in <[x]>
        db 0x02
    else if reg in <a>
        db 0x08
    else
        display '!!! Bad operands'
        err
    end if
}

macro _dec reg* {
    if reg in <x>
        db 0x01
    else if reg in <[x]>
        db 0x03
    else if reg in <a>
        db 0x09
    else
        display '!!! Bad operands'
        err
    end if
}

macro _out {
    db 0x04
}

macro _in {
    db 0x05
}

macro _jxz imm* {
    db 0x06
    db imm
}

macro _jxnz imm* {
    db 0x07
    db imm
}
