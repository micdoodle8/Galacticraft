package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldChunkManagerSpace;
import net.minecraft.world.biome.BiomeGenBase;

public class WorldChunkManagerMoon extends WorldChunkManagerSpace
{
    @Override
    public BiomeGenBase getBiome()
    {
        return BiomeGenBaseMoon.moonFlat;
    }
}
