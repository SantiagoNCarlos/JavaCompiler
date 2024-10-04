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

	c_global_cc_c1_global DD ?
	b_global_cb_b1_global DB ?
	b_global_cb_c1_global DB ?
	a_global_ca_c1_global DB ?
	c_5_us DB 5
	b_global_cb DB ?
	c_3_us DB 3
	c_global_cc DD ?
	c_2_us DB 2
	c_1_2 DD 1.2
	a_global_ca DB ?
	d_global_cc DD ?
	d_global_cc_c1_global DD ?
	c_0_us DB 0
	a_global_ca_a1_global DB ?
	c_9_us DB 9

.code
start:

	MOV AL, c_9_us
	MOV b_global_cc_c1_global,AL

	MOV EAX, OFFSET FUNCTION_m_global_ca
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX

	MOV AL, a_global_ca_a1_global
	MOV a_global_ca,AL

	CALL FUNCTION_m_global_ca

	MOV AL, a_global_ca
	MOV a_global_ca_a1_global,AL

	MOV EAX, OFFSET FUNCTION_n_global_cb
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX

	MOV AL, b_global_cb_b1_global
	MOV b_global_cb,AL

	CALL FUNCTION_n_global_cb

	MOV AL, b_global_cb
	MOV b_global_cb_b1_global,AL

	JMP _end_

FUNCTION_m_global_ca PROC
	MOV AL, a_global_ca
	MOV BL, c_0_us
	CMP AL, BL
	JNE label1

	label1:

	MOV _current_function_, 0
	RET 
FUNCTION_m_global_ca ENDP

FUNCTION_n_global_cb PROC
	MOV AL, b_global_cb
	MOV BL, c_0_us
	CMP AL, BL
	JE label2

	label2:

	MOV _current_function_, 0
	RET 
FUNCTION_n_global_cb ENDP

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
