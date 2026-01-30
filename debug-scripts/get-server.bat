@echo off

::TODO. Arguments: CraftBukkit, Spigot, Sponge, Paper, Folia
:: Set correct URLs for finding latest build version $v & downloading server jar $d

::Downloads latest paper build
powershell -c "$v='%~1'; $b=(irm https://api.papermc.io/v2/projects/paper/versions/$v).builds[-1]; $d=\"https://api.papermc.io/v2/projects/paper/versions/$v/builds/$b/downloads/paper-$v-$b.jar\"; iwr $d -OutFile server-$v/server-paper-$v.jar"; echo \"Downloaded latest %~1 paper build version $b\""