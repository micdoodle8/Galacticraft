package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerBuggy extends Container
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.BUGGY)
    public static ContainerType<ContainerBuggy> TYPE;

    private final IInventory playerInv;
    private final IInventory buggyInv;

    public EntityBuggy.BuggyType buggyType;

    public ContainerBuggy(int containerId, PlayerInventory playerInv, EntityBuggy.BuggyType type)
    {
        this(containerId, playerInv, new Inventory(type.getInvSize()), type);
    }

    public ContainerBuggy(int containerId, PlayerInventory playerInv, IInventory buggyInv, EntityBuggy.BuggyType type)
    {
        super(TYPE, containerId);
        this.playerInv = playerInv;
        this.buggyInv = buggyInv;
        this.buggyType = type;
        buggyInv.openInventory(playerInv.player);

        int var4;
        int var5;

        if (type != EntityBuggy.BuggyType.NO_INVENTORY)
        {
            for (int i = 0; i < type.getInvSize(); ++i)
            {
                int row = i / 9;
                this.addSlot(new Slot(this.buggyInv, i, 8 + row * 18, 50 + row * 18));
            }
        }

        for (var4 = 0; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                this.addSlot(new Slot(this.playerInv, var5 + var4 * 9 + 9, 8 + var5 * 18, 49 + var4 * 18 + 14 + type.ordinal() * 36));
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            this.addSlot(new Slot(this.playerInv, var4, 8 + var4 * 18, 107 + 14 + type.ordinal() * 36));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity par1EntityPlayer)
    {
        return this.buggyInv.isUsableByPlayer(par1EntityPlayer);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or
     * you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par2)
    {
        ItemStack var3 = ItemStack.EMPTY;
        final Slot var4 = (Slot) this.inventorySlots.get(par2);
        final int b = this.inventorySlots.size() - 36;

        if (var4 != null && var4.getHasStack())
        {
            final ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (par2 < b)
            {
                if (!this.mergeItemStack(var5, b, b + 36, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(var5, 0, b, false))
            {
                return ItemStack.EMPTY;
            }

            if (var5.getCount() == 0)
            {
                var4.putStack(ItemStack.EMPTY);
            }
            else
            {
                var4.onSlotChanged();
            }
        }

        return var3;
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    @Override
    public void onContainerClosed(PlayerEntity par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);
        this.playerInv.closeInventory(par1EntityPlayer);
    }

    /**
     * Return this chest container's lower chest inventory.
     */
    public IInventory getPlayerInv()
    {
        return this.playerInv;
    }
}
