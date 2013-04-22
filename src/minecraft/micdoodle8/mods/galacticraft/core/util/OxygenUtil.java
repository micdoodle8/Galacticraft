package micdoodle8.mods.galacticraft.core.util;

import java.util.List;

import micdoodle8.mods.galacticraft.API.EnumGearType;
import micdoodle8.mods.galacticraft.API.IBreathableArmor;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockBreathableAir;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiTankRefill;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryTankRefill;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenGear;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenMask;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenTank;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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

		if (gui instanceof GCCoreGuiTankRefill)
		{
			return true;
		}

		if (gui instanceof GuiInventory)
		{
			return true;
		}

		if (gui instanceof GuiChat)
		{
			return true;
		}

		return false;
	}

    public static boolean isAABBInBreathableAirBlock(Entity entity)
    {
        final int var3 = MathHelper.floor_double(entity.boundingBox.minX);
        final int var4 = MathHelper.floor_double(entity.boundingBox.maxX + 1.0D);
        final int var5 = MathHelper.floor_double(entity.boundingBox.minY);
        final int var6 = MathHelper.floor_double(entity.boundingBox.maxY + 1.0D);
        final int var7 = MathHelper.floor_double(entity.boundingBox.minZ);
        final int var8 = MathHelper.floor_double(entity.boundingBox.maxZ + 1.0D);

        final AxisAlignedBB box = AxisAlignedBB.getBoundingBox(entity.posX - 40, entity.posY - 40, entity.posZ - 40, entity.posX + 40, entity.posY + 40, entity.posZ + 40);

        final List l = entity.worldObj.loadedTileEntityList;

        for (final Object o : l)
        {
        	if (o instanceof GCCoreTileEntityOxygenDistributor)
        	{
        		final GCCoreTileEntityOxygenDistributor distributor = (GCCoreTileEntityOxygenDistributor) o;

        		if (!distributor.worldObj.isRemote)
        		{
        			final double dist = distributor.getDistanceFromServer(entity.posX, entity.posY, entity.posZ);

        			if (Math.sqrt(dist) < distributor.storedOxygen / 600.0D)
        			{
        				return true;
        			}
        		}
        	}
        }

        for (int var9 = var3; var9 < var4; ++var9)
        {
            for (int var10 = var5; var10 < var6; ++var10)
            {
                for (int var11 = var7; var11 < var8; ++var11)
                {
                    final Block var12 = Block.blocksList[entity.worldObj.getBlockId(var9, var10, var11)];

                    if (var12 != null && var12 instanceof GCCoreBlockBreathableAir)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static int getDrainSpacing(ItemStack tank)
    {
		if (tank == null)
		{
			return 0;
		}

		if (tank.getItem() instanceof GCCoreItemOxygenTank)
		{
			return 360;
		}

		return 0;
    }

	public static boolean hasValidOxygenSetup(GCCorePlayerMP player)
	{
		boolean missingComponent = false;

		final GCCoreInventoryTankRefill inventory = player.playerTankInventory;

		if (inventory.getStackInSlot(0) == null || !OxygenUtil.isItemValidForPlayerTankInv(0, inventory.getStackInSlot(0)))
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

		if (inventory.getStackInSlot(1) == null || !OxygenUtil.isItemValidForPlayerTankInv(1, inventory.getStackInSlot(1)))
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

		if ((inventory.getStackInSlot(2) == null || !OxygenUtil.isItemValidForPlayerTankInv(2, inventory.getStackInSlot(2))) && (inventory.getStackInSlot(3) == null || !OxygenUtil.isItemValidForPlayerTankInv(3, inventory.getStackInSlot(3))))
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
			return stack.getItem() instanceof GCCoreItemOxygenMask;
		case 1:
			return stack.getItem() instanceof GCCoreItemOxygenGear;
		case 2:
			return OxygenUtil.getDrainSpacing(stack) > 0;
		case 3:
			return OxygenUtil.getDrainSpacing(stack) > 0;
		}

		return false;
	}

    public static boolean isBlockGettingOxygen(World world, int par1, int par2, int par3)
    {
    	return true; // TODO
//        return isBlockProvidingOxygenTo(world, par1, par2 - 1, par3, 0) ? true : isBlockProvidingOxygenTo(world, par1, par2 + 1, par3, 1) ? true : isBlockProvidingOxygenTo(world, par1, par2, par3 - 1, 2) ? true : isBlockProvidingOxygenTo(world, par1, par2, par3 + 1, 3) ? true : isBlockProvidingOxygenTo(world, par1 - 1, par2, par3, 4) ? true : isBlockProvidingOxygenTo(world, par1 + 1, par2, par3, 5);
    }

//    public static boolean isBlockProvidingOxygenTo(World world, int par1, int par2, int par3, int par4)
//    {
//    	final TileEntity te = world.getBlockTileEntity(par1, par2, par3);
//
//    	if (te != null && te instanceof TileEntityOxygenTransmitter)
//    	{
//    		final TileEntityOxygenTransmitter teot = (TileEntityOxygenTransmitter) te;
//
//    		if (teot.getOxygenInTransmitter() > 1.0D)
//    		{
//    			return true;
//    		}
//    	}
//
//    	return false;
//    }
}
