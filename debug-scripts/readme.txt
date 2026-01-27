Remote Debugger scripts for Minecraft Server.
This lets you debug the plugin 'live' on the server as it's running!

servers/:
    * This folder holds the Minecraft server .jars and relevant files (i.e. world + configs)

.vscode/:
    * Debug Configuration that runs start-server.bat and attaches VSCodium's/VSCode's Java Debugger to the server
    * To use, copy to github root folder and select debug config in VSCodium's/VSCode's debug tab

start-server.bat:
    * Requires 2 arguments: The MC VERSION and SERVER TYPE (i.e. paper, spigot, sponge)
    * Copy latest plugin build from '..\build\libs\CrystalSpace-(MC-VERSION).jar' to 'server\plugins\CrystalSpaceTestBuild.jar'
    * Launches server from 'server\server-(SERVER-TYPE)-(MC-VERSION).jar', listening from port 5005 for debugger attacher 

TODO: Figure out if any server.properties options or otherwise can reduce server startup time.
	level-type=minecraft\:flat
	sync-chunk-writes=false		
	spawn-protection=0
	level-type=minecraft:flat
TODO: Create script which auto-download all relevant versions of paper/spigot/sponge servers and also relevant dependency plugins (Worldedit, Multiverse)
TODO: Split up server into multiple server-[version-range] folders according to common minecraft/plugin versions, if needed. Probably will be for pre-1.13 versions, pre-flattened blockIds
TODO: Idea: Create 1 folder per MC version? Can then be used for batch-running ALL versions in parallel and checking assertion-tests or something similiar.