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
 * TODO: Put defaults back to use! (currently commented out... needs some refactoring for it to work)
 *
 * @author iffa
 * @author Jack
 * @author jflory7
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
        return SpaceConfig.getConfig(ConfigFile.IDS).getString("ids."+id+".generation.planets-file", "planets.yml");
    }

    /**
     * Check if invalid block names in config should be ignored.
     * If false, the server will crash instead of ignoring them!
     * Used for enabling modded blocks, as they aren't included in the default materials list.
     *
     * @param id Id
     *
     * @return ignoreinvalidblock boolean int
     */
    public static boolean getIgnoreInvalidBlockIds(String id){
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".ignoreinvalidblockids", (Boolean) SpaceConfig.Defaults.IGNORE_INVALID_BLOCK_IDS.getDefault());
            //return (Integer) ROOM_HEIGHT.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".ignoreinvalidblockids", (Boolean) SpaceConfig.Defaults.IGNORE_INVALID_BLOCK_IDS.getDefault());
    }
    /**
     * Gets the glowstone chance of a world.
     *
     * @param id Id
     *
     * @return glowstone chance int
     */
    public static int getGlowstoneChance(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.glowstonechance", (Integer) SpaceConfig.Defaults.GLOWSTONE_CHANCE.getDefault());
            //return (Integer) GLOWSTONE_CHANCE.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.glowstonechance", (Integer) SpaceConfig.Defaults.GLOWSTONE_CHANCE.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.glowstonechance", (Integer) GLOWSTONE_CHANCE.getDefault());
    }

    /**
     * Gets the stone chance of a world.
     *
     * @param id ID
     *
     * @return asteroid chance int
     */
    public static int getStoneChance(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.stonechance", (Integer) SpaceConfig.Defaults.STONE_CHANCE.getDefault());
            //return (Integer) STONE_CHANCE.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.stonechance", (Integer) SpaceConfig.Defaults.STONE_CHANCE.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.stonechance", (Integer) STONE_CHANCE.getDefault());
    }

    /**
     * Checks if asteroid generation is enabled for a world.
     *
     * @param id
     *
     * @return true if asteroid generation is enabled
     */
    public static boolean getAsteroidsEnabled(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateasteroids", (Boolean) SpaceConfig.Defaults.ASTEROIDS_ENABLED.getDefault());
            //return (Boolean) ASTEROIDS_ENABLED.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateasteroids", (Boolean) SpaceConfig.Defaults.ASTEROIDS_ENABLED.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateasteroids", (Boolean) ASTEROIDS_ENABLED.getDefault());
    }

    /**
     * Checks if satellites are enabled.
     *
     * @param id ID
     *
     * @return True if satellites are enabled
     */
    public static boolean getSatellitesEnabled(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generatesatellites", (Boolean) SpaceConfig.Defaults.SATELLITES_ENABLED.getDefault());
            //return (Boolean) SATELLITES_ENABLED.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generatesatellites", (Boolean) SpaceConfig.Defaults.SATELLITES_ENABLED.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generatesatellites", (Boolean) SATELLITES_ENABLED.getDefault());
    }

    /**
     * Gets the satellite spawn chance.
     *
     * @param id ID
     *
     * @return Spawn chance
     */
    public static int getSatelliteChance(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.satellitechance", (Integer) SpaceConfig.Defaults.SATELLITE_CHANCE.getDefault());
            //return (Integer) SATELLITE_CHANCE.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.satellitechance", (Integer) SpaceConfig.Defaults.SATELLITE_CHANCE.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.satellitechance", (Integer) SATELLITE_CHANCE.getDefault());
    }

    /**
     * Gets generate planets value.
     *
     * @param id ID
     *
     * @return True if generateplantes=true
     */
    public static boolean getGeneratePlanets(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateplanets", (Boolean) SpaceConfig.Defaults.GENERATE_PLANETS.getDefault());
            //return (Boolean) GENERATE_PLANETS.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateplanets", (Boolean) SpaceConfig.Defaults.GENERATE_PLANETS.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateplanets", (Boolean) GENERATE_PLANETS.getDefault());
    }

    /**
     * Gets generate schematics value.
     *
     * @param id ID
     *
     * @return True if generateschematics=true
     */
    public static boolean getGenerateSchematics(String id) {
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateschematics", (Boolean) SpaceConfig.Defaults.GENERATE_SCHEMATICS.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".generation.generateschematics", (Boolean) GENERATE_SCHEMATICS.getDefault());
    }

    /**
     * Gets the schematic-chance.
     *
     * @param id ID
     *
     * @return Schematic chance
     */
    public static int getSchematicChance(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.schematicchance", (Integer) SpaceConfig.Defaults.SCHEMATIC_CHANCE.getDefault());
            //return (Integer) SCHEMATIC_CHANCE.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.schematicchance", (Integer) SpaceConfig.Defaults.SCHEMATIC_CHANCE.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".generation.schematicchance", (Integer) SCHEMATIC_CHANCE.getDefault());
    }

    /**
     * Constructor of SpaceConfigHandler.
     */
    protected SpaceConfigHandler() {
    }
}
