/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.atkoliver.duzyspace.commands;

import com.atkoliver.duzyspace.Space;
import com.atkoliver.duzyspace.handlers.LangHandler;
import com.atkoliver.duzyspace.handlers.MessageHandler;
import com.atkoliver.duzyspace.handlers.PlayerHandler;
import com.atkoliver.duzyspace.handlers.WorldHandler;
import org.bukkit.Bukkit;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Represents "/space back".
 * 
 * @author iffa
 * @author jflory7
 */
public class SpaceExitCommand extends SpaceCommand {
    // Variables
    public static Map<Player, Location> enterDest = new HashMap<Player, Location>();

    /**
     * Constructor of SpaceExitCommand.
     * 
     * @param plugin DuzySpace instance
     * @param sender Command sender
     * @param args Command arguments
     */
    public SpaceExitCommand(Space plugin, CommandSender sender, String[] args) {
        super(plugin, sender, args);
    }

    /**
     * Does the command.
     */
    @Override
    public void command() {
        Player player = (Player) getSender();
        if (WorldHandler.isInAnySpace(player)) {
            if (PlayerHandler.hasPermission("DuzySpace.teleport.exit", player)) {
                enterDest.put(player, player.getLocation());
                Location location;
                if (SpaceEnterCommand.exitDest.containsKey(player)) {
                    location = SpaceEnterCommand.exitDest.get(player);
                    MessageHandler.debugPrint(Level.INFO, "Teleported player '" + player.getName() + "' out of space.");
                    player.teleport(location);
                    return;
                } else {
                    SpaceEnterCommand.exitDest.put(player, Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
                    getSender().sendMessage(NamedTextColor.RED + LangHandler.getNoExitFoundMessage(1));
                    getSender().sendMessage(NamedTextColor.RED + LangHandler.getNoExitFoundMessage(2));
                    return;
                }
            } else {
                MessageHandler.sendNoPermissionMessage(player);
                return;
            }
        } else {
            player.sendMessage(NamedTextColor.RED + LangHandler.getNotInSpaceMessage());
            return;
        }
    }
}