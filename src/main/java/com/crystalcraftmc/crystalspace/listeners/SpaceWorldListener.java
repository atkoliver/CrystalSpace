/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.crystalcraftmc.crystalspace.listeners;

import com.crystalcraftmc.crystalspace.handlers.ConfigHandler;
import com.crystalcraftmc.crystalspace.handlers.MessageHandler;
import com.crystalcraftmc.crystalspace.handlers.WorldHandler;
import com.crystalcraftmc.crystalspace.wgen.planets.PlanetsChunkGenerator;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.logging.Level;
/**
 * Listener for world load events etc.
 *
 * @author iffa
 */
public class SpaceWorldListener implements Listener {

    /**
     * Called when a world is loaded.
     *
     * @param event Event data
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        if (!(world.getGenerator() instanceof PlanetsChunkGenerator)) {
            return;
        }
        String id = ConfigHandler.getID(world);
        if (ConfigHandler.forceNight(id)) {
            WorldHandler.startForceNightTask(world);
            MessageHandler.debugPrint(Level.INFO, "Started night forcing task for world '" + world.getName() + "'.");
        }
        
        // Adding to spaceWorlds in SpaceWorldHandler
        WorldHandler.addSpaceWorld(world.getName());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();
        if (!(world.getGenerator() instanceof PlanetsChunkGenerator)) {
            return;
        }
        String id = ConfigHandler.getID(world);
        if (ConfigHandler.forceNight(id)) {
            WorldHandler.stopForceNightTask(world);
            MessageHandler.debugPrint(Level.INFO, "Stopped night forcing task for world '" + world.getName() + "'. Reason: World unload");
        }
        WorldHandler.removeSpaceWorld(world);
    }
}
