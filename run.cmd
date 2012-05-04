@echo off
set RUNCLASS=HelloWorldApp
set PROJECT=Paralen
set CLASSPATH=C:\Progra~1\Java\jdk1.7.0\jre\lib\bcel-5.2\bcel-5.2.jar;.

javac -cp %CLASSPATH% %RUNCLASS%.java
java -cp %CLASSPATH% -noverify -javaagent:%PROJECT%.jar %RUNCLASS%
