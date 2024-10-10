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

	k_global DD ?
	s_global DD ?
	zz_global DD ?
	c_1_0 DD 1.0
	c_2147483647_l DD 2147483647
	c_3_e_2 DD 3.e+2
	a_global DB ?
	c_0_us DB 0
	zzz_global DD ?
	c_1_ DD 1.
	c__3_40282347E38 DD -3.40282347E38

.code
start:

	FLD c_3_e_2
	FSTP s_global

	FLD c_1_
	FSTP s_global

	FLD c_1_
	FSTP s_global

	FLD s_global
	FCOM c_1_0
	FSTSW AX
	SAHF
	JAE label1

	MOV AL, c_0_us
	MOV a_global,AL

	label1:

	MOV AL, c_0_us
	MOV a_global,AL

	MOV EAX, c_2147483647_l
	MOV k_global,EAX

	FLD c__3_40282347E38
	FSTP s_global

	FLD c_1_
	FSTP s_global

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
