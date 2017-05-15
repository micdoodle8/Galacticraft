package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import net.minecraft.block.BlockNetherWart;

public class BlockSpaceWart extends BlockNetherWart
{
    public BlockSpaceWart(String assetName)
    {
        super();
        this.setTickRandomly(false);
        this.setUnlocalizedName(assetName);
    }
}
