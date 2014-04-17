package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockMulti.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockMulti extends BlockMulti implements IPartialSealableBlock
{
	Icon[] fakeIcons;

	public GCCoreBlockMulti(int id, String assetName)
	{
		super(id);
		this.setStepSound(Block.soundMetalFootstep);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setUnlocalizedName(assetName);
		this.setResistance(1000000000000000.0F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.fakeIcons = new Icon[4];
		this.fakeIcons[0] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "launch_pad");
		this.fakeIcons[1] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "workbench_nasa_top");
		this.fakeIcons[2] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "solar_basic_0");

		if (Loader.isModLoaded("GalacticraftMars"))
		{
			try
			{
				Class<?> c = Class.forName("micdoodle8.mods.galacticraft.mars.GalacticraftMars");
				String texturePrefix = (String) c.getField("TEXTURE_PREFIX").get(null);
				this.fakeIcons[3] = par1IconRegister.registerIcon(texturePrefix + "cryoDummy");
			}
			catch (Exception e)
			{
				this.fakeIcons[3] = this.fakeIcons[2];
				e.printStackTrace();
			}
		}
		else
		{
			this.fakeIcons[3] = this.fakeIcons[2];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		switch (par2)
		{
		case 2:
			return this.fakeIcons[0];
		case 3:
			return this.fakeIcons[1];
		case 4:
			return this.fakeIcons[2];
		case 5:
			return this.fakeIcons[3];
		default:
			return this.fakeIcons[0];
		}
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);

		if (meta == 2)
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
		}
		else
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List list, Entity entity)
	{
		int meta = world.getBlockMetadata(x, y, z);

		if (meta == 2)
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
			super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
		}
		else
		{
			super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public boolean canDropFromExplosion(Explosion par1Explosion)
	{
		return false;
	}

	@Override
	public void makeFakeBlock(World worldObj, Vector3 position, Vector3 mainBlock)
	{
		this.makeFakeBlock(worldObj, position, mainBlock, 0);
	}

	public void makeFakeBlock(World worldObj, Vector3 position, Vector3 mainBlock, int meta)
	{
		worldObj.setBlock(position.intX(), position.intY(), position.intZ(), this.blockID, meta, 3);
		((TileEntityMulti) worldObj.getBlockTileEntity(position.intX(), position.intY(), position.intZ())).setMainBlock(mainBlock);
	}

	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
		return par1World.getBlockMetadata(par2, par3, par4) == 1 ? -1.0F : this.blockHardness;
	}

	@Override
	public boolean isSealed(World world, int x, int y, int z, ForgeDirection direction)
	{
		int metadata = world.getBlockMetadata(x, y, z);

		if (metadata == 4)
		{
			return direction == ForgeDirection.UP;
		}

		return true;
	}
}
