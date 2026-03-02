package com.atkoliver.duzyspace.wgen.populators;

import com.atkoliver.duzyspace.handlers.ConfigHandler;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Populates a world with satellites.
 * 
 * @author iffa
 * @author NeonMaster (thanks for the original satellite design, too bad my mathematics blew it up!)
 * @author atkoliver
 */

//TODO: Evaluate removal (bloat)
//Reason: Can be replaced as a default .schematic that also spotlights .schematic generator, including item chests and "space dungeons" for mob farms

public class SpaceSatellitePopulator extends BlockPopulator {
    /**
     * Populates a world with satellites.
     * 
     * @param world World
     * @param random Random
     * @param source Source chunk
     */
    @Override
    public void populate(World world, Random random, Chunk source) {
        String worldname = world.getName();
        if (random.nextInt() <= ConfigHandler.getSatelliteChance(worldname)) {
            int height = random.nextInt(world.getMaxHeight());
            buildSatellite(world, height, source);
        }
    }

    /**
     * Builds a satellite. However badly! (but it looks cool)
     * 
     * @param world World
     * @param height Height
     * @param source Source chunk
     */
    //TODO: Replace this code with a schematic. That will highlight schematic creation too, and intro users to make their own schematics.
    //TODO: Actually do this. Right now, these satellites spawn inside planets and eat asteroids
    private void buildSatellite(World world, int height, Chunk source) {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 3; y++) {
                source.getBlock(y, height, x).setType(Material.GLASS_PANE);
            }
        }
        for (int x = 6; x < 11; x++) {
            for (int y = 0; y < 3; y++) {
                source.getBlock(y, height, x).setType(Material.GLASS_PANE);
            }
        }
        for (int y = 0; y < 3; y++) {
            source.getBlock(y, height, 5).setType(Material.IRON_BLOCK);
        }
    }
}
