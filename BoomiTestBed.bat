@echo off

@rem *** SET THIS PATH ***
@rem *** example:
@rem              "%UserProfile%\BoomiTestBed"
@rem           or "C:\Users\myusername\Documents\BoomiTestBed"
set PATH_TO_BOOMITESTBED="%UserProfile%\Documents\scripts_boomi\BoomiTestBed"


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

echo.

pushd %PATH_TO_BOOMITESTBED%\src\
groovy BoomiTestBed.groovy%ARGS%
popd
