.386
.model flat, stdcall
option casemap:none


include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\masm32.inc

includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
includelib \masm32\lib\msvcrt.lib
printf PROTO C :PTR BYTE, :VARARG

.stack 200h
.data

	formatStringLong db "%d", 0
	formatStringUShort db "%hu", 0
	formatStringFloat db "%f", 0
	_current_function_ DD 0
	_max_float_value_ DD 3.40282347e+38
	SumOverflowErrorMsg DB "Overflow detected in a INTEGER SUM operation", 10, 0
	ProductOverflowErrorMsg DB "Overflow detected in a FLOAT PRODUCT operation", 10, 0
	RecursionErrorMsg DB "Recursive call detected", 10, 0

	b_global_cc DB ?
	c_5_us DB 5
	b_global_cc_act DB ?
	c_7_us DB 7
	b_global_cc_p_global DB ?
	@aux1 DB ?
	c_8_us DB 8

.code
start:

	FUNCTION_act_global_cc PROC
	MOV AL, b_global_cc_act
	MOV BL, c_8_us
	CMP AL, BL
	JNE label1

	MOV AL, c_5_us
	MOV b_global_cc_act,AL

	JMP label2
	label1:

	MOV AL, b_global_cc_act
	ADD AL, 1_us
	MOV @aux1,AL
	JC ErrorOverflow

	MOV EAX, OFFSET FUNCTION_act_global_cc
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX
	MOV AL, b_global_cc_act
	MOV b_global_cc_act,AL

	CALL FUNCTION_act_global_cc

	label2:

	MOV _current_function_, 0
	RET 
	FUNCTION_act_global_cc ENDP

	MOV AL, c_7_us
	MOV b_global_cc_p_global,AL

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
END start
