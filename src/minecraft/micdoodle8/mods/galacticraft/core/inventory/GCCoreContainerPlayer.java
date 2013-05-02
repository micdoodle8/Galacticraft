package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotArmor;
import net.minecraft.inventory.SlotCrafting;
import cpw.mods.fml.common.FMLLog;

public class GCCoreContainerPlayer extends ContainerPlayer
{
	public GCCoreContainerPlayer(InventoryPlayer par1InventoryPlayer, boolean par2, EntityPlayer par3EntityPlayer) 
	{
		super(par1InventoryPlayer, par2, par3EntityPlayer);
		
		this.inventoryItemStacks.clear();
		this.inventorySlots.clear();

        this.isLocalWorld = par2;
        this.addSlotToContainer(new SlotCrafting(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 116, 62));
        int i;
        int j;
        
        for (i = 0; i < 2; ++i)
        {
            for (j = 0; j < 2; ++j)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 2, 108 + j * 18, 6 + i * 18));
            }
        }

        for (i = 0; i < 4; ++i)
        {
            this.addSlotToContainer(new SlotArmor(this, par1InventoryPlayer, par1InventoryPlayer.getSizeInventory() - 1 - i - 5, 8, 8 + i * 18, i));
        }

        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
        }

        for (int var4 = 1; var4 < 5; ++var4)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, 40 + var4 - 1, 80, 8 + (var4 - 1) * 18));
        }

        this.addSlotToContainer(new Slot(par1InventoryPlayer, 44, 154, 6));
	}
}
