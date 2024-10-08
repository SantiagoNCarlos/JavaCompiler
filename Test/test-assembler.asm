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

	pp_global_clase2_var_global DD ?
	c_9_8 DD 9.8
	@aux4 DD ?
	c_global_testclase_foo DD ?
	s__AAA123_142_ DB " AAA123 142 ", 10, 0
	l2_global_testclase DD ?
	l1_global_testclase_prueba_global DD ?
	@aux3 DD ?
	@aux2 DD ?
	@aux1 DD ?
	c_2_l DD 2
	cc_global DD ?
	pp_global_clase2 DD ?
	k_global DD ?
	z_global_clase2_foo2 DD ?
	l2_global_testclase_prueba_global DD ?
	pp_global_clase2_prueba_global DD ?
	c_1_0 DD 1.0
	c_1_2 DD 1.2
	uu_global DD ?
	l1_global_testclase DD ?

.code
start:

	invoke StdOut, addr s__AAA123_142_

	FLD cc_global
	FLD c_1_0
	FADD
	FSTP @aux1
	FLD @aux1
	FSTP cc_global

	FLD cc_global
	FSTP _float_aux_print_
	invoke printf, cfm$("%.20Lf\n"), _float_aux_print_

	FLD cc_global
	FLD c_9_8
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
	FSTP uu_global

	MOV EAX, OFFSET FUNCTION_foo_global_testclase
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX

	MOV EAX, l1_global_testclase_prueba_global
	MOV l1_global_testclase,EAX
	CALL FUNCTION_foo_global_testclase

	MOV EAX, l1_global_testclase
	MOV l1_global_testclase_prueba_global,EAX

	FLD uu_global
	FCOM cc_global
	FSTSW AX
	SAHF
	JNE label1

	FLD uu_global
	FLD c_1_0
	FADD
	FSTP @aux3
	FLD @aux3
	FSTP uu_global

	label1:

	JMP _end_

FUNCTION_foo2_global_clase2 PROC
	MOV EAX, c_2_l
	MOV z_global_clase2_foo2,EAX

	MOV _current_function_, 0
	RET 
FUNCTION_foo2_global_clase2 ENDP

FUNCTION_foo_global_testclase PROC
	FLD c_1_2
	FILD DWORD PTR l1_global_testclase
	FADD
	FSTP @aux4
	FLD @aux4
	FSTP c_global_testclase_foo

	MOV _current_function_, 0
	RET 
FUNCTION_foo_global_testclase ENDP

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
