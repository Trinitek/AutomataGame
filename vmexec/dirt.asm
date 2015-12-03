
include "atm8.inc"

mov x, 0x03     ; read/write right side tile ID

program:        ; start of loop

in a, x         ; read tile ID
cmp a, 4        ; 
jz program      ; if right side tile ID == 4 (dirt) then loop

mov a, 4        ;
out a, x        ; otherwise, place dirt

jmp program     ; repeat forever
