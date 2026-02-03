/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.crystalcraftmc.crystalspace.api;

import com.crystalcraftmc.crystalspace.Space;
import com.crystalcraftmc.crystalspace.handlers.ConfigHandler;
import com.crystalcraftmc.crystalspace.wgen.planets.PlanetsChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that handles space worlds.
 * External use only.
 * 
 * @author iffa
 * @author Jack
 */
public class SpaceWorldHandler {
    // Variables
    protected static List<String> spaceWorldNames = new ArrayList<String>();
    private static Space plugin = (Space) Bukkit.getPluginManager().getPlugin("CrystalSpace");
    private static Map<World, Integer> forcenightId = new HashMap<World, Integer>();

    /**
     * Loads the space worlds into <code>spaceWorldNames</code>.
     */
    /*
    public static void loadSpaceWorlds() {
        for (World world : Bukkit.getServer().getWorlds()) {
            if (world.getGenerator() instanceof PlanetsChunkGenerator) {
                spaceWorldNames.add(world.getName());
            }
        }
    }*/

    /**
     * Gives all the space worlds of the server.
     * 
     * @return all space worlds as a List
     */
    public static List<World> getSpaceWorlds() {
        List<World> worlds = new ArrayList<World>();
        for (String world : spaceWorldNames) {
            World bukkitWorld = Bukkit.getServer().getWorld(world);
            if (bukkitWorld == null) continue;
            worlds.add(bukkitWorld);
        }
        return worlds;
    }

    /**
     * Checks if a world is a space world.
     * 
     * @param world World to check
     * 
     * @return true if the world is a space world
     */
    public static boolean isSpaceWorld(World world) {
        if (spaceWorldNames.contains(world.getName())) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a player is in a space world.
     * 
     * @param player Player to check
     * @param world Space world
     * 
     * @return true if the player is in the specified space world
     */
    public static boolean isInSpace(Player player, World world) {
        return (spaceWorldNames.contains(world.getName()) && player.getWorld() == world);
    }

    /**
     * Checks if a player is in any space world.
     * 
     * @param player Player
     * 
     * @return true if the player is in a space world
     */
    public static boolean isInAnySpace(Player player) {
        for (String world : spaceWorldNames) {
            if (player.getWorld().getName().equals(world)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the space world a player is in.
     *  
     * @param player Player
     * 
     * @return Null if not in a space world
     */
    public static World getSpaceWorld(Player player) {
        if (getSpaceWorlds().contains(player.getWorld())) {
            return getSpaceWorlds().get(getSpaceWorlds().indexOf(player.getWorld()));
        }
        return null;
    }

    /**
     * Checks the world to see if it is <code>spaceWorldNames</code>, and adds it if not.
     * 
     * @param worldName World name to check
     */
    public static void checkWorld(String worldName) {
        boolean in = false;
        for (String world : spaceWorldNames) {
            if (world.equals(worldName)) {
                in = true;
            }
        }
        if (!in) {
            addSpaceWorld(worldName);
        }
    }

    public static String getID(World world) {
        if (world != null && world.getGenerator() != null && world.getGenerator() instanceof PlanetsChunkGenerator) {
            return ((PlanetsChunkGenerator) world.getGenerator()).getID();
        }
        return "planets";
    }

    public static void addSpaceWorld(String worldName) {
        if (spaceWorldNames.contains(worldName)) return; // Not adding if it already exists
        spaceWorldNames.add(worldName);
        World world = Bukkit.getWorld(worldName);
        if(world == null) return;
        String id = ConfigHandler.getID(world);
    }

    protected SpaceWorldHandler() {
    }  
}
