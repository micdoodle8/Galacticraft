package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
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
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.*;

public class TileEntityIngotCompressor extends TileEntityAdvanced implements IInventory, ISidedInventory
{
    public static final int PROCESS_TIME_REQUIRED = 200;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTicks = 0;
    @NetworkedField(targetSide = Side.CLIENT)
    public int furnaceBurnTime = 0;
    @NetworkedField(targetSide = Side.CLIENT)
    public int currentItemBurnTime = 0;
    private long ticks;

    private ItemStack producingStack = ItemStack.EMPTY;
    private NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);
    public PersistantInventoryCrafting compressingCraftMatrix = new PersistantInventoryCrafting();
    public final Set<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();
    private static Random random = new Random();

    @Override
    public void update()
    {
        super.update();

        if (!this.world.isRemote)
        {
            boolean updateInv = false;
            boolean flag = this.furnaceBurnTime > 0;

            if (this.furnaceBurnTime > 0)
            {
                --this.furnaceBurnTime;
            }

            if (this.furnaceBurnTime == 0 && this.canSmelt())
            {
                ItemStack fuel = this.stacks.get(0);
                this.currentItemBurnTime = this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(fuel);

                if (this.furnaceBurnTime > 0)
                {
                    updateInv = true;

                    if (!fuel.isEmpty())
                    {
                        fuel.shrink(1);

                        if (fuel.getCount() == 0)
                        {
                            this.stacks.set(0, fuel.getItem().getContainerItem(fuel));
                        }
                    }
                }
            }

            if (this.furnaceBurnTime > 0 && this.canSmelt())
            {
                ++this.processTicks;

                if (this.processTicks % 40 == 0 && this.processTicks > TileEntityIngotCompressor.PROCESS_TIME_REQUIRED / 2)
                {
                    this.world.playSound(null, this.getPos(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.3F, this.world.rand.nextFloat() * 0.1F + 0.9F);
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

    public void updateInput()
    {
        this.producingStack = CompressorRecipes.findMatchingRecipe(this.compressingCraftMatrix, this.world);
    }

    private boolean canSmelt()
    {
        ItemStack itemstack = this.producingStack;
        if (itemstack.isEmpty())
        {
            return false;
        }
        if (this.stacks.get(1).isEmpty())
        {
            return true;
        }
        if (!this.stacks.get(1).isItemEqual(itemstack))
        {
            return false;
        }
        int result = this.stacks.get(1).getCount() + itemstack.getCount();
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
                        if (OreDictionary.itemMatches((ItemStack) next, stack, false))
                        {
                            match++;
                        }
                    }
                    else if (next instanceof List)
                    {
                        for (ItemStack itemStack : ((List<ItemStack>) next))
                        {
                            if (OreDictionary.itemMatches(itemStack, stack, false))
                            {
                                match++;
                                break;
                            }
                        }
                    }
                }

                if (match == 0)
                {
                    continue;
                }

                if (match == 1)
                {
                    return true;
                }

                return random.nextInt(match) == 0;
            }
        }

        return false;
    }

    private void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack resultItemStack = this.producingStack;
            if (ConfigManagerCore.quickMode)
            {
                if (resultItemStack.getItem().getUnlocalizedName(resultItemStack).contains("compressed"))
                {
                    resultItemStack.grow(resultItemStack.getCount());
                }
            }

            if (this.stacks.get(1).isEmpty())
            {
                this.stacks.set(1, resultItemStack.copy());
            }
            else if (this.stacks.get(1).isItemEqual(resultItemStack))
            {
                if (this.stacks.get(1).getCount() + resultItemStack.getCount() > 64)
                {
                    for (int i = 0; i < this.stacks.get(1).getCount() + resultItemStack.getCount() - 64; i++)
                    {
                        float var = 0.7F;
                        double dx = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        double dy = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        double dz = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        EntityItem entityitem = new EntityItem(this.world, this.getPos().getX() + dx, this.getPos().getY() + dy, this.getPos().getZ() + dz, new ItemStack(resultItemStack.getItem(), 1, resultItemStack.getItemDamage()));

                        entityitem.setPickupDelay(10);

                        this.world.spawnEntity(entityitem);
                    }
                    this.stacks.get(1).setCount(64);
                }
                else
                {
                    this.stacks.get(1).grow(resultItemStack.getCount());
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
        NBTTagList var2 = nbt.getTagList("Items", 10);

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
        int i;

        for (i = 0; i < this.stacks.size(); ++i)
        {
            if (!this.stacks.get(i).isEmpty())
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) i);
                this.stacks.get(i).writeToNBT(tagCompound);
                var2.appendTag(tagCompound);
            }
        }

        for (i = 0; i < this.compressingCraftMatrix.getSizeInventory(); ++i)
        {
            if (this.compressingCraftMatrix.getStackInSlot(i) != null)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) (i + this.stacks.size()));
                this.compressingCraftMatrix.getStackInSlot(i).writeToNBT(tagCompound);
                var2.appendTag(tagCompound);
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
            if (!result.isEmpty())
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
            return ItemStack.EMPTY;
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
        	return ItemStack.EMPTY;
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

    @Override
    public String getName()
    {
        return GCCoreUtil.translate("tile.machine.3.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.world.getTileEntity(this.getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
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
            return TileEntityFurnace.getItemBurnTime(itemStack) > 0;
        }
        else if (slotID >= 2)
        {
            if (!this.producingStack.isEmpty())
            {
                ItemStack stackInSlot = this.getStackInSlot(slotID);
                return stackInSlot != null && stackInSlot.isItemEqual(itemStack);
            }
            return TileEntityIngotCompressor.isItemCompressorInput(itemStack);
        }

        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        if (side == EnumFacing.DOWN)
        {
            return new int[] { 1 };
        }
        int[] slots = new int[] { 0, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        ArrayList<Integer> removeSlots = new ArrayList<>();

        for (int i = 2; i < 11; i++)
        {
            if (removeSlots.contains(i))
            {
                continue;
            }
            ItemStack stack1 = this.getStackInSlot(i);
            if (stack1 == null || stack1.isEmpty())
            {
                continue;
            }

            for (int j = i + 1; j < 11; j++)
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
}
