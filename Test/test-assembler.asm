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
	c_9_75 DD 9.75
	c_1_33 DD 1.33
	s__SI_ DB " SI ", 10, 0
	c_2_us DB 2
	c_1_3 DD 1.3
	a_global DB ?
	c_global DD ?
	@aux3 DD ?
	@aux2 DD ?
	b_global DD ?
	d_global DD ?
	c_6_us DB 6
	@aux1 DD ?

.code
start:

	MOVZX EAX, BYTE PTR c_6_us
	MUL c_3_l
	MOV @aux1, EAX
	MOV EAX, @aux1
	MOV b_global,EAX

	MOVZX EAX, BYTE PTR c_2_us
	MOV DWORD PTR [ESP-4], EAX
	FILD DWORD PTR [ESP-4]
	FLD c_1_3
	FMUL
	FLD ST(0)
	FABS
	FCOM _max_float_value_
	FSTSW AX
	SAHF
	JA _ProductOverflowError_
	FXCH
	FSTP @aux2
	FLD @aux2
	FSTP c_global

	FLD c_1_33
	FILD DWORD PTR c_3_l
	FADD
	FSTP @aux3
	FLD @aux3
	FSTP d_global

	FILD DWORD PTR b_global
	FCOM c_9_75
	FSTSW AX
	SAHF
	JBE label1

	invoke StdOut, addr s__SI_

	label1:

	invoke printf, cfm$("%hu\n"), a_global

	invoke printf, cfm$("%d\n"), b_global

	FLD c_global
	FSTP _float_aux_print_
	invoke printf, cfm$("%.20Lf\n"), _float_aux_print_

	FLD d_global
	FSTP _float_aux_print_
	invoke printf, cfm$("%.20Lf\n"), _float_aux_print_

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
