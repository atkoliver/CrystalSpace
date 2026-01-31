@echo off
:: arg1 = MC-VERSION (select correct server.jar + select correct CrystalSpace test build)
:: arg2 = SERVER-VERSION (select correct server .jar)

:: Check if there are 2 arguments
set "error=0"
if "%~1"=="" (
    echo [ERROR] %~nx0: Missing argument MC-VERSION!
    echo [ERROR] %~nx0: Example: %~nx0 1.21.11 paper
    set "error=1"
)
if %error% == 1 ( exit /b 1 )
if "%~2"=="" (
    echo [ERROR] %~nx0: Missing argument SERVER-TYPE!
    echo [ERROR] %~nx0: Example: %~nx0 1.21.11 paper
    set "error=1"
)
if %error% == 1 ( exit /b 1 )

:: Check if folder exists
if not exist "server-%~1\" (
    mkdir "server-%~1"
    echo eula=true >> "server-%~1\eula.txt"
    copy "server.properties.defaults" "server-%~1\server.properties"
)
:: Check if jar exists.
if not exist "server-%~1\server-%~2-%~1.jar" (
    ::If not, download server
    cmd /K get-server.bat %1 %2
)

cd server-%~1\

:: Copy latest plugin build
copy /Y ..\..\build\libs\CrystalSpace-%1.jar plugins\CrystalSpaceTestBuild.jar

:: arg1 = Open up server for remote debugger	= -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
:: arg2 = Optimization for server		= -XX+Always [...] etc  

:: Launch server
:: -agentlib: Opens up port 5005 for debugger attacher to connect
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar server-%~2-%~1.jar nogui

:: These are extra server arguments recommended by papermc.io
:: TODO: Test if they reduce server start-up time. Otherwise, remove this
:: -XX:+AlwaysPreTouch -XX:+DisableExplicitGC -XX:+ParallelRefProcEnabled -XX:+PerfDisableSharedMem -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1HeapRegionSize=8M -XX:G1HeapWastePercent=5 -XX:G1MaxNewSizePercent=40 -XX:G1MixedGCCountTarget=4 -XX:G1MixedGCLiveThresholdPercent=90 -XX:G1NewSizePercent=30 -XX:G1RSetUpdatingPauseTimePercent=5 -XX:G1ReservePercent=20 -XX:InitiatingHeapOccupancyPercent=15 -XX:MaxGCPauseMillis=200 -XX:MaxTenuringThreshold=1 -XX:SurvivorRatio=32