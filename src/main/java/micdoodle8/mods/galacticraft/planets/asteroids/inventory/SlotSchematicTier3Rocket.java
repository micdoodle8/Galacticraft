package micdoodle8.mods.galacticraft.planets.asteroids.inventory;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class SlotSchematicTier3Rocket extends Slot
{
    private final int index;
    private final BlockPos pos;
    private final EntityPlayer player;

    public SlotSchematicTier3Rocket(IInventory par2IInventory, int par3, int par4, int par5, BlockPos pos, EntityPlayer player)
    {
        super(par2IInventory, par3, par4, par5);
        this.index = par3;
        this.pos = pos;
        this.player = player;
    }

    @Override
    public void onSlotChanged()
    {
        if (this.player instanceof EntityPlayerMP)
        {
            int dimID = GCCoreUtil.getDimensionID(this.player.worldObj);
            GCCoreUtil.sendToAllAround(new PacketSimple(EnumSimplePacket.C_SPAWN_SPARK_PARTICLES, dimID, new Object[] { this.pos }), this.player.worldObj, dimID, this.pos, 20);
        }
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        switch (this.index)
        {
        case 1:
            return par1ItemStack.getItem() == AsteroidsItems.heavyNoseCone;
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
            return par1ItemStack.getItem() == AsteroidsItems.basicItem && par1ItemStack.getItemDamage() == 5;
        case 12:
        case 16:
            return par1ItemStack.getItem() == GCItems.rocketEngine && par1ItemStack.getItemDamage() == 1;
        case 15:
            return par1ItemStack.getItem() == AsteroidsItems.basicItem && par1ItemStack.getItemDamage() == 1;
        case 13:
        case 14:
        case 17:
        case 18:
            return par1ItemStack.getItem() == AsteroidsItems.basicItem && par1ItemStack.getItemDamage() == 2;
        case 19:
            return true;
        case 20:
            return true;
        case 21:
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
