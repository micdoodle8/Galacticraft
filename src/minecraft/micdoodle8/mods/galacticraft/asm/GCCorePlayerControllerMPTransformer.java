package micdoodle8.mods.galacticraft.asm;

import java.io.File;

public class GCCorePlayerControllerMPTransformer extends GalacticraftAccessTransformerBase
{
	public GCCorePlayerControllerMPTransformer()
	{
		this.setObfuscatedName("bds");
	}
	
	@Override
	public File getLocation()
	{
		return GalacticraftPlugin.fileLocation;
	}
}
