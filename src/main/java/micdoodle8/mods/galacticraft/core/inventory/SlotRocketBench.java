package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotRocketBench extends Slot
{
    private final int index;
    private final int x, y, z;
    private final EntityPlayer player;

    public SlotRocketBench(IInventory par2IInventory, int par3, int par4, int par5, int x, int y, int z, EntityPlayer player)
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
            final Object[] toSend = { this.x, this.y, this.z };

            for (int var12 = 0; var12 < this.player.worldObj.playerEntities.size(); ++var12)
            {
                final EntityPlayerMP var13 = (EntityPlayerMP) this.player.worldObj.playerEntities.get(var12);

                if (var13.dimension == this.player.worldObj.provider.dimensionId)
                {
                    final double var14 = this.x - var13.posX;
                    final double var16 = this.y - var13.posY;
                    final double var18 = this.z - var13.posZ;

                    if (var14 * var14 + var16 * var16 + var18 * var18 < 20 * 20)
                    {
                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SPAWN_SPARK_PARTICLES, new Object[] { this.x, this.y, this.z }), var13);
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
            return par1ItemStack.getItem() == GCItems.partNoseCone;
        case 2:
            return par1ItemStack.getItem() == GCItems.heavyPlatingTier1;
        case 3:
            return par1ItemStack.getItem() == GCItems.heavyPlatingTier1;
        case 4:
            return par1ItemStack.getItem() == GCItems.heavyPlatingTier1;
        case 5:
            return par1ItemStack.getItem() == GCItems.heavyPlatingTier1;
        case 6:
            return par1ItemStack.getItem() == GCItems.heavyPlatingTier1;
        case 7:
            return par1ItemStack.getItem() == GCItems.heavyPlatingTier1;
        case 8:
            return par1ItemStack.getItem() == GCItems.heavyPlatingTier1;
        case 9:
            return par1ItemStack.getItem() == GCItems.heavyPlatingTier1;
        case 10:
            return par1ItemStack.getItem() == GCItems.partFins;
        case 11:
            return par1ItemStack.getItem() == GCItems.partFins;
        case 12:
            return par1ItemStack.getItem() == GCItems.rocketEngine && par1ItemStack.getItemDamage() == 0;
        case 13:
            return par1ItemStack.getItem() == GCItems.partFins;
        case 14:
            return par1ItemStack.getItem() == GCItems.partFins;
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
     * Returns the maximum stack size for a given slot (usually the same as
     * getInventoryStackLimit(), but 1 in the case of armor slots)
     */
    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }
}
