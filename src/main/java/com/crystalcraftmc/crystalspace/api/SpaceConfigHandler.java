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
import com.crystalcraftmc.crystalspace.handlers.MessageHandler;
import com.crystalcraftmc.crystalspace.handlers.WorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
    //Global

    /**
     * Checks if debugging mode is enabled.
     *
     * @return true if debugging mode is enabled
     */
    public static boolean getDebugging() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("debug", (Boolean) SpaceConfig.Defaults.DEBUGGING.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("debug", (Boolean) DEBUGGING.getDefault());
    }

    /**
     * Gets the helmet given-state of a world.
     *
     * @return true if a helmet is given when teleporting to this world
     */
    public static boolean isHelmetGiven() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("global.givehelmet", (Boolean) SpaceConfig.Defaults.HELMET_GIVEN.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("global.givehelmet", (Boolean) HELMET_GIVEN.getDefault());
    }

    /**
     * Gets the suit given-state of a world.
     *
     * @return true if a suit is given when teleporting to this world
     */
    public static boolean isSuitGiven() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("global.givesuit", (Boolean) SpaceConfig.Defaults.SUIT_GIVEN.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("global.givesuit", (Boolean) SUIT_GIVEN.getDefault());
    }

    /**
     * Gets the helmet
     *
     * @return string of id or name
     */
    public static String getHelmet() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.helmet", (String) SpaceConfig.Defaults.HELMET.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.helmet", (String) HELMET.getDefault());
    }

    /**
     * Gets the chestplate
     *
     * @return string of id or name
     */
    public static String getChestPlate() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.chestplate", (String) SpaceConfig.Defaults.CHESTPLATE.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.chestplate", (String) CHESTPLATE.getDefault());
    }

    /**
     * Gets the leggings
     *
     * @return string of id or name
     */
    public static String getLeggings() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.leggings", (String) SpaceConfig.Defaults.LEGGINGS.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.leggings", (String) LEGGINGS.getDefault());
    }

    /**
     * Gets the boots
     *
     * @return string of id or name
     */
    public static String getBoots() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.boots", (String) SpaceConfig.Defaults.BOOTS.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.boots", (String) BOOTS.getDefault());
    }

    /**
     * Gets the suit armortype of a world.
     *
     * @return armortype string
     */
    public static String getArmorType() {
        String armorType = SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.armortype", (String) SpaceConfig.Defaults.ARMOR_TYPE.getDefault());
        //String armorType = SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.armortype", (String) ARMOR_TYPE.getDefault());
        if (!"id".equals(armorType) && Material.matchMaterial(armorType + "_HELMET") == null) {
            MessageHandler.print(Level.SEVERE, "Invalid armortype '" + SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.armortype") + "' in config!");
            return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.armortype");
            //return (String) ARMOR_TYPE.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.armortype", (String) SpaceConfig.Defaults.ARMOR_TYPE.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getString("global.armortype", (String) ARMOR_TYPE.getDefault());
    }

    /**
     * Gets the gravity value.
     *
     * @return True if gravity enabled
     */
    public static boolean getStopDrowning() {
        return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("global.drowning.stopdrowning", (Boolean) SpaceConfig.Defaults.STOPDROWNING.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.CONFIG).getBoolean("global.drowning.stopdrowning", (Boolean) STOPDROWNING.getDefault());
    }

    /**
     * Gets the gravity value.
     *
     * @return True if gravity enabled
     */
    public static List<World> getStopDrowningWorlds() {
        @SuppressWarnings("unchecked")
        List<String> strings = SpaceConfig.getConfig(ConfigFile.CONFIG).getStringList("global.drowning.worlds");
        List<World> worlds = new ArrayList<World>();
        for (String string : strings) {
            worlds.add(Bukkit.getWorld(string));
        }
        return worlds;
    }

    //ID-specific
    /**
     * Gets the name of the planets file. Not checked if it exists.
     *
     * @param id Id
     *
     * @return name of planets file
     */
    public static String getPlanetsFile(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return "planets.yml";//Never going to change; unless of course someone demands it
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getString("ids." + id + ".generation.planets-file", "planets.yml");
    }

    /**
     * Gets the required helmet-state of a world.
     *
     * @param id Id
     *
     * @return true if a helmet is required
     */
    public static boolean getRequireHelmet(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".helmet.required", (Boolean) SpaceConfig.Defaults.REQUIRE_HELMET.getDefault());
            //return (Boolean) REQUIRE_HELMET.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".helmet.required", (Boolean) SpaceConfig.Defaults.REQUIRE_HELMET.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".helmet.required", (Boolean) REQUIRE_HELMET.getDefault());
    }

    /**
     * Gets the required suit-state of a world.
     *
     * @param id ID
     *
     * @return true if a suit is required
     */
    public static boolean getRequireSuit(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".suit.required", (Boolean) SpaceConfig.Defaults.REQUIRE_SUIT.getDefault());
            //return (Boolean) REQUIRE_SUIT.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".suit.required", (Boolean) SpaceConfig.Defaults.REQUIRE_SUIT.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".suit.required", (Boolean) REQUIRE_SUIT.getDefault());
    }

    /**
     * Gets the force night-state of a world.
     *
     * @param id ID
     *
     * @return true if night is forced
     */
    public static boolean forceNight(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".alwaysnight", (Boolean) SpaceConfig.Defaults.FORCE_NIGHT.getDefault());
            //return (Boolean) FORCE_NIGHT.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".alwaysnight", (Boolean) SpaceConfig.Defaults.FORCE_NIGHT.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.IDS).getBoolean("ids." + id + ".alwaysnight", (Boolean) FORCE_NIGHT.getDefault());
    }

    /**
     * Gets the maximum room height of a world.
     *
     * @param id Id
     *
     * @return room height int
     */
    public static int getRoomHeight(String id) {
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".maxroomheight", (Integer) SpaceConfig.Defaults.ROOM_HEIGHT.getDefault());
            //return (Integer) ROOM_HEIGHT.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".maxroomheight", (Integer) SpaceConfig.Defaults.ROOM_HEIGHT.getDefault());
        //return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".maxroomheight", (Integer) ROOM_HEIGHT.getDefault());
    }

    public static getIgnoreInvalidBlockIds(String id){
        if (id.equalsIgnoreCase("planets")) {
            return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".ignoreinvalidblockids", (Integer) SpaceConfig.Defaults.IGNORE_INVALID_BLOCK_IDS.getDefault());
            //return (Integer) ROOM_HEIGHT.getDefault();
        }
        return SpaceConfig.getConfig(ConfigFile.IDS).getInt("ids." + id + ".ignoreinvalidblockids", (Integer) SpaceConfig.Defaults.IGNORE_INVALID_BLOCK_IDS.getDefault());
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
