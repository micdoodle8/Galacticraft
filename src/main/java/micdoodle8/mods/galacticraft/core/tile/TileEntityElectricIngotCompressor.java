package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.ShapedRecipesGC;
import micdoodle8.mods.galacticraft.api.recipe.ShapelessOreRecipeGC;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine2;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.inventory.PersistantInventoryCrafting;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TileEntityElectricIngotCompressor extends TileBaseElectricBlock implements IInventoryDefaults, ISidedInventory, IMachineSides
{
    public static final int PROCESS_TIME_REQUIRED_BASE = 200;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTimeRequired = PROCESS_TIME_REQUIRED_BASE;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTicks = 0;
    private ItemStack producingStack = null;
    private long ticks;

    private ItemStack[] containingItems = new ItemStack[3];
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

        if (!this.worldObj.isRemote)
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
                        this.worldObj.playSoundEffect(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), "random.anvil_land", 0.2F, 0.5F);
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
        if (itemstack == null)
        {
            return false;
        }
        if (this.containingItems[1] == null && this.containingItems[2] == null)
        {
            return true;
        }
        if (this.containingItems[1] != null && !this.containingItems[1].isItemEqual(itemstack) || this.containingItems[2] != null && !this.containingItems[2].isItemEqual(itemstack))
        {
            return false;
        }
        int contents1 = this.containingItems[1] == null ? 0 : this.containingItems[1].stackSize;
        int contents2 = this.containingItems[2] == null ? 0 : this.containingItems[2].stackSize;
        int result = itemstack.stackSize;
        if (ConfigManagerCore.quickMode && itemstack.getItem().getUnlocalizedName(itemstack).contains("compressed"))
        {
            result += result;
        }
        result += (contents2 < contents1) ? contents2 : contents1;
        return result <= this.getInventoryStackLimit() && result <= itemstack.getMaxStackSize();
    }

    public void updateInput()
    {
        this.producingStack = CompressorRecipes.findMatchingRecipe(this.compressingCraftMatrix, this.worldObj);
    }

    public void compressItems()
    {
        int stackSize1 = this.containingItems[1] == null ? 0 : this.containingItems[1].stackSize;
        int stackSize2 = this.containingItems[2] == null ? 0 : this.containingItems[2].stackSize;

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
                    resultItemStack.stackSize *= 2;
                }
            }

            if (this.containingItems[slot] == null)
            {
                this.containingItems[slot] = resultItemStack;
            }
            else if (this.containingItems[slot].isItemEqual(resultItemStack))
            {
                if (this.containingItems[slot].stackSize + resultItemStack.stackSize > resultItemStack.getMaxStackSize())
                {
                    resultItemStack.stackSize = this.containingItems[slot].stackSize + resultItemStack.stackSize - resultItemStack.getMaxStackSize();
                    GCCoreUtil.spawnItem(this.worldObj, this.getPos(), resultItemStack);
                    this.containingItems[slot].stackSize = resultItemStack.getMaxStackSize();
                }
                else
                {
                    this.containingItems[slot].stackSize += resultItemStack.stackSize;
                }
            }

            for (int i = 0; i < this.compressingCraftMatrix.getSizeInventory(); i++)
            {
                if (this.compressingCraftMatrix.getStackInSlot(i) != null && this.compressingCraftMatrix.getStackInSlot(i).getItem() == Items.water_bucket)
                {
                    this.compressingCraftMatrix.setInventorySlotContentsNoUpdate(i, new ItemStack(Items.bucket));
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
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.processTicks = par1NBTTagCompound.getInteger("smeltingTicks");
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
        this.containingItems = new ItemStack[this.getSizeInventory() - this.compressingCraftMatrix.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
            else if (var5 < this.containingItems.length + this.compressingCraftMatrix.getSizeInventory())
            {
                this.compressingCraftMatrix.setInventorySlotContents(var5 - this.containingItems.length, ItemStack.loadItemStackFromNBT(var4));
            }
        }
        this.readMachineSidesFromNBT(par1NBTTagCompound);  //Needed by IMachineSides
        this.updateInput();
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("smeltingTicks", this.processTicks);
        NBTTagList var2 = new NBTTagList();
        int var3;

        for (var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.containingItems[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        for (var3 = 0; var3 < this.compressingCraftMatrix.getSizeInventory(); ++var3)
        {
            if (this.compressingCraftMatrix.getStackInSlot(var3) != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) (var3 + this.containingItems.length));
                this.compressingCraftMatrix.getStackInSlot(var3).writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        par1NBTTagCompound.setTag("Items", var2);

        this.addMachineSidesToNBT(par1NBTTagCompound);  //Needed by IMachineSides
    }

    @Override
    public int getSizeInventory()
    {
        return this.containingItems.length + this.compressingCraftMatrix.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        if (par1 >= this.containingItems.length)
        {
            return this.compressingCraftMatrix.getStackInSlot(par1 - this.containingItems.length);
        }

        return this.containingItems[par1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (par1 >= this.containingItems.length)
        {
            ItemStack result = this.compressingCraftMatrix.decrStackSize(par1 - this.containingItems.length, par2);
            if (result != null)
            {
                this.updateInput();
            }
            return result;
        }

        if (this.containingItems[par1] != null)
        {
            ItemStack var3;

            if (this.containingItems[par1].stackSize <= par2)
            {
                var3 = this.containingItems[par1];
                this.containingItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.containingItems[par1].splitStack(par2);

                if (this.containingItems[par1].stackSize == 0)
                {
                    this.containingItems[par1] = null;
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
        if (par1 >= this.containingItems.length)
        {
            return this.compressingCraftMatrix.removeStackFromSlot(par1 - this.containingItems.length);
        }

        if (this.containingItems[par1] != null)
        {
            ItemStack var2 = this.containingItems[par1];
            this.containingItems[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        if (par1 >= this.containingItems.length)
        {
            this.compressingCraftMatrix.setInventorySlotContents(par1 - this.containingItems.length, par2ItemStack);
            this.updateInput();
        }
        else
        {
            this.containingItems[par1] = par2ItemStack;

            if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
            {
                par2ItemStack.stackSize = this.getInventoryStackLimit();
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
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return this.worldObj.getTileEntity(this.getPos()) == this && entityplayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
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
                        	return is3 == null || is4 != null && is3.stackSize < is4.stackSize;
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
        ArrayList<Integer> removeSlots = new ArrayList<>();

        for (int i = 3; i < 12; i++)
        {
            if (removeSlots.contains(i))
            {
                continue;
            }
            ItemStack stack1 = this.getStackInSlot(i);
            if (stack1 == null || stack1.stackSize <= 0)
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
                    if (stack2.stackSize >= stack1.stackSize)
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
    public boolean shouldUseEnergy()
    {
        return this.processTicks > 0;
    }

    @Override
    public EnumFacing getFront()
    {
        IBlockState state = this.worldObj.getBlockState(getPos()); 
        if (state.getBlock() instanceof BlockMachine2)
        {
            return state.getValue(BlockMachine2.FACING);
        }
        return EnumFacing.NORTH;
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
    public MachineSidePack[] getAllMachineSides()
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
        return BlockMachine2.MACHINESIDES_RENDERTYPE;
    }
    //------------------END OF IMachineSides implementation
}