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
import com.atkoliver.duzyspace.handlers.WorldHandler;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

/**
 * Represents "/space about".
 * 
 * @author iffa
 * @author jflory7
 */
public class SpaceAboutCommand extends SpaceCommand {
    /**
     * Constructor of SpaceAboutCommand.
     * 
     * @param plugin DuzySpace instance
     * @param sender the command sender
     * @param args command arguments
     */
    public SpaceAboutCommand(Space plugin, CommandSender sender, String[] args) {
        super(plugin, sender, args);
    }

    /**
     * Executes <code>/space about</code> command.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void command() {
        if (getArgs().length < 2) {
            getSender().sendMessage(NamedTextColor.GOLD + "About:");
            getSender().sendMessage(NamedTextColor.GOLD + "-" + NamedTextColor.GRAY + " You're running version " +
                    NamedTextColor.GOLD + getPlugin().getDescription().getVersion());
            getSender().sendMessage(NamedTextColor.GOLD + "-" + NamedTextColor.GRAY + " There are currently " +
                    NamedTextColor.GOLD + WorldHandler.getSpaceWorlds().size() + NamedTextColor.GRAY + " space worlds loaded.");
        } else if (getArgs().length < 3 && getArgs()[1].equals("developers")) {
            getSender().sendMessage(NamedTextColor.GOLD + "-" + NamedTextColor.GRAY + " Core Developers:");
            getSender().sendMessage(NamedTextColor.GOLD + "    jflory7, iffa");
        }
    }
}
