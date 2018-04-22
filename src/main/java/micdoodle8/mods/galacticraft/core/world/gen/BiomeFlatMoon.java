package micdoodle8.mods.galacticraft.core.world.gen;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeFlatMoon extends BiomeMoon
{
    public BiomeFlatMoon(BiomeProperties properties)
    {
        super(properties);
    }

    public void registerTypes(Biome b)
    {
        BiomeDictionary.addTypes(b, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD);
    }
}
