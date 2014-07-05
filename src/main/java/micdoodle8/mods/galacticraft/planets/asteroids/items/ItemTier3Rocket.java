package micdoodle8.mods.galacticraft.planets.asteroids.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityTier3Rocket;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ItemTier3Rocket extends Item implements IHoldableItem
{
	public ItemTier3Rocket(String assetName)
	{
		super();
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
		this.setUnlocalizedName(assetName);
		this.setTextureName("arrow");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxyCore.galacticraftItem;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftCore.galacticraftItemsTab;
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
					final Block id = par3World.getBlock(par4 + i, par5, par6 + j);

					if (id == GCBlocks.landingPadFull)
					{
						amountOfCorrectBlocks = 9;

						centerX = par4 + i + 0.5F;
						centerY = par5 - 2.2F;
						centerZ = par6 + j + 0.5F;
					}
				}
			}

			if (amountOfCorrectBlocks == 9)
			{
				EntityTier3Rocket rocket = new EntityTier3Rocket(par3World, centerX, centerY + 4.3D, centerZ, EnumRocketType.values()[par1ItemStack.getItemDamage()]);

				rocket.rotationYaw += 45;
				par3World.spawnEntityInWorld(rocket);

				if (!par2EntityPlayer.capabilities.isCreativeMode)
				{
					par1ItemStack.stackSize--;

					if (par1ItemStack.stackSize <= 0)
					{
						par1ItemStack = null;
					}
				}

				if (((IRocketType) rocket).getType().getPreFueled())
				{
					((EntityTieredRocket) rocket).fuelTank.fill(new FluidStack(GalacticraftCore.fluidFuel, rocket.getMaxFuel()), true);
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
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < EnumRocketType.values().length; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List par2List, boolean b)
	{
		EnumRocketType type = null;

		if (par1ItemStack.getItemDamage() < 10)
		{
			type = EnumRocketType.values()[par1ItemStack.getItemDamage()];
		}
		else
		{
			type = EnumRocketType.values()[par1ItemStack.getItemDamage() - 10];
		}

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
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return super.getUnlocalizedName(par1ItemStack) + ".t3Rocket";
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
