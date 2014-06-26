package micdoodle8.mods.galacticraft.core.util;

import java.util.List;

import micdoodle8.mods.galacticraft.api.item.IBreathableArmor;
import micdoodle8.mods.galacticraft.api.item.IBreathableArmor.EnumGearType;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
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
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;

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

		if (gui instanceof GuiChat)
		{
			return true;
		}

		return false;
	}

	public static boolean isAABBInBreathableAirBlock(Entity entity)
	{
		return OxygenUtil.isAABBInBreathableAirBlock(entity.worldObj, new Vector3(entity.boundingBox.minX - 1, entity.boundingBox.minY - 1, entity.boundingBox.minZ - 1), new Vector3(entity.boundingBox.maxX + 1, entity.boundingBox.maxY + 1, entity.boundingBox.maxZ + 1));
	}

	@SuppressWarnings("rawtypes")
	public static boolean isAABBInBreathableAirBlock(World world, Vector3 minVec, Vector3 maxVec)
	{
		final double avgX = (minVec.x + maxVec.x) / 2.0D;
		final double avgY = (minVec.y + maxVec.y) / 2.0D;
		final double avgZ = (minVec.z + maxVec.z) / 2.0D;

		final List l = world.loadedTileEntityList;

		for (final Object o : l)
		{
			if (o instanceof TileEntityOxygenDistributor)
			{
				final TileEntityOxygenDistributor distributor = (TileEntityOxygenDistributor) o;

				if (!distributor.getWorldObj().isRemote && distributor.oxygenBubble != null)
				{
					final double dist = distributor.getDistanceFromServer(avgX, avgY, avgZ);

					if (dist < Math.pow(distributor.oxygenBubble.getSize(), 2))
					{
						return true;
					}
				}
			}
		}

		return OxygenUtil.isInOxygenBlock(world, AxisAlignedBB.getAABBPool().getAABB(minVec.x, minVec.y, minVec.z, maxVec.x, maxVec.y, maxVec.z).contract(0.001D, 0.001D, 0.001D));
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
			for (int k1 = i; k1 <= j; ++k1)
			{
				for (int l1 = k; l1 <= l; ++l1)
				{
					for (int i2 = i1; i2 <= j1; ++i2)
					{
						Block j2 = world.getBlock(k1, l1, i2);

						if (j2 == GCBlocks.breatheableAir || j2 == GCBlocks.brightBreatheableAir)
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
		boolean tank1Valid = tank != null ? tank.getItem() instanceof ItemOxygenTank && tank.getMaxDamage() - tank.getItemDamage() > 0 : false;
		boolean tank2Valid = tank2 != null ? tank2.getItem() instanceof ItemOxygenTank && tank2.getMaxDamage() - tank2.getItemDamage() > 0 : false;

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
