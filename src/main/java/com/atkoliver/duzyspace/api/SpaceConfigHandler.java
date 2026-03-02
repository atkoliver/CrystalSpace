package com.atkoliver.duzyspace.api;

import com.atkoliver.duzyspace.config.SpaceConfig;
import com.atkoliver.duzyspace.config.SpaceConfig.ConfigFile;
import com.atkoliver.duzyspace.handlers.WorldHandler;
import org.bukkit.World;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Static methods handle configuration.
 * External use only
 *
 * @author iffa
 * @author Jack
 * @author jflory7
 * @author atkoliver
 */
public class SpaceConfigHandler {

    public static boolean worldHasCustomGenerator(String worldName){
        Boolean worldConfigExists = SpaceConfig.getConfig(ConfigFile.WORLD_IDS).contains("worlds."+worldName);
        if (worldConfigExists) {
            return true;
        } else return false;
    }

    public static String getPlanetsFile(String worldName) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getString("worlds." + worldName + ".generation.planetsfile", "defaultplanets.yml");
    }

    /**
     * Check if debugging mode is enabled.
     * Used by server owners to quickly troubleshoot the plugin.
     * As a developer, use the good remote debugger instead!
     *
     * @return boolean
     */
    public static boolean getDebugging() {
        return SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getBoolean("debugging", (Boolean) SpaceConfig.Defaults.DEBUGGING.getDefault());
    }

    /**
     * Check if invalworldName block names in defaultplanets.yml should be ignored.
     * If false, the server will crash when it encounters invalworldName blocks!
     * Used by modded servers to enable custom blocks.
     *
     * @param worldName Id
     *
     * @return boolean
     */
    public static boolean getIgnoreInvalworldNameBlockIds(){
        return SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getBoolean("ignoreInvalworldNameBlockIds", (Boolean) SpaceConfig.Defaults.IGNORE_INVALID_BLOCK_IDS.getDefault());
    }

    /**
     * Check if world should generate spawn planet
     * 
     * @param worldName
     *
     * @return boolean
     */
    public static boolean getSpawnPlanetEnabled(String worldName) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getBoolean("worlds." + worldName + ".generation.enablespawnplanet", (Boolean) SpaceConfig.Defaults.SPAWN_PLANET_ENABLED.getDefault());
    }
    
    /**
     * Check if asteroworldName generation is enabled for this world.
     *
     * @param worldName
     *
     * @return boolean
     */
    public static boolean getAsteroidsEnabled(String worldName) {
        if (getStoneChance(worldName) > 0 || getGlowstoneChance(worldName) > 0) {
            return true;
        } return false;
    }
    /**
     * Gets the glowstone asteroworldName chance of this world.
     *
     * @param worldName Id
     *
     * @return int asteroworldName chance 
     */
    public static int getGlowstoneChance(String worldName) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getInt("worlds." + worldName + ".generation.glowstonechance", (Integer) SpaceConfig.Defaults.GLOWSTONE_CHANCE.getDefault());
    }

    /**
     * Gets the stone asteroworldName chance of this world.
     *
     * @param worldName ID
     *
     * @return int asteroworldName chance
     */
    public static int getStoneChance(String worldName) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getInt("worlds." + worldName + ".generation.stonechance", (Integer) SpaceConfig.Defaults.STONE_CHANCE.getDefault());
    }

    /**
     * Check if satellites are enabled in this world.
     *
     * @param worldName ID
     *
     * @return boolean
     */
    public static boolean getSatellitesEnabled(String worldName) {    
        if (getSatelliteChance(worldName) > 0) {
            return true;
        } return false;
    }

    /**
     * Gets the satellite spawn chance of this world.
     *
     * @param worldName ID
     *
     * @return int satellite chance
     */
    public static int getSatelliteChance(String worldName) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getInt("worlds." + worldName + ".generation.satellitechance", (Integer) SpaceConfig.Defaults.SATELLITE_CHANCE.getDefault());
    }

    /**
     * Check if schematics are enabled in this world.
     *
     * @param worldName ID
     *
     * @return boolean
     */
    public static boolean getGenerateSchematics(String worldName) {
        if (getSchematicChance(worldName) > 0) {
            return true;
        } return false;
    }

    /**
     * Gets the schematic placement chance of this world.
     *
     * @param worldName ID
     *
     * @return boolean schematic chance
     */
    public static int getSchematicChance(String worldName) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getInt("worlds." + worldName + ".generation.schematicchance", (Integer) SpaceConfig.Defaults.SCHEMATIC_CHANCE.getDefault());
    }

    /**
     * Constructor of SpaceConfigHandler
     */
    protected SpaceConfigHandler() {
    }
}
