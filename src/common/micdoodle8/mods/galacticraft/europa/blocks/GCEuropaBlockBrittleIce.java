package micdoodle8.mods.galacticraft.europa.blocks;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockBreakable;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.StatList;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class GCEuropaBlockBrittleIce extends BlockBreakable
{
    public GCEuropaBlockBrittleIce(int par1, int par2)
    {
        super(par1, par2, Material.ice, false);
        this.slipperiness = 0.7F;
        this.setTickRandomly(true);
    }

    @Override
	@SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }

	@Override
    public int getBlockTextureFromSideAndMetadata(int side, int par2)
    {
		return side == 1 ? 1 : side == 0 ? 3 : 0;
    }
    
	@Override
	public int getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int side) 
	{
		return side == 1 ? 1 : side == 0 ? 3 : 0;
	}

    @Override
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, 1 - par5);
    }

    @Override
	public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
    {
        par2EntityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        par2EntityPlayer.addExhaustion(0.025F);

        if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(par2EntityPlayer))
        {
            final ItemStack var9 = this.createStackedBlock(par6);

            if (var9 != null)
            {
                this.dropBlockAsItem_do(par1World, par3, par4, par5, var9);
            }
        }
        else
        {
            if (par1World.provider.isHellWorld)
            {
                par1World.setBlockWithNotify(par3, par4, par5, 0);
                return;
            }

            final int var7 = EnchantmentHelper.getFortuneModifier(par2EntityPlayer);
            this.dropBlockAsItem(par1World, par3, par4, par5, par6, var7);
            final Material var8 = par1World.getBlockMaterial(par3, par4 - 1, par5);

            if (var8.blocksMovement() || var8.isLiquid())
            {
                par1World.setBlockWithNotify(par3, par4, par5, Block.waterMoving.blockID);
            }
        }
    }

    @Override
	public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par1World.getSavedLightValue(EnumSkyBlock.Block, par2, par3, par4) > 20 - Block.lightOpacity[this.blockID])
        {
            if (par1World.provider.isHellWorld)
            {
                par1World.setBlockWithNotify(par2, par3, par4, 0);
                return;
            }

            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockWithNotify(par2, par3, par4, Block.waterStill.blockID);
        }
    }
    
    @Override
	public int getMobilityFlag()
    {
        return 0;
    }
    
    @Override
    public String getTextureFile()
    {
		return "/micdoodle8/mods/galacticraft/europa/client/blocks/europa.png";
    }
}
