package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectricalSource;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class TileEntityCoalGenerator extends TileBaseUniversalElectricalSource implements IInventory, ISidedInventory, IPacketReceiver, IConnector
{
    //New energy rates:
    //
    //Tier 1 machine typically consumes 600 gJ/s = 30 gJ/t

    //Coal generator on max heat can power up to 4 Tier 1 machines
    //(fewer if one of them is an Electric Furnace)
    //Basic solar gen in full sun can power 1 Tier 1 machine

    //1 lump of coal is equivalent to 38400 gJ
    //because on max heat it produces 120 gJ/t over 320 ticks

    //Below the min_generate, all heat is wasted
    //At max generate, 100% efficient conversion coal energy -> electric makes 120 gJ/t
    public static final int MAX_GENERATE_GJ_PER_TICK = 150;
    public static final int MIN_GENERATE_GJ_PER_TICK = 30;

    private static final float BASE_ACCELERATION = 0.3f;

    public float prevGenerateWatts = 0;

    @NetworkedField(targetSide = Side.CLIENT)
    public float heatGJperTick = 0;

    @NetworkedField(targetSide = Side.CLIENT)
    public int itemCookTime = 0;
    private NonNullList<ItemStack> stacks = NonNullList.withSize(1, ItemStack.EMPTY);

    public TileEntityCoalGenerator()
    {
        this.storage.setMaxExtract(TileEntityCoalGenerator.MAX_GENERATE_GJ_PER_TICK - TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK);
    }

    @Override
    public void update()
    {
        if (this.heatGJperTick - TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK > 0)
        {
            this.receiveEnergyGC(null, (this.heatGJperTick - TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK), false);
        }

        super.update();

        if (!this.world.isRemote)
        {
            if (this.itemCookTime > 0)
            {
                this.itemCookTime--;

                this.heatGJperTick = Math.min(this.heatGJperTick + Math.max(this.heatGJperTick * 0.005F, TileEntityCoalGenerator.BASE_ACCELERATION), TileEntityCoalGenerator.MAX_GENERATE_GJ_PER_TICK);
            }

            if (this.itemCookTime <= 0 && !this.stacks.get(0).isEmpty())
            {
                if (this.stacks.get(0).getItem() == Items.COAL && this.stacks.get(0).getCount() > 0)
                {
                    this.itemCookTime = 320;
                    this.decrStackSize(0, 1);
                }
                else if (this.stacks.get(0).getItem() == Item.getItemFromBlock(Blocks.COAL_BLOCK) && this.stacks.get(0).getCount() > 0)
                {
                    this.itemCookTime = 320 * 10;
                    this.decrStackSize(0, 1);
                }
            }

            this.produce();

            if (this.itemCookTime <= 0)
            {
                this.heatGJperTick = Math.max(this.heatGJperTick - 0.3F, 0);
            }

            this.heatGJperTick = Math.min(Math.max(this.heatGJperTick, 0.0F), this.getMaxEnergyStoredGC());
        }
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.itemCookTime = nbt.getInteger("itemCookTime");
        this.heatGJperTick = nbt.getInteger("generateRateInt");

        this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.stacks);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("itemCookTime", this.itemCookTime);
        nbt.setFloat("generateRate", this.heatGJperTick);

        ItemStackHelper.saveAllItems(nbt, this.stacks);
        return nbt;
    }

    @Override
    public int getSizeInventory()
    {
        return this.stacks.size();
    }

    @Override
    public ItemStack getStackInSlot(int var1)
    {
        return this.stacks.get(var1);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.stacks, index, count);

        if (!itemstack.isEmpty())
        {
            this.markDirty();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.stacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.stacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
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
        return GCCoreUtil.translate("tile.machine.0.name");
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
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return itemstack.getItem() == Items.COAL || itemstack.getItem() == Item.getItemFromBlock(Blocks.COAL_BLOCK);
    }

//    @Override
//    public int[] getAccessibleSlotsFromSide(int var1)
//    {
//        return new int[] { 0 };
//    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing direction)
    {
        return this.isItemValidForSlot(slotID, itemstack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing direction)
    {
        return slotID == 0;
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

//    @Override
//    public boolean canInsertItem(int slotID, ItemStack itemstack, int j)
//    {
//        return this.isItemValidForSlot(slotID, itemstack);
//    }
//
//    @Override
//    public boolean canExtractItem(int slotID, ItemStack itemstack, int j)
//    {
//        return slotID == 0;
//    }

    @Override
    public float receiveElectricity(EnumFacing from, float energy, int tier, boolean doReceive)
    {
        return 0;
    }

	/*
    @Override
	public float getRequest(EnumFacing direction)
	{
		return 0;
	}
	*/

    @Override
    public EnumSet<EnumFacing> getElectricalInputDirections()
    {
        return EnumSet.noneOf(EnumFacing.class);
    }

    @Override
    public EnumSet<EnumFacing> getElectricalOutputDirections()
    {
        return EnumSet.of(this.getElectricOutputDirection());
    }

    public EnumFacing getFront()
    {
        return this.world.getBlockState(getPos()).getValue(BlockMachine.FACING);
    }

    @Override
    public EnumFacing getElectricOutputDirection()
    {
        return getFront().rotateY();
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        if (direction == null || type != NetworkType.POWER)
        {
            return false;
        }

        return direction == this.getElectricOutputDirection();
    }
}
