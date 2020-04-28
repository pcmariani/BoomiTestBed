@echo off

set DIR_OF_THIS_BATCH_FILE="%~dp0"
set ARGS=

:LOOP
if "%1"=="" goto ENDLOOP
    set ARG=%1

    if "%ARG%"=="cls" (
        cls 
      @rem if arg starts with -
    ) else if "%ARG:~0,1%"=="-" (
        set ARGS=%ARGS% %ARG%
    ) else (
        @rem add the resolved full path based on relative path
        set ARGS=%ARGS% "%~f1"
    )

    shift
    goto LOOP
:ENDLOOP

echo Script running...
echo.

pushd %DIR_OF_THIS_BATCH_FILE%
groovy BoomiTestBed.groovy%ARGS%
popd
