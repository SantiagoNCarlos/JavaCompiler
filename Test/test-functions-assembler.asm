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

	c_3_l DD 3
	c_6_l DD 6
	@aux4 DB ?
	c__23_4 DD -23.4
	s__OTRA_CUENTA_OK_ DB " OTRA CUENTA OK ", 10, 0
	c_global_subtract DD ?
	s__CUENTA_OK_ DB " CUENTA OK ", 10, 0
	x_global_divide DB ?
	c_23_4 DD 23.4
	y_global_divide DB ?
	c_3_us DB 3
	c_2_us DB 2
	sub_global DD ?
	c_0_6 DD 0.6
	c_4_3 DD 4.3
	c__4_3 DD -4.3
	sub_global_subtract DD ?
	division_global_divide DD ?
	@aux3 DD ?
	d_global_subtract DD ?
	@aux2 DD ?
	@aux1 DD ?

.code
start:

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
	MOV @aux1,EAX
	FLD @aux1
	FSTP sub_global

	FLD sub_global
	FSTP _float_aux_print_
	invoke printf, cfm$("%.20Lf\n"), _float_aux_print_

	JMP _end_

FUNCTION_subtract_global PROC
	FLD c__4_3
	FLD c__23_4
	FSUB
	FSTP @aux2

	FLD @aux2
	FSTP sub_global_subtract

	FLD c__23_4
	FLD sub_global_subtract
	FADD
	FSTP @aux3

	FLD @aux3
	FSTP d_global_subtract

	FLD d_global_subtract
	FCOM c__4_3
	FSTSW AX
	SAHF
	JA label1

	invoke StdOut, addr s__CUENTA_OK_

	label1:

	MOV _current_function_, 0
	RET 
FUNCTION_subtract_global ENDP

FUNCTION_divide_global PROC
	MOV AL, c_2_us
	DIV c_3_us
	MOV @aux4,AL
	MOVZX EAX, @aux4
	MOV DWORD PTR [esp-4], EAX
	FLD DWORD PTR [esp-4]
	FSTP division_global_divide

	FLD division_global_divide
	FCOM c_0_6
	FSTSW AX
	SAHF
	JB label2

	invoke StdOut, addr s__OTRA_CUENTA_OK_

	label2:

	MOV _current_function_, 0
	RET 
FUNCTION_divide_global ENDP

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
