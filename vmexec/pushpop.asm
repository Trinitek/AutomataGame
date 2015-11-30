
include "atm8.inc"

push 0x0A
push 0x0B
push 0x0C

pop a
pop b
pop x

mov [x], 0xFF
push [x]
pop a
