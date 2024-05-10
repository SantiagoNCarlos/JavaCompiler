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
   c_3_us DB 3
   @aux4 DB ?
   a_global DB ?
   c_1_us DB 1
   c_global DB ?
   @aux3 DB ?
   @aux2 DB ?
   b_global DB ?
   d_global DB ?
   c_6_us DB 6
   @aux1 DB ?
   c_9_us DB 9

.code
start:

    MOV AL, c_3_us
    MOV a_global,AL

    MOV AL, c_6_us
    MOV b_global,AL

    MOV AL, c_9_us
    MOV c_global,AL

    MOV AL, c_1_us
    MOV d_global,AL

    MOV AL, d:global
    ADD AL, c_1_us
    MOV @aux1,AL
    MOV AL, b:global
    ADD AL, c:global
    MOV @aux2,AL
    MOV AL, c:global
    SUB AL, d:global
    MOV @aux3,AL
    MOV AL, a:global
    ADD AL, b:global
    MOV @aux4,AL

end start
