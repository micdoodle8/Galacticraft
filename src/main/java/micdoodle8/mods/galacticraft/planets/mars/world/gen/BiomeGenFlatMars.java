package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenFlatMars extends BiomeMars
{
    public BiomeGenFlatMars(BiomeProperties properties)
    {
        super(properties);
    }
    
    @Override
    public void registerTypes(Biome b)
    {
        BiomeDictionary.addTypes(b, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
    }
}
