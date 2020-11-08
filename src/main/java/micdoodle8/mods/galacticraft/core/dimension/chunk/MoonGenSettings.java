package micdoodle8.mods.galacticraft.core.dimension.chunk;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.GenerationSettings;

public class MoonGenSettings extends GenerationSettings
{
    public MoonGenSettings()
    {
        this.defaultBlock = GCBlocks.moonStone.getDefaultState();
        this.defaultFluid = Blocks.AIR.getDefaultState();
    }

    public int getBiomeSize()
    {
        return 4;
    }

    public int getRiverSize()
    {
        return 4;
    }

    public int getBiomeId()
    {
        return -1;
    }

    @Override
    public int getBedrockFloorHeight()
    {
        return 0;
    }

    public int getHomeTreeDistance()
    {
        return 20;
    }

    public int getHomeTreeSeparation()
    {
        return 4;
    }
}