### DuzySpace World Generator 

**DuzySpace** is a world-generator plugin.
* Space-themed worlds filled with randomly-generated planets and asteroids
* Easiely customizable by editing config files
* Dependent on [Multiverse](https://mvplugins.org/core/) (or a similiar plugin) to actually generate a world with DuzySpace.

To generate a world with Multiverse, run this:
```mv create SPACE_WORLD_NAME normal -g DuzySpace:default```
To teleport to the world, run this:
```mv teleport SPACE_WORLD_NAME```
You can edit world generation for your world by adding its name into worlds.yml. See comments in worlds.yml and defaultplanets.yml for documentation.

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
