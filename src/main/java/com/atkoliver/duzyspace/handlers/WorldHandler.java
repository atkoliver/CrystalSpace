/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.atkoliver.duzyspace.handlers;

import com.atkoliver.duzyspace.api.SpaceWorldHandler;
import org.bukkit.World;

/**
 * Class that handles space worlds.
 * Internal use only
 *
 * @author Jack
 */
public class WorldHandler extends SpaceWorldHandler {
    /**
     * Removes a space world from the list of space worlds.
     * 
     * @param world World to remove
     */
    public static void removeSpaceWorld(World world) {
        if (spaceWorldNames.contains(world.getName())) {
            spaceWorldNames.remove(world.getName());
        }
    }
    private WorldHandler() {
    }
}
