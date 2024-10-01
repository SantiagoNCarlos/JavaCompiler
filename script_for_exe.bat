@echo off

REM IMPORTANTE: Asegurarse que la carpeta bin de java (para ejecutar Java -jar) y la carpeta bin de MASM (ml.exe y link.exe) se encuentran en el PATH del sistema. 
REM IMPORTANTE: Se utilizo el SDK openjdk-22.0.2 de Java.

REM Verifica si se pasaron los argumentos necesarios
if "%1"=="" (
    echo Por favor, proporciona el archivo .jar.
    exit /b 1
)

if "%2"=="" (
    echo Por favor, proporciona el archivo .txt.
    exit /b 1
)

set jarFile=%1
set txtFile=%2

REM Obtenemos la carpeta donde está el archivo .txt con el codigo
set txtDir=%~dp2

REM Navegamos a la carpeta donde está el archivo .txt
cd /d %txtDir%

REM IMPORTANTE: 
REM Ejecuta el archivo .jar con el archivo .txt como argumento
java -jar %jarFile% %txtFile%

REM Define el nombre del archivo .asm basado en el archivo .txt
set asmFile=%~n2-assembler.asm
set objFile=%~n2-assembler.obj

REM IMPORTANTE: Asegurarse que ml.exe y link.exe se encuentran en el PATH del sistema. 
REM Ejecuta ml.exe y link.exe. 
ml.exe /c /Zd /coff %asmFile%
link.exe /SUBSYSTEM:CONSOLE %objFile%

echo Proceso completado.
