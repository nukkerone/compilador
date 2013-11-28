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
__RET DD 0
_x_main DD 0
__PARAM DD 0
_param_func1 DD 0
cadena0 DB 'Gabi'
.CODE
SALIR:
invoke ExitProcess,0			; DOS: termina el programa
START:
_etiq0: 
JMP _etiq11
_etiq2: 
PUSH AX
PUSH BX
PUSH CX
PUSH DX
MOV EBX, __PARAM
MOV _param_func1, EBX
MOV EBX, _x_main
CMP EBX, 10
JE _etiq8
invoke MessageBox, NULL, addr cadena0, addr cadena0, MB_OK
_etiq8: 
MOV __RET, 0
POP DX
POP CX
POP BX
POP AX
ret
_etiq11: 
MOV _y_main, 10
MOV EBX, _y_main
MOV __PARAM, EBX
CALL _etiq2
MOV EBX, __RET
MOV _x_main, EBX
JMP SALIR
END START
