package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockT1TreasureChest;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityTreasureChestVenus;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTier3TreasureChest extends BlockT1TreasureChest
{
    public BlockTier3TreasureChest(String assetName)
    {
        super(assetName);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityTreasureChestVenus();
    }
}