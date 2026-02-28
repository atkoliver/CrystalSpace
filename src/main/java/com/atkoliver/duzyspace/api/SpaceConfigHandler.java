/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.atkoliver.duzyspace.api;

import com.atkoliver.duzyspace.config.SpaceConfig;
import com.atkoliver.duzyspace.config.SpaceConfig.ConfigFile;
import com.atkoliver.duzyspace.handlers.WorldHandler;
import org.bukkit.World;


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

    /**
     * @see WorldHandler#getID(org.bukkit.World)
     *
     * @param world World
     *
     * @return ID of world
     *
     * @see(WorldHandler.java)
     */
    public static String getID(World world) {
        return WorldHandler.getID(world);
    }

    public static String getPlanetsFile(String id) {
        if (id.equalsIgnoreCase("planets")){
            return "defaultplanets.yml";
        }
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getString("worlds."+id+".generation.planets-file", "defaultplanets.yml");
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
     * Check if invalid block names in defaultplanets.yml should be ignored.
     * If false, the server will crash when it encounters invalid blocks!
     * Used by modded servers to enable custom blocks.
     *
     * @param id Id
     *
     * @return boolean
     */
    public static boolean getIgnoreInvalidBlockIds(){
        return SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getBoolean("ignoreInvalidBlockIds", (Boolean) SpaceConfig.Defaults.IGNORE_INVALID_BLOCK_IDS.getDefault());
    }

    /**
     * Check if world should generate spawn planet
     * 
     * @param id
     *
     * @return boolean
     */
    public static boolean getSpawnPlanetEnabled(String id) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getBoolean("worlds." + id + ".generation.enablespawnplanet", (Boolean) SpaceConfig.Defaults.SPAWN_PLANET_ENABLED.getDefault());
    }
    
    /**
     * Check if asteroid generation is enabled for this world.
     *
     * @param id
     *
     * @return boolean
     */
    public static boolean getAsteroidsEnabled(String id) {
        if (getStoneChance(id) > 0 || getGlowstoneChance(id) > 0) {
            return true;
        } return false;
    }
    /**
     * Gets the glowstone asteroid chance of this world.
     *
     * @param id Id
     *
     * @return int asteroid chance 
     */
    public static int getGlowstoneChance(String id) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getInt("worlds." + id + ".generation.glowstonechance", (Integer) SpaceConfig.Defaults.GLOWSTONE_CHANCE.getDefault());
    }

    /**
     * Gets the stone asteroid chance of this world.
     *
     * @param id ID
     *
     * @return int asteroid chance
     */
    public static int getStoneChance(String id) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getInt("worlds." + id + ".generation.stonechance", (Integer) SpaceConfig.Defaults.STONE_CHANCE.getDefault());
    }

    /**
     * Check if satellites are enabled in this world.
     *
     * @param id ID
     *
     * @return boolean
     */
    public static boolean getSatellitesEnabled(String id) {    
        if (getSatelliteChance(id) > 0) {
            return true;
        } return false;
    }

    /**
     * Gets the satellite spawn chance of this world.
     *
     * @param id ID
     *
     * @return int satellite chance
     */
    public static int getSatelliteChance(String id) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getInt("worlds." + id + ".generation.satellitechance", (Integer) SpaceConfig.Defaults.SATELLITE_CHANCE.getDefault());
    }

    /**
     * Check if schematics are enabled in this world.
     *
     * @param id ID
     *
     * @return boolean
     */
    public static boolean getGenerateSchematics(String id) {
        if (getSchematicChance(id) > 0) {
            return true;
        } return false;
    }

    /**
     * Gets the schematic placement chance of this world.
     *
     * @param id ID
     *
     * @return boolean schematic chance
     */
    public static int getSchematicChance(String id) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getInt("worlds." + id + ".generation.schematicchance", (Integer) SpaceConfig.Defaults.SCHEMATIC_CHANCE.getDefault());
    }

    /**
     * Constructor of SpaceConfigHandler
     */
    protected SpaceConfigHandler() {
    }
}
