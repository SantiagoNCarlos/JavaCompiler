;.386
;.model flat, stdcall
;option casemap:none


include \masm32\include\masm32rt.inc

includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
includelib \masm32\lib\msvcrt.lib
includelib \masm32\lib\user32.lib

dll_dllcrt0 PROTO C
printf PROTO C :PTR BYTE, :VARARG

.stack 200h
.data

	_float_aux_print_ DQ 0
	_current_function_ DD 0
	_max_float_value_ DD 3.40282347e+38
	SumOverflowErrorMsg DB "Overflow detected in a INTEGER SUM operation", 10, 0
	ProductOverflowErrorMsg DB "Overflow detected in a FLOAT PRODUCT operation", 10, 0
	RecursionErrorMsg DB "Recursive call detected", 10, 0

	c_3_l DD 3
	c_6_l DD 6
	@aux4 DD ?
	c_8_l DD 8
	c_3_77E6 DD 3.77E6
	c_21_0 DD 21.0
	a_global DD ?
	c_global DD ?
	@aux3 DD ?
	@aux2 DD ?
	b_global DD ?
	d_global DD ?
	c_1_l DD 1
	@aux1 DD ?
	c_2_l DD 2
	c__21_0 DD -21.0

.code
start:

	MOV EAX, b_global
	ADD EAX, c_1_l
	MOV @aux1,EAX
	JO _SumOverflowError_

	MOV EAX, @aux1
	MOV b_global,EAX

	MOV EAX, c_3_l
	SUB EAX, c_8_l
	MOV @aux2,EAX
	MOV EAX, @aux2
	MOV a_global,EAX

	MOV EAX, a_global
	DIV c_2_l
	MOV @aux3,EAX
	MOV EAX, @aux3
	MOV a_global,EAX

	MOV EAX, c_6_l
	MUL c_8_l
	MOV @aux4,EAX
	MOV EAX, @aux4
	MOV b_global,EAX

	invoke printf, cfm$("%d\n"), a_global

	invoke printf, cfm$("%d\n"), b_global

	JMP _end_

	JMP _end_
	_SumOverflowError_:
	invoke StdOut, addr SumOverflowErrorMsg
	JMP _end_
	_ProductOverflowError_:
	invoke StdOut, addr ProductOverflowErrorMsg
	JMP _end_
	_RecursionError_:
	invoke StdOut, addr RecursionErrorMsg
	JMP _end_
	_end_:
	invoke ExitProcess, 0
END start
