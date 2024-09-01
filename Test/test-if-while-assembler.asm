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

	@aux6 DD ?
	ii_global DD ?
	@aux5 DD ?
	@aux4 DD ?
	s_#Test_para_comprobar_la_funcionalidad_de_los_bloques_IF_y_WHILE DB "#Test para comprobar la funcionalidad de los bloques IF y WHILE", 10, 0
	c_7_77777 DD 7.77777
	c_99_l DD 99
	c_0_1e3 DD 0.1e3
	@aux3 DD ?
	@aux2 DD ?
	c_1_l DD 1
	@aux1 DD ?
	c_2_l DD 2
	l_global DD ?
	c_3_l DD 3
	c_4_l DD 4
	k_global DD ?
	c_6_l DD 6
	c_7_l DD 7
	c_8_l DD 8
	bb_global DD ?
	c_7_74 DD 7.74
	c_3_us DB 3
	s_global DB ?
	c_0_3 DD 0.3
	h_global DD ?
	c_8_0 DD 8.0
	c_1_us DB 1
	c_2_ DD 2.
	c_432554_l DD 432554

.code
start:

	invoke printf, addr #Test para comprobar la funcionalidad de los bloques IF y WHILE

	MOV EAX, c_432554_l
	MOV l_global,EAX

	MOV EAX, c_1_l
	ADD EAX, c_2_l
	MOV @aux1,EAX
	JO ErrorOverflow

	MOV EAX, @aux1
	MOV l_global,EAX

	MOV EAX, l_global
	ADD EAX, c_1_l
	MOV @aux2,EAX
	JO ErrorOverflow

	FLD c_2_
	FSTP h_global

	FLD c_2_
	FLD c_2_l
	FSUB
	FSTP @aux3

	FLD @aux3
	FSTP h_global

	MOV EBX, s_global
	MOV ECX, c_3_l
	CMP EBX, ECX
	JAE label1

	MOV EAX, c_8_l
	MOV k_global,EAX

	FLD c_4_l
	FSTP h_global

	FLD c_8_0
	FSTP ii_global

	label1:

	FLD c_2_
	FSTP h_global

	label2:
	MOV EBX, l_global
	MOV ECX, c_6_l
	CMP EBX, ECX
	JAE label3

	FLD c_7_77777
	FSTP bb_global

	FLD c_99_l
	FSTP h_global

	JMP label2
	label3:

	FLD c_7_74
	FSTP h_global

	MOV EAX, c_7_l
	MOV l_global,EAX

	MOV EAX, c_1_l
	MUL EAX, c_3_l
	MOV @aux4,EAX
	MOV EAX, @aux4
	MOV l_global,EAX

	MOV EBX, l_global
	MOV ECX, c_6_l
	CMP EBX, ECX
	JAE label4

	MOV EBX, l_global
	MOV ECX, c_2_l
	CMP EBX, ECX
	JBE label5

	MOV EAX, c_3_us
	MOV l_global,EAX

	label6:
	FLD c_7_74
	FLD c_0_1e3
	FSTSW aux_mem_2bytes
	MOV AX, aux_mem_2bytes
	SAHF
	JBE label7

	FLD c_7_74
	FLD c_1_us
	FADD
	FSTP @aux5

	FLD @aux5
	FSTP h_global

	JMP label6
	label7:

	JMP label8
	label5:

	FLD h_global
	FLD c_0_3
	FSTSW aux_mem_2bytes
	MOV AX, aux_mem_2bytes
	SAHF
	JE label9

	FLD h_global
	FLD 1_0
	FADD
	FSTP @aux6

	label9:

	label8:

	label4:

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
