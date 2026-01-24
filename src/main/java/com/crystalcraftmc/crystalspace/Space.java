/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.crystalcraftmc.crystalspace;

import com.crystalcraftmc.crystalspace.api.SpaceAddon;
import com.crystalcraftmc.crystalspace.api.schematic.SpaceSchematicHandler;
import com.crystalcraftmc.crystalspace.commands.SpaceCommandHandler;
import com.crystalcraftmc.crystalspace.config.SpaceConfig;
import com.crystalcraftmc.crystalspace.handlers.*;
import com.crystalcraftmc.crystalspace.listeners.SpaceEntityListener;
import com.crystalcraftmc.crystalspace.listeners.SpacePlayerListener;
import com.crystalcraftmc.crystalspace.listeners.SpaceSuffocationListener;
import com.crystalcraftmc.crystalspace.listeners.misc.SpaceWorldListener;
import com.crystalcraftmc.crystalspace.wgen.planets.PlanetsChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Main class of CrystalSpace.
 *
 * @author iffa
 * @author kitskub
 * @author HACKhalo2
 * @author jflory7
 */
public class Space extends JavaPlugin {
    // Variables
    private static String prefix;
    private static String version;
    private static Map<Player, Location> locCache = null;
    private static Map<Player, Boolean> jumpPressed = new HashMap<Player, Boolean>();
    private final SpaceEntityListener entityListener = new SpaceEntityListener();
    private final SpaceWorldListener worldListener = new SpaceWorldListener();
    private final SpacePlayerListener playerListener = new SpacePlayerListener();
    private final SpaceSuffocationListener suffocationListener = new SpaceSuffocationListener(this);
    private PluginManager pm;
    private SpaceCommandHandler sce = null;

    /**
     * Gets the jump pressed value. (ie = wtf is this)
     *
     * @param player Player
     *
     * @return Jump pressed
     */
    public static boolean getJumpPressed(Player player) {
        return jumpPressed.get(player);
    }

    /**
     * Sets the jump pressed value. (ie = wtf is this ??)
     *
     * @param player Player
     * @param newJumpPressed New jump pressed value
     */
    public static void setJumpPressed(Player player, boolean newJumpPressed) {
        jumpPressed.put(player, newJumpPressed);
    }

    /**
     * Gets the location cache.
     *
     * @return Location cache
     */
    public static Map<Player, Location> getLocCache() {
        return locCache;
    }

    /**
     * Gets the plugin's prefix.
     *
     * @return Prefix
     */
    public static String getPrefix() {
        return prefix;
    }

    /**
     * Gets the plugin's version.
     *
     * @return Version
     */
    public static String getVersion() {
        return version;
    }

    /*
     * Some API methods
     */

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        for (SpaceAddon addon : AddonHandler.addons) {
            addon.onSpaceDisable();
        }
        // Finishing up disablation.
        MessageHandler.print(Level.INFO, LangHandler.getDisabledMessage());
    }

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        // Initializing variables.
        initVariables();
        MessageHandler.debugPrint(Level.INFO, "Initialized startup variables.");

        // Loading configuration files.
        SpaceConfig.loadConfigs();
        MessageHandler.debugPrint(Level.INFO, "Loaded configuration files");

        // Registering events.
        registerEvents();

        // Loading schematic files.
        SpaceSchematicHandler.loadSchematics();

        // Loading space worlds (startup).
        //WorldHandler.loadSpaceWorlds();

        // Initializing the CommandExecutor for /space.
        sce = new SpaceCommandHandler(this);
        getCommand("space").setExecutor(sce);
        MessageHandler.debugPrint(Level.INFO, "Initialized CommandExecutors.");

        // Checking if it should always be night in space worlds.
        for (World world : WorldHandler.getSpaceWorlds()) {
            String id = ConfigHandler.getID(world);
            if (ConfigHandler.forceNight(id)) {
                WorldHandler.startForceNightTask(world);
                MessageHandler.debugPrint(Level.INFO, "Started night forcing task for world '" + world.getName() + "'.");
            }
        }

        // Finishing up enablation.
        MessageHandler.print(Level.INFO, LangHandler.getEnabledMessage());
    }

    /**
     * Initializes variables (used on startup).
     */
    private void initVariables() {
        pm = getServer().getPluginManager();
        version = getDescription().getVersion();
        prefix = "[" + getDescription().getName() + "]";
    }

    /**
     * Registers events for CrystalSpace.
     */
    private void registerEvents() {
        // Registering other events.
        pm.registerEvents(worldListener, this);
        MessageHandler.debugPrint(Level.INFO, "Registered events (other).");

        // Registering entity & player events.
        pm.registerEvents(entityListener, this);
        pm.registerEvents(playerListener, this);
        pm.registerEvents(suffocationListener, this);
        MessageHandler.debugPrint(Level.INFO, "Registered events (entity & player).");
    }

    /**
     * Gets the default world generator of the plugin.
     *
     * @param worldName World name
     * @param id ID (cow, fish etc)
     *
     * @return ChunkGenerator to use
     */
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        boolean realID = (id == null || id.isEmpty() || id.length() == 0) ? false : true;
        if (realID) {
            MessageHandler.debugPrint(Level.INFO, "Getting generator for '" + worldName + "' using id: '" + id + "'");
        } else {
            MessageHandler.debugPrint(Level.INFO, "Getting generator for '" + worldName + "' using default id,planets.");
        }
        WorldHandler.checkWorld(worldName);
        if (!realID) {
            return new PlanetsChunkGenerator("planets");
        }
        //TODO check if id is in ids.yml
        return new PlanetsChunkGenerator(id);
    }
}