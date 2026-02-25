Remote Debugger scripts for Minecraft Server.
This lets you debug the plugin 'live' on the server as it's running! Very powerful.

HOW TO INSTALL FOR VSCODIUM:
    * Copy ".vscode-(your OS)" to root folder (../) as ".vscode" to use the debug scripts in VSCodium's debug tab
    
HOW IT WORKS:
    * Debug Configuration downloads server, updates plugin. jar, runs server AND then attaches VSCodium's/VSCode's Java Debugger to the server


DETAILED INFO ON "start-server":
    * Requires 2 arguments: The MC VERSION and SERVER TYPE (i.e. paper, spigot, sponge)
    * Copies latest plugin build from '..\build\libs\DuzySpace-(MC-VERSION).jar' to 'server\plugins\DuzySpaceTestBuild.jar'
    * Launches server from 'server\server-(SERVER-TYPE)-(MC-VERSION).jar', listening from port 5005 for debugger attacher 
    
TODO: Figure out if any server.properties options or otherwise can reduce server startup time.
	level-type=minecraft\:flat
	sync-chunk-writes=false		
	spawn-protection=0
	level-type=minecraft:flat
TODO: Enable support for downloading spigot, sponge, old bukkit, etc... servers.

