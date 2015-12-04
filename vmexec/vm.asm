
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
; inc a         08          increment A vm_register
; dec a         09          decrement A vm_register
;

include "atm8.inc"

vm_start:
    mov a, 0xBF     ; relocate stack pointer
    mov s, a

;
; Main loop
;

vm_doForever:
    mov x, vm_regA  ; write regA to stdout
    out [x], 0x00
    mov x, vm_regX  ; write regX to stdout
    out [x], 0x00
    mov x, vm_regP  ; write regP to stdout
    out [x], 0x00
    out a, 0x01     ; newline

jmp vm_doForever
    
    mov x, vm_regP
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

vm_incX:            ; vm_regX++
    mov x, vm_regX
    inc [x]
    jmp vm_nextP

vm_decX:            ; vm_regX--
    mov x, vm_regX
    dec [x]
    jmp vm_nextP

vm_incXval:         ; [vm_regX]++
    mov x, vm_regX
    mov x, [x]
    inc [x]
    jmp vm_nextP

vm_decXval:         ; [vm_regX]--
    mov x, vm_regX
    mov x, [x]
    dec [x]
    jmp vm_nextP

vm_outX:            ; outport(vm_regA, [vm_regX])
    mov x, vm_regA
    mov a, [x]
    mov x, vm_regX
    mov x, [x]
    out [x], a
    jmp vm_nextP

vm_inX:             ; [vm_regX] = inport(vm_regA)
    mov x, vm_regA
    mov a, [x]
    in b, a
    mov x, vm_regX
    mov x, [x]
    mov [x], b
    jmp vm_nextP

vm_doJxz:           ; if ([vm_regX] == 0) goto imm
    mov x, vm_regX
    mov x, [x]
    mov a, [x]
    cmp a, 0
    jz vm_doImmJump
    mov x, vm_regP
    add [x], 2
    or [x], 0xC0
    jmp vm_doForever

vm_doJxnz:          ; if ([vm_regX] != 0) goto imm
    mov x, vm_regX
    mov x, [x]
    mov a, [x]
    cmp a, 0
    jz .continue
    jmp vm_doImmJump
    .continue:
    mov x, vm_regP
    add [x], 2
    or [x], 0xC0
    jmp vm_doForever

vm_doImmJump:       ; vm_regP = [vm_regP + 1]
    mov x, vm_regP
    inc x
    mov a, [x]
    mov x, vm_regP
    mov [x], a
    jmp vm_doForever

vm_incA:            ; vm_regA--
    mov x, vm_regA
    inc [x]
    jmp vm_nextP

vm_decA:            ; vm_regA--
    mov x, vm_regA
    dec [x]
    jmp vm_nextP

vm_nextP:           ; vm_regP = (vm_regP + 1) | 0xC0
    mov x, vm_regP
    inc [x]
    or [x], 0xC0
    jmp vm_doForever

;
; EOF data and alignment
;

    vm_regA db 0
    vm_regX db 0
    vm_regP db 0xC0

    times 0xC0-$ db 0

;
; Macroinstructions for the VM emulator
;

macro inc vm_reg* {
    if vm_reg in <x>
        db 0x00
    else if vm_reg in <[x]>
        db 0x02
    else if vm_reg in <a>
        db 0x08
    else
        display '!!! Bad operands'
        err
    end if
}

macro dec vm_reg* {
    if vm_reg in <x>
        db 0x01
    else if vm_reg in <[x]>
        db 0x03
    else if vm_reg in <a>
        db 0x09
    else
        display '!!! Bad operands'
        err
    end if
}

macro out {
    db 0x04
}

macro in {
    db 0x05
}

macro jxz vm_imm* {
    db 0x06
    db vm_imm
}

macro jxnz vm_imm* {
    db 0x07
    db vm_imm
}
