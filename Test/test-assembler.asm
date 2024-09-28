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

	s__NO_LLEGUE_ DB " NO LLEGUE ", 10, 0
	c_3_us DB 3
	c_2_us DB 2
	b_global_clase_obj_global DB ?
	s__LLEGUE_ DB " LLEGUE ", 10, 0
	b_global_clase DB ?
	c_global DB ?
	@aux1 DB ?
	c_8_us DB 8

.code
start:

	MOV AL, c_2_us
	MUL c_3_us
	MOV @aux1,AL
	MOV AL, @aux1
	MOV c_global,AL

	JMP _end_

FUNCTION_func_global_clase PROC
	MOV AL, b_global_clase
	MOV BL, c_8_us
	CMP AL, BL
	JNE label1

	invoke StdOut, addr s__LLEGUE_

	JMP label2
	label1:

	invoke StdOut, addr s__NO_LLEGUE_

	label2:

	MOV _current_function_, 0
	RET 
FUNCTION_func_global_clase ENDP

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
