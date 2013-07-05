package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.API.world.IGalaxy;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GCCoreGalaxyBlockyWay implements IGalaxy
{
    @Override
    public String getGalaxyName()
    {
        return LanguageRegistry.instance().getStringLocalization("galaxy.blockyway.name");
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
