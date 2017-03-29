package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenFlatMars extends BiomeMars
{
    public BiomeGenFlatMars(BiomeProperties properties)
    {
        super(properties);
        if (!ConfigManagerCore.disableBiomeTypeRegistrations)
        {
            BiomeDictionary.addTypes(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
        }
    }
}
