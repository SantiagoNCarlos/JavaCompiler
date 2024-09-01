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

	f2_global DD ?
	f1_global DD ?
	c_3_40282348E-38 DD 3.40282348E-38
	c__5 DD .5
	maxf_global DD ?
	maxe_global DD ?
	c_2147483647_l DD 2147483647
	c_-2147483648_l DD -2147483648
	novalido_global DD ?
	c_0_us DB 0
	c_1_ DD 1.
	aaaaaaaaaaaaaaaaaaaa_f5_global DD ?
	c_-3_40282348E-38 DD -3.40282348E-38
	c_4_l DD 4
	c__l DD 
	maxs_global DB ?
	c_3_40282347E+38 DD 3.40282347E+38
	c_--2147483648_l DD --2147483648
	minf_global DD ?
	mine_global DD ?
	novalidos_global DB ?
	mins_global DB ?
	novalidof_global DD ?
	c_3_40282347E38 DD 3.40282347E38
	c_255_us DB 255

.code
start:

	MOV EAX, c_--2147483648_l
	MOV mine_global,EAX

	MOV EAX, c_2147483647_l
	MOV maxe_global,EAX

	MOV EAX, c_2147483647_l
	MOV novalido_global,EAX

	MOV EAX, c_--2147483648_l
	MOV novalido_global,EAX

	FLD c_-3_40282348E-38
	FSTP minf_global

	FLD c_3_40282347E+38
	FSTP maxf_global

	FLD c_3_40282347E38
	FSTP novalidof_global

	MOV AL, c_0_us
	MOV mins_global,AL

	MOV AL, c_255_us
	MOV maxs_global,AL

	MOV AL, c_255_us
	MOV novalidos_global,AL

	FLD c__5
	FSTP f1_global

	FLD c_1_
	FSTP f2_global

	MOV EAX, c__l
	MOV novalido_global,EAX

	MOV EAX, c_4_l
	MOV aaaaaaaaaaaaaaaaaaaa_f5_global,EAX

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
