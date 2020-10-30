package micdoodle8.mods.galacticraft.planets.asteroids.inventory;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.inventory.SlotRocketBenchResult;
import micdoodle8.mods.galacticraft.planets.mars.util.RecipeUtilMars;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerSchematicTier3Rocket extends Container
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + AsteroidsContainerNames.SCHEMATIC_TIER_3_ROCKET)
    public static ContainerType<ContainerSchematicTier3Rocket> TYPE;

    public InventorySchematicTier3Rocket craftMatrix = new InventorySchematicTier3Rocket(this);
    public IInventory craftResult = new CraftResultInventory();
    private final World world;

    public ContainerSchematicTier3Rocket(int containerId, PlayerInventory playerInv)
    {
        super(TYPE, containerId);
        final int change = 27;
        this.world = playerInv.player.world;
        this.addSlot(new SlotRocketBenchResult(playerInv.player, this.craftMatrix, this.craftResult, 0, 142, 18 + 69 + change));
        int var6;
        int var7;

        // Cone
        this.addSlot(new SlotSchematicTier3Rocket(this.craftMatrix, 1, 48, -8 + change, playerInv.player));

        // Body
        for (var6 = 0; var6 < 5; ++var6)
        {
            this.addSlot(new SlotSchematicTier3Rocket(this.craftMatrix, 2 + var6, 39, -6 + var6 * 18 + 16 + change, playerInv.player));
        }

        // Body Right
        for (var6 = 0; var6 < 5; ++var6)
        {
            this.addSlot(new SlotSchematicTier3Rocket(this.craftMatrix, 7 + var6, 57, -6 + var6 * 18 + 16 + change, playerInv.player));
        }

        // Left fins
        this.addSlot(new SlotSchematicTier3Rocket(this.craftMatrix, 12, 21, 64 + change, playerInv.player));
        this.addSlot(new SlotSchematicTier3Rocket(this.craftMatrix, 13, 21, 82 + change, playerInv.player));
        this.addSlot(new SlotSchematicTier3Rocket(this.craftMatrix, 14, 21, 100 + change, playerInv.player));

        // Engine
        this.addSlot(new SlotSchematicTier3Rocket(this.craftMatrix, 15, 48, 100 + change, playerInv.player));

        // Right fins
        this.addSlot(new SlotSchematicTier3Rocket(this.craftMatrix, 16, 75, 64 + change, playerInv.player));
        this.addSlot(new SlotSchematicTier3Rocket(this.craftMatrix, 17, 75, 82 + change, playerInv.player));
        this.addSlot(new SlotSchematicTier3Rocket(this.craftMatrix, 18, 75, 100 + change, playerInv.player));

        // Addons
        for (int var8 = 0; var8 < 3; var8++)
        {
            this.addSlot(new SlotSchematicTier3Rocket(this.craftMatrix, 19 + var8, 93 + var8 * 26, -15 + change, playerInv.player));
        }

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlot(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 129 + var6 * 18 + change));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlot(new Slot(playerInv, var6, 8 + var6 * 18, 18 + 169 + change));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
    public void onContainerClosed(PlayerEntity par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);

        if (!this.world.isRemote)
        {
            for (int var2 = 1; var2 < this.craftMatrix.getSizeInventory(); ++var2)
            {
                final ItemStack var3 = this.craftMatrix.removeStackFromSlot(var2);

                if (!var3.isEmpty())
                {
                    par1EntityPlayer.entityDropItem(var3, 0.0F);
                }
            }
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        this.craftResult.setInventorySlotContents(0, RecipeUtilMars.findMatchingSpaceshipT3Recipe(this.craftMatrix));
    }

    @Override
    public boolean canInteractWith(PlayerEntity par1EntityPlayer)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot var3 = this.inventorySlots.get(par1);

        if (var3 != null && var3.getHasStack())
        {
            final ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            boolean done = false;
            if (par1 <= 21)
            {
                if (!this.mergeItemStack(var4, 22, 58, false))
                {
                    return ItemStack.EMPTY;
                }

                var3.onSlotChange(var4, var2);
            }
            else
            {
                boolean valid = false;
                for (int i = 1; i < 19; i++)
                {
                    Slot testSlot = this.inventorySlots.get(i);
                    if (!testSlot.getHasStack() && testSlot.isItemValid(var2))
                    {
                        valid = true;
                        break;
                    }
                }
                if (valid)
                {
                    if (!this.mergeOneItemTestValid(var4, 1, 19, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    boolean foundChest = false;
//                    for (ItemStack woodChest : OreDictionary.getOres("chestWood"))
//                    {
//                        if (var2.getItem() == woodChest.getItem())
//                        {
//                            foundChest = true;
//                            break;
//                        }
//                    } TODO rocket container w/ cargo
                    if (foundChest)
                    {
                        if (!this.mergeOneItemTestValid(var4, 19, 22, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (par1 >= 22 && par1 < 49)
                    {
                        if (!this.mergeItemStack(var4, 49, 58, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (par1 >= 49 && par1 < 58)
                    {
                        if (!this.mergeItemStack(var4, 22, 49, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.mergeItemStack(var4, 22, 58, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (var4.isEmpty())
            {
                var3.putStack(ItemStack.EMPTY);
            }
            else
            {
                var3.onSlotChanged();
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            var3.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }

    protected boolean mergeOneItemTestValid(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
        boolean flag1 = false;
        if (!par1ItemStack.isEmpty())
        {
            Slot slot;
            ItemStack slotStack;

            for (int k = par2; k < par3; k++)
            {
                slot = this.inventorySlots.get(k);
                slotStack = slot.getStack();

                if (slotStack.isEmpty() && slot.isItemValid(par1ItemStack))
                {
                    ItemStack stackOneItem = par1ItemStack.copy();
                    stackOneItem.setCount(1);
                    par1ItemStack.shrink(1);
                    slot.putStack(stackOneItem);
                    slot.onSlotChanged();
                    flag1 = true;
                    break;
                }
            }
        }

        return flag1;
    }
}
