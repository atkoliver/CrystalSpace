/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

/*
 */
package com.atkoliver.duzyspace.api;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author kitskub
 */
public class SpaceAddonHandler {
    
    public static Set<SpaceAddon> addons = new HashSet<SpaceAddon>();
    
    public static void registerAddon(SpaceAddon addon){
        addons.add(addon);
    }
    
    protected SpaceAddonHandler(){}
    
}
