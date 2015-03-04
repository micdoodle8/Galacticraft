package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

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
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        this.dropEntireInventory(worldIn, pos, state);
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
     * Override this if you don't need it. This will eject all items out of this
     * machine if it has an inventory.
     */
    public void dropEntireInventory(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity != null)
        {
            if (tileEntity instanceof IInventory)
            {
                IInventory inventory = (IInventory) tileEntity;

                for (int var6 = 0; var6 < inventory.getSizeInventory(); ++var6)
                {
                    ItemStack var7 = inventory.getStackInSlot(var6);

                    if (var7 != null)
                    {
                        Random random = new Random();
                        float var8 = random.nextFloat() * 0.8F + 0.1F;
                        float var9 = random.nextFloat() * 0.8F + 0.1F;
                        float var10 = random.nextFloat() * 0.8F + 0.1F;

                        while (var7.stackSize > 0)
                        {
                            int var11 = random.nextInt(21) + 10;

                            if (var11 > var7.stackSize)
                            {
                                var11 = var7.stackSize;
                            }

                            var7.stackSize -= var11;
                            EntityItem var12 = new EntityItem(worldIn, pos.getX() + var8, pos.getY() + var9, pos.getZ() + var10, new ItemStack(var7.getItem(), var11, var7.getItemDamage()));

                            if (var7.hasTagCompound())
                            {
                                var12.getEntityItem().setTagCompound((NBTTagCompound) var7.getTagCompound().copy());
                            }

                            float var13 = 0.05F;
                            var12.motionX = (float) random.nextGaussian() * var13;
                            var12.motionY = (float) random.nextGaussian() * var13 + 0.2F;
                            var12.motionZ = (float) random.nextGaussian() * var13;
                            worldIn.spawnEntityInWorld(var12);
                        }
                    }
                }
            }
        }
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
