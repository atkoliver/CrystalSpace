/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.atkoliver.duzyspace;

import com.atkoliver.duzyspace.api.SpaceAddon;
import com.atkoliver.duzyspace.api.schematic.SpaceSchematicHandler;
import com.atkoliver.duzyspace.config.SpaceConfig;
import com.atkoliver.duzyspace.handlers.*;
import com.atkoliver.duzyspace.wgen.planets.PlanetsChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * Main class of DuzySpace.
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
    private PluginManager pm;
    private SpaceCommandHandler sce = null;

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
     * Registers events for DuzySpace.
     */
    private void registerEvents() { //TODO: Evaluate deletion
        // Registering other events.
        //pm.registerEvents(worldListener, this);
        //MessageHandler.debugPrint(Level.INFO, "Registered events (other).");
    }

    /**
     * Gets the default world generator of the plugin.
     *
     * @param worldName World name
     * @param id ID World id inside multiverse ("spaceworld", "test2", "Sirius", etc...)
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

        //TODO Check if id is in worlds.yml
        //if (!realID || !idHasCustomGenerator(id)) {
        if (!realID) {
            return new PlanetsChunkGenerator("planets");
        }
        return new PlanetsChunkGenerator(id);
    }
}