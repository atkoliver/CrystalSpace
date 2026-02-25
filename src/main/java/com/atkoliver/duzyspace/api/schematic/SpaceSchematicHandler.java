/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.atkoliver.duzyspace.api.schematic;

import com.atkoliver.duzyspace.handlers.MessageHandler;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BlockVector;
import org.jnbt.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.logging.Level;

/**
 * Schematic-file loading class. To be used for populators
 * 
 * @author iffa
 * @author kitskub
 * @author DrAgonmoray (original NBT loading code)
 * @author sk89q and other people who accidently a WE commit (schematic handling)
 */
public class SpaceSchematicHandler {
    // Variables
    public static File schematicFolder = new File("plugins" + File.separator + "DuzySpace" + File.separator + "schematics");
    private static List<Schematic> schematics = new ArrayList<Schematic>();

    /**
     * Gets the list of schematics loaded.
     * 
     * @return List of schematics loaded
     */
    public static List<Schematic> getSchematics() {
        return Collections.unmodifiableList(schematics);
    }

    /**
     * Places a schematic to a location.
     * 
     * @param schematic Schematic
     * @param origin Location the schematic should be placed to
     */
    public static void placeSchematic(Schematic schematic, Location origin) {
        Map<Location, Map<Material, BlockData>> blocksMap = getBlocksMap(schematic, origin);
        Map<BlockVector, Map<String, Tag>> tileEntitiesMap = getTileEntitiesMap(schematic.getTileEntities());

        // Builds the schematic.
        MessageHandler.debugPrint(Level.INFO, "Now going inside buildSchematic.");
        buildSchematic(origin, blocksMap, tileEntitiesMap);
    }

    /**
     * Internal buildSchematic that places the schematic (hopefully succesfully!).
     * 
     * @param origin Origin location
     * @param blocksMap Blocks Map
     * @param tileEntitiesMap Tile Entities Map
     */
    private static void buildSchematic(Location origin, 
            Map<Location, Map<Material, BlockData>> blocksMap, 
            Map<BlockVector, Map<String, Tag>> tileEntitiesMap) {
        
        // Variables
        MessageHandler.debugPrint(Level.INFO, "Inside buildSchematic with origin location: '" + origin.toString() + "'.");
        World world = origin.getWorld();

        // Setting blocks
        for (Location location : blocksMap.keySet()) {
            int originX = origin.getBlockX() + location.getBlockX();
            int originY = origin.getBlockY() + location.getBlockY();
            int originZ = origin.getBlockZ() + location.getBlockZ();

            for (Material material : blocksMap.get(location).keySet()) {
                Block block = world.getBlockAt(originX, originY, originZ);
                block.setType(material);
                block.setBlockData(blocksMap.get(location).get(material));
            }
        }
    }
    
    /**
     * Loads all schematics from plugins/DuzySpace/schematics.
     */
    public static void loadSchematics() {
        List<File> files = Arrays.asList(new File("plugins" + File.separator + "DuzySpace" + File.separator + "schematics").listFiles());
        if (files.isEmpty()) {
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".schematic")) {
                loadSchematic(file);
            }
        }
    }
    
    private static Schematic loadSchematic(String name, NBTInputStream nbtStream) throws Exception{
            // Schematic tag
            CompoundTag schematicTag = (CompoundTag) nbtStream.readTag();
            if (!schematicTag.getName().equals("Schematic")) {
                throw new Exception("Tag \"Schematic\" does not exist or is not first");
            }

            // Check
            Map<String, Tag> tagCollection = schematicTag.getValue();
            if (!tagCollection.containsKey("Blocks")) {
                throw new Exception("Schematic file is missing a \"Blocks\" tag");
            }

            // Get information
            short width = getChildTag(tagCollection, "Width", ShortTag.class).getValue();
            short length = getChildTag(tagCollection, "Length", ShortTag.class).getValue();
            short height = getChildTag(tagCollection, "Height", ShortTag.class).getValue();

            // Get blocks
            byte[] blocks = getChildTag(tagCollection, "Blocks", ByteArrayTag.class).getValue();
            byte[] data = getChildTag(tagCollection, "Data", ByteArrayTag.class).getValue();
            
            List<Tag> entities = getChildTag(tagCollection, "Entities", ListTag.class).getValue();

            // Check type of Schematic
            String materials = getChildTag(tagCollection, "Materials", StringTag.class).getValue();
            if (!materials.equals("Alpha")) {
                throw new Exception("Schematic file is not an Alpha schematic");
            }

            // Need to pull out tile entities
            List<Tag> tileEntities = getChildTag(tagCollection, "TileEntities", ListTag.class).getValue();
            return new Schematic(name, blocks, data, width, height, length, entities, tileEntities);
    }

    /**
     * Loads a schematic file and adds it to the schematics list for later usage.
     * 
     * @param file Schematic file
     */
    public static void loadSchematic(File file) {
        try{
            FileInputStream fis = new FileInputStream(file);
            NBTInputStream nbt = new NBTInputStream(fis);
            Schematic schematic = loadSchematic(file.getName().replaceAll(".schematic", ""), nbt);
            if (nbt != null) {
                nbt.close();
            }
            if (fis != null) {
                fis.close();
            }
            schematics.add(schematic);
            MessageHandler.debugPrint(Level.INFO, "Added Schematic '" + file.getName() + "' to schematics list:\n" + schematic.getWidth() + "\n" + schematic.getHeight() + "\n" + schematic.getLength() + "\n" + schematic.getBlocks() + "\n" + schematic.getBlockData());
        } catch (Exception e) {
            MessageHandler.print(Level.WARNING, "There was a problem while loading schematic file '" + file.getName() + "'! Are you sure it's a schematic: " + e.getMessage());
        }
    }

    /**
     * Gets a child tag.
     * 
     * @param items Tag collection
     * @param key Key
     * @param expected Expected
     * 
     * @return Child tag
     */
    private static <T extends Tag>  T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws Exception {
        if (!items.containsKey(key)) {
            throw new Exception("Schematic file is missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new Exception(
                    key + " tag is not of tag type " + expected.getName());
        }
        return expected.cast(tag);
    }
