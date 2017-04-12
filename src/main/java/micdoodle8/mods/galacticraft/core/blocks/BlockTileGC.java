package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * An advanced block class that is to be extended for wrenching capabilities.
 */
public abstract class BlockTileGC extends BlockAdvanced implements ITileEntityProvider
{
    public BlockTileGC(Material material)
    {
        super(material);
        this.isBlockContainer = true;
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an
     * update, as appropriate
     * 
     *   Note: breakBlock is called when placing blocks
     *   getTileEntity() at this point will give the NEW block's tileEntity, if the old block's tile is already invalid
     *   so: do NOT invalidate old tileEntities before breaking blocks 
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (worldIn.getTileEntity(pos) instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) worldIn.getTileEntity(pos));
        }
        super.breakBlock(worldIn, pos, state);
    }

    /**
     * Called when the block receives a BlockEvent - see World.addBlockEvent. By
     * default, passes it on to the tile entity at this location. Args: world,
     * x, y, z, blockID, EventID, event parameter
     */
    @Override
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
    {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
    }

    /**
     * Returns the TileEntity used by this block. You should use the metadata
     * sensitive version of this to get the maximum optimization!
     */
    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return null;
    }

}
