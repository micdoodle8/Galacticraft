package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenFlatMoon extends BiomeMoon
{
    public BiomeGenFlatMoon(int par1)
    {
        super(par1);
        this.setColor(11111111);
        this.setHeight(new Height(1.5F, 0.4F));
        if (!ConfigManagerCore.disableBiomeTypeRegistrations)
        {
            BiomeDictionary.registerBiomeType(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD);
        }
    }
}
