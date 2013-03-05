package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.API.TileEntityOxygenTransmitter;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiTankRefill;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerBase;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenGear;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenMask;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenTank;
import micdoodle8.mods.galacticraft.core.tile.GCCoreInventoryTankRefill;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
	
	public static boolean hasValidOxygenSetup(EntityPlayer player)
	{
		boolean missingComponent = false;
		
		final GCCoreInventoryTankRefill inventory = PlayerUtil.getPlayerBaseServerFromPlayer(player).playerTankInventory;
		
		if (inventory.getStackInSlot(0) == null || !OxygenUtil.isItemValidForPlayerTankInv(0, inventory.getStackInSlot(0)))
		{
			missingComponent = true;
		}
		
		if (inventory.getStackInSlot(1) == null || !OxygenUtil.isItemValidForPlayerTankInv(1, inventory.getStackInSlot(1)))
		{
			missingComponent = true;
		}
		
		if ((inventory.getStackInSlot(2) == null || !OxygenUtil.isItemValidForPlayerTankInv(2, inventory.getStackInSlot(2))) && (inventory.getStackInSlot(3) == null || !OxygenUtil.isItemValidForPlayerTankInv(3, inventory.getStackInSlot(3))))
		{
			missingComponent = true;
		}
		
        if (missingComponent)
        {
    		return false;
        }
        else
        {
        	return true;
        }
	}
	
	public static boolean hasValidOxygenSetup(GCCorePlayerBase player)
	{
		boolean missingComponent = false;
		
		final GCCoreInventoryTankRefill inventory = player.playerTankInventory;
		
		if (inventory.getStackInSlot(0) == null || !OxygenUtil.isItemValidForPlayerTankInv(0, inventory.getStackInSlot(0)))
		{
			missingComponent = true;
		}
		
		if (inventory.getStackInSlot(1) == null || !OxygenUtil.isItemValidForPlayerTankInv(1, inventory.getStackInSlot(1)))
		{
			missingComponent = true;
		}

		if ((inventory.getStackInSlot(2) == null || !OxygenUtil.isItemValidForPlayerTankInv(2, inventory.getStackInSlot(2))) && (inventory.getStackInSlot(3) == null || !OxygenUtil.isItemValidForPlayerTankInv(3, inventory.getStackInSlot(3))))
		{
			missingComponent = true;
		}
		
		if (missingComponent)
        {
    		return false;
        }
        else
        {
        	return true;
        }
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
        return isBlockProvidingOxygenTo(world, par1, par2 - 1, par3, 0) ? true : (isBlockProvidingOxygenTo(world, par1, par2 + 1, par3, 1) ? true : (isBlockProvidingOxygenTo(world, par1, par2, par3 - 1, 2) ? true : (isBlockProvidingOxygenTo(world, par1, par2, par3 + 1, 3) ? true : (isBlockProvidingOxygenTo(world, par1 - 1, par2, par3, 4) ? true : isBlockProvidingOxygenTo(world, par1 + 1, par2, par3, 5)))));
    }

    public static boolean isBlockProvidingOxygenTo(World world, int par1, int par2, int par3, int par4)
    {
    	TileEntity te = world.getBlockTileEntity(par1, par2, par3);
    	
    	if (te != null && te instanceof TileEntityOxygenTransmitter)
    	{
    		TileEntityOxygenTransmitter teot = (TileEntityOxygenTransmitter) te;
    		
    		if (teot.getOxygenInTransmitter() > 1.0D)
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
}
