package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.API.ICargoEntity;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoLoader.EnumCargoLoadingState;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoLoader.RemovalResult;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.multiblock.TileEntityMulti;
import universalelectricity.prefab.network.PacketManager;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GCCoreTileEntityCargoUnloader extends GCCoreTileEntityElectric implements IInventory, ISidedInventory, ICargoEntity
{
    private ItemStack[] containingItems = new ItemStack[15];
    public static final double WATTS_PER_TICK = 300;
    public boolean targetEmpty;
    public boolean targetNoInventory;
    public boolean noTarget;

    public ICargoEntity attachedFuelable;

    public GCCoreTileEntityCargoUnloader()
    {
        super(300, 130, 1, 1.0D);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (this.ticks % 100 == 0)
            {
                this.checkForCargoEntity();
            }

            if (this.attachedFuelable != null)
            {
                this.noTarget = false;
                RemovalResult result = this.attachedFuelable.removeCargo(false);

                if (result.resultStack != null)
                {
                    this.targetEmpty = false;

                    EnumCargoLoadingState state = this.addCargo(result.resultStack, false);

                    this.targetEmpty = state == EnumCargoLoadingState.EMPTY;

                    if (this.ticks % 15 == 0 && state == EnumCargoLoadingState.SUCCESS && !this.disabled && (this.ic2Energy > 0 || this.wattsReceived > 0 || this.getPowerProvider() != null && this.getPowerProvider().getEnergyStored() > 0))
                    {
                        this.addCargo(this.attachedFuelable.removeCargo(true).resultStack, true);
                    }
                }
                else
                {
                    this.targetNoInventory = result.resultState == EnumCargoLoadingState.NOINVENTORY;
                    this.noTarget = result.resultState == EnumCargoLoadingState.NOTARGET;
                    this.targetEmpty = true;
                }
            }
            else
            {
                this.noTarget = true;
            }
        }
    }

    public void checkForCargoEntity()
    {
        boolean foundFuelable = false;

        for (final ForgeDirection dir : ForgeDirection.values())
        {
            if (dir != ForgeDirection.UNKNOWN)
            {
                Vector3 vecAt = new Vector3(this);
                vecAt = vecAt.modifyPositionFromSide(dir);

                final TileEntity pad = vecAt.getTileEntity(this.worldObj);

                if (pad != null && pad instanceof TileEntityMulti)
                {
                    final TileEntity mainTile = ((TileEntityMulti) pad).mainBlockPosition.getTileEntity(this.worldObj);

                    if (mainTile != null && mainTile instanceof ICargoEntity)
                    {
                        this.attachedFuelable = (ICargoEntity) mainTile;
                        foundFuelable = true;
                        break;
                    }
                }
                else if (pad != null && pad instanceof ICargoEntity)
                {
                    this.attachedFuelable = (ICargoEntity) pad;
                    foundFuelable = true;
                    break;
                }
            }
        }

        if (!foundFuelable)
        {
            this.attachedFuelable = null;
        }
    }

    @Override
    public int getSizeInventory()
    {
        return this.containingItems.length;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.containingItems[par1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
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
        if (this.containingItems[par1] != null)
        {
            final ItemStack var2 = this.containingItems[par1];
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
        this.containingItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        final NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
            final byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);

        final NBTTagList list = new NBTTagList();

        for (int var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.containingItems[var3].writeToNBT(var4);
                list.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", list);
    }

    @Override
    public String getInvName()
    {
        return LanguageRegistry.instance().getStringLocalization("container.cargounloader.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return side == ForgeDirection.getOrientation(this.getBlockMetadata() - 2).getOpposite().ordinal() ? new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 } : new int[] {};
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
    {
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
    {
        if (side == ForgeDirection.getOrientation(this.getBlockMetadata() - 2).getOpposite().ordinal())
        {
            if (slotID == 0)
            {
                return itemstack.getItem() instanceof IItemElectric;
            }
            else
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }

    @Override
    public boolean isStackValidForSlot(int slotID, ItemStack itemstack)
    {
        if (slotID == 0)
        {
            return itemstack.getItem() instanceof IItemElectric;
        }
        else
        {
            return true;
        }
    }

    @Override
    public boolean shouldPullEnergy()
    {
        return !this.disabled;
    }

    @Override
    public void readPacket(ByteArrayDataInput data)
    {
        if (this.worldObj.isRemote)
        {
            this.wattsReceived = data.readDouble();
            this.ic2Energy = data.readDouble();
            this.disabled = data.readBoolean();
            this.disableCooldown = data.readInt();
            this.bcEnergy = data.readDouble();
            this.targetEmpty = data.readBoolean();
            this.noTarget = data.readBoolean();
            this.targetNoInventory = data.readBoolean();
        }
    }

    @Override
    public Packet getPacket()
    {
        return PacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.wattsReceived, this.ic2Energy, this.disabled, this.disableCooldown, this.getPowerProvider() != null ? (double) this.getPowerProvider().getEnergyStored() : 0.0D, this.targetEmpty, this.noTarget, this.targetNoInventory);
    }

    @Override
    public ForgeDirection getElectricInputDirection()
    {
        return ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    @Override
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        int count = 1;

        for (count = 1; count < this.containingItems.length; count++)
        {
            ItemStack stackAt = this.containingItems[count];

            if (stackAt != null && stackAt.itemID == stack.itemID && stackAt.getItemDamage() == stack.getItemDamage() && stackAt.stackSize < stackAt.getMaxStackSize())
            {
                if (doAdd)
                {
                    this.containingItems[count].stackSize += stack.stackSize;
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        for (count = 1; count < this.containingItems.length; count++)
        {
            ItemStack stackAt = this.containingItems[count];

            if (stackAt == null)
            {
                if (doAdd)
                {
                    this.containingItems[count] = stack;
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        return EnumCargoLoadingState.FULL;
    }

    @Override
    public RemovalResult removeCargo(boolean doRemove)
    {
        for (int i = 1; i < this.containingItems.length; i++)
        {
            ItemStack stackAt = this.containingItems[i];

            if (stackAt != null)
            {
                if (doRemove && --this.containingItems[i].stackSize <= 0)
                {
                    this.containingItems[i] = null;
                }

                return new RemovalResult(EnumCargoLoadingState.SUCCESS, new ItemStack(stackAt.itemID, 1, stackAt.getItemDamage()));
            }
        }

        return new RemovalResult(EnumCargoLoadingState.EMPTY, null);
    }
}
