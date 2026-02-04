#!/usr/bin/env bash
# arg1 = MC-VERSION (select correct server.jar + select correct CrystalSpace test build)
# arg2 = SERVER-VERSION (select correct server .jar)

# TODO. Use Server-Verion Argument: CraftBukkit, Spigot, Sponge, Paper, Folia.
#       Set correct URLs for finding latest build version $ver & downloading server jar $d
#     servertype=$2
#     if servertype == "spigot" then build=spigoturl.. etc..

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

# Downloads latest paper build
ver=$1
build=$(wget -O - https://api.papermc.io/v2/projects/paper/versions/$1 | jq ".builds[-1]");
url=https://api.papermc.io/v2/projects/paper/versions/$1/builds/$build/downloads/paper-$1-$build.jar;
wget $url -O ./server-$ver/server-paper-$ver.jar;

if [[ -f ./server-$ver/server-paper-$ver.jar ]]; then
    echo "Downloaded latest $1 paper build version $build";
    exit 0
else
    echo "[ERROR] $0: Couldn't download file.";
    exit 1
fi