package micdoodle8.mods.galacticraft.core.tile;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.inventory.PersistantInventoryCrafting;
import micdoodle8.mods.galacticraft.core.network.PacketDynamicInventory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityCrafting extends TileEntity implements IInventoryDefaults, ISidedInventory, IInventorySettable
{
    private static final int SIZEINVENTORY = 9;
    public PersistantInventoryCrafting craftMatrix = new PersistantInventoryCrafting();
    public NonNullList<ItemStack> memory = NonNullList.withSize(SIZEINVENTORY, ItemStack.EMPTY);
    private ItemStack hiddenOutputBuffer = ItemStack.EMPTY;   //Used for Buildcraft pipes and other inventory-slot setters which do not fully clear the results slot - see setInventorySlotContents()
    private Boolean overriddenStatus;
    private ItemStack memoryResult;

    public TileEntityCrafting()
    {
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

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    public void updateMemory(ItemStack craftingResult)
    {
        if (CraftingManager.findMatchingResult(this.craftMatrix, this.getWorld()) == ItemStack.EMPTY) return;
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
            return this.craftMatrix.getStackInSlot(par1);

        //Results slot
        //First, try to clear the hidden output buffer before generating new results items
        if (!this.hiddenOutputBuffer.isEmpty())
        {
            return this.hiddenOutputBuffer;
        }
        
        // Crafting Manager can produce concurrent modification exception in single player
        // if a server-side tick (e.g. from a Hopper) calls this while client-side is still initialising recipes
        try {
            return CraftingManager.findMatchingResult(this.craftMatrix, this.getWorld());
        } catch (Exception ignore) { }
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
                return this.hiddenOutputBuffer.splitStack(par2);
            }
            if (this.stillMatchesRecipe())
            {
                ItemStack craftingResult = CraftingManager.findMatchingResult(this.craftMatrix, this.getWorld());
                if (!craftingResult.isEmpty())
                {
                    this.pullOneResultStack();
                    this.markDirty();
                    this.updateOverriddenStatus();
                    ItemStack result = craftingResult.splitStack(par2);
                    this.hiddenOutputBuffer = craftingResult;  //save any balance
                    return result;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private void pullOneResultStack()
    {
//        Thread.dumpStack();
        NonNullList<ItemStack> aitemstack = CraftingManager.getRemainingItems(this.craftMatrix, this.world);

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

    protected boolean stillMatchesRecipe()
    {
        int emptyCount = 0;
        for (int i = 0; i < SIZEINVENTORY; i++)
        {
            ItemStack stack = this.craftMatrix.getStackInSlot(i);
            ItemStack targetOther = this.memory.get(i);
            if (targetOther.isEmpty() && stack.isEmpty())
                continue;

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
            ItemStack craftingResult = CraftingManager.findMatchingResult(this.craftMatrix, this.getWorld());
            if (!craftingResult.isEmpty())
            {
                this.pullOneResultStack();
            }
        }
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate("container.magneticcrafting.name");
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (GCCoreUtil.getEffectiveSide() == Side.SERVER)
        {
            this.craftMatrix.clear();
            for (int i = 0; i < SIZEINVENTORY; ++i)
                this.memory.set(i, ItemStack.EMPTY);
            NBTTagList contents = nbt.getTagList("Items", 10);
            if (contents != null && contents.tagCount() > 0)
            {
                for (int i = 0; i < contents.tagCount(); ++i)
                {
                    NBTTagCompound var4 = contents.getCompoundTagAt(i);
                    int slot = var4.getByte("Slot") & 255;

                    if (slot < SIZEINVENTORY)
                    {
                        this.craftMatrix.setInventorySlotContents(slot, new ItemStack(var4));
                    }
                    else if (slot < 18)
                    {
                        this.memory.set(slot - SIZEINVENTORY, new ItemStack(var4));
                    }
                }
            }
            NBTTagCompound buffer = nbt.getCompoundTag("buf");
            this.hiddenOutputBuffer = new ItemStack(buffer);
            this.updateMemoryItem();
        }
        this.updateOverriddenStatus();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        NBTTagList var2 = new NBTTagList();
        for (int i = 0; i < SIZEINVENTORY; ++i)
        {
            if (!this.craftMatrix.getStackInSlot(i).isEmpty())
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) (i));
                this.craftMatrix.getStackInSlot(i).writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        for (int i = 0; i < SIZEINVENTORY; ++i)
        {
            if (!this.memory.get(i).isEmpty())
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) (i + SIZEINVENTORY));
                this.memory.get(i).writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        nbt.setTag("Items", var2);
        NBTTagCompound buffer = new NBTTagCompound();
        this.hiddenOutputBuffer.writeToNBT(buffer);
        nbt.setTag("buf", buffer);
        return nbt;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.world.getTileEntity(this.getPos()) == this && player.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        this.updateContainingBlockInfo();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction)
    {
        if (index >= SIZEINVENTORY || stack.isEmpty()) return false;
        boolean override = this.overriddenMemory();
        ItemStack target = override ? this.craftMatrix.getStackInSlot(index) : this.memory.get(index);
        
		if (!target.isEmpty() && sameItem(target, stack))
        {
            ItemStack is3 = this.getStackInSlot(index);
            if (is3.isEmpty()) return true;
            int currentSize = is3.getCount();
            
            //If any other slot matching this item has a smaller stacksize, return false (and hopefully that slot will be filled instead)
            for (int i = 0; i < SIZEINVENTORY; i++)
            {
                if (i == index) continue;
                ItemStack targetOther = override ? this.craftMatrix.getStackInSlot(i) : this.memory.get(i);

                //It's another memory slot matching this item, and may be a more suitable recipient if the stack count is lower
                if (!targetOther.isEmpty() && sameItem(targetOther, stack))
                {
                    ItemStack itemstack2;
                    if (override) {
                        itemstack2 = targetOther;   //Performance shortcut as we already know that targetOther is not empty
                    } else {
                        itemstack2 = this.craftMatrix.getStackInSlot(i);
                        if (itemstack2.isEmpty())
                            return false;    //A totally empty slot in the recipe needs this item
                    }

                    if (override || sameItem(stack, itemstack2))
                    {
                        if (itemstack2.getCount() < currentSize)
                            return false;    //A less well-filled slot in the recipe needs this item
                    }
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return index == SIZEINVENTORY && (!this.hiddenOutputBuffer.isEmpty() || this.stillMatchesRecipe());
    }

    /**
     * Does not include a null check, do a null check first
     */
    private boolean sameItem(ItemStack target, ItemStack stack)
    {
        return target.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == target.getMetadata()) && RecipeUtil.areItemStackTagsEqual(stack, target);
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
        if (allEmpty) return false;
        if (!CraftingManager.findMatchingResult(this.craftMatrix, this.getWorld()).isEmpty())
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
        if (this.overriddenStatus != null) return this.overriddenStatus;
        if (!CraftingManager.findMatchingResult(this.craftMatrix, this.getWorld()).isEmpty())
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
        return !target.isEmpty() && target.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == target.getMetadata()) && RecipeUtil.areItemStackTagsEqual(stack, target) && target.isStackable() && target.getCount() < target.getMaxStackSize();
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
                EntityItem var12 = new EntityItem(worldIn, pos.getX() + var8, pos.getY() + var9, pos.getZ() + var10, var7.splitStack(syncRandom.nextInt(21) + 10));
                float var13 = 0.05F;
                var12.motionX = (float) syncRandom.nextGaussian() * var13;
                var12.motionY = (float) syncRandom.nextGaussian() * var13 + 0.2F;
                var12.motionZ = (float) syncRandom.nextGaussian() * var13;
                worldIn.spawnEntity(var12);
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
        ItemStack stack = CraftingManager.findMatchingResult(memCopy, this.getWorld());
        if (!stack.isEmpty()) this.setMemoryHeld(stack);
    }

    public void setStacksClientSide(ItemStack[] stacks)
    {
        for (int i = 0; i < SIZEINVENTORY; i++)
        {
            this.setInventorySlotContents(i, stacks[i]);
        }
        if (stacks.length > SIZEINVENTORY) this.setMemoryHeld(stacks[SIZEINVENTORY]);
    }

    @Override
    public void setSizeInventory(int size)
    {
        //Intentionally no operation
    }
}
