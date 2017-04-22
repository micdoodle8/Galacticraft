package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine2;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.inventory.PersistantInventoryCrafting;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TileEntityElectricIngotCompressor extends TileBaseElectricBlock implements IInventory, ISidedInventory
{
    public static final int PROCESS_TIME_REQUIRED_BASE = 200;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTimeRequired = PROCESS_TIME_REQUIRED_BASE;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTicks = 0;
    private ItemStack producingStack = null;
    private long ticks;

    private NonNullList<ItemStack> stacks = NonNullList.withSize(3, ItemStack.EMPTY);
    public PersistantInventoryCrafting compressingCraftMatrix = new PersistantInventoryCrafting();
    private static Random randnum = new Random();

    public TileEntityElectricIngotCompressor()
    {
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 90 : 75);
        this.setTierGC(2);
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.world.isRemote)
        {
            boolean updateInv = false;

            if (this.hasEnoughEnergyToRun)
            {
                if (this.canCompress())
                {
                    ++this.processTicks;

                    this.processTimeRequired = TileEntityElectricIngotCompressor.PROCESS_TIME_REQUIRED_BASE * 2 / (1 + this.poweredByTierGC);

                    if (this.processTicks >= this.processTimeRequired)
                    {
                        this.world.playSound(null, this.getPos(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.3F, this.world.rand.nextFloat() * 0.1F + 0.9F);
                        this.processTicks = 0;
                        this.compressItems();
                        updateInv = true;
                    }
                }
                else
                {
                    this.processTicks = 0;
                }
            }
            else
            {
                this.processTicks = 0;
            }

            if (updateInv)
            {
                this.markDirty();
            }
        }

        this.ticks++;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }

    private boolean canCompress()
    {
        ItemStack itemstack = this.producingStack;
        if (itemstack.isEmpty())
        {
            return false;
        }
        if (this.stacks.get(1).isEmpty() && this.stacks.get(2).isEmpty())
        {
            return true;
        }
        if (!this.stacks.get(1).isEmpty() && !this.stacks.get(1).isItemEqual(itemstack) || !this.stacks.get(2).isEmpty() && !this.stacks.get(2).isItemEqual(itemstack))
        {
            return false;
        }
        int result = this.stacks.get(1).isEmpty() ? 0 : this.stacks.get(1).getCount() + itemstack.getCount();
        int result2 = this.stacks.get(2).isEmpty() ? 0 : this.stacks.get(2).getCount() + itemstack.getCount();
        return result <= this.getInventoryStackLimit() && result <= itemstack.getMaxStackSize() && result2 <= this.getInventoryStackLimit() && result2 <= itemstack.getMaxStackSize();
    }

    public void updateInput()
    {
        this.producingStack = CompressorRecipes.findMatchingRecipe(this.compressingCraftMatrix, this.world);
    }

    public void compressItems()
    {
        int stackSize1 = this.stacks.get(1).isEmpty() ? 0 : this.stacks.get(1).getCount();
        int stackSize2 = this.stacks.get(2).isEmpty() ? 0 : this.stacks.get(2).getCount();

        if (stackSize1 <= stackSize2)
        {
            this.compressIntoSlot(1);
            this.compressIntoSlot(2);
        }
        else
        {
            this.compressIntoSlot(2);
            this.compressIntoSlot(1);
        }
    }

    private void compressIntoSlot(int slot)
    {
        if (this.canCompress())
        {
            ItemStack resultItemStack = this.producingStack.copy();
            if (ConfigManagerCore.quickMode)
            {
                if (resultItemStack.getItem().getUnlocalizedName(resultItemStack).contains("compressed"))
                {
                    resultItemStack.grow(resultItemStack.getCount());
                }
            }

            if (this.stacks.get(slot).isEmpty())
            {
                this.stacks.set(slot, resultItemStack);
            }
            else if (this.stacks.get(slot).isItemEqual(resultItemStack))
            {
                if (this.stacks.get(slot).getCount() + resultItemStack.getCount() > 64)
                {
                    for (int i = 0; i < this.stacks.get(slot).getCount() + resultItemStack.getCount() - 64; i++)
                    {
                        float var = 0.7F;
                        double dx = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        double dy = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        double dz = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        EntityItem entityitem = new EntityItem(this.world, this.getPos().getX() + dx, this.getPos().getY() + dy, this.getPos().getZ() + dz, new ItemStack(resultItemStack.getItem(), 1, resultItemStack.getItemDamage()));

                        entityitem.setPickupDelay(10);

                        this.world.spawnEntity(entityitem);
                    }
                    this.stacks.get(slot).setCount(64);
                }
                else
                {
                    this.stacks.get(slot).grow(resultItemStack.getCount());
                }
            }

            for (int i = 0; i < this.compressingCraftMatrix.getSizeInventory(); i++)
            {
                if (!this.compressingCraftMatrix.getStackInSlot(i).isEmpty() && this.compressingCraftMatrix.getStackInSlot(i).getItem() == Items.WATER_BUCKET)
                {
                    this.compressingCraftMatrix.setInventorySlotContentsNoUpdate(i, new ItemStack(Items.BUCKET));
                }
                else
                {
                    this.compressingCraftMatrix.decrStackSize(i, 1);
                }
            }

            this.updateInput();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.processTicks = nbt.getInteger("smeltingTicks");

        this.stacks = NonNullList.withSize(this.getSizeInventory() - this.compressingCraftMatrix.getSizeInventory(), ItemStack.EMPTY);
        NBTTagList nbttaglist = nbt.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.stacks.size())
            {
                this.stacks.set(j, new ItemStack(nbttagcompound));
            }
            else if (j < this.stacks.size() + this.compressingCraftMatrix.getSizeInventory())
            {
                this.compressingCraftMatrix.setInventorySlotContents(j - this.stacks.size(), new ItemStack(nbttagcompound));
            }
        }

        this.updateInput();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("smeltingTicks", this.processTicks);
        NBTTagList var2 = new NBTTagList();
        int var3;

        for (var3 = 0; var3 < this.stacks.size(); ++var3)
        {
            if (!this.stacks.get(var3).isEmpty())
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.stacks.get(var3).writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        for (var3 = 0; var3 < this.compressingCraftMatrix.getSizeInventory(); ++var3)
        {
            if (this.compressingCraftMatrix.getStackInSlot(var3) != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) (var3 + this.stacks.size()));
                this.compressingCraftMatrix.getStackInSlot(var3).writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        nbt.setTag("Items", var2);
        return nbt;
    }

    @Override
    public int getSizeInventory()
    {
        return this.stacks.size() + this.compressingCraftMatrix.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        if (par1 >= this.stacks.size())
        {
            return this.compressingCraftMatrix.getStackInSlot(par1 - this.stacks.size());
        }

        return this.stacks.get(par1);
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (par1 >= this.stacks.size())
        {
            ItemStack result = this.compressingCraftMatrix.decrStackSize(par1 - this.stacks.size(), par2);
            if (result != null)
            {
                this.updateInput();
            }
            return result;
        }

        if (!this.stacks.get(par1).isEmpty())
        {
            ItemStack var3;

            if (this.stacks.get(par1).getCount() <= par2)
            {
                var3 = this.stacks.get(par1);
                this.stacks.set(par1, ItemStack.EMPTY);
                return var3;
            }
            else
            {
                var3 = this.stacks.get(par1).splitStack(par2);

                if (this.stacks.get(par1).isEmpty())
                {
                    this.stacks.set(par1, ItemStack.EMPTY);
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (par1 >= this.stacks.size())
        {
            return this.compressingCraftMatrix.removeStackFromSlot(par1 - this.stacks.size());
        }

        if (!this.stacks.get(par1).isEmpty())
        {
            ItemStack var2 = this.stacks.get(par1);
            this.stacks.set(par1, ItemStack.EMPTY);
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack stack)
    {
        if (par1 >= this.stacks.size())
        {
            this.compressingCraftMatrix.setInventorySlotContents(par1 - this.stacks.size(), stack);
            this.updateInput();
        }
        else
        {
            this.stacks.set(par1, stack);

            if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
            {
                stack.setCount(this.getInventoryStackLimit());
            }
        }
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate("tile.machine2.4.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return this.world.getTileEntity(this.getPos()) == this && entityplayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.stacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        if (slotID == 0)
        {
            return itemStack != null && ItemElectricBase.isElectricItem(itemStack.getItem());
        }
        else if (slotID >= 3)
        {
            if (this.producingStack != null)
            {
                ItemStack stackInSlot = this.getStackInSlot(slotID);
                return stackInSlot != null && stackInSlot.isItemEqual(itemStack);
            }
        	return this.isItemCompressorInput(itemStack, slotID - 3);
        }

        return false;
    }

    public boolean isItemCompressorInput(ItemStack stack, int id)
    {
        for (IRecipe recipe : CompressorRecipes.getRecipeList())
        {
            if (recipe instanceof ShapedRecipes)
            {
                if (id >= ((ShapedRecipes) recipe).recipeItems.length) continue;
            	ItemStack itemstack1 = ((ShapedRecipes) recipe).recipeItems[id];
                if (stack.getItem() == itemstack1.getItem() && (itemstack1.getItemDamage() == 32767 || stack.getItemDamage() == itemstack1.getItemDamage()))
                {
                	for (int i = 0; i < ((ShapedRecipes) recipe).recipeItems.length; i++)
                	{
                		if (i == id) continue;
                        ItemStack itemstack2 = ((ShapedRecipes) recipe).recipeItems[i];
                        if (stack.getItem() == itemstack2.getItem() && (itemstack2.getItemDamage() == 32767 || stack.getItemDamage() == itemstack2.getItemDamage()))
                        {
                        	ItemStack is3 = this.getStackInSlot(id + 3);
                        	ItemStack is4 = this.getStackInSlot(i + 3);
                        	return is3 == null || is4 != null && is3.getCount() < is4.getCount();
                        }
                	}
                	return true;
                }
            }
            else if (recipe instanceof ShapelessOreRecipe)
            {
                @SuppressWarnings("unchecked")
                ArrayList<Object> required = new ArrayList<Object>(((ShapelessOreRecipe) recipe).getInput());
                
                Iterator<Object> req = required.iterator();

                int match = 0;

                while (req.hasNext())
                {
                    Object next = req.next();

                    if (next instanceof ItemStack)
                    {
                        if ( OreDictionary.itemMatches((ItemStack)next, stack, false)) match++;
                    }
                    else if (next instanceof List)
                    {
                        Iterator<ItemStack> itr = ((List<ItemStack>)next).iterator();
                        while (itr.hasNext())
                        {
                            if (OreDictionary.itemMatches(itr.next(), stack, false))
                            {
                            	match++;
                            	break;
                            }
                        }
                    }
                }
                
                if (match == 0) continue;
                
                if (match == 1) return true;
                
                //Shapeless recipe can go into (match) number of slots
                int slotsFilled = 0;
                for (int i = 3; i < 12; i++)
                {
                	ItemStack inMatrix = this.getStackInSlot(i); 
                	if (inMatrix != null && inMatrix.isItemEqual(stack))
                		slotsFilled++;
                }
                if (slotsFilled < match)
                {
                	return this.getStackInSlot(id + 3) == null;
                }
                	
                return randnum.nextInt(match) == 0;
            }
        }

        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        if (side == EnumFacing.DOWN)
        {
            return new int[] { 1, 2 };
        }
        int[] slots = new int[] { 0, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
        ArrayList<Integer> removeSlots = new ArrayList();

        for (int i = 3; i < 12; i++)
        {
            if (removeSlots.contains(i))
            {
                continue;
            }
            ItemStack stack1 = this.getStackInSlot(i);
            if (stack1 == null || stack1.getCount() <= 0)
            {
                continue;
            }

            for (int j = i + 1; j < 12; j++)
            {
                if (removeSlots.contains(j))
                {
                    continue;
                }
                ItemStack stack2 = this.getStackInSlot(j);
                if (stack2 == null)
                {
                    continue;
                }

                if (stack1.isItemEqual(stack2))
                {
                    if (stack2.getCount() >= stack1.getCount())
                    {
                        removeSlots.add(j);
                    }
                    else
                    {
                        removeSlots.add(i);
                    }
                    break;
                }
            }
        }

        if (removeSlots.size() > 0)
        {
            int[] returnSlots = new int[slots.length - removeSlots.size()];
            int j = 0;
            for (int i = 0; i < slots.length; i++)
            {
                if (i > 0 && removeSlots.contains(slots[i]))
                {
                    continue;
                }
                returnSlots[j] = slots[i];
                j++;
            }

            return returnSlots;
        }

        return slots;
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack par2ItemStack, EnumFacing par3)
    {
        return this.isItemValidForSlot(slotID, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack par2ItemStack, EnumFacing par3)
    {
        return slotID == 1 || slotID == 2;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {

    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {

    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.processTicks > 0;
    }

    @Override
    public EnumFacing getFront()
    {
        return this.world.getBlockState(getPos()).getValue(BlockMachine2.FACING);
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return getFront().rotateY();
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }
}
