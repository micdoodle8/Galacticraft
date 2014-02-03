package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCollector;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockOxygenCollector.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockOxygenCollector extends GCCoreBlockAdvancedTile
{
	@SideOnly(Side.CLIENT)
	private Icon[] collectorIcons;

	private Icon iconMachineSide;
	private Icon iconInput;
	private Icon iconOutput;

	public GCCoreBlockOxygenCollector(int id, String assetName)
	{
		super(id, Material.rock);
		this.setHardness(1.0F);
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
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getBlockRenderID(this.blockID);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.iconMachineSide = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_collector_fan");
		this.iconInput = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_oxygen_output");
		this.iconOutput = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "machine_input");
	}

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		final int metadata = par1World.getBlockMetadata(x, y, z);
		final int original = metadata;

		int change = 0;

		// Re-orient the block
		switch (original)
		{
		case 0:
			change = 3;
			break;
		case 3:
			change = 1;
			break;
		case 1:
			change = 2;
			break;
		case 2:
			change = 0;
			break;
		}

		par1World.setBlockMetadataWithNotify(x, y, z, change, 3);
		return true;
	}

	@Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		entityPlayer.openGui(GalacticraftCore.instance, -1, world, x, y, z);
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new GCCoreTileEntityOxygenCollector();
	}

	@Override
	public Icon getIcon(int side, int metadata)
	{
		if (side == metadata + 2)
		{
			return this.iconOutput;
		}
		else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
		{
			return this.iconInput;
		}

		return this.iconMachineSide;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		final int angle = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		int change = 0;

		switch (angle)
		{
		case 0:
			change = 3;
			break;
		case 1:
			change = 1;
			break;
		case 2:
			change = 2;
			break;
		case 3:
			change = 0;
			break;
		}

		world.setBlockMetadataWithNotify(x, y, z, change, 3);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(World par1World, int x, int y, int z, Random rand)
	{
		if (par1World.getBlockTileEntity(x, y, z) instanceof GCCoreTileEntityOxygenCollector)
		{
			if (((GCCoreTileEntityOxygenCollector) par1World.getBlockTileEntity(x, y, z)).lastOxygenCollected > 1)
			{
				for (int particleCount = 0; particleCount < 10; particleCount++)
				{
					double x2 = x + rand.nextFloat();
					double y2 = y + rand.nextFloat();
					double z2 = z + rand.nextFloat();
					double mX = 0.0D;
					double mY = 0.0D;
					double mZ = 0.0D;
					int dir = rand.nextInt(2) * 2 - 1;
					mX = (rand.nextFloat() - 0.5D) * 0.5D;
					mY = (rand.nextFloat() - 0.5D) * 0.5D;
					mZ = (rand.nextFloat() - 0.5D) * 0.5D;

					final int var2 = par1World.getBlockMetadata(x, y, z);

					if (var2 == 3 || var2 == 2)
					{
						x2 = x + 0.5D + 0.25D * dir;
						mX = rand.nextFloat() * 2.0F * dir;
					}
					else
					{
						z2 = z + 0.5D + 0.25D * dir;
						mZ = rand.nextFloat() * 2.0F * dir;
					}

					GalacticraftCore.proxy.spawnParticle("oxygen", new Vector3(x2, y2, z2), new Vector3(mX, mY, mZ), new Vector3(0.7D, 0.7D, 1.0D));
				}
			}
		}
	}
}
