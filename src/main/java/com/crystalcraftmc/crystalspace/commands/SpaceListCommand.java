/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.crystalcraftmc.crystalspace.commands;

import com.crystalcraftmc.crystalspace.Space;
import com.crystalcraftmc.crystalspace.handlers.LangHandler;
import com.crystalcraftmc.crystalspace.handlers.MessageHandler;
import com.crystalcraftmc.crystalspace.handlers.PlayerHandler;
import com.crystalcraftmc.crystalspace.handlers.WorldHandler;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Represents "/space list".
 * 
 * @author iffa
 */
public class SpaceListCommand extends SpaceCommand {
    /**
     * Constructor of SpaceListCommand.
     * 
     * @param plugin CrystalSpace instance
     * @param sender Command sender
     * @param args Command arguments
     */
    public SpaceListCommand(Space plugin, CommandSender sender, String[] args) {
        super(plugin, sender, args);
    }

    /**
     * Does the command.
     */
    @Override
    public void command() {
        if (!PlayerHandler.hasPermission("CrystalSpace.teleport.list", (Player) this.getSender())) {
            MessageHandler.sendNoPermissionMessage((Player) getSender());
            return;
        }
        if (WorldHandler.getSpaceWorlds().isEmpty()) {
            getSender().sendMessage(NamedTextColor.RED + LangHandler.getNoSpaceLoaded());
            return;
        }
        getSender().sendMessage(NamedTextColor.GOLD + Space.getPrefix() + " " + LangHandler.getListOfSpaceMessage());
        List<String> spaceWorlds = new ArrayList<String>();
        for (World world : WorldHandler.getSpaceWorlds()) {
            if (world == null) {
                MessageHandler.debugPrint(Level.SEVERE, "world is null in SpaceListCommand! :(");
                continue;
            }
            spaceWorlds.add(world.getName());
        }
        getSender().sendMessage(NamedTextColor.GRAY + spaceWorlds.toString().replace("]", "").replace("[", ""));
    }
}
