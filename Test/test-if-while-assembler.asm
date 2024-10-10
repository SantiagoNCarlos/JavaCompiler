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

	ii_global DD ?
	@aux4 DD ?
	s_Test_para_comprobar_la_funcionalidad_de_los_bloques_IF_y_WHILE DB "Test para comprobar la funcionalidad de los bloques IF y WHILE", 10, 0
	s__l_ya_vale_6_ DB " l ya vale 6 ", 10, 0
	s__Iterando_ DB " Iterando ", 10, 0
	@aux3 DD ?
	c_0_l DD 0
	@aux2 DD ?
	c_1_l DD 1
	@aux1 DD ?
	c_2_l DD 2
	l_global DD ?
	c_3_l DD 3
	s__WHILE_con_L_ DB " WHILE con L ", 10, 0
	c_4_l DD 4
	k_global DD ?
	c_6_l DD 6
	c_8_l DD 8
	c_7_74 DD 7.74
	c_3_us DB 3
	s_global DB ?
	c_1_0 DD 1.0
	c_1_2 DD 1.2
	c_0_3 DD 0.3
	h_global DD ?
	c_8_0 DD 8.0
	c_1_us DB 1
	c_2_ DD 2.
	c_0_1e_1 DD 0.1e-1

.code
start:

	invoke StdOut, addr s_Test_para_comprobar_la_funcionalidad_de_los_bloques_IF_y_WHILE

	MOV AL, c_1_us
	MOV s_global,AL

	MOV EAX, c_0_l
	MOV l_global,EAX

	FLD c_2_
	FSTP h_global

	FLD c_2_
	FSTP _float_aux_print_
	invoke printf, cfm$("%.20Lf\n"), _float_aux_print_

	MOV AL, c_1_us
	MOV s_global,AL

	MOVZX EBX, BYTE PTR s_global
	MOV ECX, c_3_l
	CMP EBX, ECX
	JAE label1

	MOV EAX, c_8_l
	MOV k_global,EAX

	FILD DWORD PTR c_4_l
	FSTP h_global

	FLD c_8_0
	FSTP ii_global

	label1:

	FLD h_global
	FSTP _float_aux_print_
	invoke printf, cfm$("%.20Lf\n"), _float_aux_print_

	invoke StdOut, addr s__WHILE_con_L_

	label2:
	MOV EBX, l_global
	MOV ECX, c_6_l
	CMP EBX, ECX
	JAE label3

	invoke StdOut, addr s__Iterando_

	MOV EAX, l_global
	ADD EAX, c_1_l
	MOV @aux1, EAX
	JO _SumOverflowError_

	MOV EAX, @aux1
	MOV l_global,EAX

	JMP label2
	label3:

	invoke printf, cfm$("%d\n"), l_global

	FLD c_7_74
	FLD c_1_2
	FADD
	FSTP @aux2
	FLD @aux2
	FSTP h_global

	MOVZX EAX, c_3_us
	MOV l_global,EAX

	FLD h_global
	FSTP _float_aux_print_
	invoke printf, cfm$("%.20Lf\n"), _float_aux_print_

	MOVZX EAX, c_3_us
	MOV l_global,EAX

	MOV EBX, l_global
	MOV ECX, c_6_l
	CMP EBX, ECX
	JAE label4

	MOV EBX, l_global
	MOV ECX, c_2_l
	CMP EBX, ECX
	JBE label5

	MOVZX EAX, c_3_us
	MOV l_global,EAX

	label6:
	FLD h_global
	FCOM c_0_1e_1
	FSTSW AX
	SAHF
	JBE label7

	FLD h_global
	MOVZX EAX, BYTE PTR c_1_us
	MOV DWORD PTR [ESP-4], EAX
	FILD DWORD PTR [ESP-4]
	FSUB
	FSTP @aux3
	FLD @aux3
	FSTP h_global

	JMP label6
	label7:

	JMP label8
	label5:

	FLD h_global
	FCOM c_0_3
	FSTSW AX
	SAHF
	JE label9

	FLD h_global
	FLD c_1_0
	FADD
	FSTP @aux4
	FLD @aux4
	FSTP h_global

	label9:

	label8:

	JMP label10
	label4:

	invoke StdOut, addr s__l_ya_vale_6_

	label10:

	FLD h_global
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
