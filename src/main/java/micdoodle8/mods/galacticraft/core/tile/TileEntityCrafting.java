package micdoodle8.mods.galacticraft.core.tile;

import java.util.Optional;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCBlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCrafting;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.inventory.PersistantInventoryCrafting;
import micdoodle8.mods.galacticraft.core.network.PacketDynamicInventory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityCrafting extends TileEntity implements IInventoryDefaults, ISidedInventory, IInventorySettable, INamedContainerProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.crafting)
    public static TileEntityType<TileEntityCrafting> TYPE;

    private static final int SIZEINVENTORY = 9;
    public PersistantInventoryCrafting craftMatrix = new PersistantInventoryCrafting();
    public NonNullList<ItemStack> memory = NonNullList.withSize(SIZEINVENTORY, ItemStack.EMPTY);
    private ItemStack hiddenOutputBuffer = ItemStack.EMPTY;   //Used for Buildcraft pipes and other inventory-slot setters which do not fully clear the results slot - see setInventorySlotContents()
    private Boolean overriddenStatus;
    private ItemStack memoryResult;

    public TileEntityCrafting()
    {
        super(TYPE);
    }

    @Override
    public void onLoad()
    {
        if (this.world.isRemote)
        {
            //Request size + contents information from server
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamicInventory(this));
        }
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }

    public void updateMemory(ItemStack craftingResult)
    {
        Optional<ICraftingRecipe> optional = this.getWorld().getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, this.craftMatrix, this.getWorld());
        if (optional.isPresent() && optional.get().getCraftingResult(this.craftMatrix).isEmpty())
        {
            return;
        }
