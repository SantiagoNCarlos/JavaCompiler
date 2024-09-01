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

	@aux4 DD ?
	c_4_324 DD 4.324
	c_-23_4562 DD -23.4562
	bb_global_divide DD ?
	c_global_subtract DD ?
	c_-4_324 DD -4.324
	c_23_4562 DD 23.4562
	c_7_77777 DD 7.77777
	sub_global DD ?
	c_99_l DD 99
	sub_global_subtract DD ?
	c_0_us DB 0
	@aux3 DB ?
	d_global_subtract DD ?
	@aux2 DD ?
	c_6_us DB 6
	@aux1 DD ?
	c_3_l DD 3
	c_6_l DD 6
	x_global_divide DB ?
	y_global_divide DB ?
	c_3_us DB 3
	division_global_divide DD ?

.code
start:

	FUNCTION_subtract_global PROC
	FLD c_-4_324
	FSTP c_global_subtract

	FLD c_-23_4562
	FSTP d_global_subtract

	FLD c_-4_324
	FLD c_-23_4562
	FSUB
	FSTP @aux1

	FLD @aux1
	FSTP sub_global_subtract

	FLD c_-23_4562
	FLD sub_global_subtract
	FADD
	FSTP @aux2

	FLD @aux2
	FSTP d_global_subtract

	MOV _current_function_, 0
	RET 
	FUNCTION_subtract_global ENDP

	FUNCTION_divide_global PROC
	MOV AL, c_0_us
	MOV x_global_divide,AL

	MOV AL, c_3_us
	MOV y_global_divide,AL

	MOV AL, c_0_us
	DIV AL, c_3_us
	MOV @aux3,AL
	FLD @aux3
	FSTP division_global_divide

	label1:
	MOV AL, c_0_us
	MOV BL, c_6_us
	CMP AL, BL
	JAE label2

	FLD c_7_77777
	FSTP bb_global_divide

	MOV EAX, c_99_l
	MOV y_global_divide,EAX

	JMP label1
	label2:

	MOV _current_function_, 0
	RET 
	FUNCTION_divide_global ENDP

	MOV EAX, OFFSET FUNCTION_subtract_global
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX
	FLD sub_global
	FSTP sub_global_subtract

	CALL FUNCTION_subtract_global


	CALL FUNCTION_divide_global

	MOV EAX, c_6_l
	SUB EAX, c_3_l
	MOV @aux4,EAX
	FLD @aux4
	FSTP sub_global

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
