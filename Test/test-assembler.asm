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

	b_global_clase_object_global DD ?
	newa_global_clase_updatea DB ?
	@aux4 DD ?
	a_global_clase DB ?
	s__MITAD_DE_a_ DB " MITAD DE a ", 10, 0
	b_global_clase DD ?
	a_global_clase_object_global DB ?
	c_global_clase DD ?
	half_global_printhalf DD ?
	c_15_us DB 15
	p_global_parent_object_global DD ?
	@aux3 DD ?
	@aux2 DD ?
	c_1_l DD 1
	@aux1 DB ?
	newc_global_clase_updatec DD ?
	newb_global_clase_updateb DD ?
	c_20_us DB 20
	s__VALOR_DE_p_ DB " VALOR DE p ", 10, 0
	c_global_clase_object_global DD ?
	newp_global_parent_convert2ten DD ?
	a_global_printhalf DD ?
	div_global DD ?
	c_2_0 DD 2.0
	c_10_us DB 10
	c_5_0 DD 5.0
	p_global_parent DD ?
	c_1_us DB 1
	s__VALOR_DE_a_ DB " VALOR DE a ", 10, 0
	s__VALOR_DE_c_ DB " VALOR DE c ", 10, 0

.code
start:

	FLD c_2_0
	FSTP div_global

	MOV AL, c_20_us
	ADD AL, c_10_us
	MOV @aux1, AL
	JC _SumOverflowError_

	MOV AL, @aux1
	MOV a_global_clase_object_global,AL

	invoke StdOut, addr s__VALOR_DE_a_

	invoke printf, cfm$("%hu\n"), a_global_clase_object_global

	MOV EAX, OFFSET FUNCTION_convert2ten_global_parent
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX

	MOV EAX, p_global_parent_object_global
	MOV p_global_parent,EAX
	MOVZX EAX, c_15_us
	MOV newp_global_parent_convert2ten,EAX

	CALL FUNCTION_convert2ten_global_parent

	MOV EAX, p_global_parent
	MOV p_global_parent_object_global,EAX

	MOV EAX, OFFSET FUNCTION_updatec_global_clase
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX

	MOV AL, a_global_clase_object_global
	MOV a_global_clase,AL

	FLD c_global_clase_object_global
	FSTP c_global_clase
	FLD c_5_0
	FSTP newc_global_clase_updatec

	CALL FUNCTION_updatec_global_clase

	MOV AL, a_global_clase
	MOV a_global_clase_object_global,AL
	FLD c_global_clase
	FSTP c_global_clase_object_global

	invoke StdOut, addr s__VALOR_DE_p_

	invoke printf, cfm$("%d\n"), p_global_parent_object_global

	invoke StdOut, addr s__VALOR_DE_c_

	FLD c_global_clase_object_global
	FSTP _float_aux_print_
	invoke printf, cfm$("%.20Lf\n"), _float_aux_print_

	invoke StdOut, addr s__MITAD_DE_a_

	MOV EAX, OFFSET FUNCTION_printhalf_global
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX
	MOVZX EAX, BYTE PTR a_global_clase_object_global
	MOV DWORD PTR [ESP-4], EAX
	FILD DWORD PTR [ESP-4]
	FSTP a_global_printhalf

	CALL FUNCTION_printhalf_global

	JMP _end_

FUNCTION_convert2ten_global_parent PROC
	MOV EBX, p_global_parent
	MOV ECX, newp_global_parent_convert2ten
	CMP EBX, ECX
	JE label1

	MOV EBX, newp_global_parent_convert2ten
	MOVZX ECX, BYTE PTR c_10_us
	CMP EBX, ECX
	JBE label2

	label3:
	MOV EBX, newp_global_parent_convert2ten
	MOVZX ECX, BYTE PTR c_10_us
	CMP EBX, ECX
	JBE label4

	MOV EAX, newp_global_parent_convert2ten
	MOVZX ECX, BYTE PTR c_1_us
	SUB EAX, ECX
	MOV @aux2, EAX
	MOV EAX, @aux2
	MOV newp_global_parent_convert2ten,EAX

	JMP label3
	label4:

	JMP label5
	label2:

	label6:
	MOV EBX, newp_global_parent_convert2ten
	MOVZX ECX, BYTE PTR c_10_us
	CMP EBX, ECX
	JAE label7

	MOV EAX, newp_global_parent_convert2ten
	ADD EAX, c_1_l
	MOV @aux3, EAX
	JO _SumOverflowError_

	MOV EAX, @aux3
	MOV newp_global_parent_convert2ten,EAX

	JMP label6
	label7:

	label5:

	MOV EAX, newp_global_parent_convert2ten
	MOV p_global_parent,EAX

	label1:

	MOV _current_function_, 0
	RET 
FUNCTION_convert2ten_global_parent ENDP

FUNCTION_updatea_global_clase PROC
	MOV AL, newa_global_clase_updatea
	MOV a_global_clase,AL

	MOV _current_function_, 0
	RET 
FUNCTION_updatea_global_clase ENDP

FUNCTION_updateb_global_clase PROC
	MOV EBX, b_global_clase
	MOV ECX, newb_global_clase_updateb
	CMP EBX, ECX
	JE label8

	MOV EAX, newb_global_clase_updateb
	MOV b_global_clase,EAX

	label8:

	MOV _current_function_, 0
	RET 
FUNCTION_updateb_global_clase ENDP

FUNCTION_updatec_global_clase PROC
	FLD newc_global_clase_updatec
	MOVZX EAX, BYTE PTR a_global_clase
	MOV DWORD PTR [ESP-4], EAX
	FILD DWORD PTR [ESP-4]
	FADD
	FSTP @aux4
	FLD @aux4
	FSTP c_global_clase

	MOV _current_function_, 0
	RET 
FUNCTION_updatec_global_clase ENDP

FUNCTION_printhalf_global PROC
	FLD a_global_printhalf
	FSTP half_global_printhalf

	FLD half_global_printhalf
	FSTP _float_aux_print_
	invoke printf, cfm$("%.20Lf\n"), _float_aux_print_

	MOV _current_function_, 0
	RET 
FUNCTION_printhalf_global ENDP

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
