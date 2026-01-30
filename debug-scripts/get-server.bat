@echo on
::TODO: Define version and dl urls for spigot, sponge, bukkit and folia

:: Check if there are 2 arguments

set "error=0"
if "%~1"=="" (
    echo [ERROR] %~nx0: Missing argument MC-VERSION!
    echo [ERROR] %~nx0: Example: %~nx0 1.21.11 paper
    set "error=1"
)
if %error% equ 1 exit /b 1
if "%~2"=="" (
    echo [ERROR] %~nx0: Missing argument SERVER-TYPE!
    echo [ERROR] %~nx0: Example: %~nx0 1.21.11 paper
    set "error=1"
)
if %error% equ 1 exit /b 1

:: Create server folder and eula
if not exist "server-%~1\" (
    mkdir "server-%~1"
    echo eula=true >> "server-%~1\eula.txt"
    copy "server.properties.defaults" "server-%~1\server.properties"
)

::Set correct URLs for finding latest build version & downloading server jar

set "error=1"
if "%~2" == "paper" (
    set "vurl=https://api.papermc.io/v2/projects/paper/versions/$v"
    set "dlurl=https://api.papermc.io/v2/projects/paper/versions/$v/builds/$b/downloads/paper-$v-$b.jar"
    set "error=0"
)
if "%~2" == "folia" (
    ::set "vurl="
    ::set "dlurl="
    ::set "error=0"
)
if "%~2" == "spigot" (
    ::set "vurl="
    ::set "dlurl="
    ::set "error=0"
)
if "%~2" == "bukkit" (
    ::set "vurl="
    ::set "dlurl="
    ::set "error=0"
)
if %error% equ 1 echo [ERROR] %~nx0: Missing argument SERVER-TYPE!
if %error% equ 1 exit /b 1

::Script downloads latest build of server
powershell -c "$v='%~1'; $b=(irm %vurl%).builds[-1]; $u=\"%dlurl%\"; iwr $u -OutFile server-$v/server-paper-$v.jar";