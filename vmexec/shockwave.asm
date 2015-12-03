
include "atm8.inc"

mov a, 11       ; shockwave virus tile ID

start:
mov x, 0x02     ; tile ports are between 0x02 and 0x0A inclusive

nextTile:
in b, x         ; is this tile a virus?
cmp b, a        ;
jz dontPlace    ; if so, don't overwrite
out a, x        ; if not, place

dontPlace:
inc x           ; next port number
cmp x, 0x0A     ; are we above port 0x0A?
jg start        ; if so, reset the port number
jmp nextTile    ; if not, place more tiles
