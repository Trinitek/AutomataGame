
include "atm8.inc"

mov ax, 0xFF
mov x, 0x06
in a, x
mov b, 0x01
out b, x
in a, x
