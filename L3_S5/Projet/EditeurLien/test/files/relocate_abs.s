.global main
.text
main:
	ldr r1, ptr1
	ldr r2, ptr2
	ldr r3, ptr3
    swi 0x123456

ptr1: .word var
ptr2: .hword var
ptr3: .byte var
.skip 1

.data
var: .word 0x11223344
