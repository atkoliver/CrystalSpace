#!/usr/bin/env bash
# arg1 = MC-VERSION (select correct server.jar + select correct CrystalSpace test build)
# arg2 = SERVER-VERSION (select correct server .jar)

# Check if there are 2 arguments
if [[ $1 == "" ]]; then
    echo "[ERROR] $0: Missing argument MC-VERSION!";
    echo "[ERROR] $0: Example: $0 1.21.11 paper";
    exit 1
fi
if [[ $2 == "" ]]; then
    echo "[ERROR] $0: Missing argument SERVER-TYPE!"
    echo "[ERROR] $0: Example: $0 1.21.11 paper"
    exit 1
fi

#Check if folder  exists
if [[ ! -d "./server-$1" ]]; then
    mkdir "server-$1"
    echo eula=true >> "server-$1/eula.txt"
    cp ./server.properties.defaults ./server-$1/server.properties
fi

#Check if jar exists
if [[ ! -f ./server-$1/server-paper-$1.jar ]]; then
    ./get-server.sh $1 $2
fi

cd ./server-$1

# Copy latest plugin build
cp ../../build/libs/CrystalSpace-$1.jar plugins/CrystalSpaceTestBuild.jar

# arg1 = Open up server for remote debugger	= -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
# arg2 = Optimization for server		= -XX+Always [...] etc  

# Launch server
# -agentlib: Opens up port 5005 for debugger attacher to connect
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar server-$2-$1.jar nogui

# These are extra server arguments recommended by papermc.io
# TODO: Test if they reduce server start-up time. Otherwise, remove this
# -XX:+AlwaysPreTouch -XX:+DisableExplicitGC -XX:+ParallelRefProcEnabled -XX:+PerfDisableSharedMem -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1HeapRegionSize=8M -XX:G1HeapWastePercent=5 -XX:G1MaxNewSizePercent=40 -XX:G1MixedGCCountTarget=4 -XX:G1MixedGCLiveThresholdPercent=90 -XX:G1NewSizePercent=30 -XX:G1RSetUpdatingPauseTimePercent=5 -XX:G1ReservePercent=20 -XX:InitiatingHeapOccupancyPercent=15 -XX:MaxGCPauseMillis=200 -XX:MaxTenuringThreshold=1 -XX:SurvivorRatio=32