.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
include \masm32\include\masm32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
includelib \masm32\lib\user32.lib
.DATA
_y_main DD 0
_x_main DD 0
cadena0 DB 'ENTRO',0
cadena1 DB 'NO ANDA',0
OVERFLOW_MULTI_MESSAGE DB "Fuera de rango en Multiplicación$"
.CODE
OVERFLOW_MULTI:
invoke MessageBox, NULL, addr OVERFLOW_MULTI_MESSAGE, addr OVERFLOW_MULTI_MESSAGE, MB_OK 
invoke ExitProcess,0
SALIR:
invoke ExitProcess,0			; DOS: termina el programa
START:
MOV _y_main, 5
MOV EAX, 10
MUL _y_main
JC OVERFLOW_MULTI
MOV _x_main, EAX
MOV EBX, _x_main
CMP EBX, 0
JNE _etiq7
invoke MessageBox, NULL, addr cadena0, addr cadena0, MB_OK
JMP _etiq9
_etiq7: 
invoke MessageBox, NULL, addr cadena1, addr cadena1, MB_OK
_etiq9: 
JMP SALIR
END START