package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.inventory.PersistantInventoryCrafting;
import micdoodle8.mods.galacticraft.core.network.PacketDynamicInventory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.block.state.IBlockState;
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

public class TileEntityCrafting extends TileEntity implements IInventoryDefaults, ISidedInventory
{
    private static final int SIZEINVENTORY = 9;
    public PersistantInventoryCrafting craftMatrix = new PersistantInventoryCrafting();
    public NonNullList<ItemStack> memory = NonNullList.withSize(SIZEINVENTORY, ItemStack.EMPTY);

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

    public void updateMemory()
    {
        if (CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.getWorld()) == ItemStack.EMPTY) return;
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

        // Crafting Manager can produce concurrent modification exception in single player
        // if a server-side tick (e.g. from a Hopper) calls this while client-side is still initialising recipes
        try {
            return CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.getWorld());
        } catch (Exception ignore) { }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (par1 >= 0 && par1 < SIZEINVENTORY)
        {
            ItemStack result = this.craftMatrix.decrStackSize(par1, par2);
            this.markDirty();
            return result;
        }
        else if (par1 == SIZEINVENTORY)
        {
            if (this.stillMatchesRecipe())
            {
                ItemStack craftingResult = CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.getWorld());
                if (!craftingResult.isEmpty())
                {
                    this.pullOneResultStack();
                    this.markDirty();
                    return craftingResult;
                }
            }
        }
        return null;
    }
    
    private void pullOneResultStack()
    {
        NonNullList<ItemStack> aitemstack = CraftingManager.getInstance().getRemainingItems(this.craftMatrix, this.world);

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
            this.markDirty();
        }
        else if (par1 == SIZEINVENTORY && stack.isEmpty())
        {
            if (this.stillMatchesRecipe())
            {
                ItemStack craftingResult = CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.getWorld());
                if (craftingResult != null)
                {
                    this.pullOneResultStack();
                }
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
        }
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
        if (index >= SIZEINVENTORY) return false;
        boolean override = this.overriddenMemory();
        ItemStack target = override ? this.craftMatrix.getStackInSlot(index).copy() : this.memory.get(index);
        
		if (!target.isEmpty() && !stack.isEmpty() && sameItem(target, stack))
        {
            ItemStack is3 = this.getStackInSlot(index);
            if (is3.isEmpty()) return true;
            int currentSize = is3.getCount();
            
            //If any other slot matching this item has a smaller stacksize, return false (and hopefully that slot will be filled instead)
            for (int i = 0; i < SIZEINVENTORY; i++)
            {
                if (i == index) continue;
                ItemStack targetOther = override ? this.craftMatrix.getStackInSlot(i).copy() : this.memory.get(i);
                if (targetOther.isEmpty()) continue;
                //It's another memory slot matching this item 
                if (sameItem(targetOther, stack))
                {
                    ItemStack itemstack2 = this.craftMatrix.getStackInSlot(i);
                    if (itemstack2.isEmpty())
                        return false;

                    if (sameItem(stack, itemstack2))
                    {
                        if (itemstack2.getCount() < currentSize)
                            return false;
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
        return index == SIZEINVENTORY && this.stillMatchesRecipe();
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
        if (!CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.getWorld()).isEmpty())
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
        if (!CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.getWorld()).isEmpty())
        {
            //Valid recipe on the table.  Does it fuzzy match this tile's memory (empty slots which should have recipe components are OK)
            for (int i = 0; i < 9; i++)
            {
               if (!this.craftMatrix.getStackInSlot(i).isEmpty() && !matchingStacks(this.craftMatrix.getStackInSlot(i), this.getMemory(i)))
               {
                   return true;
               }
            }
        }
        return false;
    }
    
    private boolean matchingStacks(ItemStack stack, ItemStack target)
    {
        return !target.isEmpty() && target.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == target.getMetadata()) && RecipeUtil.areItemStackTagsEqual(stack, target) && target.isStackable() && target.getCount() < target.getMaxStackSize();
    }
}
