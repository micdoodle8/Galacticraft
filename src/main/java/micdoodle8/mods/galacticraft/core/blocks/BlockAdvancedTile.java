package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
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
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
    	if (this.hasTileEntity(metadata))
    	{
    		TileEntity tileNew = world.getTileEntity(x,  y,  z);
    		if (tileNew != null)
    		{
		    	this.dropEntireInventory(world, x, y, z, block, metadata);
    			tileNew.invalidate();
    		}
    	}
    }

    @Override
    public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
        TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
        return tileentity != null && tileentity.receiveClientEvent(par5, par6);
    }

    public void dropEntireInventory(World world, int x, int y, int z, Block par5, int par6)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

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
                            EntityItem var12 = new EntityItem(world, x + var8, y + var9, z + var10, new ItemStack(var7.getItem(), var11, var7.getItemDamage()));

                            if (var7.hasTagCompound())
                            {
                                var12.getEntityItem().setTagCompound((NBTTagCompound) var7.getTagCompound().copy());
                            }

                            float var13 = 0.05F;
                            var12.motionX = (float) random.nextGaussian() * var13;
                            var12.motionY = (float) random.nextGaussian() * var13 + 0.2F;
                            var12.motionZ = (float) random.nextGaussian() * var13;
                            world.spawnEntityInWorld(var12);
                        }
                    }
                }
            }
        }
    }
}
