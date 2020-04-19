package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.ShapedRecipesGC;
import micdoodle8.mods.galacticraft.api.recipe.ShapelessOreRecipeGC;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine2;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine4;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachineBase;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.inventory.PersistantInventoryCrafting;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TileEntityElectricIngotCompressor extends TileBaseElectricBlock implements IInventoryDefaults, ISidedInventory, IMachineSides
{
    private static final int PROCESS_TIME_REQUIRED_BASE = 200;
    private int processTimeRequiredBase;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTimeRequired = PROCESS_TIME_REQUIRED_BASE;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTicks = 0;
    private ItemStack producingStack = ItemStack.EMPTY;
    private long ticks;
    private static final int[] allSlots = new int[] { 0, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
    private boolean advanced;

    public PersistantInventoryCrafting compressingCraftMatrix = new PersistantInventoryCrafting();
    private static Random randnum = new Random();

    public TileEntityElectricIngotCompressor()
    {
        super("tile.machine2.4.name");
        this.processTimeRequiredBase = PROCESS_TIME_REQUIRED_BASE; 
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 90 : 75);
        this.setTierGC(2);
        inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    public TileEntityElectricIngotCompressor(boolean advanced)
    {
        super("tile.machine4.11.name");
        this.processTimeRequiredBase = PROCESS_TIME_REQUIRED_BASE * 3 / 5;
        this.processTimeRequired = processTimeRequiredBase;
        this.advanced = true;
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 90 : 75);
        this.setTierGC(3);
        inventory = NonNullList.withSize(3, ItemStack.EMPTY);
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

                    this.processTimeRequired = this.processTimeRequiredBase / (1 + this.poweredByTierGC * 2);

                    if (this.processTicks >= this.processTimeRequired)
                    {
                        if (this.advanced)
                            this.world.playSound(null, this.getPos(), GCSounds.advanced_compressor, SoundCategory.BLOCKS, 0.23F, this.world.rand.nextFloat() * 0.1F + 9.5F);
                        else
                            this.world.playSound(null, this.getPos(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.12F, this.world.rand.nextFloat() * 0.1F - 9.5F);
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

    private boolean canCompress()
    {
        ItemStack itemstack = this.producingStack;
        if (itemstack.isEmpty())
        {
            return false;
        }
        if (this.getInventory().get(1).isEmpty() && this.getInventory().get(2).isEmpty())
        {
            return true;
        }
        if (!this.getInventory().get(1).isEmpty() && !this.getInventory().get(1).isItemEqual(itemstack) || !this.getInventory().get(2).isEmpty() && !this.getInventory().get(2).isItemEqual(itemstack))
        {
            return false;
        }
        int contents1 = this.getInventory().get(1).getCount();
        int contents2 = this.getInventory().get(2).getCount();
        int result = itemstack.getCount();
        if (ConfigManagerCore.quickMode && itemstack.getItem().getUnlocalizedName(itemstack).contains("compressed"))
        {
            result += result;
        }
        result += (contents2 < contents1) ? contents2 : contents1;
        return result <= this.getInventoryStackLimit() && result <= itemstack.getMaxStackSize();
    }

    public void updateInput()
    {
        this.producingStack = CompressorRecipes.findMatchingRecipe(this.compressingCraftMatrix, this.world);
    }

    public void compressItems()
    {
        int stackSize1 = this.getInventory().get(1).isEmpty() ? 0 : this.getInventory().get(1).getCount();
        int stackSize2 = this.getInventory().get(2).isEmpty() ? 0 : this.getInventory().get(2).getCount();

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

            if (this.getInventory().get(slot).isEmpty())
            {
                this.getInventory().set(slot, resultItemStack);
            }
            else if (this.getInventory().get(slot).isItemEqual(resultItemStack))
            {
                if (this.getInventory().get(slot).getCount() + resultItemStack.getCount() > resultItemStack.getMaxStackSize())
                {
					resultItemStack.grow(this.getInventory().get(slot).getCount() - resultItemStack.getMaxStackSize());
                    GCCoreUtil.spawnItem(this.world, this.getPos(), resultItemStack);
                    this.getInventory().get(slot).setCount(resultItemStack.getMaxStackSize());
                }
                else
                {
                    this.getInventory().get(slot).grow(resultItemStack.getCount());
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
    protected boolean handleInventory()
    {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.advanced = nbt.getBoolean("adv");
        if (this.advanced)  {
            this.processTimeRequiredBase = PROCESS_TIME_REQUIRED_BASE * 3 / 5;
            this.processTimeRequired = processTimeRequiredBase;
            this.setTierGC(3);
        }
        this.processTicks = nbt.getInteger("smeltingTicks");

        this.inventory = NonNullList.withSize(this.getSizeInventory() - this.compressingCraftMatrix.getSizeInventory(), ItemStack.EMPTY);
        NBTTagList nbttaglist = nbt.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventory.size())
            {
                this.inventory.set(j, new ItemStack(nbttagcompound));
            }
            else if (j < this.inventory.size() + this.compressingCraftMatrix.getSizeInventory())
            {
                this.compressingCraftMatrix.setInventorySlotContents(j - this.inventory.size(), new ItemStack(nbttagcompound));
            }
        }
        this.readMachineSidesFromNBT(nbt);  //Needed by IMachineSides
        this.updateInput();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setBoolean("adv", this.advanced);
        nbt.setInteger("smeltingTicks", this.processTicks);
        NBTTagList items = new NBTTagList();
        int i;

        for (i = 0; i < this.inventory.size(); ++i)
        {
            if (!this.inventory.get(i).isEmpty())
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) i);
                this.inventory.get(i).writeToNBT(var4);
                items.appendTag(var4);
            }
        }

        for (i = 0; i < this.compressingCraftMatrix.getSizeInventory(); ++i)
        {
            if (!this.compressingCraftMatrix.getStackInSlot(i).isEmpty())
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) (i + this.inventory.size()));
                this.compressingCraftMatrix.getStackInSlot(i).writeToNBT(var4);
                items.appendTag(var4);
            }
        }
        nbt.setTag("Items", items);

        this.addMachineSidesToNBT(nbt);  //Needed by IMachineSides
        return nbt;
    }

    @Override
    public int getSizeInventory()
    {
        return super.getSizeInventory() + this.compressingCraftMatrix.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        if (par1 >= this.getInventory().size())
        {
            return this.compressingCraftMatrix.getStackInSlot(par1 - this.inventory.size());
        }

        return this.inventory.get(par1);
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (par1 >= this.inventory.size())
        {
            ItemStack result = this.compressingCraftMatrix.decrStackSize(par1 - this.inventory.size(), par2);
            if (!result.isEmpty())
            {
                this.updateInput();
            }
            this.markDirty();
            return result;
        }

        if (!this.inventory.get(par1).isEmpty())
        {
            ItemStack var3;

            if (this.inventory.get(par1).getCount() <= par2)
            {
                var3 = this.inventory.get(par1);
                this.inventory.set(par1, ItemStack.EMPTY);
                this.markDirty();
                return var3;
            }
            else
            {
                var3 = this.inventory.get(par1).splitStack(par2);

                if (this.inventory.get(par1).isEmpty())
                {
                    this.inventory.set(par1, ItemStack.EMPTY);
                }

                this.markDirty();
                return var3;
            }
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (par1 >= this.inventory.size())
        {
        	this.markDirty();
            return this.compressingCraftMatrix.removeStackFromSlot(par1 - this.inventory.size());
        }

        if (!this.inventory.get(par1).isEmpty())
        {
            ItemStack var2 = this.inventory.get(par1);
            this.inventory.set(par1, ItemStack.EMPTY);
            this.markDirty();
            return var2;
        }
        else
        {
        	return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack stack)
    {
        if (par1 >= this.inventory.size())
        {
            this.compressingCraftMatrix.setInventorySlotContents(par1 - this.inventory.size(), stack);
            this.updateInput();
        }
        else
        {
            this.inventory.set(par1, stack);

            if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
            {
                stack.setCount(this.getInventoryStackLimit());
            }
        }
        this.markDirty();
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
            return itemStack != null && !itemStack.isEmpty() && ItemElectricBase.isElectricItem(itemStack.getItem());
        }
        else if (slotID >= 3)
        {
            if (!this.producingStack.isEmpty())
            {
                ItemStack stackInSlot = this.getStackInSlot(slotID);
                return !stackInSlot.isEmpty() && stackInSlot.isItemEqual(itemStack);
            }
        	return this.isItemCompressorInput(itemStack, slotID - 3);
        }

        return false;
    }

    public boolean isItemCompressorInput(ItemStack stack, int id)
    {
        for (IRecipe recipe : CompressorRecipes.getRecipeList())
        {
            if (recipe instanceof ShapedRecipesGC)
            {
                if (id >= ((ShapedRecipesGC) recipe).recipeItems.length) continue;
            	ItemStack itemstack1 = ((ShapedRecipesGC) recipe).recipeItems[id];
                if (stack.getItem() == itemstack1.getItem() && (itemstack1.getItemDamage() == 32767 || stack.getItemDamage() == itemstack1.getItemDamage()))
                {
                	for (int i = 0; i < ((ShapedRecipesGC) recipe).recipeItems.length; i++)
                	{
                		if (i == id) continue;
                        ItemStack itemstack2 = ((ShapedRecipesGC) recipe).recipeItems[i];
                        if (stack.getItem() == itemstack2.getItem() && (itemstack2.getItemDamage() == 32767 || stack.getItemDamage() == itemstack2.getItemDamage()))
                        {
                        	ItemStack is3 = this.getStackInSlot(id + 3);
                        	ItemStack is4 = this.getStackInSlot(i + 3);
                        	return is3.isEmpty() || !is4.isEmpty() && is3.getCount() < is4.getCount();
                        }
                	}
                	return true;
                }
            }
            else if (recipe instanceof ShapelessOreRecipeGC)
            {
                ArrayList<Object> required = new ArrayList<Object>(((ShapelessOreRecipeGC) recipe).getInput());
                
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
                	if (!inMatrix.isEmpty() && inMatrix.isItemEqual(stack))
                		slotsFilled++;
                }
                if (slotsFilled < match)
                {
                	return this.getStackInSlot(id + 3).isEmpty();
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
        ArrayList<Integer> removeSlots = new ArrayList<>();
        ArrayList<Integer> doneSlots = new ArrayList<>();

        for (int i = 3; i < 12; i++)
        {
            if (doneSlots.contains(i))
            {
                continue;
            }
            ItemStack stack1 = this.getStackInSlot(i);
            if (stack1.isEmpty())
            {
                continue;
            }

            int lowestCount = stack1.getCount();
            int lowestSlot = i;
            ArrayList<Integer> slotsWithSameItem = new ArrayList<>();
            for (int j = i + 1; j < 12; j++)
            {
                if (doneSlots.contains(j))
                {
                    continue;
                }
                ItemStack stack2 = this.getStackInSlot(j);
                if (stack2.isEmpty())
                {
                    continue;
                }

                if (stack1.isItemEqual(stack2))
                {
                    slotsWithSameItem.add(j);
                    if (stack2.getCount() < lowestCount)
                    {
                        lowestCount = stack2.getCount();
                        lowestSlot = j;
                    }
                }
            }
            if (!slotsWithSameItem.isEmpty())
            {
                if (lowestSlot != i) removeSlots.add(i);
                for (Integer k : slotsWithSameItem)
                {
                    doneSlots.add(k);
                    if (k.intValue() != lowestSlot) removeSlots.add(k);
                }
            }
        }

        if (removeSlots.size() > 0)
        {
            int[] returnSlots = new int[10 - removeSlots.size()];
            returnSlots[0] = 0;
            int j = 1;
            for (int i = 3; i < 12; i++)
            {
                if (removeSlots.contains(i))
                {
                    continue;
                }
                returnSlots[j++] = i;
            }

            return returnSlots;
        }

        return allSlots;
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
    public boolean shouldUseEnergy()
    {
        return this.processTicks > 0;
    }

    @Override
    public EnumFacing getFront()
    {
        return BlockMachineBase.getFront(this.world.getBlockState(getPos())); 
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_IN))
        {
        case RIGHT:
            return getFront().rotateYCCW();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return EnumFacing.UP;
        case BOTTOM:
            return EnumFacing.DOWN;
        case LEFT:
        default:
            return getFront().rotateY();
        }
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    //------------------
    //Added these methods and field to implement IMachineSides properly 
    //------------------
    @Override
    public MachineSide[] listConfigurableSides()
    {
        return new MachineSide[] { MachineSide.ELECTRIC_IN };
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return new Face[] { Face.LEFT };
    }
    
    private MachineSidePack[] machineSides;

    @Override
    public synchronized MachineSidePack[] getAllMachineSides()
    {
        if (this.machineSides == null)
        {
            this.initialiseSides();
        }

        return this.machineSides;
    }

    @Override
    public void setupMachineSides(int length)
    {
        this.machineSides = new MachineSidePack[length];
    }
    
    @Override
    public void onLoad()
    {
        this.clientOnLoad();
    }
    
    @Override
    public IMachineSidesProperties getConfigurationType()
    {
        return this.advanced ? BlockMachine4.MACHINESIDES_RENDERTYPE : BlockMachine2.MACHINESIDES_RENDERTYPE;
    }
    //------------------END OF IMachineSides implementation
}