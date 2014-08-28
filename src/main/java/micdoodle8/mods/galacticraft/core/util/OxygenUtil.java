package micdoodle8.mods.galacticraft.core.util;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.api.item.IBreathableArmor;
import micdoodle8.mods.galacticraft.api.item.IBreathableArmor.EnumGearType;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenGear;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenMask;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDistributor;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class OxygenUtil
{
	public static boolean shouldDisplayTankGui(GuiScreen gui)
	{
		if (FMLClientHandler.instance().getClient().gameSettings.hideGUI)
		{
			return false;
		}

		if (gui == null)
		{
			return true;
		}

		if (gui instanceof GuiInventory)
		{
			return false;
		}

        return gui instanceof GuiChat;

    }

	public static boolean isAABBInBreathableAirBlock(EntityLivingBase entity)
	{
		return OxygenUtil.isAABBInBreathableAirBlock(entity.worldObj, entity.boundingBox);
	}

	@SuppressWarnings("rawtypes")
	public static boolean isAABBInBreathableAirBlock(World world, AxisAlignedBB bb)
	{
		if (!world.isRemote)
		{
		
		final double avgX = (bb.minX + bb.maxX) / 2.0D;
		final double avgY = (bb.minY + bb.maxY) / 2.0D;
		final double avgZ = (bb.minZ + bb.maxZ) / 2.0D;

		final List l = world.loadedTileEntityList;

		for (final Object o : l)
		{
			if (o instanceof TileEntityOxygenDistributor)
			{
				final TileEntityOxygenDistributor distributor = (TileEntityOxygenDistributor) o;

				if (distributor.oxygenBubble != null)
				{
					final double dist = distributor.getDistanceFromServer(avgX, avgY, avgZ);
					double r = distributor.oxygenBubble.getSize();

					if (dist < r * r)
					{
						return true;
					}
				}
			}
		}

		}
		return OxygenUtil.isInOxygenBlock(world, bb.copy().expand(0.999D, 0.999D, 0.999D));
	}

	public static boolean isInOxygenBlock(World world, AxisAlignedBB bb)
	{
		int i = MathHelper.floor_double(bb.minX);
		int j = MathHelper.floor_double(bb.maxX);
		int k = MathHelper.floor_double(bb.minY);
		int l = MathHelper.floor_double(bb.maxY);
		int i1 = MathHelper.floor_double(bb.minZ);
		int j1 = MathHelper.floor_double(bb.maxZ);

		if (world.checkChunksExist(i, k, i1, j, l, j1))
		{
			for (int x = i; x <= j; ++x)
			{
				for (int y = k; y <= l; ++y)
				{
					for (int z = i1; z <= j1; ++z)
					{
						Block block = world.getBlock(x, y, z);

						if (block == GCBlocks.breatheableAir || block == GCBlocks.brightBreatheableAir)
						{
							return true;
						}				
					}
				}
			}
		}

		return false;
	}

	public static int getDrainSpacing(ItemStack tank, ItemStack tank2)
	{
		boolean tank1Valid = tank != null && tank.getItem() instanceof ItemOxygenTank && tank.getMaxDamage() - tank.getItemDamage() > 0;
		boolean tank2Valid = tank2 != null && tank2.getItem() instanceof ItemOxygenTank && tank2.getMaxDamage() - tank2.getItemDamage() > 0;

		if (!tank1Valid && !tank2Valid)
		{
			return 0;
		}

		if (tank1Valid && !tank2Valid || !tank1Valid && tank2Valid)
		{
			return 9;
		}

		return 18;
	}

	public static boolean hasValidOxygenSetup(GCEntityPlayerMP player)
	{
		boolean missingComponent = false;

		if (player.getPlayerStats().extendedInventory.getStackInSlot(0) == null || !OxygenUtil.isItemValidForPlayerTankInv(0, player.getPlayerStats().extendedInventory.getStackInSlot(0)))
		{
			boolean handled = false;

			for (final ItemStack armorStack : player.inventory.armorInventory)
			{
				if (armorStack != null && armorStack.getItem() instanceof IBreathableArmor)
				{
					final IBreathableArmor breathableArmor = (IBreathableArmor) armorStack.getItem();

					if (breathableArmor.handleGearType(EnumGearType.HELMET))
					{
						if (breathableArmor.canBreathe(armorStack, player, EnumGearType.HELMET))
						{
							handled = true;
						}
					}
				}
			}

			if (!handled)
			{
				missingComponent = true;
			}
		}

		if (player.getPlayerStats().extendedInventory.getStackInSlot(1) == null || !OxygenUtil.isItemValidForPlayerTankInv(1, player.getPlayerStats().extendedInventory.getStackInSlot(1)))
		{
			boolean handled = false;

			for (final ItemStack armorStack : player.inventory.armorInventory)
			{
				if (armorStack != null && armorStack.getItem() instanceof IBreathableArmor)
				{
					final IBreathableArmor breathableArmor = (IBreathableArmor) armorStack.getItem();

					if (breathableArmor.handleGearType(EnumGearType.GEAR))
					{
						if (breathableArmor.canBreathe(armorStack, player, EnumGearType.GEAR))
						{
							handled = true;
						}
					}
				}
			}

			if (!handled)
			{
				missingComponent = true;
			}
		}

		if ((player.getPlayerStats().extendedInventory.getStackInSlot(2) == null || !OxygenUtil.isItemValidForPlayerTankInv(2, player.getPlayerStats().extendedInventory.getStackInSlot(2))) && (player.getPlayerStats().extendedInventory.getStackInSlot(3) == null || !OxygenUtil.isItemValidForPlayerTankInv(3, player.getPlayerStats().extendedInventory.getStackInSlot(3))))
		{
			boolean handled = false;

			for (final ItemStack armorStack : player.inventory.armorInventory)
			{
				if (armorStack != null && armorStack.getItem() instanceof IBreathableArmor)
				{
					final IBreathableArmor breathableArmor = (IBreathableArmor) armorStack.getItem();

					if (breathableArmor.handleGearType(EnumGearType.TANK1))
					{
						if (breathableArmor.canBreathe(armorStack, player, EnumGearType.TANK1))
						{
							handled = true;
						}
					}

					if (breathableArmor.handleGearType(EnumGearType.TANK2))
					{
						if (breathableArmor.canBreathe(armorStack, player, EnumGearType.TANK2))
						{
							handled = true;
						}
					}
				}
			}

			if (!handled)
			{
				missingComponent = true;
			}
		}

		return !missingComponent;
	}

	public static boolean isItemValidForPlayerTankInv(int slotIndex, ItemStack stack)
	{
		switch (slotIndex)
		{
		case 0:
			return stack.getItem() instanceof ItemOxygenMask;
		case 1:
			return stack.getItem() instanceof ItemOxygenGear;
		case 2:
			return stack.getItem() instanceof ItemOxygenTank;
		case 3:
			return stack.getItem() instanceof ItemOxygenTank;
		}

		return false;
	}
}
