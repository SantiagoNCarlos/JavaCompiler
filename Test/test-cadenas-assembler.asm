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

	s__soy_una_CADENA_ DB " soy una CADENA ", 10, 0
	k_global DD ?
	c_3_us DB 3
	c_6_l DD 6
	@aux1 DD ?
	c_8_us DB 8

.code
start:

	MOV EAX, c_6_l
	MOVZX ECX, c_3_us
	ADD EAX, ECX
	MOV @aux1, EAX
	JO _SumOverflowError_

	MOV EAX, @aux1
	MOV k_global,EAX

	invoke StdOut, addr s__soy_una_CADENA_

	invoke printf, cfm$("%d\n"), k_global

	invoke printf, cfm$("%hu\n"), c_8_us

	invoke printf, cfm$("%d\n"), c_6_l

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
