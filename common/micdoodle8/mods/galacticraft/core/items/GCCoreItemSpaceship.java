package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityRocketT1;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreItemSpaceship.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreItemSpaceship extends Item implements IHoldableItem
{
	public GCCoreItemSpaceship(int id, String assetName)
	{
		super(id);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
		this.setTextureName("arrow");
		this.setUnlocalizedName(assetName);
	}

	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		int amountOfCorrectBlocks = 0;

		if (par3World.isRemote)
		{
			return false;
		}
		else
		{
			float centerX = -1;
			float centerY = -1;
			float centerZ = -1;

			for (int i = -1; i < 2; i++)
			{
				for (int j = -1; j < 2; j++)
				{
					final int id = par3World.getBlockId(par4 + i, par5, par6 + j);

					if (id == GCCoreBlocks.landingPadFull.blockID)
					{
						amountOfCorrectBlocks = 9;

						centerX = par4 + i + 0.5F;
						centerY = par5 + 1.6F;
						centerZ = par6 + j + 0.5F;
					}
				}
			}

			if (amountOfCorrectBlocks == 9)
			{
				final GCCoreEntityRocketT1 spaceship = new GCCoreEntityRocketT1(par3World, centerX, centerY + 0.2D, centerZ, EnumRocketType.values()[par1ItemStack.getItemDamage()]);

				par3World.spawnEntityInWorld(spaceship);

				if (!par2EntityPlayer.capabilities.isCreativeMode)
				{
					par1ItemStack.stackSize--;

					if (par1ItemStack.stackSize <= 0)
					{
						par1ItemStack = null;
					}
				}

				if (spaceship.rocketType.getPreFueled())
				{
					spaceship.fuelTank.fill(new FluidStack(GalacticraftCore.fluidFuel, 2000), true);
				}
			}
			else
			{
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < EnumRocketType.values().length; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxyCore.galacticraftItem;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List par2List, boolean b)
	{
		EnumRocketType type = EnumRocketType.values()[par1ItemStack.getItemDamage()];

		if (!type.getTooltip().isEmpty())
		{
			par2List.add(type.getTooltip());
		}

		if (type.getPreFueled())
		{
			par2List.add(EnumColor.RED + "\u00a7o" + "Creative Only");
		}
	}

	@Override
	public boolean shouldHoldLeftHandUp(EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean shouldHoldRightHandUp(EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean shouldCrouch(EntityPlayer player)
	{
		return true;
	}
}
