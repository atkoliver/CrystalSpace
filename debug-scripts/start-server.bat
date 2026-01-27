:: arg1 = MC-VERSION (select correct server.jar + select correct CrystalSpace test build)
:: arg2 = SERVER-VERSION (select correct server .jar)

:: Check if there are 2 arguments
@echo off
if "%~2"=="" (
    echo [ERROR] %~nx0: Missing argument! For detailed usage info, open me and read me.
    echo [ERROR] %~nx0: Usage:   %~nx0 MC-VERSION SERVER-TYPE
    echo [ERROR] %~nx0: Example: %~nx0 1.21.11 paper
    pause >nul
    exit /b 1
)

:: Copy latest plugin build

copy /Y ..\build\libs\CrystalSpace-%1.jar server\plugins\CrystalSpaceTestBuild.jar

:: arg1 = Open up server for remote debugger	= -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
:: arg2 = Optimization for server		= -XX+Always [...] etc  

cd server\

java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -XX:+AlwaysPreTouch -XX:+DisableExplicitGC -XX:+ParallelRefProcEnabled -XX:+PerfDisableSharedMem -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1HeapRegionSize=8M -XX:G1HeapWastePercent=5 -XX:G1MaxNewSizePercent=40 -XX:G1MixedGCCountTarget=4 -XX:G1MixedGCLiveThresholdPercent=90 -XX:G1NewSizePercent=30 -XX:G1RSetUpdatingPauseTimePercent=5 -XX:G1ReservePercent=20 -XX:InitiatingHeapOccupancyPercent=15 -XX:MaxGCPauseMillis=200 -XX:MaxTenuringThreshold=1 -XX:SurvivorRatio=32 -jar server-%2-%1.jar nogui