/*
    public static boolean isGZIP(File file) {
        DataInputStream str = null;
        try {
            str = new DataInputStream(new GZIPInputStream(new FileInputStream(file)));
            if ((str.readByte() & 0xFF) != NBTConstants.TYPE_COMPOUND) {
                return false;
            }
            byte[] nameBytes = new byte[str.readShort() & 0xFFFF];
            str.readFully(nameBytes);
            String name = new String(nameBytes, NBTConstants.CHARSET);
            return name.equals("Schematic");
        } catch (IOException e) {
            return false;
        } finally {
            if (str != null) {
                try {
                    str.close();
                } catch (IOException ignore) {
                    // blargh
                }
            }
        }
    }
*/
    private static Map<BlockVector, Map<String, Tag>> getTileEntitiesMap(List<Tag> tileEntities){
        Map<BlockVector, Map<String, Tag>> tileEntitiesMap = new HashMap<BlockVector, Map<String, Tag>>();

        for (Tag tag : tileEntities) {
            if (!(tag instanceof CompoundTag)) continue;
            CompoundTag t = (CompoundTag) tag;

            int x = 0;
            int y = 0;
            int z = 0;

            Map<String, Tag> values = new HashMap<String, Tag>();

            for (Map.Entry<String, Tag> entry : t.getValue().entrySet()) {
                if (entry.getKey().equals("x")) {
                    if (entry.getValue() instanceof IntTag) {
                        x = ((IntTag) entry.getValue()).getValue();
                    }
                } else if (entry.getKey().equals("y")) {
                    if (entry.getValue() instanceof IntTag) {
                        y = ((IntTag) entry.getValue()).getValue();
                    }
                } else if (entry.getKey().equals("z")) {
                    if (entry.getValue() instanceof IntTag) {
                        z = ((IntTag) entry.getValue()).getValue();
                    }
                }

                values.put(entry.getKey(), entry.getValue());
            }

            BlockVector vec = new BlockVector(x, y, z);
            tileEntitiesMap.put(vec, values);
        }
        return tileEntitiesMap;
    }
    
    private static Map<Location, Map<Material, BlockData>> getBlocksMap(Schematic schematic, Location origin){
        Map<Location, Map<Material, BlockData>> blocksMap = new HashMap<Location, Map<Material, BlockData>>();
        int width = schematic.getWidth(); int height = schematic.getHeight(); int length = schematic.getLength();
        boolean readerror = false;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    try {
                        //PROBLEM: How to construct BlockData from the (byte) data from schematic? How is (byte) data formatted?
                        //TODO (SOLUTION?): Use schematic loader from Worledit instead. Then I'll have code that works.
                        //blockData = new BlockData(block, schematic.getBlockData()[index]);
                        //Material block = Material.getMaterial(schematic.getBlocks()[index]);
                        Material block = Material.OAK_LOG;
                        //This is a temporary workaround. Problem: This won't copy blockdata like chest contents from schematic.
                        BlockData blockData = block.createBlockData();
                        Map<Material, BlockData> tempMap = new EnumMap<Material, BlockData>(Material.class);
                        tempMap.put(block, blockData);
                        blocksMap.put(new Location(origin.getWorld(), x, y, z), tempMap);
                    } catch (Exception e) {
                        if (!readerror) {
                            MessageHandler.print(Level.WARNING, "Unreadable block(s) in this schematic:" + e.getMessage());
                        }
                    }
                }
            }
        }
        return blocksMap;
    }
    
    /**
     * Constructor of SpaceSchematicHandler.
     */
    private SpaceSchematicHandler() {
    }
}
