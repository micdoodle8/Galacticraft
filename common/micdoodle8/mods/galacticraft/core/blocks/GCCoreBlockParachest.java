package micdoodle8.mods.galacticraft.core.blocks;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import java.util.Random;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityParachest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GCCoreBlockParachest extends BlockChest
{
    private final Random random = new Random();

    protected GCCoreBlockParachest(int par1)
    {
        super(par1, 0);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        ;
    }

    @Override
    public void unifyAdjacentChests(World par1World, int par2, int par3, int par4)
    {
        ;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            IInventory iinventory = this.getInventory(par1World, par2, par3, par4);

            if (iinventory != null && par5EntityPlayer instanceof EntityPlayerMP)
            {
                par5EntityPlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiParachest, par1World, par2, par3, par4);
            }

            return true;
        }
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack)
    {
        ;
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        GCCoreTileEntityParachest tileentitychest = (GCCoreTileEntityParachest) par1World.getBlockTileEntity(par2, par3, par4);

        if (tileentitychest != null)
        {
            tileentitychest.updateContainingBlockInfo();
        }
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        int l = par1World.getBlockId(par2, par3, par4);
        Block block = Block.blocksList[l];
        return block == null || block.isBlockReplaceable(par1World, par2, par3, par4);
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        GCCoreTileEntityParachest tileentitychest = (GCCoreTileEntityParachest) par1World.getBlockTileEntity(par2, par3, par4);

        if (tileentitychest != null)
        {
            for (int j1 = 0; j1 < tileentitychest.getSizeInventory(); ++j1)
            {
                ItemStack itemstack = tileentitychest.getStackInSlot(j1);

                if (itemstack != null)
                {
                    float f = this.random.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld(entityitem))
                    {
                        int k1 = this.random.nextInt(21) + 10;

                        if (k1 > itemstack.stackSize)
                        {
                            k1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= k1;
                        entityitem = new EntityItem(par1World, par2 + f, par3 + f1, par4 + f2, new ItemStack(itemstack.itemID, k1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (float) this.random.nextGaussian() * f3;
                        entityitem.motionY = (float) this.random.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float) this.random.nextGaussian() * f3;

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }
                    }
                }
            }

            par1World.func_96440_m(par2, par3, par4, par5);
        }

        par1World.removeBlockTileEntity(par2, par3, par4);
    }

    @Override
    public IInventory getInventory(World par1World, int par2, int par3, int par4)
    {
        Object object = par1World.getBlockTileEntity(par2, par3, par4);

        if (object == null)
        {
            return null;
        }
        else if (par1World.isBlockSolidOnSide(par2, par3 + 1, par4, DOWN))
        {
            return null;
        }
        else if (BlockChest.isOcelotBlockingChest(par1World, par2, par3, par4))
        {
            return null;
        }
        else if (par1World.getBlockId(par2 - 1, par3, par4) == this.blockID && (par1World.isBlockSolidOnSide(par2 - 1, par3 + 1, par4, DOWN) || BlockChest.isOcelotBlockingChest(par1World, par2 - 1, par3, par4)))
        {
            return null;
        }
        else if (par1World.getBlockId(par2 + 1, par3, par4) == this.blockID && (par1World.isBlockSolidOnSide(par2 + 1, par3 + 1, par4, DOWN) || BlockChest.isOcelotBlockingChest(par1World, par2 + 1, par3, par4)))
        {
            return null;
        }
        else if (par1World.getBlockId(par2, par3, par4 - 1) == this.blockID && (par1World.isBlockSolidOnSide(par2, par3 + 1, par4 - 1, DOWN) || BlockChest.isOcelotBlockingChest(par1World, par2, par3, par4 - 1)))
        {
            return null;
        }
        else if (par1World.getBlockId(par2, par3, par4 + 1) == this.blockID && (par1World.isBlockSolidOnSide(par2, par3 + 1, par4 + 1, DOWN) || BlockChest.isOcelotBlockingChest(par1World, par2, par3, par4 + 1)))
        {
            return null;
        }
        else
        {
            return (IInventory) object;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World par1World)
    {
        return new GCCoreTileEntityParachest();
    }

    @Override
    public boolean canProvidePower()
    {
        return false;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return 0;
    }
}
