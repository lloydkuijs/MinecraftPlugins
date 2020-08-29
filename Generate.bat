@echo OFF

for /d %%d in (*) do if not %%d==build CALL :JarFolder %%d

:: Jar plugin function
: JarFolder
echo %~1
EXIT /B 0