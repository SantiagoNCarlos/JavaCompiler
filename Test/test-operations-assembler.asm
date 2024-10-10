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

	@aux6 DD ?
	s__VALOR_CORRECTO_ DB " VALOR CORRECTO ", 10, 0
	@aux5 DD ?
	c_6_l DD 6
	@aux4 DD ?
	c_8_l DD 8
	c_3_77E_6 DD 3.77E+6
	c_48_us DB 48
	c_4_us DB 4
	c_3_us DB 3
	c_2_us DB 2
	c_21_0 DD 21.0
	c_0_5 DD 0.5
	a_global DD ?
	c_global DD ?
	@aux3 DD ?
	f_global DD ?
	@aux2 DD ?
	b_global DD ?
	d_global DD ?
	@aux1 DB ?
	c__21_0 DD -21.0

.code
start:

	MOV AL, c_4_us
	MOV AH, 0
	DIV c_2_us
	MOV @aux1, AL
	MOVZX EAX, BYTE PTR @aux1
	MOV DWORD PTR [ESP-4], EAX
	FILD DWORD PTR [ESP-4]
	FSTP f_global

	MOVZX EAX, c_3_us
	MOV a_global,EAX

	MOV EAX, c_8_l
	MOV b_global,EAX

	MOVZX EAX, BYTE PTR c_3_us
	SUB EAX, c_8_l
	MOV @aux2, EAX
	MOV EAX, @aux2
	MOV a_global,EAX

	MOV EAX, c_6_l
	MUL c_8_l
	MOV @aux3, EAX
	MOV EAX, @aux3
	MOV b_global,EAX

	FLD c__21_0
	MOVZX EAX, BYTE PTR c_3_us
	MOV DWORD PTR [ESP-4], EAX
	FILD DWORD PTR [ESP-4]
	FADD
	FSTP @aux4
	FLD @aux4
	FILD DWORD PTR b_global
	FSUB
	FSTP @aux5
	FLD @aux5
	FSTP c_global

	FLD c_3_77E_6
	FLD c_0_5
	FADD
	FSTP @aux6
	FLD @aux6
	FSTP d_global

	MOV EBX, b_global
	MOVZX ECX, BYTE PTR c_48_us
	CMP EBX, ECX
	JNE label1

	invoke StdOut, addr s__VALOR_CORRECTO_

	label1:

	invoke printf, cfm$("%d\n"), a_global

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
