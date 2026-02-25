#!/usr/bin/env bash
# arg1 = MC-VERSION (select correct server.jar + select correct DuzySpace test build)
# arg2 = SERVER-VERSION (select correct server .jar)

# TODO. Use Server-Verion Argument: CraftBukkit, Spigot, Sponge, Paper, Folia.
#       Set correct URLs for finding latest build version $ver & downloading server jar $d
#     servertype=$2
#     if servertype == "spigot" then build=spigoturl.. etc..

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