package micdoodle8.mods.galacticraft.core.tile;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.core.inventory.PersistantInventoryCrafting;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class TileEntityIngotCompressor extends TileEntityAdvanced implements IInventory, ISidedInventory, IPacketReceiver
{
    public static final int PROCESS_TIME_REQUIRED = 200;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTicks = 0;
    @NetworkedField(targetSide = Side.CLIENT)
    public int furnaceBurnTime = 0;
    @NetworkedField(targetSide = Side.CLIENT)
    public int currentItemBurnTime = 0;
    private long ticks;

    private ItemStack producingStack = null;
    private ItemStack[] containingItems = new ItemStack[2];
    public PersistantInventoryCrafting compressingCraftMatrix = new PersistantInventoryCrafting();
    public final Set<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();
    private static Random randnum = new Random();

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            boolean updateInv = false;
            boolean flag = this.furnaceBurnTime > 0;

            if (this.furnaceBurnTime > 0)
            {
                --this.furnaceBurnTime;
            }

            if (this.furnaceBurnTime == 0 && this.canSmelt())
            {
                ItemStack fuel = this.containingItems[0];
                this.currentItemBurnTime = this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(fuel);

                if (this.furnaceBurnTime > 0)
                {
                    updateInv = true;

                    if (fuel != null)
                    {
                        --fuel.stackSize;

                        if (fuel.stackSize == 0)
                        {
                            this.containingItems[0] = fuel.getItem().getContainerItem(fuel);
                        }
                    }
                }
            }

            if (this.furnaceBurnTime > 0 && this.canSmelt())
            {
                ++this.processTicks;

                if (this.processTicks % 40 == 0 && this.processTicks > TileEntityIngotCompressor.PROCESS_TIME_REQUIRED / 2)
                {
                    this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "random.anvil_land", 0.2F, 0.5F);
                }

                if (this.processTicks == TileEntityIngotCompressor.PROCESS_TIME_REQUIRED)
                {
                    this.processTicks = 0;
                    this.smeltItem();
                    updateInv = true;
                }
            }
            else
            {
                this.processTicks = 0;
            }

            if (flag != this.furnaceBurnTime > 0)
            {
                updateInv = true;
            }

            if (updateInv)
            {
                this.markDirty();
            }
        }

        if (this.ticks >= Long.MAX_VALUE)
        {
            this.ticks = 0;
        }

        this.ticks++;
    }

    @Override
    public void openInventory()
    {
    }

    @Override
    public void closeInventory()
    {
    }

    public void updateInput()
    {
        this.producingStack = CompressorRecipes.findMatchingRecipe(this.compressingCraftMatrix, this.worldObj);
    }

    private boolean canSmelt()
    {
        ItemStack itemstack = this.producingStack;
        if (itemstack == null)
        {
            return false;
        }
        if (this.containingItems[1] == null)
        {
            return true;
        }
        if (!this.containingItems[1].isItemEqual(itemstack))
        {
            return false;
        }
        int result = this.containingItems[1].stackSize + itemstack.stackSize;
        return result <= this.getInventoryStackLimit() && result <= itemstack.getMaxStackSize();
    }

    public static boolean isItemCompressorInput(ItemStack stack)
    {
        for (IRecipe recipe : CompressorRecipes.getRecipeList())
        {
            if (recipe instanceof ShapedRecipes)
            {
                for (ItemStack itemstack1 : ((ShapedRecipes) recipe).recipeItems)
                {
                    if (stack.getItem() == itemstack1.getItem() && (itemstack1.getItemDamage() == 32767 || stack.getItemDamage() == itemstack1.getItemDamage()))
                    {
                        return true;
                    }
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
                    else if (next instanceof ArrayList)
                    {
                        Iterator<ItemStack> itr = ((ArrayList<ItemStack>)next).iterator();
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
                
                return randnum.nextInt(match) == 0;
            }
        }

        return false;
    }

    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack resultItemStack = this.producingStack;

            if (this.containingItems[1] == null)
            {
                this.containingItems[1] = resultItemStack.copy();
            }
            else if (this.containingItems[1].isItemEqual(resultItemStack))
            {
                if (this.containingItems[1].stackSize + resultItemStack.stackSize > 64)
                {
                    for (int i = 0; i < this.containingItems[1].stackSize + resultItemStack.stackSize - 64; i++)
                    {
                        float var = 0.7F;
                        double dx = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        double dy = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        double dz = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        EntityItem entityitem = new EntityItem(this.worldObj, this.xCoord + dx, this.yCoord + dy, this.zCoord + dz, new ItemStack(resultItemStack.getItem(), 1, resultItemStack.getItemDamage()));

                        entityitem.delayBeforeCanPickup = 10;

                        this.worldObj.spawnEntityInWorld(entityitem);
                    }
                }

                this.containingItems[1].stackSize += resultItemStack.stackSize;
            }

            for (int i = 0; i < this.compressingCraftMatrix.getSizeInventory(); i++)
            {
                this.compressingCraftMatrix.decrStackSize(i, 1);
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
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
            else if (var5 < this.containingItems.length + this.compressingCraftMatrix.getSizeInventory())
            {
                this.compressingCraftMatrix.setInventorySlotContents(var5 - this.containingItems.length, ItemStack.loadItemStackFromNBT(var4));
            }
        }

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
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (par1 >= this.containingItems.length)
        {
            return this.compressingCraftMatrix.getStackInSlotOnClosing(par1 - this.containingItems.length);
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
    public String getInventoryName()
    {
        return GCCoreUtil.translate("tile.machine.3.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        if (slotID == 0)
        {
            return TileEntityFurnace.getItemBurnTime(itemStack) > 0;
        }
        else if (slotID >= 2)
        {
        	if (this.producingStack != null)
        	{
                ItemStack stackInSlot = this.getStackInSlot(slotID);
                return stackInSlot != null && stackInSlot.isItemEqual(itemStack);
        	}
        	return TileEntityIngotCompressor.isItemCompressorInput(itemStack);
        }

        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
    	if (side == 0) return new int[] { 1 };
    	int[] slots = new int[] { 0, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    	ArrayList<Integer> removeSlots = new ArrayList();
    	
    	for (int i = 1; i < slots.length; i++)
    	{
			if (removeSlots.contains(i)) continue;
    		ItemStack stack1 = this.getStackInSlot(i);
    		if (stack1 == null || stack1.stackSize <= 0) continue;
    		
    		for (int j = i + 1; j < slots.length; j++)
    		{
    			if (removeSlots.contains(j)) continue;
    			ItemStack stack2 = this.getStackInSlot(j);
    			if (stack2 == null) continue;
    			
    			if (stack1.isItemEqual(stack2))
    			{
    				if (stack2.stackSize >= stack1.stackSize)
    					removeSlots.add(j);
    				else
    					removeSlots.add(i);
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
    			if (i > 0 && removeSlots.contains(slots[i])) { continue; }
    			returnSlots[j] = slots[i];
    			j++;    			
        	}
        	
        	return returnSlots;
    	}
    	
    	return slots;
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack par2ItemStack, int par3)
    {
        return this.isItemValidForSlot(slotID, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack par2ItemStack, int par3)
    {
        return slotID == 1;
    }

    @Override
    public double getPacketRange()
    {
        return 12.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 3;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }
}
