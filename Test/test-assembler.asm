.386
.model flat, stdcall
option casemap:none


include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\masm32.inc
include \masm32\include\user32.inc

includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
includelib \masm32\lib\msvcrt.lib
includelib \masm32\lib\user32.lib
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

	@aux4 DB ?
	s__NO_LLEGUE_PTM_ DB " NO LLEGUE PTM ", 10, 0
	b_global_clase_obj_global DB ?
	s__LLEGUE_ DB " LLEGUE ", 10, 0
	b_global_clase DB ?
	c_1_us DB 1
	@aux3 DB ?
	@aux2 DB ?
	c_6_us DB 6
	@aux1 DB ?
	c_8_us DB 8

.code
start:

	MOV AL, c_6_us
	MOV b_global_clase_obj_global,AL

	invoke printf, offset formatStringUShort, b_global_clase_obj_global

	MOV EAX, OFFSET FUNCTION_func_global_clase
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX
	MOV AL, b_global_clase_obj_global
	MOV b_global_clase,AL

	CALL FUNCTION_func_global_clase

	MOV AL, c_6_us
	ADD AL, c_1_us
	MOV @aux1,AL
	JC _SumOverflowError_

	MOV AL, @aux1
	MOV b_global_clase_obj_global,AL

	invoke printf, offset formatStringUShort, b_global_clase_obj_global

	MOV EAX, OFFSET FUNCTION_func_global_clase
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX
	MOV AL, b_global_clase_obj_global
	MOV b_global_clase,AL

	CALL FUNCTION_func_global_clase

	MOV AL, b_global_clase_obj_global
	ADD AL, c_1_us
	MOV @aux2,AL
	JC _SumOverflowError_

	MOV AL, @aux2
	MOV b_global_clase_obj_global,AL

	invoke printf, offset formatStringUShort, b_global_clase_obj_global

	MOV EAX, OFFSET FUNCTION_func_global_clase
	CMP EAX, _current_function_
	JE _RecursionError_
	MOV _current_function_, EAX
	MOV AL, b_global_clase_obj_global
	MOV b_global_clase,AL

	CALL FUNCTION_func_global_clase

	JMP _end_

FUNCTION_func_global_clase PROC
	MOV AL, b_global_clase
	MOV BL, c_8_us
	CMP AL, BL
	JNE label1

	invoke printf, addr s__LLEGUE_

	JMP label2
	label1:

	invoke printf, addr s__NO_LLEGUE_PTM_

	MOV AL, b_global_clase
	ADD AL, c_1_us
	MOV @aux3,AL
	JC _SumOverflowError_

	MOV AL, @aux3
	MOV b_global_clase,AL

	MOV AL, b_global_clase
	ADD AL, c_1_us
	MOV @aux4,AL
	JC _SumOverflowError_

	MOV AL, @aux4
	MOV b_global_clase,AL

	label2:

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
END start
