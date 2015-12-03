
include "atm8.inc"

mov x, 0x02     ; read/write right side tile ID

program:        ; start of loop

in a, x         ; read tile ID
cmp a, 0        ; 
jz program      ; if right side tile ID == 0 (grass) then loop

mov a, 0        ;
out a, x        ; otherwise, place grass

jmp program     ; repeat forever
