package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalaxy;
import net.minecraft.util.StatCollector;

/**
 * GCCoreGalaxyBlockyWay.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreGalaxyBlockyWay implements IGalaxy
{
	@Override
	public String getGalaxyName()
	{
		return StatCollector.translateToLocal("galaxy.blockyway.name");
	}

	@Override
	public int getXCoord()
	{
		return 0;
	}

	@Override
	public int getYCoord()
	{
		return 0;
	}

	@Override
	public Vector3 getRGBRingColors()
	{
		return new Vector3(50.0D / 256.0D, 60.0D / 256.0D, 256.0D / 256.0D);
	}
}
