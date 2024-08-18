.586
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

	c_5_us DB 5
	c_0_us DB 0
	c_1_us DB 1
	b_global DB ?

.code
start:

	MOV AL, b_global
	MOV BL, c_0_us
	CMP AL, BL
	JE label1

	MOV AL, c_5_us
	MOV b_global,AL

	JMP label2
	label1:

	MOV AL, c_1_us
	MOV b_global,AL

	label2:

	FUNCTION_act_global PROC
	MOV AL, c_1_us
	MOV BL, c_0_us
	CMP AL, BL
	JE label3

	MOV AL, c_5_us
	MOV b_global,AL

	JMP label4
	label3:

	MOV AL, c_1_us
	MOV b_global,AL

	label4:

	RET 
	FUNCTION_act_global ENDP


END start
