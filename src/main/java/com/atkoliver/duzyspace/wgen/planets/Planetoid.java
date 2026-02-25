/*
 * Copyright (c) 2016 CrystalCraftMC
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// Package Declaration
package com.atkoliver.duzyspace.wgen.planets;

import org.bukkit.Material;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Holder class for an individual planetoid.
 * 
 * @author Canis85
 * @author iffa
 */
public class Planetoid implements Serializable {
    // Variables
    private static final long serialVersionUID = 1L;
    public ArrayList<Material> coreBlkIds;
    public ArrayList<Material> shellBlkIds;
    public int shellThickness;
    public int radius;
    public int xPos;
    public int yPos;
    public int zPos;

    /**
     * Constructor of Planetoid.
     */
    public Planetoid() {
    }

    /**
     * Constructor of Planetoid.
     * 
     * @param coreID Core material
     * @param shellID Shell material
     * @param shellThick Shell thickness
     * @param radius Radius
     * @param x X-coord
     * @param y Y-coord
     * @param z Z-coord
     */
    public Planetoid(ArrayList<Material> coreID, ArrayList<Material> shellID, int shellThick, int radius, int x, int y, int z) {
        this.coreBlkIds = coreID;
        this.shellBlkIds = shellID;
        this.shellThickness = shellThick;
        this.radius = radius;
        this.xPos = x;
        this.yPos = y;
        this.zPos = z;
    }
}
