.586
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
	u_global_persona_actualizar DD ?
	@aux4 DD ?
	o_global_persona DD ?
	edad_global_persona DB ?
	u_global_actualizar DD ?
	a_global DB ?
	x_global DD ?
	c_1_us DB 1
	o_global_persona_p_global DD ?
	edad_global_persona_p_global DD ?
	c_global DB ?
	@aux3 DB ?
	@aux2 DD ?
	b_global DB ?
	d_global DB ?
	c_6_us DB 6
	@aux1 DD ?
	c_9_us DB 9

.code
start:

	MOV AL, c_6_us
	MOV b_global,AL

	MOV AL, c_9_us
	MOV c_global,AL

	MOV AL, c_1_us
	MOV d_global,AL

	FLD x_global
	FLD 1_0
	FADD
	FSTP @aux1

	FLD o_global_persona_p_global
	FSTP u_global_persona_actualizar

	CALL FUNCTION_actualizar_global_persona

	FUNCTION_actualizar_global_persona PROC
	FLD u_global_persona_actualizar
	FLD c_1_us
	FADD
	FSTP @aux2

	FLD @aux2
	FSTP u_global_persona_actualizar

	RET 
	FUNCTION_actualizar_global_persona ENDP
	FUNCTION_actualizar2_global_persona PROC
	MOV AL, edad_global_persona
	ADD AL, c_1_us
	MOV @aux3,AL
	MOV AL, @aux3
	MOV edad_global_persona,AL

	RET 
	FUNCTION_actualizar2_global_persona ENDP
	FUNCTION_actualizar_global PROC
	FLD u_global_actualizar
	FLD c_1_us
	FADD
	FSTP @aux4

	FLD @aux4
	FSTP u_global_actualizar

	RET 
	FUNCTION_actualizar_global ENDP

end start
