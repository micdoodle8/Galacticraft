package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.api.world.ICelestialBodyRenderer;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class CommonProxyCore
{
    public void preInit(FMLPreInitializationEvent event)
    {
        ;
    }

    public void init(FMLInitializationEvent event)
    {
        ;
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        ;
    }

    public void registerRenderInformation()
    {
        ;
    }

    public int getBlockRenderID(int blockID)
    {
        return -1;
    }

    public int getTitaniumArmorRenderIndex()
    {
        return 0;
    }

    public int getSensorArmorRenderIndex()
    {
        return 0;
    }

    public World getClientWorld()
    {
        return null;
    }

    public void addSlotRenderer(ICelestialBodyRenderer slotRenderer)
    {
        ;
    }

    public void spawnParticle(String particleID, Vector3 position, Vector3 motion)
    {
        ;
    }

    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Vector3 color)
    {
        ;
    }
}
