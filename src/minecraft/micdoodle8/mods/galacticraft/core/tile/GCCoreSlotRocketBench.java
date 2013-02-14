package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLLog;

public class GCCoreSlotRocketBench extends Slot
{
    private final int index;
    private final int x, y, z;
    private final EntityPlayer player;

    public GCCoreSlotRocketBench(IInventory par2IInventory, int par3, int par4, int par5, int x, int y, int z, EntityPlayer player)
    {
        super(par2IInventory, par3, par4, par5);
        this.index = par3;
        this.x = x;
        this.y = y;
        this.z = z;
        this.player = player;
    }

    @Override
    public void onSlotChanged()
    {
    	if (this.player instanceof EntityPlayerMP)
    	{
    		Object[] toSend = {x, y, z};
    		
            for (int var12 = 0; var12 < player.worldObj.playerEntities.size(); ++var12)
            {
                EntityPlayerMP var13 = (EntityPlayerMP) player.worldObj.playerEntities.get(var12);

                if (var13.dimension == player.worldObj.provider.dimensionId)
                {
                    double var14 = x - var13.posX;
                    double var16 = y - var13.posY;
                    double var18 = z - var13.posZ;

                    if (var14 * var14 + var16 * var16 + var18 * var18 < 20 * 20)
                    {
                    	var13.playerNetServerHandler.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 9, toSend));
                    }
                }
            }
    	}
    }

    @Override
	public boolean isItemValid(ItemStack par1ItemStack)
    {
    	switch (this.index)
    	{
    	case 1:
    		return par1ItemStack.getItem().itemID == GCCoreItems.rocketNoseCone.itemID ? true : false;
    	case 2:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 3:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 4:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 5:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 6:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 7:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 8:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 9:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 10:
    		return par1ItemStack.getItem().itemID == GCCoreItems.rocketFins.itemID ? true : false;
    	case 11:
    		return par1ItemStack.getItem().itemID == GCCoreItems.rocketFins.itemID ? true : false;
    	case 12:
    		return par1ItemStack.getItem().itemID == GCCoreItems.rocketEngine.itemID ? true : false;
    	case 13:
    		return par1ItemStack.getItem().itemID == GCCoreItems.rocketFins.itemID ? true : false;
    	case 14:
    		return par1ItemStack.getItem().itemID == GCCoreItems.rocketFins.itemID ? true : false;
    	case 15:
    		return true;
    	case 16:
    		return true;
    	case 17:
    		return true;
    	}
    	
    	return false;
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    @Override
	public int getSlotStackLimit()
    {
        return 64;
    }
}
