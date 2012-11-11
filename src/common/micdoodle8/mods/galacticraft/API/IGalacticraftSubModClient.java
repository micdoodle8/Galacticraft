package micdoodle8.mods.galacticraft.API;

import micdoodle8.mods.galacticraft.core.GCCoreLocalization;

public interface IGalacticraftSubModClient 
{
	public String getDimensionName();
	
	public GCCoreLocalization getLanguageFile();
	
	public String getPlanetSpriteDirectory();

	public void preLoad();
	
	public void load();
	
	public void postLoad();
}
