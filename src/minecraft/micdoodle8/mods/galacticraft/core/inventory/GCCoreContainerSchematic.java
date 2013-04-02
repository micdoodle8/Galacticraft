package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.world.World;

public class GCCoreContainerSchematic  extends Container
{
    public GCCoreInventorySchematic craftMatrix = new GCCoreInventorySchematic(this);
    public IInventory craftResult = new InventoryCraftResult();
    private final World worldObj;

    public GCCoreContainerSchematic(InventoryPlayer par1InventoryPlayer, int x, int y, int z)
    {
    	int change = 27;
		this.worldObj = par1InventoryPlayer.player.worldObj;
	    this.addSlotToContainer(new GCCoreSlotRocketBenchResult(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 110, 96));

        this.onCraftMatrixChanged(this.craftMatrix);
    }

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) 
	{
		return true;
	}
}
