package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenFlatMars extends BiomeMars
{
    public BiomeGenFlatMars(BiomeProperties properties)
    {
        super(properties);
    }
    
    public void registerTypes()
    {
        BiomeDictionary.addTypes(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
    }
}
