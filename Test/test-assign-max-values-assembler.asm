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

	aux_2bytes DW 0
	_current_function_ DD 0
	_max_float_value_ DD 3.40282347e+38
	SumOverflowErrorMsg DB "Overflow detected in a INTEGER SUM operation", 10, 0
	ProductOverflowErrorMsg DB "Overflow detected in a FLOAT PRODUCT operation", 10, 0
	RecursionErrorMsg DB "Recursive call detected", 10, 0

	f2_global DD ?
	f1_global DD ?
	c_3_40282348E_38 DD 3.40282348E-38
	c__5 DD 0.5
	maxf_global DD ?
	maxe_global DD ?
	c_2147483647_l DD 2147483647
	c__2147483648_l DD -2147483648
	novalido_global DD ?
	c_0_us DB 0
	c_1_ DD 1.
	@aux1 DD ?
	aaaaaaaaaaaaaaaaaaaa_f5_global DD ?
	c__3_40282348E_38 DD -3.40282348E-38
	c_4_l DD 4
	maxs_global DB ?
	s__TODO_JOYA_ DB " TODO JOYA ", 10, 0
	c_3_40282347E_38 DD 3.40282347E+38
	c_0_5 DD 0.5
	minf_global DD ?
	mine_global DD ?
	novalidos_global DB ?
	mins_global DB ?
	novalidof_global DD ?
	c_3_40282347E38 DD 3.40282347E38
	c_255_us DB 255

.code
start:

	invoke printf, cfm$("%llu\n"), novalido_global

	invoke printf, cfm$("%hu\n"), maxs_global

	FLD c__5
	FLD c_1_
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
	FSTP f1_global

	FLD f1_global
	FCOM c_0_5
	FSTSW AX
	SAHF
	JNE label1

	invoke StdOut, addr s__TODO_JOYA_

	label1:

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
