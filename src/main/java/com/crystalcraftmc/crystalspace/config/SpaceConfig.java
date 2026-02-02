/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.crystalcraftmc.crystalspace.config;

import com.crystalcraftmc.crystalspace.api.schematic.SpaceSchematicHandler;
import com.crystalcraftmc.crystalspace.handlers.LangHandler;
import com.crystalcraftmc.crystalspace.handlers.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * A class that handles the configuration file.
 * 
 * @author iffa
 * @author Pandarr
 * @author Sammy
 * @author kitskub
 */
public class SpaceConfig {
    // Variables
    private static Map<ConfigFile, YamlConfiguration> config = new EnumMap<ConfigFile, YamlConfiguration>(ConfigFile.class);
    private static Map<ConfigFile, File> fileMap = new EnumMap<ConfigFile, File>(ConfigFile.class);
    private static Map<ConfigFile, Boolean> loaded = new EnumMap<ConfigFile, Boolean>(ConfigFile.class);

    /**
     * Gets the configuration file.
     * 
     * @param configfile ConfigFile to get
     * 
     * @return YamlConfiguration object
     */
    public static YamlConfiguration getConfig(ConfigFile configfile) {
        if (loaded.get(configfile) == null || !loaded.get(configfile)) {
            loadConfig(configfile);
        }
        return config.get(configfile);
    }

    /**
     * Gets the configuration file.
     * 
     * @param configfile ConfigFile to get
     * 
     * @return Configuration file
     */
    public static File getConfigFile(ConfigFile configfile) {
        return fileMap.get(configfile);
    }

    /**
     * Checks if the configuration file is loaded.
     * 
     * @param configfile ConfigFile to get
     * 
     * @return True if configuraton file is loaded
     */
    public static boolean getLoaded(ConfigFile configfile) {
        return loaded.get(configfile);
    }

    /**
     * Loads all configuration files. (can be used to save a total of 2 lines!)
     */
    public static void loadConfigs() {
        // Since this is usually only going to be called once:
        SpaceSchematicHandler.schematicFolder.mkdir();
        for (ConfigFile configfile : ConfigFile.values()) {
            loadConfig(configfile);
        }
    }

    /**
     * Loads the configuration file from the .jar.
     * 
     * @param configFile ConfigFile to load
     */
    public static void loadConfig(ConfigFile configFile) {
        fileMap.put(configFile, new File(Bukkit.getServer().getPluginManager().getPlugin("CrystalSpace").getDataFolder(), configFile.getNameWithLocation()));
        if (fileMap.get(configFile).exists()) {
            config.put(configFile, new YamlConfiguration());
            try {
                config.get(configFile).load(fileMap.get(configFile));
            } catch (FileNotFoundException ex) {
                MessageHandler.print(Level.WARNING, ex.getMessage());
                loaded.put(configFile, false);
                return;
            } catch (IOException ex) {
                MessageHandler.print(Level.WARNING, ex.getMessage());
                loaded.put(configFile, false);
                return;
            } catch (InvalidConfigurationException ex) {
                MessageHandler.print(Level.WARNING, ex.getMessage());
                loaded.put(configFile, false);
                return;
            }
            loaded.put(configFile, true);
        } else {
            try {
                // Making schematics folder
                File makeSchematics = new File(Bukkit.getServer().getPluginManager().getPlugin("CrystalSpace").getDataFolder() + File.separator + "schematics");
                if (!makeSchematics.exists()) {
                    makeSchematics.mkdirs();
                }
                
                Bukkit.getServer().getPluginManager().getPlugin("CrystalSpace").getDataFolder().mkdir();
                Bukkit.getServer().getPluginManager().getPlugin("CrystalSpace").getDataFolder().mkdir();
                fileMap.get(configFile).getParentFile().mkdir();
                if(!fileMap.get(configFile).exists()) fileMap.get(configFile).createNewFile();
                InputStream jarURL = SpaceConfig.class.getResourceAsStream("/" + configFile.getName());
                copyFile(jarURL, fileMap.get(configFile));
                config.put(configFile, new YamlConfiguration());
                config.get(configFile).load(fileMap.get(configFile));
                loaded.put(configFile, true);
                MessageHandler.print(Level.INFO, LangHandler.getConfigLoadedMessage(configFile));
            } catch (Exception e) {
                e.printStackTrace();
                //MessageHandler.print(Level.SEVERE, e.toString());
            }
        }
    }

    /**
     * Copies a file to a new location.
     * 
     * @param in InputStream
     * @param out File
     * 
     * @throws Exception
     */
    private static void copyFile(InputStream in, File out) throws Exception {
        InputStream fis = in;
        FileOutputStream fos = new FileOutputStream(out);
        try {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * Constructor of SpaceConfig.
     */
    private SpaceConfig() {
    }

    /**
     * All config files.
     */
    public enum ConfigFile {
        // Enums

        DEFAULT_PLANETS("planets.yml", "planets/planets.yml"),
        IDS("worlds.yml", "worlds.yml"),
        LANG("lang.yml", "lang.yml");
        // Variables
        private String name;
        private String location;

        /**
         * Constructor of ConfigFile.
         * @param file 
         */
        ConfigFile(String file, String location) {
            this.name = file;
            this.location = location;
        }

        /**
         * Gets the file associated with the enum.
         * 
         * @return File associated with the enum
         */
        public String getName() {
            return name;
        }
        
        /**
         * Gets the location associated with the enum.
         * 
         * @return File associated with the enum
         */
        public String getNameWithLocation() {
            return location;
        }
    }

    /**
     * Default config values.
     * 
     * @author Jack
     * @author iffa
     */
    public enum Defaults {
        DEBUGGING(false),
        ASTEROIDS_ENABLED(true),
        GLOWSTONE_CHANCE(1),
        STONE_CHANCE(3),
        SATELLITES_ENABLED(true),
        SATELLITE_CHANCE(1),
        GENERATE_PLANETS(true),
        GENERATE_SCHEMATICS(true),
        SCHEMATIC_CHANCE(5),
        // ConfigFile.DEFAULT_PLANETS
        DENSITY(15000),
        MIN_DISTANCE(10),
        MIN_SIZE(4),
        MAX_SIZE(20),
        MAX_SHELL_SIZE(5),
        MIN_SHELL_SIZE(3),
        FLOOR_HEIGHT(0),
        FLOOR_BLOCK("STATIONARY_WATER"),
        BEDROCK_ENABLED(true),
        IGNORE_INVALID_BLOCK_IDS(true);
        // Variables
        private Object value;

        /**
         * Constructor of Defaults.
         * 
         * @param def Boolean
         */
        Defaults(Object def) {
            this.value = def;
        }

        /**
         * Constructor of Defaults.
         * 
         * @param def Integer
         */
        Defaults(int def) {
            this.value = def;
        }

        /**
         * Constructor of Defaults.
         * 
         * @param def String
         */
        Defaults(String def) {
            this.value = def;
        }

        /**
         * Gets the default value.
         * 
         * @return Default value
         */
        public Object getDefault() {
            return value;
        }
    }
}