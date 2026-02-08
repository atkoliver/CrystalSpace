@echo off
:: arg1 = MC-VERSION (select correct server.jar + select correct CrystalSpace test build)
:: arg2 = SERVER-VERSION (select correct server .jar)

::TODO. Use Server-Verion Argument: CraftBukkit, Spigot, Sponge, Paper, Folia.
::      Set correct URLs for finding latest build version $ver & downloading server jar $d

::Downloads latest paper build
set commands="$ver=\"%~1\"; $build=(irm https://api.papermc.io/v2/projects/paper/versions/$ver).builds[-1]; $url=\"https://api.papermc.io/v2/projects/paper/versions/$ver/builds/$build/downloads/paper-$ver-$build.jar\"; iwr $url -OutFile \"./server-$ver/server-paper-$ver.jar\";"

powershell -c %commands%


if exist "./server-$ver/server-paper-$ver.jar" (
  echo Downloaded latest %~1 paper build version $build
)
if not exist "./server-$ver/server-paper-$ver.jar" (
  echo [ERROR] %~nx0: Couldn't download file.
)