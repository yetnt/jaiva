@echo off
REM %~dp0 returns the drive letter and path of this script.
java --sun-misc-unsafe-memory-access=allow -jar "%~dp0jaiva.jar" %*