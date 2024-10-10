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

	@aux5 DD ?
	@aux4 DD ?
	c__23_4 DD -23.4
	c_global_subtract DD ?
	s__CUENTA_OK_ DB " CUENTA OK ", 10, 0
	c_23_4 DD 23.4
	c_2_0 DD 2.0
	c_3_0 DD 3.0
	c_3_1 DD 3.1
	sub_global DD ?
	c_2_3 DD 2.3
	c_4_3 DD 4.3
	c__4_3 DD -4.3
	sub_global_subtract DD ?
	@aux3 DD ?
	d_global_subtract DD ?
	@aux2 DD ?
	@aux1 DD ?

.code
start:

	FLD c_3_0
	FLD c_2_3
	FADD
	FSTP @aux1
	FLD @aux1
	FSTP sub_global

	MOV EAX, OFFSET FUNCTION_subtract_global
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX
	FLD sub_global
	FSTP sub_global_subtract

	CALL FUNCTION_subtract_global

	JMP _end_

FUNCTION_subtract_global PROC
	FLD c__4_3
	FLD c_2_0
	FADD
	FSTP @aux2
	FLD @aux2
	FSTP c_global_subtract

	FLD c__23_4
	FLD c_3_1
	FSUB
	FSTP @aux3
	FLD @aux3
	FSTP d_global_subtract

	FLD c_global_subtract
	FLD d_global_subtract
	FSUB
	FSTP @aux4
	FLD @aux4
	FSTP sub_global_subtract

	FLD d_global_subtract
	FLD sub_global_subtract
	FADD
	FSTP @aux5
	FLD @aux5
	FSTP d_global_subtract

	FLD d_global_subtract
	FSTP _float_aux_print_
	invoke printf, cfm$("%.20Lf\n"), _float_aux_print_

	FLD d_global_subtract
	FCOM c__4_3
	FSTSW AX
	SAHF
	JBE label1

	invoke StdOut, addr s__CUENTA_OK_

	label1:

	MOV _current_function_, 0
	RET 
FUNCTION_subtract_global ENDP

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
