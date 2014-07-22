package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockAdvancedTile;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityTelepadFake;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockTelepadFake extends BlockAdvancedTile implements ITileEntityProvider
{
	public BlockTelepadFake(String assetName)
	{
		super(GCBlocks.machine);
		this.setStepSound(Block.soundTypeMetal);
		this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
		this.setBlockName(assetName);
        this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + "launch_pad");
		this.setResistance(1000000000000000.0F);
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

		if (meta == 0)
		{
			this.setBlockBounds(0.0F, 0.55F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
        else if (meta == 1)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.38F, 1.0F);
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

		if (meta == 0)
		{
			this.setBlockBounds(0.0F, 0.55F, 0.0F, 1.0F, 1.0F, 1.0F);
			super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
		}
        else if (meta == 1)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.38F, 1.0F);
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

	public void makeFakeBlock(World worldObj, BlockVec3 position, BlockVec3 mainBlock, int meta)
	{
		worldObj.setBlock(position.x, position.y, position.z, this, meta, 3);
		((TileEntityTelepadFake) worldObj.getTileEntity(position.x, position.y, position.z)).setMainBlock(mainBlock);
	}

	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
        TileEntity tileEntity = par1World.getTileEntity(par2, par3, par4);

        if (tileEntity != null)
        {
            BlockVec3 mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null)
            {
                return mainBlockPosition.getBlock(par1World).getBlockHardness(par1World, par2, par3, par4);
            }
        }

		return this.blockHardness;
	}

	@Override
	public Block setBlockTextureName(String name)
	{
		this.textureName = name;
		return this;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (tileEntity instanceof TileEntityTelepadFake)
		{
			((TileEntityTelepadFake) tileEntity).onBlockRemoval();
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		TileEntityTelepadFake tileEntity = (TileEntityTelepadFake) par1World.getTileEntity(x, y, z);
		return tileEntity.onBlockActivated(par1World, x, y, z, par5EntityPlayer);
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int meta)
	{
		return new TileEntityTelepadFake();
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World par1World, int x, int y, int z)
	{
		TileEntity tileEntity = par1World.getTileEntity(x, y, z);
		BlockVec3 mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

		if (mainBlockPosition != null)
		{
			Block mainBlockID = par1World.getBlock(mainBlockPosition.x, mainBlockPosition.y, mainBlockPosition.z);

			if (mainBlockID != Blocks.air)
			{
				return mainBlockID.getPickBlock(target, par1World, mainBlockPosition.x, mainBlockPosition.y, mainBlockPosition.z);
			}
		}

		return null;
	}

	@Override
	public int getBedDirection(IBlockAccess world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		BlockVec3 mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

		if (mainBlockPosition != null)
		{
			return mainBlockPosition.getBlock(world).getBedDirection(world, mainBlockPosition.x, mainBlockPosition.y, mainBlockPosition.z);
		}

		return BlockDirectional.getDirection(world.getBlockMetadata(x, y, z));
	}

	@Override
	public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		BlockVec3 mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

		if (mainBlockPosition != null)
		{
			return mainBlockPosition.getBlock(world).isBed(world, mainBlockPosition.x, mainBlockPosition.y, mainBlockPosition.z, player);
		}

		return super.isBed(world, x, y, z, player);
	}

	@Override
	public void setBedOccupied(IBlockAccess world, int x, int y, int z, EntityPlayer player, boolean occupied)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		BlockVec3 mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

		if (mainBlockPosition != null)
		{
			mainBlockPosition.getBlock(world).setBedOccupied(world, mainBlockPosition.x, mainBlockPosition.y, mainBlockPosition.z, player, occupied);
		}
		else
		{
			super.setBedOccupied(world, x, y, z, player, occupied);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer)
	{
		if (worldObj.getBlockMetadata(target.blockX, target.blockY, target.blockZ) == 6)
		{
			return true;
		}

		TileEntity tileEntity = worldObj.getTileEntity(target.blockX, target.blockY, target.blockZ);

		if (tileEntity instanceof TileEntityTelepadFake)
		{
			BlockVec3 mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

			if (mainBlockPosition != null)
			{
				effectRenderer.addBlockHitEffects(mainBlockPosition.x, mainBlockPosition.y, mainBlockPosition.z, target);
			}
		}

		return super.addHitEffects(worldObj, target, effectRenderer);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
	{
		if (world.getBlockMetadata(x, y, z) == 6)
		{
			return true;
		}

		return super.addDestroyEffects(world, x, y, z, meta, effectRenderer);
	}
}