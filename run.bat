@echo off
:: Changes directory to current directory
cd /D %~dp0
:: Executes app using bash.exe
bash.exe -c "./weather alerts"