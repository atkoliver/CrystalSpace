/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.crystalcraftmc.crystalspace.wgen.populators;

import org.bukkit.Chunk;
import org.bukkit.generator.WorldInfo;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author kitskub
 */
public class SpaceDataPopulator extends BlockPopulator {
    public static Map<WorldInfo, Map<WrappedCoords, Material>> coords = new HashMap<WorldInfo, Map<WrappedCoords, Material>>();
    
    public static void addCoords(WorldInfo worldInfo, int chunkX, int chunkZ, int x, int y, int z, Material mat) {
        if (coords.get(worldInfo) == null) {
            coords.put(worldInfo, new HashMap<WrappedCoords, Material>());
        }
        WrappedCoords key = new WrappedCoords();
        key.chunkX = chunkX;
        key.chunkZ = chunkZ;
        key.x = x;
        key.y = y;
        key.z = z;
        coords.get(worldInfo).put(key, mat);
    }

    @Override
    public void populate(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion limitedRegion) {
        if (coords.get(worldInfo) == null) return;
        for (WrappedCoords c : coords.get(worldInfo).keySet()) { //TODO: Replace WrappedCoords with Location. Same purpose, but already exists in bukkit.
            if (c.chunkX == chunkX && c.chunkZ == chunkZ) { //TODO: Find way to optimize this
                limitedRegion.setType(c.x, c.y, c.z, coords.get(worldInfo).get(c));
            }
        }
    }
    
    public static class WrappedCoords {
        public int chunkX;
        public int chunkZ;
        public int x;
        public int y;
        public int z;

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final WrappedCoords other = (WrappedCoords) obj;
            if (this.chunkX != other.chunkX) {
                return false;
            }
            if (this.chunkZ != other.chunkZ) {
                return false;
            }
            if (this.x != other.x) {
                return false;
            }
            if (this.y != other.y) {
                return false;
            }
            if (this.z != other.z) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 59 * hash + this.chunkX;
            hash = 59 * hash + this.chunkZ;
            hash = 59 * hash + this.x;
            hash = 59 * hash + this.y;
            hash = 59 * hash + this.z;
            return hash;
        }
        
    }
}
