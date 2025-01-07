@echo off
setlocal enabledelayedexpansion

:: 输入端口号
echo 输入要查询的端口号:
set /p port=

:: 查找端口对应的PID
for /f "tokens=5" %%i in ('netstat -ano ^| findstr :%port%') do (
    set pid=%%i
    goto :FoundPID
)

echo 没有找到与端口 %port% 对应的进程!
pause
exit /b

:FoundPID
echo 端口 %port% 对应的进程PID: %pid%

:: 查找PID对应的进程名称
for /f "tokens=1 delims=," %%a in ('tasklist /FI "PID eq %pid%" /FO CSV ^| findstr /V "Image Name"') do (
    set processName=%%~a
)

echo PID: %pid% 对应的进程名称: %processName%


:: 是否终止进程
echo 是否终止该进程 (y/n):
set /p kill=

if /i "%kill%"=="y" (
	taskkill /F /PID %pid%
	echo 已终止进程:%pid%-%processName%
) else (
    echo 未终止进程:%pid%-%processName%
)

pause
exit /b