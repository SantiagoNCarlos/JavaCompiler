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

	s__VALOR_CORRECTO_ DB " VALOR CORRECTO ", 10, 0
	@aux5 DB ?
	@aux4 DB ?
	c_5_us DB 5
	c_2_us DB 2
	a_global DB ?
	c_0_us DB 0
	c_1_us DB 1
	c_global DB ?
	@aux3 DB ?
	@aux2 DB ?
	b_global DB ?
	@aux1 DB ?
	c_8_us DB 8

.code
start:

	MOV AL, c_1_us
	MUL c_2_us
	MOV @aux1,AL
	MOV AL, @aux1
	MOV b_global,AL

	MOV AL, c_0_us
	MOV c_global,AL

	MOV AL, c_1_us
	MOV a_global,AL

	label1:
	MOV AL, c_global
	MOV BL, c_8_us
	CMP AL, BL
	JA label2

	MOV AL, b_global
	ADD AL, c_5_us
	MOV @aux2,AL
	JC _SumOverflowError_

	MOV AL, @aux2
	SUB AL, a_global
	MOV @aux3,AL
	MOV AL, @aux3
	MOV b_global,AL

	MOV AL, c_global
	ADD AL, c_1_us
	MOV @aux4,AL
	JC _SumOverflowError_

	MOV AL, @aux4
	MOV c_global,AL

	JMP label1
	label2:

	MOV AL, c_1_us
	ADD AL, a_global
	MOV @aux5,AL
	JC _SumOverflowError_

	MOV AL, @aux5
	MOV b_global,AL

	MOV AL, b_global
	MOV BL, c_2_us
	CMP AL, BL
	JNE label3

	invoke StdOut, addr s__VALOR_CORRECTO_

	label3:

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
