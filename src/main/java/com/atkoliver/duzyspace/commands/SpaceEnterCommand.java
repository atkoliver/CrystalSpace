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
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Represents "/space enter [spaceworld]".
 * 
 * @author iffa
 */
public class SpaceEnterCommand extends SpaceCommand {
    // Variables
    public static Map<Player, Location> exitDest = new HashMap<Player, Location>();

    /**
     * Constructor of SpaceEnterCommand.
     * 
     * @param plugin DuzySpace instance
     * @param sender Command sender
     * @param args Command arguments
     */
    public SpaceEnterCommand(Space plugin, CommandSender sender, String[] args) {
        super(plugin, sender, args);
    }

    /**
     * Does the command.
     */
    @Override
    public void command() {
        Player player = (Player) this.getSender();
        if (getArgs().length == 1) {
            if (PlayerHandler.hasPermission("DuzySpace.teleport.enter", player)) {
                if (WorldHandler.getSpaceWorlds().isEmpty()) {
                    player.sendMessage(NamedTextColor.RED + LangHandler.getNoSpaceLoaded());
                    return;
                }
                if (WorldHandler.getSpaceWorlds().get(0) == player.getWorld()) {
                    player.sendMessage(NamedTextColor.RED + LangHandler.getAlreadyInThatWorldMessage());
                    MessageHandler.debugPrint(Level.INFO, player.getName() + "tried to use /space enter, but he was already in that space world.");
                    return;
                }
                
                exitDest.put(player, player.getLocation());
                Location location;
                if (SpaceExitCommand.enterDest.containsKey(player)) {
                    location = SpaceExitCommand.enterDest.get(player);
                } else {
                    if (WorldHandler.getSpaceWorlds().get(0) == null) {
                        MessageHandler.debugPrint(Level.SEVERE, "Entry #0 in getSpaceWorlds() is null!");
                    }
                    location = WorldHandler.getSpaceWorlds().get(0).getSpawnLocation();
                }
                MessageHandler.debugPrint(Level.INFO, "Teleported player '" + player.getName() + "' to space.");
                player.teleport(location, TeleportCause.COMMAND);
                return;
            }
            MessageHandler.sendNoPermissionMessage(player);
            return;
        } else if (getArgs().length >= 2) {
            if (PlayerHandler.hasPermission("DuzySpace.teleport.enter", player)) {
                if (Bukkit.getServer().getWorld(getArgs()[1]) == null) {
                    player.sendMessage(NamedTextColor.RED + LangHandler.getWorldNotFoundMessage());
                    return;
                }
                if (!WorldHandler.isSpaceWorld(Bukkit.getServer().getWorld(this.getArgs()[1]))) {
                    player.sendMessage(NamedTextColor.RED + LangHandler.getWorldNotSpaceMessage());
                    return;
                }
                if (Bukkit.getServer().getWorld(getArgs()[1]) == player.getWorld()) {
                    player.sendMessage(NamedTextColor.RED + LangHandler.getAlreadyInThatWorldMessage());
                    return;
                }
                exitDest.put(player, player.getLocation());
                Location location;
                if (SpaceExitCommand.enterDest.containsKey(player)) {
                    location = SpaceExitCommand.enterDest.get(player);
                } else {
                    location = Bukkit.getServer().getWorld(getArgs()[1]).getSpawnLocation();
                }
                MessageHandler.debugPrint(Level.INFO, "Teleported player '" + player.getName() + "' to space.");
                player.teleport(location, TeleportCause.COMMAND);
                return;
            }
        }
        MessageHandler.sendNoPermissionMessage(player);
    }
}