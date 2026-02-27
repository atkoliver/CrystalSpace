/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.atkoliver.duzyspace.wgen.planets;

import com.atkoliver.duzyspace.config.SpaceConfig;
import com.atkoliver.duzyspace.handlers.ConfigHandler;
import com.atkoliver.duzyspace.handlers.MessageHandler;
import com.atkoliver.duzyspace.wgen.populators.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import org.bukkit.configuration.InvalidConfigurationException;

/**
 * Generates a space world with planets.
 * 
 * @author Jack
 * @author Canis85
 * @author iffa
 * @author atkoliver
 */
public class PlanetsChunkGenerator extends ChunkGenerator {
    // Variables
    //TODO: Evaluate deletion. Find out why settings are defined twice (once here, once at the bottom of this file)
    private Map<ArrayList<BlockData>, Float> shellBlocklists;
    private Map<ArrayList<BlockData>, Float> coreBlocklists;
    private int density = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("density", (Integer) SpaceConfig.Defaults.DENSITY.getDefault()); // Number of planetoids it will try to create per
    private int minDistance = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("minDistance", (Integer) SpaceConfig.Defaults.MIN_DISTANCE.getDefault()); // Minimum distance between planets, in blocks
    private int minSize = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("minSize", (Integer) SpaceConfig.Defaults.MIN_SIZE.getDefault()); // Minimum radius
    private int maxSize = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("maxSize", (Integer) SpaceConfig.Defaults.MAX_SIZE.getDefault()); // Maximum radius
    private int maxShellSize = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("maxShellSize", (Integer) SpaceConfig.Defaults.MAX_SHELL_SIZE.getDefault()); // Maximum shell thickness
    private int minShellSize = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("minShellSize", (Integer) SpaceConfig.Defaults.MIN_SHELL_SIZE.getDefault()); // Minimum shell thickness, should be at least 3
    private int floorHeight = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("floorHeight", (Integer) SpaceConfig.Defaults.FLOOR_HEIGHT.getDefault()); // Floor height
    private Material floorBlock = Material.matchMaterial(SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getString("floorBlock", (String) SpaceConfig.Defaults.FLOOR_BLOCK.getDefault()));// BlockID for the floor
    private boolean bedrockEnabled = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getBoolean("bedrockEnabled", (Boolean) SpaceConfig.Defaults.BEDROCK_ENABLED.getDefault()); // Bedrock layer at y=0
    private boolean ignoreInvalidBlockIds = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getBoolean("ignoreInvalidBlockIds", (Boolean) SpaceConfig.Defaults.IGNORE_INVALID_BLOCK_IDS.getDefault()); // Ignore invalid block ids, i.e. typos and modded block ids
    private static HashMap<WorldInfo, List<Planetoid>> planets = new HashMap<WorldInfo, List<Planetoid>>();
    private final String ID;
    private final boolean GENERATE;

    /**
     * Constructor of PlanetsChunkGenerator.
     * 
     * @param id ID
     */
    public PlanetsChunkGenerator(String id) {
        this(id, ConfigHandler.getGeneratePlanets(id));
    }

    /**
     * Constructor of PlanetsChunkGenerator 2.
     * 
     * @param id ID
     * @param generate ?
     */
    public PlanetsChunkGenerator(String id, boolean generate) {
        this.ID = id.toLowerCase();
        this.GENERATE = generate;
        loadPossibleBlocks();
        loadPlanetSettings();
    }

    /**
     * Get ID of PlanetsChunkGenerator
     * 
     * @return id ID
     */
    public String getID(){
        return this.ID;
    }

