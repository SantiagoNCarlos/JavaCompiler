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
   cc_global DD ?
   pp_global_clase2 DD ?
   c_5_9 DD 5.9
   k_global DD ?
   c_9_8 DD 9.8
   prueba_global DD ?
   z_global_clase2_foo2 DD ?
   c_global_testclase_foo DD ?
   s_#_AAA123_142_ DB "# AAA123 142 ", 10, 0
   l2_global_testclase DD ?
   c_1_0 DD 1.0
   c_1_2 DD 1.2
   @aux2 DD ?
   @aux1 DD ?
   c_2_l DD 2
   uu_global DD ?
   l1_global_testclase DD ?

.code
start:

    invoke printf, addr # AAA123 142 

Acceso
    FLD cc:global
    FLD c_1_0
    FADD
    FSTP @aux1

    FLD cc:global
    FLD c_9_8
    FMUL 
    FSTP @aux2

LlamadoMetodoClase
    FLD c_5_9
    FSTP uu_global

    FLD uu:global
    FLD cc:global
    FSTSW aux_mem_2bytes
    MOV AX, aux_mem_2bytes
    SAHF
    JE label1
    MOV EAX, c_2_l
    MOV z_global_clase2_foo2,EAX

    FLD c_1_2
    FSTP c_global_testclase_foo


end start
