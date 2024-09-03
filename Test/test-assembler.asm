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

	_current_function_ DD 0
	_max_float_value_ DD 3.40282347e+38
	SumOverflowErrorMsg DB "Overflow detected in a INTEGER SUM operation", 10, 0
	ProductOverflowErrorMsg DB "Overflow detected in a FLOAT PRODUCT operation", 10, 0
	RecursionErrorMsg DB "Recursive call detected", 10, 0

	c_2_40282347e_38 DD 2.40282347e+38
	c_1_0 DD 1.0
	x_global DD ?
	@aux1 DD ?

.code
start:

	FLD c_2_40282347e_38
	FSTP x_global

	FLD c_2_40282347e_38
	FLD c_1_0
	FMUL 
	FLD ST(0)
	FABS 
	FCOM _max_float_value_
	FSTSW AX
	SAHF
	JA _ProductOverflowError_
	FXCH
	FSTP @aux1

	FLD @aux1
	FSTP x_global

	invoke printf, cfm$("%.20Lf\n"), x_global

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
