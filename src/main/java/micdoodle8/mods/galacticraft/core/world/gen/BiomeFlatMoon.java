package micdoodle8.mods.galacticraft.core.world.gen;

import net.minecraftforge.common.BiomeDictionary;

public class BiomeFlatMoon extends BiomeMoon
{
    public BiomeFlatMoon(BiomeProperties properties)
    {
        super(properties);
    }

    public void registerTypes()
    {
        BiomeDictionary.addTypes(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD);
    }
}
