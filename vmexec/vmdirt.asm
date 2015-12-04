
; vmtest.asm

include "vm.asm"

define id 4         ; tile ID for dirt

start:
    inc x           ; [1] = tile ID
    rept id {
        inc [x]
    }
    dec x
    
    rept 3 {        ; read/write tile to right side (port 0x03)
        inc a
    }

main:
    in              ; [0] = inport(a)
    rept id {       ; [0] -= id should be 0 if equal
        dec [x]
    }
    jxnz putTile    ; if not equal, place tile
    jxz main        ; otherwise, loop
    
putTile:
    inc x           ; place tile ID specified by [1]
    out
    dec x
    jxnz main       ; return to main loop
    jxz main
    