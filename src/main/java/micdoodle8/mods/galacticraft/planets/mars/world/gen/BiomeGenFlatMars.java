package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenFlatMars extends BiomeGenBaseMars
{
    public BiomeGenFlatMars(int par1)
    {
        super(par1);
        this.setBiomeName("marsFlat");
        this.setColor(16711680);
        this.setHeight(new Height(2.5F, 0.4F));
        if (!ConfigManagerCore.disableBiomeTypeRegistrations)
        {
            BiomeDictionary.registerBiomeType(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
        }
    }
}
