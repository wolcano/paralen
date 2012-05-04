@echo off
set CLASSPATH=C:\Progra~1\Java\jdk1.7.0\jre\lib\bcel-5.2\bcel-5.2.jar;
set Path=C:\Program Files\Java\jdk1.7.0\jre\bin;C:\Program Files\NVIDIA Corporation\PhysX\Common;C:\Program Files\PC Connectivity Solution\;c:\unix;c:\unix\bin;C:\WINDOWS\system32\kktools;C:\WINDOWS\system32;C:\WINDOWS;C:\WINDOWS\system32\wbem;C:\Program Files\Windows Resource Kits\Tools;C:\Program Files\Common Files\GTK\2.0\bin;c:\program files\vim\vim70;C:\Program Files\Debugging Tools for Windows;C:\Program Files\Nmap;C:\Program Files\OpenVPN\bin;C:\Program Files\hdparm\bin;C:\Program Files\Altiris\Software Virtualization Agent;C:\Program Files\PC Connectivity Solution;C:\Program Files\QuickTime\QTSystem;C:\Program Files\Smart Projects\IsoBuster;C:\Program Files\QuickTime\QTSystem\;C:\Program Files\Java\jdk1.7.0\bin

javac -cp "%CLASSPATH%" paralen.java
del Paralen.jar >NUL 2>NUL
jar -cvfm0 Paralen.jar Paralen.MF Paralen.class
