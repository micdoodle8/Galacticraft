package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.tile.ILockable;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
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

public abstract class BlockAdvancedTile extends BlockAdvanced implements ITileEntityProvider
{
    public BlockAdvancedTile(Material par3Material)
    {
        super(par3Material);
        this.isBlockContainer = true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return null;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        this.dropEntireInventory(worldIn, pos, state);
        super.breakBlock(worldIn, pos, state);
    }

    public void dropEntireInventory(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof ILockable)
        {
            ((ILockable)tileEntity).clearLockedInventory();
        }

        if (tileEntity != null)
        {
            if (tileEntity instanceof IInventory)
            {
                IInventory inventory = (IInventory) tileEntity;

                Random syncRandom = GCCoreUtil.getRandom(pos);

                for (int var6 = 0; var6 < inventory.getSizeInventory(); ++var6)
                {
                    ItemStack var7 = inventory.getStackInSlot(var6);

                    if (var7 != null)
                    {
                        float var8 = syncRandom.nextFloat() * 0.8F + 0.1F;
                        float var9 = syncRandom.nextFloat() * 0.8F + 0.1F;
                        float var10 = syncRandom.nextFloat() * 0.8F + 0.1F;

                        while (var7.stackSize > 0)
                        {
                            int var11 = syncRandom.nextInt(21) + 10;

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
                            var12.motionX = (float) syncRandom.nextGaussian() * var13;
                            var12.motionY = (float) syncRandom.nextGaussian() * var13 + 0.2F;
                            var12.motionZ = (float) syncRandom.nextGaussian() * var13;
                            worldIn.spawnEntityInWorld(var12);
                        }
                    }
                }
            }
        }
    }
}
