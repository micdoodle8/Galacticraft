package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;

public class GCCoreContainerSchematic  extends Container
{
    public GCCoreInventorySchematic craftMatrix = new GCCoreInventorySchematic(this);
    public IInventory craftResult = new InventoryCraftResult();
    private final World worldObj;

    public GCCoreContainerSchematic(InventoryPlayer par1InventoryPlayer, int x, int y, int z)
    {
		this.worldObj = par1InventoryPlayer.player.worldObj;
	    this.addSlotToContainer(new GCCoreSlotRocketBenchResult(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 80, 1));
        int var6;
        int var7;

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 111 + var6 * 18 - 59 + 16));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 169 - 59 + 16));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) 
	{
		return true;
	}
}
