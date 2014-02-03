package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFallenMeteor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * GCCoreBlockFallenMeteor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockFallenMeteor extends Block implements ITileEntityProvider
{
	public GCCoreBlockFallenMeteor(int id, String assetName)
	{
		super(id, Material.rock);
		this.setBlockBounds(0.2F, 0.2F, 0.2F, 0.8F, 0.8F, 0.8F);
		this.setHardness(50.0F);
		this.setStepSound(Block.soundStoneFootstep);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setUnlocalizedName(assetName);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "fallen_meteor");
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getBlockRenderID(this.blockID);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public int quantityDroppedWithBonus(int par1, Random par2Random)
	{
		return 1;
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return GCCoreItems.meteoricIronRaw.itemID;
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
	{
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);

		if (tile instanceof GCCoreTileEntityFallenMeteor)
		{
			GCCoreTileEntityFallenMeteor meteor = (GCCoreTileEntityFallenMeteor) tile;

			if (meteor.getHeatLevel() <= 0)
			{
				return;
			}

			if (par5Entity instanceof EntityLivingBase)
			{
				final EntityLivingBase livingEntity = (EntityLivingBase) par5Entity;

				par1World.playSoundEffect(par2 + 0.5F, par3 + 0.5F, par4 + 0.5F, "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);

				for (int var5 = 0; var5 < 8; ++var5)
				{
					par1World.spawnParticle("largesmoke", par2 + Math.random(), par3 + 0.2D + Math.random(), par4 + Math.random(), 0.0D, 0.0D, 0.0D);
				}

				if (!livingEntity.isBurning())
				{
					livingEntity.setFire(2);
				}

				double var9 = par2 + 0.5F - livingEntity.posX;
				double var7;

				for (var7 = livingEntity.posZ - par4; var9 * var9 + var7 * var7 < 1.0E-4D; var7 = (Math.random() - Math.random()) * 0.01D)
				{
					var9 = (Math.random() - Math.random()) * 0.01D;
				}

				livingEntity.knockBack(livingEntity, 1, var9, var7);
			}
		}
	}

	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
	}

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
	{
		par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
	}

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		if (!par1World.isRemote)
		{
			this.tryToFall(par1World, par2, par3, par4);
		}
	}

	private void tryToFall(World par1World, int par2, int par3, int par4)
	{
		if (GCCoreBlockFallenMeteor.canFallBelow(par1World, par2, par3 - 1, par4) && par3 >= 0)
		{
			int prevHeatLevel = ((GCCoreTileEntityFallenMeteor) par1World.getBlockTileEntity(par2, par3, par4)).getHeatLevel();
			par1World.setBlock(par2, par3, par4, 0, 0, 3);

			while (GCCoreBlockFallenMeteor.canFallBelow(par1World, par2, par3 - 1, par4) && par3 > 0)
			{
				--par3;
			}

			if (par3 > 0)
			{
				par1World.setBlock(par2, par3, par4, this.blockID, 0, 3);
				((GCCoreTileEntityFallenMeteor) par1World.getBlockTileEntity(par2, par3, par4)).setHeatLevel(prevHeatLevel);
			}
		}
	}

	public static boolean canFallBelow(World par0World, int par1, int par2, int par3)
	{
		final int var4 = par0World.getBlockId(par1, par2, par3);

		if (var4 == 0)
		{
			return true;
		}
		else if (var4 == Block.fire.blockID)
		{
			return true;
		}
		else
		{
			final Material var5 = Block.blocksList[var4].blockMaterial;
			return var5 == Material.water ? true : var5 == Material.lava;
		}
	}

	@Override
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		TileEntity tile = par1IBlockAccess.getBlockTileEntity(par2, par3, par4);

		if (tile instanceof GCCoreTileEntityFallenMeteor)
		{
			GCCoreTileEntityFallenMeteor meteor = (GCCoreTileEntityFallenMeteor) tile;

			Vector3 col = new Vector3(198, 58, 108);
			col.translate(200 - meteor.getScaledHeatLevel() * 200);
			col.x = Math.min(255, col.x);
			col.y = Math.min(255, col.y);
			col.z = Math.min(255, col.z);

			return GCCoreUtil.convertTo32BitColor(255, (byte) col.x, (byte) col.y, (byte) col.z);
		}

		return super.colorMultiplier(par1IBlockAccess, par2, par3, par4);
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new GCCoreTileEntityFallenMeteor();
	}
}