    /**
     * Override non-important stuff
     */
    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunkData) { }
    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunkData) { 
        generateChunkData(worldInfo, random, chunkX, chunkZ, chunkData);
    }
    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunkData) { }
    @Override
    public void generateCaves(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunkData) { }
    @Override
    public boolean shouldGenerateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ){ return false; }
    @Override
    public boolean shouldGenerateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ){ return false; }
    @Override
    public boolean shouldGenerateBedrock(){ return false; }
    @Override
    public boolean shouldGenerateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ){ return false; }

    /**
     * Generates chunk data for a chunk.
     * 
     * @param worldInfo
     * @param random
     * @param chunkX
     * @param chunkZ
     * @param biome
     * @return 
     */

    public ChunkData generateChunkData(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunkData) {
        if (!planets.containsKey(worldInfo)) {
            planets.put(worldInfo, new ArrayList<Planetoid>());
        }
        
        if (GENERATE) {
            MessageHandler.debugPrint(Level.INFO, "GENERATE == true, generating planet");
            generatePlanetoids(worldInfo, chunkX, chunkZ);
            BlockData bdata;
            // Go through the current system's planetoids and fill in this chunk as needed.
            for (Planetoid curPl : planets.get(worldInfo)) {
                // Find planet's center point relative to this chunk.
                int relCenterX = curPl.xPos - chunkX * 16;
                int relCenterZ = curPl.zPos - chunkZ * 16;
                for (int curX = -curPl.radius; curX <= curPl.radius; curX++) {//Iterate across every x block
                    boolean xShell = false; //Shell or core block
                    int relativeX = curX + relCenterX;
                    if (relativeX >= 0 && relativeX < 16) {
                        //int worldX = curX + curPl.xPos;//Get the absolute x coordinate
                        // Find radius of this circle
                        int distFromCenter = Math.abs(curX);//Distance from center in the x 
                        if (curPl.radius - distFromCenter < curPl.shellThickness) {//Check if part of xShell
                            xShell = true;
                        }
                        int zHalfLength = (int) Math.ceil(Math.sqrt((curPl.radius * curPl.radius) - (distFromCenter * distFromCenter)));//Half the amount of blocks in the z direction
                        for (int curZ = -zHalfLength; curZ <= zHalfLength; curZ++) {//Iterate over all z blocks 
                            int relativeZ = curZ + relCenterZ;
                            if (relativeZ >= 0 && relativeZ < 16) {
                                //int worldZ = curZ + curPl.zPos; //Get the absolute z coordinate
                                boolean zShell = false; //Shell or core block
                                int zDistFromCenter = Math.abs(curZ); //Distance from center in the z
                                if (zHalfLength - zDistFromCenter < curPl.shellThickness) {//Check if part of zShell
                                    zShell = true;
                                }
                                int yHalfLength = (int) Math.ceil(Math.sqrt((zHalfLength * zHalfLength) - (zDistFromCenter * zDistFromCenter)));
                                for (int curY = -yHalfLength; curY <= yHalfLength; curY++) {
                                    int worldY = curY + curPl.yPos;
                                    boolean yShell = false;
                                    if (yHalfLength - Math.abs(curY) < curPl.shellThickness) {
                                        yShell = true;
                                    }
                                    if (xShell || zShell || yShell) {
                                        bdata = getRandomBlockdata(random, curPl.shellBlkIds);
                                    } else {
                                        bdata = getRandomBlockdata(random, curPl.coreBlkIds);
                                    }
                                    if (ignoreInvalidBlockIds == false || bdata != null) {
                                        //SpaceDataPopulator.addCoords(worldInfo, chunkX, chunkZ, worldX, worldY, worldZ, mat);
                                        chunkData.setBlock(relativeX, worldY, relativeZ, bdata);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        // Fill in the floor
        for (int floorY = 0; floorY < floorHeight; floorY++) {
            for (int floorX = 0; floorX < 16; floorX++) {
                for (int floorZ = 0; floorZ < 16; floorZ++) {
                    if (bedrockEnabled == true && floorY == 0) {
                        chunkData.setBlock(floorX, floorY, floorZ, Material.BEDROCK);
                    } else {
                        chunkData.setBlock(floorX, floorY, floorZ, floorBlock);
                    }
                }
            }
        }
        return chunkData;
    }

    //TODO: Add toggle option in worlds.yml. Allow custom planet settings
    private void generateSpawnPlanet(WorldInfo worldInfo){
        // Generate a log/leaf planet close to 0,0
        Planetoid spawnPl = new Planetoid();
        spawnPl.coreBlkIds = new ArrayList<BlockData>();
        spawnPl.coreBlkIds.add(Material.GRASS_BLOCK.createBlockData()); //33% Grass, 66% Log
        spawnPl.coreBlkIds.add(Material.OAK_LOG.createBlockData()); spawnPl.coreBlkIds.add(Material.OAK_LOG.createBlockData());
        spawnPl.shellBlkIds = new ArrayList<BlockData>();
        Leaves oak_leaves = (Leaves) Material.OAK_LEAVES.createBlockData(); oak_leaves.setPersistent(true);
        spawnPl.shellBlkIds.add(oak_leaves);
        spawnPl.shellThickness = 4;
        spawnPl.radius = 7;
        spawnPl.xPos = spawnPl.radius;
        spawnPl.yPos = 64+spawnPl.radius;
        spawnPl.zPos = spawnPl.radius;
        planets.get(worldInfo).add(spawnPl);
    }

    /**
     * Generates planets.
     * 
     * @param worldInfo WorldInfo
     * @param chunkX Chunk X
     * @param chunkZ Chunk Z
     */
    private void generatePlanetoids(WorldInfo worldInfo, int chunkX, int chunkZ) {
        List<Planetoid> planetoids = new ArrayList<Planetoid>();
        //long seed = worldInfo.getSeed();
//        //Seed shift;
//        // if X is negative, left shift seed by one
//        if (x < 0) {
//            seed <<= 1;
//        } // if Z is negative, change sign on seed.
//        if (z < 0) {
//            seed = -seed;
//        }

        if (ConfigHandler.getSpawnPlanetEnabled(getID())) {
            //TODO: Test if there's a custom starting planet config
            // if (customStarterPlanet) {
            //      generateSpawnPlanet(worldInfo, customPlanetParameters)
            //}
            //else { }
            generateSpawnPlanet(worldInfo);
        }
        //x = (x*16) - minDistance;
        //z = (z*16) - minDistance;
        Random rand = new Random(getSeed(worldInfo, chunkX, 0, chunkZ, 0));
//        for (int i = 0; i < Math.abs(x) + Math.abs(z); i++) {
//            // cycle generator
//            rand.nextInt();
//            rand.nextInt();
//            rand.nextInt();
//            //rand.nextInt();
//            //rand.nextInt();
//            //rand.nextInt();
//            //rand.nextInt();
//        }

        for (int i = 0; i < density; i++) {
            // Create planet
            Planetoid curPl = new Planetoid();
            curPl.shellBlkIds = getRandomShellBlocks(rand);
            curPl.coreBlkIds = getRandomCoreBlocks(rand);
        
            curPl.shellThickness = rand.nextInt(maxShellSize - minShellSize) + minShellSize;
            curPl.radius = rand.nextInt(maxSize - minSize) + minSize;
            // Set position
            curPl.xPos = (chunkX * 16) - minDistance + rand.nextInt(minDistance + 16 + minDistance);
            int maxY = worldInfo.getMaxHeight() - curPl.radius * 2 - floorHeight;
            curPl.yPos = rand.nextInt(maxY >= 0 ? maxY : 0) + curPl.radius + floorHeight;
            curPl.zPos = (chunkZ * 16) - minDistance + rand.nextInt(minDistance + 16 + minDistance);

            // If too close to other planet, discard planet
            boolean discard = false;
            for (Planetoid pl : planetoids) {
                int distMin = pl.radius + curPl.radius + minDistance;
                if (distanceSquared(pl, curPl) < distMin * distMin) {
                    discard = true;
                    break;
                }
            } // TODO: Check if this can be optimized. How large can the 'planetoids' list become?
            if (!planets.isEmpty()) {
                if (!planets.get(worldInfo).isEmpty()) {
                    List<Planetoid> tempPlanets = planets.get(worldInfo);
                    for (Planetoid pl : tempPlanets) {
                        int distMin = pl.radius + curPl.radius + minDistance;
                        if (distanceSquared(pl, curPl) < distMin * distMin) {
                            discard = true;
                            break;
                        }
                    }
                }
            }
            
            if (!discard) {
                planetoids.add(curPl);
            }
        }
        planets.get(worldInfo).addAll(planetoids);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        ArrayList<BlockPopulator> populators = new ArrayList<BlockPopulator>();
        if (ConfigHandler.getSatellitesEnabled(ID)) {
            populators.add(new SpaceSatellitePopulator());
        }
        if (ConfigHandler.getAsteroidsEnabled(ID)) {
            populators.add(new SpaceAsteroidPopulator());
        }
        if (ConfigHandler.getGenerateSchematics(ID)) {
            populators.add(new SpaceSchematicPopulator());
        }
        //populators.add(new SpaceDataPopulator());
        
        return populators;
    }

    /**
     * load possible blocks for planet generation
     */

    private void loadPossibleBlocks() {
        try {       
            coreBlocklists = getBlocklistChances(SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getStringList("blocks.cores"));
            shellBlocklists = getBlocklistChances(SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getStringList("blocks.shells"));

            MessageHandler.debugPrint(Level.INFO, "coreBlocklists has " + coreBlocklists.size() + " entries\n"
                                                + "shellBlocklists has " + shellBlocklists.size() + " entries");
        }
        catch (Exception ex){ //Throwable?
            //TODO: Turn this into a generic "short trace log"
            MessageHandler.debugPrint(Level.WARNING, "PlanetsChunkGenerator has a problem:");
            StackTraceElement[] details = ex.getStackTrace();
            int fiveTraces = Math.max(0, details.length-6);
            for (int i = details.length-1; fiveTraces < i; i-- ){
                MessageHandler.debugPrint(Level.WARNING, details[i].getMethodName());
            }
        }
    }

    private HashMap<ArrayList<BlockData>, Float> getBlocklistChances(List<String> readList){
        HashMap<ArrayList<BlockData>, Float> blocklistChances = new HashMap<ArrayList<BlockData>, Float>();
        for (String s : readList) {
            String[] sSplit = s.replaceAll("\\s","").split("-");
            String[] mats = sSplit[0].split(",");
            ArrayList<BlockData> bdList = makeBlocklist(mats);
            float probability;
            if (sSplit.length == 2) {
                probability = Float.valueOf(sSplit[1]);
            } else {
                probability = 1.0f;
            }
            blocklistChances.put(bdList, probability);
        }
        return blocklistChances;
    }

    private ArrayList<BlockData> makeBlocklist(String[] mats) {
        ArrayList<BlockData> bdList = new ArrayList<BlockData>();
        for (String s : mats) {
            String name = "";
            if(s.split(":").length == 2){
                name = s.split(":")[0];
            }
            else {
                name = s;
            }
            MessageHandler.debugPrint(Level.INFO, "Trying to match material with name: " + name);
            Material newMat = Material.matchMaterial(name);
            if(newMat != null) {
                if (ignoreInvalidBlockIds || newMat.isBlock()) { //If ignoreInvalid: we accept unknown blocks (typo or modded)
                    BlockData newBd = newMat.createBlockData();
                    if (Leaves.class.isInstance(newBd)){ // If Leaves
                        Leaves newLeaves = (Leaves) newBd;
                        newLeaves.setPersistent(true); //Otherwise, leaves disappear
                        bdList.add(newLeaves);
                    }
                    else {
                        bdList.add(newBd);
                    }
                }
                else { // Bad block! Probably a typo
                    MessageHandler.print(Level.WARNING, "Unrecognized id (" + name + ") in planets.yml (Not vanilla. Error can be ignored by setting ignoreInvalidBlockIds=true)");
                }
            }
            else { 
                MessageHandler.print(Level.WARNING, "Unrecognized id (" + name + ") in planets.yml (Null error)");
            }
        }
        if (bdList.size() == 0) {
            bdList.add(Material.AIR.createBlockData());
        }
        return bdList;
    }

    /*
     * Gets the squared distance.
     * @param pl1 Planetoid 1
     * @param pl2 Planetoid 2
     * 
     * @return Distance
     */
    private int distanceSquared(Planetoid pl1, Planetoid pl2) {
        int xDist = pl2.xPos - pl1.xPos;
        int yDist = pl2.yPos - pl1.yPos;
        int zDist = pl2.zPos - pl1.zPos;

        return xDist * xDist + yDist * yDist + zDist * zDist;
    }

    /**
     * Returns a valid block type.
     * 
     * @param random
     * @param blocks From 'planets.yml'. Example: "STONE,COBBLESTONE,DIRT,DIRT,DIRT-1.0".
     * 
     * @return Material
     */
    private BlockData getRandomBlockdata(Random random, ArrayList<BlockData> blocks) {
        return blocks.get(random.nextInt(blocks.size()));
    }

    private ArrayList<BlockData> getRandomCoreBlocks(Random rand) {
        return getRandomBlockdataList(rand, coreBlocklists);
    }

    private ArrayList<BlockData> getRandomShellBlocks(Random rand) {
        return getRandomBlockdataList(rand, shellBlocklists);
    }

    private ArrayList<BlockData> getRandomBlockdataList(Random rand, Map<ArrayList<BlockData>, Float> possibleBlockSets) {
        while(true){ // Run until a list is selected
            //Select a random list
            ArrayList<BlockData> blockList = new ArrayList<ArrayList<BlockData>>(possibleBlockSets.keySet()).get(rand.nextInt(possibleBlockSets.size()));
            //Check if blocks's probability (float) is higher than random number (float)
            if (possibleBlockSets.get(blockList) > rand.nextFloat()) {
                return blockList;
            }
        }
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        // TODO: figure out if it's our fault or MV's fault that this is not used...
        return new Location(world, 7, 79, 7);
    }
    
    private final int HASH_SHIFT = 19;
    private final long HASH_SHIFT_MASK = (1L << HASH_SHIFT) - 1;

    /**
     * Returns the particular seed a Random should use for a position
     *
     * The meaning of the x, y and z coordinates can be determined by the
     * generator.
     *
     * This gives consistent results for world generation.
     *
     * The extra seed allows multiple Randoms to be returned for the same
     * position for use by populators and different stages of generation.
     *
     * @param worldInfo the World
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param extraSeed the extra seed value
     * @return the seed 
     */
    public long getSeed(WorldInfo worldInfo, int x, int y, int z, int extraSeed) {
            long hash = worldInfo.getSeed();
            hash += (hash << HASH_SHIFT) + (hash >> 64 - HASH_SHIFT & HASH_SHIFT_MASK) + extraSeed;
            hash += (hash << HASH_SHIFT) + (hash >> 64 - HASH_SHIFT & HASH_SHIFT_MASK) + x;
            hash += (hash << HASH_SHIFT) + (hash >> 64 - HASH_SHIFT & HASH_SHIFT_MASK) + y;
            hash += (hash << HASH_SHIFT) + (hash >> 64 - HASH_SHIFT & HASH_SHIFT_MASK) + z;

            return hash;
    }

    private void loadPlanetSettings() {
        if(ID.equals("planets")) return;
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(new File(Bukkit.getPluginManager().getPlugin("DuzySpace").getDataFolder(), "planets/" + ConfigHandler.getPlanetsFile(ID)));

            density = config.getInt("density", (Integer) SpaceConfig.Defaults.DENSITY.getDefault()); // Number of planetoids it will try to create per
            minDistance = config.getInt("minDistance", (Integer) SpaceConfig.Defaults.MIN_DISTANCE.getDefault()); // Minimum distance between planets, in blocks
            minSize = config.getInt("minSize", (Integer) SpaceConfig.Defaults.MIN_SIZE.getDefault()); // Minimum radius
            maxSize = config.getInt("maxSize", (Integer) SpaceConfig.Defaults.MAX_SIZE.getDefault()); // Maximum radius
            maxShellSize = config.getInt("maxShellSize", (Integer) SpaceConfig.Defaults.MAX_SHELL_SIZE.getDefault()); // Maximum shell thickness
            minShellSize = config.getInt("minShellSize", (Integer) SpaceConfig.Defaults.MIN_SHELL_SIZE.getDefault()); // Minimum shell thickness, should be at least 3
            floorHeight = config.getInt("floorHeight", (Integer) SpaceConfig.Defaults.FLOOR_HEIGHT.getDefault()); // Floor height
            floorBlock = Material.matchMaterial(config.getString("floorBlock", (String) SpaceConfig.Defaults.FLOOR_BLOCK.getDefault()));// BlockID for the floor
            bedrockEnabled = config.getBoolean("bedrockEnabled", (Boolean) SpaceConfig.Defaults.BEDROCK_ENABLED.getDefault()); // Bedrock layer at y=0
            ignoreInvalidBlockIds = config.getBoolean("ignoreInvalidBlockIds", (Boolean) SpaceConfig.Defaults.IGNORE_INVALID_BLOCK_IDS.getDefault()); // Ignore invalid block ids, i.e. typos and modded block ids
        } catch (IOException ex) {
            MessageHandler.debugPrint(Level.WARNING, "IOException when getting info for planets file for id "+ ID);
        } catch (InvalidConfigurationException ex) {
            MessageHandler.debugPrint(Level.WARNING, "InvalidConfigurationException when getting info for planets file for id "+ ID);
        } 
    }
}
