/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.crystalcraftmc.crystalspace.wgen.planets;

import com.crystalcraftmc.crystalspace.config.SpaceConfig;
import com.crystalcraftmc.crystalspace.handlers.ConfigHandler;
import com.crystalcraftmc.crystalspace.handlers.MessageHandler;
import com.crystalcraftmc.crystalspace.wgen.populators.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

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
    private Map<Set<Material>, Float> possibleShellIds;
    private Map<Set<Material>, Float> possibleCoreIds;
    private int density = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("density", (Integer) SpaceConfig.Defaults.DENSITY.getDefault()); // Number of planetoids it will try to create per
    private int minSize = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("minSize", (Integer) SpaceConfig.Defaults.MIN_SIZE.getDefault()); // Minimum radius
    private int maxSize = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("maxSize", (Integer) SpaceConfig.Defaults.MAX_SIZE.getDefault()); // Maximum radius
    private int minDistance = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("minDistance", (Integer) SpaceConfig.Defaults.MIN_DISTANCE.getDefault()); // Minimum distance between planets, in blocks
    private int floorHeight = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("floorHeight", (Integer) SpaceConfig.Defaults.FLOOR_HEIGHT.getDefault()); // Floor height
    private boolean bedrockEnabled = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getBoolean("bedrockEnabled", (Boolean) SpaceConfig.Defaults.BEDROCK_ENABLED.getDefault()); // Bedrock layer at y=0
    private int maxShellSize = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("maxShellSize", (Integer) SpaceConfig.Defaults.MAX_SHELL_SIZE.getDefault()); // Maximum shell thickness
    private int minShellSize = SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getInt("minShellSize", (Integer) SpaceConfig.Defaults.MIN_SHELL_SIZE.getDefault()); // Minimum shell thickness, should be at least 3
    private Material floorBlock = Material.matchMaterial(SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getString("floorBlock", (String) SpaceConfig.Defaults.FLOOR_BLOCK.getDefault()));// BlockID for the floor
    private static HashMap<World, List<Planetoid>> planets = new HashMap<World, List<Planetoid>>();
    public final String ID;
    public final boolean GENERATE;

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
     * Selects random Material from blocks and returns it
     * 
     * @param blocks Set<Material>
     */
    Material getRandomMaterial(Random random, ArrayList<Material> blocks) {
        //Example in planets.yml: "STONE,COBBLESTONE,DIRT,DIRT,DIRT-1.0".
        //The example 'blocks' array would then be: [STONE, COBBLESTONE, DIRT, DIRT, DIRT] (1.0 is the chance that this blocklist gets selected, after the blocklist is randomly chosen)
        return blocks.get(random.nextInt(blocks.size()));
    }

    /**
     * Generates chunk data for a chunk.
     * 
     * @param world
     * @param random
     * @param x
     * @param z
     * @param biome
     * @return 
     */

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        if (!planets.containsKey(world)) {
            planets.put(world, new ArrayList<Planetoid>());
        }
        Material mat;
        ChunkData cData = this.createChunkData(world);
        
        if (GENERATE) {
            MessageHandler.debugPrint(Level.INFO, "GENERATE == true, generating planet");
            generatePlanetoids(world, x, z);
            // Go through the current system's planetoids and fill in this chunk as needed.
            for (Planetoid curPl : planets.get(world)) {
                // Find planet's center point relative to this chunk.
                int relCenterX = curPl.xPos - x * 16;
                int relCenterZ = curPl.zPos - z * 16;

                for (int curX = -curPl.radius; curX <= curPl.radius; curX++) {//Iterate across every x block
                    boolean xShell = false;//Is part of the x shell
                    int chunkX = curX + relCenterX;
                    if (chunkX >= 0 && chunkX < 16) {
                        int worldX = curX + curPl.xPos;//get the x block number in the world
                        // Figure out radius of this circle
                        int distFromCenter = Math.abs(curX);//Distance from center in the x 
                        if (curPl.radius - distFromCenter < curPl.shellThickness) {//Check if part of xShell
                            xShell = true;
                        }
                        int zHalfLength = (int) Math.ceil(Math.sqrt((curPl.radius * curPl.radius) - (distFromCenter * distFromCenter)));//Half the amount of blocks in the z direction
                        for (int curZ = -zHalfLength; curZ <= zHalfLength; curZ++) {//Iterate over all z blocks 
                            int chunkZ = curZ + relCenterZ;
                            if (chunkZ >= 0 && chunkZ < 16) {
                                int worldZ = curZ + curPl.zPos;//get the z block number in the world
                                boolean zShell = false;//Is part of z shell
                                int zDistFromCenter = Math.abs(curZ);//Distance from center in the z
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
                                        mat = getRandomMaterial(random, curPl.shellBlkIds);
                                    } else {
                                        mat = getRandomMaterial(random, curPl.coreBlkIds);
                                    }
                                    cData.setBlock(chunkX, worldY, chunkZ, mat);
                                    if (mat != null) { //Has data
                                         SpaceDataPopulator.addCoords(world, x, z, chunkX, worldY, chunkZ, mat);
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
                        cData.setBlock(floorX, floorY, floorZ, Material.BEDROCK);
                        //setBlock(retVal, floorX, floorY, floorZ, (byte) Material.BEDROCK.getId());
                    } else {
                        cData.setBlock(floorX, floorY, floorZ, floorBlock);
                        //setBlock(retVal, floorX, floorY, floorZ, (byte) floorBlock.getId());
                    }
                }
            }
        }
        return cData;
    }

    /**
     * Generates a world.
     * 
     * @param world World
     * @param random Random
     * @param x X-pos
     * @param z Z-pos
     * @param biomes 
     * @return Byte array
     * @deprecated generateChunkData for 1.8+
     */
    @Override
    public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomes){
        if (!planets.containsKey(world)) {
            planets.put(world, new ArrayList<Planetoid>());
        }
        Material mat;
        byte[][] retVal = new byte[world.getMaxHeight() / 16][];

        if (GENERATE) {
            generatePlanetoids(world, x, z);
            // Go through the current system's planetoids and fill in this chunk as needed.
            for (Planetoid curPl : planets.get(world)) {
                // Find planet's center point relative to this chunk.
                int relCenterX = curPl.xPos - x * 16;
                int relCenterZ = curPl.zPos - z * 16;

                for (int curX = -curPl.radius; curX <= curPl.radius; curX++) {//Iterate across every x block
                    boolean xShell = false;//Is part of the x shell
                    int chunkX = curX + relCenterX;
                    if (chunkX >= 0 && chunkX < 16) {
                        int worldX = curX + curPl.xPos;//get the x block number in the world
                        // Figure out radius of this circle
                        int distFromCenter = Math.abs(curX);//Distance from center in the x 
                        if (curPl.radius - distFromCenter < curPl.shellThickness) {//Check if part of xShell
                            xShell = true;
                        }
                        int zHalfLength = (int) Math.ceil(Math.sqrt((curPl.radius * curPl.radius) - (distFromCenter * distFromCenter)));//Half the amount of blocks in the z direction
                        for (int curZ = -zHalfLength; curZ <= zHalfLength; curZ++) {//Iterate over all z blocks 
                            int chunkZ = curZ + relCenterZ;
                            if (chunkZ >= 0 && chunkZ < 16) {
                                int worldZ = curZ + curPl.zPos;//get the z block number in the world
                                boolean zShell = false;//Is part of z shell
                                int zDistFromCenter = Math.abs(curZ);//Distance from center in the z
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
                                        mat = getRandomMaterial(random, curPl.shellBlkIds);
                                    } else {
                                        mat = getRandomMaterial(random, curPl.coreBlkIds);
                                    }
                                    setBlock(retVal, chunkX, worldY, chunkZ, (byte) mat.ordinal());
                                    if (mat.ordinal() != 0) { //Has data
                                         SpaceDataPopulator.addCoords(world, x, z, chunkX, worldY, chunkZ, mat);
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
                    if (floorY == 0) {
                        setBlock(retVal, floorX, floorY, floorZ, (byte) Material.BEDROCK.ordinal());
                    } else {
                        setBlock(retVal, floorX, floorY, floorZ, (byte) floorBlock.ordinal());
                    }
                }
            }
        }
        return retVal;
    }

    /**
     * Adds the block to the argument "result".
     * 
     * @param result
     * @param x
     * @param y
     * @param z
     * @param blkid 
     * @deprecated ChunkGenerator.ChunkData.setBlock for 1.8+
     */
    static void setBlock(byte[][] result, int x, int y, int z, byte blkid) {
        if (result[y >> 4] == null) {
            result[y >> 4] = new byte[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
    }

    /**
     * Generates planets.
     * 
     * @param world World
     * @param x X-pos
     * @param z Z-pos
     */
    @SuppressWarnings("fallthrough")
    private void generatePlanetoids(World world, int x, int z) {
        long seed = world.getSeed();
        List<Planetoid> planetoids = new ArrayList<Planetoid>();
//        //Seed shift;
//        // if X is negative, left shift seed by one
//        if (x < 0) {
//            seed <<= 1;
//        } // if Z is negative, change sign on seed.
//        if (z < 0) {
//            seed = -seed;
//        }

        // If x and Z are zero, generate a log/leaf planet close to 0,0
        if (x == 0 && z == 0) {
            Planetoid spawnPl = new Planetoid();
            spawnPl.xPos = 7;
            spawnPl.yPos = 70;
            spawnPl.zPos = 7;
            spawnPl.coreBlkIds = new ArrayList<Material>(Collections.singleton(Material.matchMaterial("LOG")));
            spawnPl.shellBlkIds = new ArrayList<Material>(Collections.singleton(Material.matchMaterial("LEAVES")));
            spawnPl.shellThickness = 3;
            spawnPl.radius = 6;
            planets.get(world).add(spawnPl);
        }

        //x = (x*16) - minDistance;
        //z = (z*16) - minDistance;
        Random rand = new Random(getSeed(world, x, 0, z, 0));
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
            // Try to make a planet
            Planetoid curPl = new Planetoid();
            curPl.shellBlkIds = getShellBlocks(rand);
            curPl.coreBlkIds = getCoreBlocks(rand);

            curPl.shellThickness = rand.nextInt(maxShellSize - minShellSize) + minShellSize;
            curPl.radius = rand.nextInt(maxSize - minSize) + minSize;

            // Set position
            curPl.xPos = (x * 16) - minDistance + rand.nextInt(minDistance + 16 + minDistance);
            int randInt = world.getMaxHeight() - curPl.radius * 2 - floorHeight;
            curPl.yPos = rand.nextInt(randInt >= 0 ? randInt : 0) + curPl.radius + floorHeight;
            curPl.zPos = (z * 16) - minDistance + rand.nextInt(minDistance + 16 + minDistance);

            // TODO: Check if this can be optimized.
            // How large can the 'planetoids' list become?

            // Created a planet, check for collisions with existing planets
            // If any collision, discard planet
            boolean discard = false;
            for (Planetoid pl : planetoids) {
                // each planetoid has to be at least pl1.radius + pl2.radius +
                // min distance apart
                int distMin = pl.radius + curPl.radius + minDistance;
                if (distanceSquared(pl, curPl) < distMin * distMin) {
                    discard = true;
                    break;
                }
            }
            if (!planets.isEmpty()) {
                if (!planets.get(world).isEmpty()) {
                    List<Planetoid> tempPlanets = planets.get(world);
                    for (Planetoid pl : tempPlanets) {
                        // each planetoid has to be at least pl1.radius + pl2.radius +
                        // min distance apart
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
        planets.get(world).addAll(planetoids);
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
        populators.add(new SpaceDataPopulator());
        
        // Not FPS friendly
        if (false) {
            populators.add(new SpaceEffectPopulator());
        }
        return populators;
    }

    /**
     * load possible blocks for planet generation
     */

    @SuppressWarnings("unchecked")
    private void loadPossibleBlocks() {
        try {       
            possibleCoreIds = new HashMap<Set<Material>, Float>();
            possibleShellIds = new HashMap<Set<Material>, Float>();
            readPossibleBlockSets(possibleCoreIds, SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getStringList("blocks.cores"));
            readPossibleBlockSets(possibleShellIds, SpaceConfig.getConfig(SpaceConfig.ConfigFile.DEFAULT_PLANETS).getStringList("blocks.shells"));

            MessageHandler.debugPrint(Level.INFO, "possibleCoreIds has " + possibleCoreIds.size() + " entries\n"
                                                + "possibleShellIds has " + possibleShellIds.size() + " entries");
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

    private static void readPossibleBlockSets(Map<Set<Material>, Float> possibleMap, List<String> readList){
        for (String s : readList) {
            String[] sSplit = s.replaceAll("\\s","").split("-");
            String[] matList = sSplit[0].split(",");
            Set<Material> matSet = makePossibleBlockSet(matList);
            float probability;
            if (sSplit.length == 2) {
                probability = Float.valueOf(sSplit[1]);
            } else {
                probability = 1.0f;
            }
            possibleMap.put(matSet, probability);
        }
    }

    private static Set<Material> makePossibleBlockSet(String[] matList) {
        Set<Material> matSet = new HashSet<Material>();
        for (String s : matList) {
            int data = 0;
            String name = "";
            if(s.split(":").length == 2){
                try {
                    name = s.split(":")[0];
                    data = Integer.parseInt(s.split(":")[1]);
                } catch (NumberFormatException numberFormatException) {
                    MessageHandler.print(Level.WARNING, "Invalid core block in planets.yml");
                }
            }
            else{
                name = s;
            }
            MessageHandler.debugPrint(Level.INFO, "Trying to match material with name: " + name);
            Material newMat = Material.matchMaterial(name);

            if(newMat != null){//Vanilla material
                if (newMat.isBlock()) {
                    matSet.add(newMat);
                } else {
                    MessageHandler.print(Level.WARNING, newMat.toString() + " is not a block");
                }
            }
                else if (ConfigHandler.getIgnoreInvalidBlockIds()) { //Do we check for bad ids? Disabled in config for modded blocks
                try {
                    matSet.add(newMat);
                } catch (NumberFormatException numberFormatException) {
                    MessageHandler.print(Level.WARNING, "Unrecognized id (" + name + ") in planets.yml");
                }
            }
            else { // Bad block id! Typically a typo
                MessageHandler.print(Level.WARNING, "Unrecognized id (" + name + ") in planets.yml");
            }
            
        }
        return matSet;
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
     * //@param randrandom generator to use
     * @param core if true, searching through possible cores, otherwise possible shells
     * //@param heated if true, will not return a block that gives off heat
     * 
     * @return Material
     */
    private Set<Material> getRandomCoreBlocks(Random rand) {
        return getBlockSet(rand, possibleCoreIds);
    }

    private Set<Material> getRandomShellBlocks(Random rand) {
        return getBlockSet(rand, possibleShellIds);
    }

    private Set<Material> getRandomBlockSet(Random rand, Map<Set<Material>, Float> possibleBlkIds) {
        Set<Material> retVal = null;
        //Select random material from possibleBlkIds
        Set<Material> planetMaterials = new ArrayList<Set<Material>>(possibleBlkIds.keySet()).get(rand.nextInt(possibleBlkIds.size()));
        //Test if planetMaterials' probability (float) is higher than random number (float)
        if (possibleBlkIds.get(planetMaterials) > rand.nextFloat()) {
            retVal = planetMaterials;
        }
        return retVal;
    }

    /*TODO: Delete after checking getBlockTypes() new version works.
    Old
    private Set<Material> getBlockTypes(Random rand, boolean core) {
        Set<Material> retVal = null;
        Map<Set<Material>, Float> refMap;
        if (core) {
            refMap = possibleCoreIds;
        } else {
            refMap = possibleShellIds;
        }
        outer:
        while (retVal == null) {
            Set<Material> dataList = new ArrayList<Set<Material>>(refMap.keySet()).get(rand.nextInt(refMap.size()));
            float testVal = rand.nextFloat();
            if (refMap.get(dataList) > testVal) {
                retVal = dataList;
                //'noHeat' was an argument to this function.
                //It was used to remove fire-y blocks if the shell contained wool.
                //Removed as an undocumented and obstructive behavior. (some people may WANT wool planet that burn themselves)
                //Can the code below be repurposed, or should it just be deleted? 
                for (Material mat : dataList) {
                  //if (noHeat) {
                    if (true) {
                        if(mat == null){//Not a Vanilla Material. Don't care.
                            continue;
                        }
                        switch (mat) {
                            case BURNING_FURNACE:
                            case FIRE:
                            case GLOWSTONE:
                            case JACK_O_LANTERN:
                            case STATIONARY_LAVA:
                                retVal = null;//Try again
                                continue outer;
                            default:
                        }
                    }
                }
            }
        }
        return retVal;
    } */

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        // TODO: figure out if it's our fault or MV's fault that this is not used...
        return new Location(world, 7, 78, 7);
    }
    
    private final static int HASH_SHIFT = 19;
    private final static long HASH_SHIFT_MASK = (1L << HASH_SHIFT) - 1;

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
     * @param world the World
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param extraSeed the extra seed value
     * @return the seed 
     */
    public static long getSeed(World world, int x, int y, int z, int extraSeed) {
            long hash = world.getSeed();
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
            config.load(new File(Bukkit.getPluginManager().getPlugin("CrystalSpace").getDataFolder(), "planets/" + ConfigHandler.getPlanetsFile(ID)));

            density = config.getInt("density", (Integer) SpaceConfig.Defaults.DENSITY.getDefault()); // Number of planetoids it will try to create per
            minSize = config.getInt("minSize", (Integer) SpaceConfig.Defaults.MIN_SIZE.getDefault()); // Minimum radius
            maxSize = config.getInt("maxSize", (Integer) SpaceConfig.Defaults.MAX_SIZE.getDefault()); // Maximum radius
            minDistance = config.getInt("minDistance", (Integer) SpaceConfig.Defaults.MIN_DISTANCE.getDefault()); // Minimum distance between planets, in blocks
            floorHeight = config.getInt("floorHeight", (Integer) SpaceConfig.Defaults.FLOOR_HEIGHT.getDefault()); // Floor height
            bedrockEnabled = config.getBoolean("bedrockEnabled", (Boolean) SpaceConfig.Defaults.BEDROCK_ENABLED.getDefault()); // Bedrock layer at y=0
            maxShellSize = config.getInt("maxShellSize", (Integer) SpaceConfig.Defaults.MAX_SHELL_SIZE.getDefault()); // Maximum shell thickness
            minShellSize = config.getInt("minShellSize", (Integer) SpaceConfig.Defaults.MIN_SHELL_SIZE.getDefault()); // Minimum shell thickness, should be at least 3
            floorBlock = Material.matchMaterial(config.getString("floorBlock", (String) SpaceConfig.Defaults.FLOOR_BLOCK.getDefault()));// BlockID for the floor
            } catch (IOException ex) {
            MessageHandler.debugPrint(Level.WARNING, "IOException when getting info for planets file for id "+ ID);
        } catch (InvalidConfigurationException ex) {
            MessageHandler.debugPrint(Level.WARNING, "InvalidConfigurationException when getting info for planets file for id "+ ID);
        } 
    }
}
