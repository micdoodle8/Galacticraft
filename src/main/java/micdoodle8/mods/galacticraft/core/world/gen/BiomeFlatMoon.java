package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeFlatMoon extends BiomeMoon
{
    public BiomeFlatMoon(BiomeProperties properties)
    {
        super(properties);
        if (!ConfigManagerCore.disableBiomeTypeRegistrations)
        {
            BiomeDictionary.addTypes(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD);
        }
    }
}
