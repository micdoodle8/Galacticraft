package micdoodle8.mods.galacticraft.asm;

import java.io.File;

public class GCCoreServerConfigurationManagerTransformer extends GalacticraftAccessTransformerBase
{
	public GCCoreServerConfigurationManagerTransformer()
	{
		this.setObfuscatedName("gu");
	}
	
	public File getLocation()
	{
		return GalacticraftPlugin.fileLocation;
	}
}
