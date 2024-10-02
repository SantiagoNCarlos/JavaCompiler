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

	c_8_l DD 8
	c_3_us DB 3
	c_2_us DB 2
	a_global DB ?
	c_1_us DB 1
	c_global DD ?
	@aux3 DD ?
	@aux2 DB ?
	b_global DD ?
	d_global DD ?
	c_6_us DB 6
	@aux1 DB ?
	c_2_l DD 2

.code
start:

	MOV AL, c_3_us
	ADD AL, c_1_us
	MOV @aux1,AL
	JC _SumOverflowError_

	MOV AL, @aux1
	MOV a_global,AL

	MOV AL, c_3_us
	MOV AH, 0
	DIV c_2_us
	MOV @aux2,AL
	MOV AL, @aux2
	MOV a_global,AL

	FLD c_8_l
	FSTP c_global

	MOV EAX, c_6_us
	ADD EAX, c_2_l
	MOV @aux3,EAX
	JO _SumOverflowError_

	MOV EAX, @aux3
	MOV b_global,EAX

	invoke printf, cfm$("%hu\n"), a_global

	invoke printf, cfm$("%d\n"), b_global

	FLD c_global
	FSTP _float_aux_print_
	invoke printf, cfm$("%.20Lf\n"), _float_aux_print_

	FLD d_global
	FSTP _float_aux_print_
	invoke printf, cfm$("%.20Lf\n"), _float_aux_print_

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
