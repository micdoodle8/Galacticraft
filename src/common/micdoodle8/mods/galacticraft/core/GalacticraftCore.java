package micdoodle8.mods.galacticraft.core;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.network.NetworkMod;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@Mod(name="Galacticraft Core", version="v1", useMetadata = true, modid = "GalacticraftCore")
@NetworkMod(channels = {"GalacticraftCore"}, clientSideRequired = true, serverSideRequired = false)
public class GalacticraftCore 
{
	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.client.CoreClientProxy", serverSide = "micdoodle8.mods.galacticraft.core.CoreCommonProxy")
	public static CoreCommonProxy proxy;
	
	@Instance("GalacticraftCore")
	public static GalacticraftCore instance;
	
}
