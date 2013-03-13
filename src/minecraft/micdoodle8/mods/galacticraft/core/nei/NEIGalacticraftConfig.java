package micdoodle8.mods.galacticraft.core.nei;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;

public class NEIGalacticraftConfig implements IConfigureNEI
{
	@Override
	public void loadConfig() 
	{
		API.hideItems(GalacticraftCore.hiddenItems);
	}

	@Override
	public String getName() 
	{
		return "Galacticraft NEI Plugin";
	}

	@Override
	public String getVersion() 
	{
		return GalacticraftCore.LOCALMAJVERSION + "." + GalacticraftCore.LOCALMINVERSION + "." + GalacticraftCore.LOCALBUILDVERSION;
	}
}
