
include "atm8.inc"

; inc x         00
; dec x         01
; inc [x]       02
; dec [x]       03
; out [x], a    04
; in [x], a     05
; jxz imm       06 imm
; jxnz imm      07 imm
; inc a         08
; dec a         09

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

postpone {
    regA db 0
    regX db 0
    regP db 0x80
    
    virtual
        align 0x10
        _buf = $-$$
    end virtual
    db _buf dup 0
}

start:
    mov a, 0xBF     ; relocate stack pointer
    mov s, a

doForever:
    mov x, regP
    mov a, [x]
    cmp a, 0x00     ; inc x
    jz incX         
    cmp a, 0x01     ; dec x
    jz decX         
    cmp a, 0x02     ; inc [x]
    jz incXval      
    cmp a, 0x03     ; dec [x]
    jz decXval
    cmp a, 0x04     ; out [x], a
    jz outX
    cmp a, 0x05     ; in [x], a
    jz inX
    cmp a, 0x06     ; jxz imm
    jz doJxz
    cmp a, 0x07     ; jxnz imm
    jz doJxnz
    cmp a, 0x08     ; inc a
    jz incA
    cmp a, 0x09     ; dec a
    jz decA

incX:               ; regX++
    mov x, regX
    inc [x]
    jmp nextP

decX:               ; regX--
    mov x, regX
    dec [x]
    jmp nextP

incXval:            ; [regX]++
    mov x, regX
    mov x, [x]
    inc [x]
    jmp nextP

decXval:            ; [regX]--
    mov x, regX
    mov x, [x]
    dec [x]
    jmp nextP

outX:               ; outport(regA, [regX])
    mov x, regA
    mov a, [x]
    mov x, regX
    mov x, [x]
    out [x], a
    jmp nextP

inX:                ; [regX] = inport(regA)
    mov x, regA
    mov a, [x]
    in b, a
    mov x, regX
    mov x, [x]
    mov [x], b
    jmp nextP

doJxz:              ; if ([regX] == 0) goto imm
    mov x, regX
    mov x, [x]
    mov a, [x]
    cmp a, 0
    jz doImmJump
    mov x, regP
    add [x], 2
    or [x], 0xC0
    jmp doForever

doJxnz:             ; if ([regX] != 0) goto imm
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
    jmp doForever

doImmJump:          ; regP = [regP + 1]
    mov x, regP
    inc x
    mov a, [x]
    mov x, regP
    mov [x], a
    jmp doForever

incA:               ; regA--
    mov x, regA
    inc [x]
    jmp nextP

decA:               ; regA--
    mov x, regA
    dec [x]
    jmp nextP

nextP:              ; regP = (regP + 1) | 0xC0
    mov x, regP
    inc [x]
    or [x], 0xC0
    jmp doForever
