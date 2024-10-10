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

	c_3_us DB 3
	c_2_us DB 2
	a_global DB ?
	c_1_us DB 1
	b_global DB ?
	@aux1 DB ?

.code
start:

	MOV AL, c_1_us
	MOV b_global,AL

	MOV AL, c_1_us
	MOV a_global,AL

	MOV AL, c_1_us
	MOV b_global,AL

	MOV AL, b_global
	MOV BL, c_2_us
	CMP AL, BL
	JAE label1

	MOV AL, a_global
	MUL BYTE PTR c_3_us
	MOV @aux1, AL
	MOV AL, @aux1
	MOV a_global,AL

	label1:

	MOV AL, b_global
	MOV a_global,AL

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
