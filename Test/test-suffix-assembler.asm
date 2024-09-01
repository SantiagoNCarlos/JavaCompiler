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

	k_global DD ?
	s_global DD ?
	zz_global DD ?
	c_1_0 DD 1.0
	c_2147483647_l DD 2147483647
	a_global DB ?
	c_0_us DB 0
	c_--3_40282347E38 DD --3.40282347E38
	zzz_global DD ?
	c_1_ DD 1.
	c_-3_40282347E38 DD -3.40282347E38
	c_3_e2 DD 3.e2

.code
start:

	FLD c_3_e2
	FSTP s_global

	FLD c_1_
	FSTP s_global

	FLD c_1_
	FLD c_1_0
	FSTSW aux_mem_2bytes
	MOV AX, aux_mem_2bytes
	SAHF
	JAE label1

	MOV AL, c_0_us
	MOV a_global,AL

	label1:

	MOV AL, c_0_us
	MOV a_global,AL

	MOV EAX, c_2147483647_l
	MOV k_global,EAX

	FLD c_--3_40282347E38
	FSTP s_global

	FLD c_1_
	FSTP s_global

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
