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

.data

   formatStringLong db "%d", 0
   formatStringUShort db "%hu", 0
   formatStringFloat db "%f", 0
   c_3_l DD 3
   k_global DD ?
   c_8_l DD 8
   c_1_l DD 1
   @aux1 DD ?

.code
start:

    MOV EAX, c_3_l
    MOV k:global,EAX

    invoke printf, offset formatStringLong, k_global

    MOV EAX, c_1_l
    ADD EAX, c_8_l
    MOV @aux1,EAX
    invoke printf, offset formatStringLong, k_global


end start
