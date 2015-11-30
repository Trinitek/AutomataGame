
; ATM8 Processor Instruction Macros
; For use with Flat Assembler (http://flatassembler.net)

macro add dest*, src* {
    _i = 0x00
    if dest in <b>
        _i = _i or 0100b
    else if dest in <x>
        _i = _i or 1000b
    else if dest in <[x]>
        _i = _i or 1100b
    end if
    if src in <b>
        _i = _i or 01b
    else if src in <x>
        _i = _i or 10b
    else if ~ src in <a>
        _i = _i or 11b
    end if
    db _i
    if ~ src in <a,b,x>
        db src
    end if
}

macro sub dest*, src* {
    _i = 0x10
    if dest in <b>
        _i = _i or 0100b
    else if dest in <x>
        _i = _i or 1000b
    else if dest in <[x]>
        _i = _i or 1100b
    end if
    if src in <b>
        _i = _i or 01b
    else if src in <x>
        _i = _i or 10b
    else if ~ src in <a>
        _i = _i or 11b
    end if
    db _i
    if ~ src in <a,b,x>
        db src
    end if
}

macro mov dest*, src* {
    _i = 0x50
    if dest in <b>
        _i = _i or 0100b
    else if dest in <x>
        _i = _i or 1000b
    else if dest in <[x]>
        _i = _i or 1100b
    end if
    if src in <b>
        _i = _i or 01b
    else if src in <x>
        _i = _i or 10b
    else if src in <[x]>
        _i = _i or 11b
    end if
    if ~ src in <a,b,x,[x],s>
        _i = (_i and 0x0F) or 0x60
        db _i
        db src
    else if src in <s> & dest in <a>
        db 0xDA
    else if src in <a> & dest in <s>
        db 0xD9
    else
        db _i
    end if
}

macro push src* {
    _i = 0x60
    if src in <a>
        db _i or 1000b
    else if src in <b>
        db _i or 1001b
    else if src in <x>
        db _i or 1010b
    else if src in <[x]>
        db _i or 1011b
    else if src in <f>
        db 0xD2
    end if
    if ~ src in <a,b,x,[x],f>
        db 0x64
        db src
    end if
}

macro pushf {
    push f
}

macro pop dest* {
    _i = 0x60
    if dest in <a>
        db _i or 1100b
    else if dest in <b>
        db _i or 1101b
    else if dest in <x>
        db _i or 1110b
    else if dest in <[x]>
        db _i or 1111b
    else if dest in <f>
        db 0xD3
    end if
}

macro popf {
    pop f
}

macro cmp dest*, src* {
    _i = 0xA0
    if dest in <b>
        _i = _i or 0100b
    else if dest in <x>
        _i = _i or 1000b
    else if dest in <[x]>
        _i = _i or 1100b
    end if
    if src in <b>
        _i = _i or 01b
    else if src in <x>
        _i = _i or 10b
    else if ~ src in <a>
        _i = _i or 11b
    end if
    db _i
    if ~ src in <a,b,x>
        db src
    end if
}

macro jo src* {
    _i = 0xB0
    if src in <b>
        _i = _i or 01b
    else if src in <x>
        _i = _i or 10b
    else if ~ src in <a>
        _i = _i or 11b
    end if
    db _i
    if ~ src in <a,b,x>
        db src
    end if
}

macro jz src* {
    _i = 0xB4
    if src in <b>
        _i = _i or 01b
    else if src in <x>
        _i = _i or 10b
    else if ~ src in <a>
        _i = _i or 11b
    end if
    db _i
    if ~ src in <a,b,x>
        db src
    end if
}

macro jg src* {
    _i = 0xB8
    if src in <b>
        _i = _i or 01b
    else if src in <x>
        _i = _i or 10b
    else if ~ src in <a>
        _i = _i or 11b
    end if
    db _i
    if ~ src in <a,b,x>
        db src
    end if
}

macro jl src* {
    _i = 0xBC
    if src in <b>
        _i = _i or 01b
    else if src in <x>
        _i = _i or 10b
    else if ~ src in <a>
        _i = _i or 11b
    end if
    db _i
    if ~ src in <a,b,x>
        db src
    end if
}

macro cls {
    db 0xD0
}

macro sts {
    db 0xD1
}

macro and dest*, src* {
    _i = 0xE0
    if dest in <b>
        _i = _i or 0100b
    else if dest in <x>
        _i = _i or 1000b
    else if dest in <[x]>
        _i = _i or 1100b
    end if
    if src in <b>
        _i = _i or 01b
    else if src in <x>
        _i = _i or 10b
    else if ~ src in <a>
        _i = _i or 11b
    end if
    db _i
    if ~ src in <a,b,x>
        db src
    end if
}

macro or dest*, src* {
    _i = 0xF0
    if dest in <b>
        _i = _i or 0100b
    else if dest in <x>
        _i = _i or 1000b
    else if dest in <[x]>
        _i = _i or 1100b
    end if
    if src in <b>
        _i = _i or 01b
    else if src in <x>
        _i = _i or 10b
    else if ~ src in <a>
        _i = _i or 11b
    end if
    db _i
    if ~ src in <a,b,x>
        db src
    end if
}

macro not dest* {
    if dest in <a>
        db 0xD0 or 0100b
    else if dest in <b>
        db 0xD0 or 0101b
    else if dest in <x>
        db 0xD0 or 0110b
    else if dest in <[x]>
        db 0xD0 or 0111b
    end if
}
