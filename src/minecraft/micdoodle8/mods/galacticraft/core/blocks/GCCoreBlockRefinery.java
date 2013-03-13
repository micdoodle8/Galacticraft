package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockRefinery extends BlockContainer
{
    private final Random refineryRand = new Random();

    private static boolean keepRefineryInventory = false;

    protected GCCoreBlockRefinery(int par1, int blockIndexInTexture)
    {
        super(par1, Material.rock);
    }

    @Override
	public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
	public int getRenderType()
    {
        return GalacticraftCore.proxy.getGCRefineryRenderID();
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	final TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);

    	if (te instanceof GCCoreTileEntityRefinery)
    	{
    		final GCCoreTileEntityRefinery refinery = (GCCoreTileEntityRefinery) te;

            if (refinery.isCookin())
            {
                par1World.getBlockMetadata(par2, par3, par4);
                final float var7 = par2 + 0.5F;
                final float var8 = par3 + 1.1F;
                final float var9 = par4 + 0.5F;
                final float var10 = 0.0F;
                final float var11 = 0.0F;

                for (int i = -1; i <= 1; i++)
                {
                    for (int j = -1; j <= 1; j++)
                    {
                        par1World.spawnParticle("smoke", var7 + var11 + i * 0.2, var8, var9 + var10 + j * 0.2, 0.0D, 0.01D, 0.0D);
                        par1World.spawnParticle("flame", var7 + var11 + i * 0.1, var8 - 0.2, var9 + var10 + j * 0.1, 0.0D, 0.0001D, 0.0D);
                    }
                }
            }
    	}
    }

    @Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
    	par5EntityPlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiRefinery, par1World, par2, par3, par4);
    	return true;
    }

    @Override
	public TileEntity createNewTileEntity(World par1World)
    {
        return new GCCoreTileEntityRefinery();
    }

    @Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        final GCCoreTileEntityRefinery var7 = (GCCoreTileEntityRefinery)par1World.getBlockTileEntity(par2, par3, par4);

        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
            {
                final ItemStack var9 = var7.getStackInSlot(var8);

                if (var9 != null)
                {
                    final float var10 = this.refineryRand.nextFloat() * 0.8F + 0.1F;
                    final float var11 = this.refineryRand.nextFloat() * 0.8F + 0.1F;
                    final float var12 = this.refineryRand.nextFloat() * 0.8F + 0.1F;

                    while (var9.stackSize > 0)
                    {
                        int var13 = this.refineryRand.nextInt(21) + 10;

                        if (var13 > var9.stackSize)
                        {
                            var13 = var9.stackSize;
                        }

                        var9.stackSize -= var13;
                        final EntityItem var14 = new EntityItem(par1World, par2 + var10, par3 + var11, par4 + var12, new ItemStack(var9.itemID, var13, var9.getItemDamage()));

                        if (var9.hasTagCompound())
                        {
                            var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                        }

                        final float var15 = 0.05F;
                        var14.motionX = (float)this.refineryRand.nextGaussian() * var15;
                        var14.motionY = (float)this.refineryRand.nextGaussian() * var15 + 0.2F;
                        var14.motionZ = (float)this.refineryRand.nextGaussian() * var15;
                        par1World.spawnEntityInWorld(var14);
                    }
                }
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
}
