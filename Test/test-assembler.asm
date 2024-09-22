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

	@aux6 DB ?
	@aux5 DB ?
	@aux4 DB ?
	b_global_clase_obj_global DB ?
	s__LLEGUE_ DB " LLEGUE ", 10, 0
	b_global_clase DB ?
	s__NO_LLEGUE_ DB " NO LLEGUE ", 10, 0
	c_4_us DB 4
	c_2_us DB 2
	@aux3 DB ?
	@aux2 DD ?
	c_6_us DB 6
	@aux1 DB ?
	c_8_us DB 8
	c_3_l DD 3
	c_5_us DB 5
	e_global DD ?
	c_3_us DB 3
	a_global DD ?
	p_global DB ?
	c_global DB ?
	c_7_us DB 7
	d_global DB ?
	c_9_us DB 9

.code
start:

	MOV AL, c_2_us
	ADD AL, c_3_us
	MOV @aux1,AL
	JC _SumOverflowError_

	MOV AL, @aux1
	MOV c_global,AL

	MOV EAX, c_3_l
	MUL c_2_us
	MOV @aux2,EAX
	MOV EAX, @aux2
	MOV a_global,EAX

	MOV AL, c_2_us
	MOV b_global_clase_obj_global,AL

	label1:
	MOV AL, b_global_clase_obj_global
	MOV BL, c_8_us
	CMP AL, BL
	JA label2

	MOV AL, b_global_clase_obj_global
	MUL c_2_us
	MOV @aux3,AL
	MOV AL, @aux3
	MOV b_global_clase_obj_global,AL

	MOV AL, c_9_us
	ADD AL, c_4_us
	MOV @aux4,AL
	JC _SumOverflowError_

	MOV AL, @aux4
	MOV p_global,AL

	invoke printf, cfm$("%hu\n"), b_global_clase_obj_global

	MOV EAX, OFFSET FUNCTION_func_global_clase
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX

	MOV AL, b_global_clase_obj_global
	MOV b_global_clase,AL

	CALL FUNCTION_func_global_clase

	MOV AL, b_global_clase
	MOV b_global_clase_obj_global,AL

	JMP label1
	label2:

	MOV AL, c_3_us
	MUL b_global_clase_obj_global
	MOV @aux5,AL
	MOV AL, @aux5
	MOV c_global,AL

	MOV AL, b_global_clase_obj_global
	MOV BL, c_5_us
	CMP AL, BL
	JAE label3

	label3:

	MOV AL, d_global
	DIV c_global
	MOV @aux6,AL
	MOV AL, @aux6
	MOV d_global,AL

	MOV AL, c_7_us
	MOV d_global,AL

	JMP _end_

FUNCTION_func_global_clase PROC
	MOV AL, b_global_clase
	MOV BL, c_8_us
	CMP AL, BL
	JNE label4

	invoke StdOut, addr s__LLEGUE_

	JMP label5
	label4:

	invoke StdOut, addr s__NO_LLEGUE_

	label5:

	MOV _current_function_, 0
	RET 
FUNCTION_func_global_clase ENDP

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
