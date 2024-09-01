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
	@aux1 DD ?
	c_2_l DD 2
	c_-21_0 DD -21.0

.code
start:

	MOV EAX, c_3_l
	MOV a_global,EAX

	MOV EAX, c_8_l
	MOV b_global,EAX

	MOV EAX, b_global
	ADD EAX, 1_l
	MOV @aux1,EAX
	JO ErrorOverflow

	MOV EAX, c_3_l
	SUB EAX, c_8_l
	MOV @aux2,EAX
	MOV EAX, @aux2
	MOV a_global,EAX

	MOV EAX, a_global
	DIV EAX, c_2_l
	MOV @aux3,EAX
	MOV EAX, @aux3
	MOV a_global,EAX

	MOV EAX, c_6_l
	MUL EAX, c_8_l
	MOV @aux4,EAX
	MOV EAX, @aux4
	MOV b_global,EAX

	FLD c_-21_0
	FSTP c_global

	FLD c_3_77E6
	FSTP d_global

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
