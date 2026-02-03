/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.crystalcraftmc.crystalspace.api;

import com.crystalcraftmc.crystalspace.config.SpaceConfig;
import com.crystalcraftmc.crystalspace.config.SpaceConfig.ConfigFile;
import com.crystalcraftmc.crystalspace.handlers.WorldHandler;
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
            return "planets.yml";
        }
        //TODO: Evaluate removal. What purpose does this serve?
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getString("ids."+id+".generation.planets-file", "planets.yml");
    }

    /**
<<<<<<< HEAD
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
     * Check if invalid block names in planets.yml should be ignored.
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
     * Check if asteroid generation is enabled for this world.
     *
     * @param id
     *
     * @return boolean
     */
    public static boolean getAsteroidsEnabled(String id) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getBoolean("ids." + id + ".generation.generateasteroids", (Boolean) SpaceConfig.Defaults.ASTEROIDS_ENABLED.getDefault());
    }
    /**
     * Gets the glowstone asteroid chance of this world.
     *
     * @param id Id
     *
     * @return int asteroid chance 
     */
    public static int getGlowstoneChance(String id) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getInt("ids." + id + ".generation.glowstonechance", (Integer) SpaceConfig.Defaults.GLOWSTONE_CHANCE.getDefault());
    }

    /**
     * Gets the stone asteroid chance of this world.
     *
     * @param id ID
     *
     * @return int asteroid chance
     */
    public static int getStoneChance(String id) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getInt("ids." + id + ".generation.stonechance", (Integer) SpaceConfig.Defaults.STONE_CHANCE.getDefault());
    }

    /**
     * Check if satellites are enabled in this world.
     *
     * @param id ID
     *
     * @return boolean
     */
    public static boolean getSatellitesEnabled(String id) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getBoolean("ids." + id + ".generation.generatesatellites", (Boolean) SpaceConfig.Defaults.SATELLITES_ENABLED.getDefault());
    }

    /**
     * Gets the satellite spawn chance of this world.
     *
     * @param id ID
     *
     * @return int satellite chance
     */
    public static int getSatelliteChance(String id) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getInt("ids." + id + ".generation.satellitechance", (Integer) SpaceConfig.Defaults.SATELLITE_CHANCE.getDefault());
    }

    /**
     * Check if planets are enabled in this world.
     *
     * @param id ID
     *
     * @return boolean
     */
    public static boolean getGeneratePlanets(String id) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getBoolean("ids." + id + ".generation.generateplanets", (Boolean) SpaceConfig.Defaults.GENERATE_PLANETS.getDefault());
    }

    /**
     * Check if schematics are enabled in this world.
     *
     * @param id ID
     *
     * @return boolean
     */
    public static boolean getGenerateSchematics(String id) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getBoolean("ids." + id + ".generation.generateschematics", (Boolean) SpaceConfig.Defaults.GENERATE_SCHEMATICS.getDefault());
    }

    /**
     * Gets the schematic placement chance of this world.
     *
     * @param id ID
     *
     * @return boolean schematic chance
     */
    public static int getSchematicChance(String id) {
        return SpaceConfig.getConfig(ConfigFile.WORLD_IDS).getInt("ids." + id + ".generation.schematicchance", (Integer) SpaceConfig.Defaults.SCHEMATIC_CHANCE.getDefault());
    }

    /**
     * Constructor of SpaceConfigHandler.
     */
    protected SpaceConfigHandler() {
    }
}