//        if (CraftingManager.findMatchingResult(this.craftMatrix, this.getWorld()) == ItemStack.EMPTY) return;
        this.setMemoryHeld(craftingResult);
        for (int i = 0; i < SIZEINVENTORY; i++)
        {
            ItemStack stack = this.craftMatrix.getStackInSlot(i);
            if (stack.isEmpty())
            {
                this.memory.set(i, ItemStack.EMPTY);
            }
            else
            {
                ItemStack stack2 = stack.copy();
                stack2.setCount(1);
                this.memory.set(i, stack2.copy());
            }
        }
    }

    public ItemStack getMemory(int i)
    {
        return this.memory.get(i);
    }

    @Override
    public int getSizeInventory()
    {
        return SIZEINVENTORY;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        if (par1 < SIZEINVENTORY)
        {
            return this.craftMatrix.getStackInSlot(par1);
        }

        //Results slot
        //First, try to clear the hidden output buffer before generating new results items
        if (!this.hiddenOutputBuffer.isEmpty())
        {
            return this.hiddenOutputBuffer;
        }

        // Crafting Manager can produce concurrent modification exception in single player
        // if a server-LogicalSide tick (e.g. from a Hopper) calls this while client-LogicalSide is still initialising recipes
        try
        {
            Optional<ICraftingRecipe> optional = this.getWorld().getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, this.craftMatrix, this.getWorld());
            if (optional.isPresent())
            {
                return optional.get().getCraftingResult(this.craftMatrix);
            }
//            return CraftingManager.findMatchingResult(this.craftMatrix, this.getWorld());
        }
        catch (Exception ignore)
        {
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (par1 >= 0 && par1 < SIZEINVENTORY)
        {
            ItemStack result = this.craftMatrix.decrStackSize(par1, par2);
            this.markDirty();
            this.updateOverriddenStatus();
            return result;
        }
        else if (par1 == SIZEINVENTORY)
        {
            if (!this.hiddenOutputBuffer.isEmpty())
            {
                return this.hiddenOutputBuffer.split(par2);
            }
            if (this.stillMatchesRecipe())
            {
                Optional<ICraftingRecipe> optional = this.getWorld().getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, this.craftMatrix, this.getWorld());
                if (optional.isPresent())
                {
                    ItemStack craftingResult = optional.get().getCraftingResult(this.craftMatrix);
//                ItemStack craftingResult = CraftingManager.findMatchingResult(this.craftMatrix, this.getWorld());
                    if (!craftingResult.isEmpty())
                    {
                        this.pullOneResultStack();
                        this.markDirty();
                        this.updateOverriddenStatus();
                        ItemStack result = craftingResult.split(par2);
                        this.hiddenOutputBuffer = craftingResult;  //save any balance
                        return result;
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private void pullOneResultStack()
    {
//        Thread.dumpStack();
        Optional<ICraftingRecipe> optional = this.getWorld().getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, this.craftMatrix, this.getWorld());
        if (optional.isPresent())
        {
            NonNullList<ItemStack> aitemstack = optional.get().getRemainingItems(this.craftMatrix);

            for (int i = 0; i < aitemstack.size(); ++i)
            {
                ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
                ItemStack itemstack1 = aitemstack.get(i);

                if (!itemstack.isEmpty())
                {
                    this.craftMatrix.decrStackSize(i, 1);
                }

                if (!itemstack1.isEmpty())
                {
                    if (this.craftMatrix.getStackInSlot(i).isEmpty())
                    {
                        this.craftMatrix.setInventorySlotContents(i, itemstack1);
                    }
                    else
                    {
                        //TODO - things like buckets which can't go back into this - drop?
                    }
                }
            }
        }
    }

    protected boolean stillMatchesRecipe()
    {
        int emptyCount = 0;
        for (int i = 0; i < SIZEINVENTORY; i++)
        {
            ItemStack stack = this.craftMatrix.getStackInSlot(i);
            ItemStack targetOther = this.memory.get(i);
            if (targetOther.isEmpty() && stack.isEmpty())
            {
                continue;
            }

            if (targetOther.isEmpty() || stack.isEmpty() || !sameItem(targetOther, stack))
            {
                return false;
            }
        }
        return emptyCount < SIZEINVENTORY;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.memory)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (par1 >= 0)
        {
            this.markDirty();
            this.updateOverriddenStatus();
            return this.craftMatrix.removeStackFromSlot(par1);
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack stack)
    {
        if (par1 >= 0 && par1 < SIZEINVENTORY)
        {
            this.craftMatrix.setInventorySlotContents(par1, stack);
        }
        else if (par1 == SIZEINVENTORY)
        {
            if (stack.isEmpty())
            {
                if (this.hiddenOutputBuffer.isEmpty())
                {
                    //Standard behaviour: hiddenOutputBuffer not in use
                    this.craftOutput();
                }
            }
            else
            //Buildcraft pipes and some other extractors may not fully clear the results slot, but instead set it to partial contents
            {
                if (!stack.isItemEqual(this.hiddenOutputBuffer))  //also true if hiddenOutputBuffer is empty, e.g. craft 9 new items and pipe takes 4, setting output stack to 5 items
                {
                    this.dropHiddenOutputBuffer(this.world, this.pos);
                    this.craftOutput();
                }
            }
            this.hiddenOutputBuffer = stack;
        }
        this.markDirty();
        this.updateOverriddenStatus();
    }

    private void craftOutput()
    {
        if (this.stillMatchesRecipe())
        {
//            ItemStack craftingResult = CraftingManager.findMatchingResult(this.craftMatrix, this.getWorld());
            Optional<ICraftingRecipe> optional = this.getWorld().getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, this.craftMatrix, this.getWorld());
//            if (!craftingResult.isEmpty())
            if (optional.isPresent() && !optional.get().getCraftingResult(this.craftMatrix).isEmpty())
            {
                this.pullOneResultStack();
            }
        }
    }

//    @Override
//    public String getName()
//    {
//        return GCCoreUtil.translate("container.magneticcrafting");
//    }
//
//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        if (GCCoreUtil.getEffectiveSide() == LogicalSide.SERVER)
        {
            this.craftMatrix.clear();
            for (int i = 0; i < SIZEINVENTORY; ++i)
            {
                this.memory.set(i, ItemStack.EMPTY);
            }
            ListNBT contents = nbt.getList("Items", 10);
            if (!contents.isEmpty())
            {
                for (int i = 0; i < contents.size(); ++i)
                {
                    CompoundNBT var4 = contents.getCompound(i);
                    int slot = var4.getByte("Slot") & 255;

                    if (slot < SIZEINVENTORY)
                    {
                        this.craftMatrix.setInventorySlotContents(slot, ItemStack.read(var4));
                    }
                    else if (slot < 18)
                    {
                        this.memory.set(slot - SIZEINVENTORY, ItemStack.read(var4));
                    }
                }
            }
            CompoundNBT buffer = nbt.getCompound("buf");
            this.hiddenOutputBuffer = ItemStack.read(buffer);
            this.updateMemoryItem();
        }
        this.updateOverriddenStatus();
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        ListNBT list = new ListNBT();
        for (int i = 0; i < SIZEINVENTORY; ++i)
        {
            if (!this.craftMatrix.getStackInSlot(i).isEmpty())
            {
                CompoundNBT var4 = new CompoundNBT();
                var4.putByte("Slot", (byte) (i));
                this.craftMatrix.getStackInSlot(i).write(var4);
                list.add(var4);
            }
        }
        for (int i = 0; i < SIZEINVENTORY; ++i)
        {
            if (!this.memory.get(i).isEmpty())
            {
                CompoundNBT var4 = new CompoundNBT();
                var4.putByte("Slot", (byte) (i + SIZEINVENTORY));
                this.memory.get(i).write(var4);
                list.add(var4);
            }
        }

        nbt.put("Items", list);
        CompoundNBT buffer = new CompoundNBT();
        this.hiddenOutputBuffer.write(buffer);
        nbt.put("buf", buffer);
        return nbt;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        return this.world.getTileEntity(this.getPos()) == this && player.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }

    @Override
    public void remove()
    {
        super.remove();
        this.updateContainingBlockInfo();
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, Direction direction)
    {
        if (index >= SIZEINVENTORY || stack.isEmpty())
        {
            return false;
        }
        boolean override = this.overriddenMemory();
        ItemStack target = override ? this.craftMatrix.getStackInSlot(index) : this.memory.get(index);

        if (!target.isEmpty() && sameItem(target, stack))
        {
            ItemStack is3 = this.getStackInSlot(index);
            if (is3.isEmpty())
            {
                return true;
            }
            int currentSize = is3.getCount();

            //If any other slot matching this item has a smaller stacksize, return false (and hopefully that slot will be filled instead)
            for (int i = 0; i < SIZEINVENTORY; i++)
            {
                if (i == index)
                {
                    continue;
                }
                ItemStack targetOther = override ? this.craftMatrix.getStackInSlot(i) : this.memory.get(i);

                //It's another memory slot matching this item, and may be a more suitable recipient if the stack count is lower
                if (!targetOther.isEmpty() && sameItem(targetOther, stack))
                {
                    ItemStack itemstack2;
                    if (override)
                    {
                        itemstack2 = targetOther;   //Performance shortcut as we already know that targetOther is not empty
                    }
                    else
                    {
                        itemstack2 = this.craftMatrix.getStackInSlot(i);
                        if (itemstack2.isEmpty())
                        {
                            return false;    //A totally empty slot in the recipe needs this item
                        }
                    }

                    if (override || sameItem(stack, itemstack2))
                    {
                        if (itemstack2.getCount() < currentSize)
                        {
                            return false;    //A less well-filled slot in the recipe needs this item
                        }
                    }
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction)
    {
        return index == SIZEINVENTORY && (!this.hiddenOutputBuffer.isEmpty() || this.stillMatchesRecipe());
    }

    /**
     * Does not include a null check, do a null check first
     */
    private boolean sameItem(ItemStack target, ItemStack stack)
    {
        return target.getItem() == stack.getItem() /*&& (!stack.getHasSubtypes() || stack.getMetadata() == target.getMetadata())*/ && RecipeUtil.areItemStackTagsEqual(stack, target);
    }

    public boolean overrideMemory(ItemStack itemstack1, NonNullList<ItemStack> memory2)
    {
        boolean allEmpty = true;
        for (int i = 0; i < 9; i++)
        {
            if (!this.craftMatrix.getStackInSlot(i).isEmpty())
            {
                allEmpty = false;
                break;
            }
        }
        if (allEmpty)
        {
            return false;
        }
        Optional<ICraftingRecipe> optional = this.getWorld().getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, this.craftMatrix, this.getWorld());
//        if (!CraftingManager.findMatchingResult(this.craftMatrix, this.getWorld()).isEmpty())
        if (!optional.get().getCraftingResult(this.craftMatrix).isEmpty())
        {
            //Valid recipe on the table.  Does it fuzzy match this tile's memory (empty slots which should have recipe components are OK)
            boolean fuzzyMatch = true;
            for (int i = 0; i < 9; i++)
            {
                if (!this.craftMatrix.getStackInSlot(i).isEmpty() && !matchingStacks(this.craftMatrix.getStackInSlot(i), this.getMemory(i)))
                {
                    fuzzyMatch = false;
                    break;
                }
            }

            //If it's a valid recipe and CAN'T match the memory, then override the remembered recipe
            if (!fuzzyMatch)
            {
                for (int i = 0; i < 9; i++)
                {
                    if (matchingStacks(itemstack1, this.craftMatrix.getStackInSlot(i)))
                    {
                        for (int j = 0; j < 9; j++)
                        {
                            memory2.set(j, this.craftMatrix.getStackInSlot(j).copy());
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean overriddenMemory()
    {
        if (this.overriddenStatus != null)
        {
            return this.overriddenStatus;
        }
        Optional<ICraftingRecipe> optional = this.getWorld().getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, this.craftMatrix, this.getWorld());
//        if (!CraftingManager.findMatchingResult(this.craftMatrix, this.getWorld()).isEmpty())
        if (!optional.get().getCraftingResult(this.craftMatrix).isEmpty())
        {
            //Valid recipe on the table.  Does it fuzzy match this tile's memory (empty slots which should have recipe components are OK)
            for (int i = 0; i < 9; i++)
            {
                if (!this.craftMatrix.getStackInSlot(i).isEmpty() && !matchingStacks(this.craftMatrix.getStackInSlot(i), this.getMemory(i)))
                {
                    this.overriddenStatus = true;
                    return true;
                }
            }
        }
        this.overriddenStatus = false;
        return false;
    }

    private void updateOverriddenStatus()
    {
        this.overriddenStatus = null;
    }

    private boolean matchingStacks(ItemStack stack, ItemStack target)
    {
        return !target.isEmpty() && target.getItem() == stack.getItem() /*&& (!stack.getHasSubtypes() || stack.getMetadata() == target.getMetadata())*/ && RecipeUtil.areItemStackTagsEqual(stack, target) && target.isStackable() && target.getCount() < target.getMaxStackSize();
    }

    public void dropHiddenOutputBuffer(World worldIn, BlockPos pos)
    {
        ItemStack var7 = this.hiddenOutputBuffer;

        if (var7 != null && !var7.isEmpty())
        {
            Random syncRandom = GCCoreUtil.getRandom(pos);

            float var8 = syncRandom.nextFloat() * 0.8F + 0.1F;
            float var9 = syncRandom.nextFloat() * 0.8F + 0.1F;
            float var10 = syncRandom.nextFloat() * 0.8F + 0.1F;

            while (!var7.isEmpty())
            {
                ItemEntity var12 = new ItemEntity(worldIn, pos.getX() + var8, pos.getY() + var9, pos.getZ() + var10, var7.split(syncRandom.nextInt(21) + 10));
                float std = 0.05F;
                var12.setMotion(syncRandom.nextGaussian() * std, syncRandom.nextGaussian() * std + 0.2, syncRandom.nextGaussian() * std);
                worldIn.addEntity(var12);
            }
        }
    }

    public void setMemoryHeld(ItemStack stack)
    {
        this.memoryResult = stack.copy();
    }

    public ItemStack getMemoryHeld()
    {
        return this.memoryResult == null ? ItemStack.EMPTY : this.memoryResult;
    }

    private void updateMemoryItem()
    {
        PersistantInventoryCrafting memCopy = new PersistantInventoryCrafting();
        for (int i = 0; i < SIZEINVENTORY; i++)
        {
            memCopy.setInventorySlotContents(i, this.getMemory(i));
        }
        Optional<ICraftingRecipe> optional = this.getWorld().getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, this.craftMatrix, this.getWorld());
//        ItemStack stack = CraftingManager.findMatchingResult(memCopy, this.getWorld());
        ItemStack stack = optional.map(iCraftingRecipe -> iCraftingRecipe.getCraftingResult(memCopy)).orElse(ItemStack.EMPTY);
        if (!stack.isEmpty())
        {
            this.setMemoryHeld(stack);
        }
    }

    public void setStacksClientSide(ItemStack[] stacks)
    {
        for (int i = 0; i < SIZEINVENTORY; i++)
        {
            this.setInventorySlotContents(i, stacks[i]);
        }
        if (stacks.length > SIZEINVENTORY)
        {
            this.setMemoryHeld(stacks[SIZEINVENTORY]);
        }
    }

    @Override
    public void setSizeInventory(int size)
    {
        //Intentionally no operation
    }

    @Override
    public Container createMenu(int containerId, PlayerInventory playerInv, PlayerEntity player)
    {
        return new ContainerCrafting(containerId, playerInv, this);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.crafting");
    }
}