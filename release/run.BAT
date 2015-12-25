@echo off

SET JAVA_HOME=D:\jdk1.8.0_51
SET programPath=G:\numerical\common\release
SET startParam=

SET runjar=compareExcel.jar
SET maiClass=org.solq.excel.Main
SET PATH=.;%JAVA_HOME%\bin;C:\Windows;C:\Windows\System32;
SET baseDir=%programPath%\lib
SET runjar=%programPath%\%runjar%



SET jar_Lib=
setlocal EnableDelayedExpansion 
for /R %baseDir%\ %%i in (*.jar) do (  
    SET jar_Lib=%%i;!jar_Lib!
)  
SET jar=%jar_Lib%;!runjar!

java %startParam% -cp %jar% %maiClass% %1 %2

pause