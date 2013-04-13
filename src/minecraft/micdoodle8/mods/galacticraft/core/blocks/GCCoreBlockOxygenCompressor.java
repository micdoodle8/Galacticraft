package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCompressor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.block.BlockAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreBlockOxygenCompressor extends BlockAdvanced
{
    private final Random distributorRand = new Random();

	private Icon iconMachineSide;
	private Icon iconCompressor1;
	private Icon iconCompressor2;
	private Icon iconInput;
	private Icon iconOutput;

    private static boolean keepDistributorInventory = false;

	public GCCoreBlockOxygenCompressor(int par1, boolean isActive)
	{
		super(par1, Material.rock);
	}

	@Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.iconMachineSide = par1IconRegister.registerIcon("galacticraftcore:machine_blank");
        this.iconCompressor1 = par1IconRegister.registerIcon("galacticraftcore:machine_compressor_1");
        this.iconCompressor2 = par1IconRegister.registerIcon("galacticraftcore:machine_compressor_2");
        this.iconInput = par1IconRegister.registerIcon("galacticraftcore:machine_power_input");
        this.iconOutput = par1IconRegister.registerIcon("galacticraftcore:machine_oxygen_input");
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
    	entityPlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiAirCompressor, world, x, y, z);
    	return true;
    }

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new GCCoreTileEntityOxygenCompressor();
	}

	@Override
	public Icon getIcon(int side, int metadata)
	{
		if (side == 0 || side == 1)
		{
			return this.iconMachineSide;
		}
		else if (side == metadata + 2)
		{
			return this.iconOutput;
		}
		else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
		{
			return this.iconInput;
		}
		else if (metadata == 0 && side == 5 || metadata == 3 && side == 3 || metadata == 1 && side == 4 || metadata == 2 && side == 2)
		{
			return this.iconCompressor2;
		}
		else
		{
			return this.iconCompressor1;
		}
    }

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack)
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
}
