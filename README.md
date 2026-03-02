### DuzySpace World Generator 
<p align="center">
<img src="config/multiverse-banner.png" alt="Duzsyspace Logo">
</p>
[![Modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/plugin/multiverse-core)
[![Hangar](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/hangar_vector.svg)](https://hangar.papermc.io/Multiverse/Multiverse-Core)
[![Bukkit](https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/available/bukkit_vector.svg)](https://dev.bukkit.org/projects/multiverse-core)
[![Spigot](https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/available/spigot_vector.svg)](https://www.spigotmc.org/resources/multiverse-core.390/)

**DuzySpace** is a world-generator plugin.
* Space-themed worlds filled with randomly-generated planets and asteroids
* Easiely customizable by editing config files
* Dependent on [Multiverse](https://mvplugins.org/core/) (or a similiar plugin) to actually generate a world with DuzySpace.

To generate a world with Multiverse, run this:
```mv create SPACE_WORLD_NAME normal -g DuzySpace:default```
To teleport to the world, run this:
```mv teleport SPACE_WORLD_NAME```
You can edit world generation for your world by adding its name into worlds.yml. See comments in worlds.yml and defaultplanets.yml for documentation.

<p align="center">
<img src="README-image.jpeg" alt="SPAAAAAAACE!">
</p>

Short history of Plugin:
* 2011: iffa spawns 'styxspace'
* 2011-2013 kitskub forks and rebrands plugin as 'bspace'
* 2013-2016: CrystalCraft crew fork and rebrand plugin as 'CrystalSpace' 
* 2026: atkoliver forks and rebrands as 'DuzySpace'.

## Building

Simply enter the root project folder and run:

`gradlew build`

The plugin jar will then appear in build/libs/DuzySpace-VERSION.jar 

## How to Contribute

Click: [CONTRIBUTING.md](CONTRIBUTING.md).

## Complementary Plugins

The following plugins fit well with the plugin:

* [Movecraft](https://github.com/APDevTeam/Movecraft) - Create spaceships from blocks, and move them!
* [Cannons](https://github.com/Intybyte/Cannons) - Build multi-block cannons for spaceship battles!