ECHO OFF

REM change to directory of batch
cd /d %~dp0
REM run it
play -Dconfig.resource=database.conf h2-browser compile run