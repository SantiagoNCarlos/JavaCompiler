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

	f2_global DD ?
	c_3_40282347e_38 DD 3.40282347e+38
	f1_global DD ?
	s__VERIFICACION_DE_RECURSION_ DB " VERIFICACION DE RECURSION ", 10, 0
	p_global_func DB ?
	l1_global DD ?
	c_2147483647_l DD 2147483647
	s__VERIFICACION_DE_OVERFLOW_EN_SUMA_DE_ENTEROS_ DB " VERIFICACION DE OVERFLOW EN SUMA DE ENTEROS ", 10, 0
	c_1_us DB 1
	l2_global DD ?
	@aux3 DB ?
	s__VERIFICACION_DE_OVERFLOW_EN_PRODUCTO_DE_FLOATS_ DB " VERIFICACION DE OVERFLOW EN PRODUCTO DE FLOATS ", 10, 0
	@aux2 DD ?
	@aux1 DD ?

.code
start:

	invoke StdOut, addr s__VERIFICACION_DE_RECURSION_

	MOV EAX, OFFSET FUNCTION_func_global
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX
	MOV AL, c_1_us
	MOV p_global_func,AL

	CALL FUNCTION_func_global

	invoke StdOut, addr s__VERIFICACION_DE_OVERFLOW_EN_PRODUCTO_DE_FLOATS_

	FLD c_3_40282347e_38
	FSTP f1_global

	FLD c_3_40282347e_38
	FSTP f2_global

	FLD c_3_40282347e_38
	FLD c_3_40282347e_38
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

	invoke StdOut, addr s__VERIFICACION_DE_OVERFLOW_EN_SUMA_DE_ENTEROS_

	MOV EAX, c_2147483647_l
	MOV l1_global,EAX

	MOV EAX, c_2147483647_l
	MOV l2_global,EAX

	MOV EAX, c_2147483647_l
	ADD EAX, c_2147483647_l
	MOV @aux2, EAX
	JO _SumOverflowError_

	MOV EAX, @aux2
	MOV l1_global,EAX

	JMP _end_

FUNCTION_func_global PROC
	MOV AL, p_global_func
	ADD AL, c_1_us
	MOV @aux3, AL
	JC _SumOverflowError_

	MOV AL, @aux3
	MOV p_global_func,AL

	MOV EAX, OFFSET FUNCTION_func_global
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX
	MOV AL, p_global_func
	MOV p_global_func,AL

	CALL FUNCTION_func_global

	MOV _current_function_, 0
	RET 
FUNCTION_func_global ENDP

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
