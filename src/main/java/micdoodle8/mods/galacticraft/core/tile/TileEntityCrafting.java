package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.inventory.PersistantInventoryCrafting;
import micdoodle8.mods.galacticraft.core.network.PacketDynamicInventory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityCrafting extends TileEntity implements IInventoryDefaults, ISidedInventory
{
    private static final int SIZEINVENTORY = 9;
    public PersistantInventoryCrafting craftMatrix = new PersistantInventoryCrafting();
    public ItemStack[] memory = new ItemStack[SIZEINVENTORY];

    public TileEntityCrafting()
    {
    }

    @Override
    public void onLoad()
    {
        if (this.worldObj.isRemote)
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
        if (CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.getWorld()) == null) return;
        for (int i = 0; i < SIZEINVENTORY; i++)
        {
            ItemStack stack = this.craftMatrix.getStackInSlot(i);
            if (stack == null)
            {
                this.memory[i] = null;
            }
            else
            {
                ItemStack stack2 = stack.copy();
                stack2.stackSize = 0;
                this.memory[i] = stack2;
            }
        }
    }
    
    public ItemStack getMemory(int i)
    {
        return this.memory[i];
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
            return result;
        }
        else if (par1 == SIZEINVENTORY)
        {
            if (this.stillMatchesRecipe())
            {
                ItemStack craftingResult = CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.getWorld());
                if (craftingResult != null)
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
        ItemStack[] aitemstack = CraftingManager.getInstance().func_180303_b(this.craftMatrix, this.worldObj);

        for (int i = 0; i < aitemstack.length; ++i)
        {
            ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            ItemStack itemstack1 = aitemstack[i];

            if (itemstack != null)
            {
                this.craftMatrix.decrStackSize(i, 1);
            }

            if (itemstack1 != null)
            {
                if (this.craftMatrix.getStackInSlot(i) == null)
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
            ItemStack targetOther = this.memory[i];
            if (targetOther == null && stack == null)
                continue;

            if (targetOther == null || stack == null || stack.stackSize <= 0 || !sameItem(targetOther, stack))
            {
                return false;
            }
        }
        return emptyCount < SIZEINVENTORY;
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (par1 >= 0)
        {
            return this.craftMatrix.removeStackFromSlot(par1);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        if (par1 >= 0 && par1 < SIZEINVENTORY)
        {
            this.craftMatrix.setInventorySlotContents(par1, par2ItemStack);
        }
        else if (par1 == SIZEINVENTORY && par2ItemStack == null || par2ItemStack.stackSize == 0)
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
                this.memory[i] = null;
            NBTTagList contents = nbt.getTagList("Items", 10);
            if (contents != null && contents.tagCount() > 0)
            {
                for (int i = 0; i < contents.tagCount(); ++i)
                {
                    NBTTagCompound var4 = contents.getCompoundTagAt(i);
                    int slot = var4.getByte("Slot") & 255;
    
                    if (slot < SIZEINVENTORY)
                    {
                        this.craftMatrix.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(var4));
                    }
                    else if (slot < 18)
                    {
                        this.memory[slot - SIZEINVENTORY] = ItemStack.loadItemStackFromNBT(var4);
                    }
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        NBTTagList var2 = new NBTTagList();
        for (int i = 0; i < SIZEINVENTORY; ++i)
        {
            if (this.craftMatrix.getStackInSlot(i) != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) (i));
                this.craftMatrix.getStackInSlot(i).writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        for (int i = 0; i < SIZEINVENTORY; ++i)
        {
            if (this.memory[i] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) (i + SIZEINVENTORY));
                this.memory[i].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        nbt.setTag("Items", var2);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
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
        ItemStack target = override ? ItemStack.copyItemStack(this.craftMatrix.getStackInSlot(index)) : this.memory[index];

        if (target != null && stack != null && sameItem(target, stack))
        {
            ItemStack is3 = this.getStackInSlot(index);
            if (is3 == null) return true;
            int currentSize = is3.stackSize;
            
            //If any other slot matching this item has a smaller stacksize, return false (and hopefully that slot will be filled instead)
            for (int i = 0; i < SIZEINVENTORY; i++)
            {
                if (i == index) continue;
                ItemStack targetOther = override ? ItemStack.copyItemStack(this.craftMatrix.getStackInSlot(i)) : this.memory[i];
                if (targetOther == null) continue;
                //It's another memory slot matching this item 
                if (sameItem(targetOther, stack))
                {
                    ItemStack itemstack2 = this.craftMatrix.getStackInSlot(i);
                    if (itemstack2 == null)
                        return false;

                    if (sameItem(stack, itemstack2))
                    {
                        if (itemstack2.stackSize < currentSize)
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
        return target.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == target.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, target);
    }

    public boolean overrideMemory(ItemStack itemstack1, ItemStack[] memory2)
    {
        boolean allEmpty = true;
        for (int i = 0; i < 9; i++)
        {
            if (this.craftMatrix.getStackInSlot(i) != null)
            {
                allEmpty = false;
                break;
            }
        }        
        if (allEmpty) return false;
        if (CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.getWorld()) != null)
        {
            //Valid recipe on the table.  Does it fuzzy match this tile's memory (empty slots which should have recipe components are OK)
            boolean fuzzyMatch = true;
            for (int i = 0; i < 9; i++)
            {
               if (this.craftMatrix.getStackInSlot(i) != null && !matchingStacks(this.craftMatrix.getStackInSlot(i), this.getMemory(i)))
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
                            memory2[j] = ItemStack.copyItemStack(this.craftMatrix.getStackInSlot(j));
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
        if (CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.getWorld()) != null)
        {
            //Valid recipe on the table.  Does it fuzzy match this tile's memory (empty slots which should have recipe components are OK)
            for (int i = 0; i < 9; i++)
            {
               if (this.craftMatrix.getStackInSlot(i) != null && !matchingStacks(this.craftMatrix.getStackInSlot(i), this.getMemory(i)))
               {
                   return true;
               }
            }
        }
        return false;
    }
    
    private boolean matchingStacks(ItemStack stack, ItemStack target)
    {
        return target != null && target.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == target.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, target) && target.isStackable() && target.stackSize < target.getMaxStackSize();
    }
}
