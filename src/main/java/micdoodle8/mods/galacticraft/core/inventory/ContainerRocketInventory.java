package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerRocketInventory extends Container
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.ROCKET_INVENTORY)
    public static ContainerType<ContainerRocketInventory> TYPE;

    private final IInventory playerInv;
    private final IInventory spaceshipInv;
    private final EnumRocketType rocketType;

    public ContainerRocketInventory(int containerId, PlayerInventory playerInv, EntityTieredRocket rocket)
    {
        super(TYPE, containerId);
        this.playerInv = playerInv;
        this.spaceshipInv = rocket;
        this.rocketType = rocket.getRocketType();
        rocket.openInventory(playerInv.player);
        this.addSlotsWithInventory(rocketType.getInventorySpace());
    }

    public EnumRocketType getRocketType()
    {
        return rocketType;
    }

    private void addSlotsWithInventory(int slotCount)
    {
        int y;
        int x;
        int ySize = 145 + (this.rocketType.getInventorySpace() - 2) * 2;
        int lastRow = slotCount / 9;

        for (y = 0; y < lastRow; ++y)
        {
            for (x = 0; x < 9; ++x)
            {
                this.addSlot(new Slot(this.spaceshipInv, x + y * 9, 8 + x * 18, 50 + y * 18));
            }
        }

        for (y = 0; y < 3; ++y)
        {
            for (x = 0; x < 9; ++x)
            {
                this.addSlot(new Slot(this.playerInv, x + y * 9 + 9, 8 + x * 18, ySize - 82 + y * 18));
            }
        }

        for (y = 0; y < 9; ++y)
        {
            this.addSlot(new Slot(this.playerInv, y, 8 + y * 18, ySize - 24));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity par1EntityPlayer)
    {
        return this.spaceshipInv.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par2)
    {
        ItemStack var3 = ItemStack.EMPTY;
        final Slot var4 = this.inventorySlots.get(par2);
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
    public IInventory getLowerChestInventory()
    {
        return this.playerInv;
    }
}
