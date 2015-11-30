
; atm8test.asm

include "atm8.inc"

add a, a
add a, b
add a, x
add a, 0xFF

add b, a
add b, b
add b, x
add b, 0xFF

add x, a
add x, b
add x, x
add x, 0xFF

add [x], a
add [x], b
add [x], x
add [x], 0xFF

sub a, a
sub a, b
sub a, x
sub a, 0xFF

sub b, a
sub b, b
sub b, x
sub b, 0xFF

sub x, a
sub x, b
sub x, x
sub x, 0xFF

sub [x], a
sub [x], b
sub [x], x
sub [x], 0xFF

mov a, a
mov a, b
mov a, x
mov a, [x]

mov b, a
mov b, b
mov b, x
mov b, [x]

mov x, a
mov x, b
mov x, x
mov x, [x]

mov [x], a
mov [x], b
mov [x], x
mov [x], [x]

mov a, 0xFF
mov b, 0xFF
mov x, 0xFF
mov [x], 0xFF

mov a, s
mov s, a

cmp a, a
cmp a, b
cmp a, x
cmp a, 0xFF

cmp b, a
cmp b, b
cmp b, x
cmp b, 0xFF

cmp x, a
cmp x, b
cmp x, x
cmp x, 0xFF

cmp [x], a
cmp [x], b
cmp [x], x
cmp [x], 0xFF

jo a
jo b
jo x
jo 0xFF

jz a
jz b
jz x
jz 0xFF

jg a
jg b
jg x
jg 0xFF

jl a
jl b
jl x
jl 0xFF

and a, a
and a, b
and a, x
and a, 0xFF

and b, a
and b, b
and b, x
and b, 0xFF

and x, a
and x, b
and x, x
and x, 0xFF

and [x], a
and [x], b
and [x], x
and [x], 0xFF

or a, a
or a, b
or a, x
or a, 0xFF

or b, a
or b, b
or b, x
or b, 0xFF

or x, a
or x, b
or x, x
or x, 0xFF

or [x], a
or [x], b
or [x], x
or [x], 0xFF

push a
push b
push x
push [x]
push f
push 0xFF

pop a
pop b
pop x
pop [x]
pop f
