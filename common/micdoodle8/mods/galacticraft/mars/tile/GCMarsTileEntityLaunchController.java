package micdoodle8.mods.galacticraft.mars.tile;

import java.util.EnumSet;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoPad;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectric;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlockMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IElectrical;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GCMarsTileEntityLaunchController extends GCCoreTileEntityElectric implements IElectrical, IInventory, ISidedInventory, IPacketReceiver, ILandingPadAttachable
{
    public static final float WATTS_PER_TICK = 0.05000001f;
    private ItemStack[] containingItems = new ItemStack[1];
    private boolean launchPadRemovalDisabled = true;

    public GCMarsTileEntityLaunchController()
    {
        super(WATTS_PER_TICK, 50);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        this.discharge(this.containingItems[0]);

        if (!this.worldObj.isRemote)
        {
        }
    }

    @Override
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        NBTTagList var2 = nbt.getTagList("Items");
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
        
        this.setDisabled(1, nbt.getBoolean("LeaveLaunchPads"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.containingItems[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        nbt.setTag("Items", var2);
        nbt.setBoolean("LeaveLaunchPads", this.getDisabled(1));
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
        this.containingItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName()
    {
        return LanguageRegistry.instance().getStringLocalization("container.launchcontroller.name");
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
    public boolean isInvNameLocalized()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        return slotID == 0 ?  itemStack.getItem() instanceof IItemElectric : false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack par2ItemStack, int par3)
    {
        return this.isItemValidForSlot(slotID, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack par2ItemStack, int par3)
    {
        return slotID == 0;
    }

    @Override
    public EnumSet<ForgeDirection> getOutputDirections()
    {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    @Override
    public float getProvide(ForgeDirection direction)
    {
        return 0;
    }

    @Override
    public boolean shouldPullEnergy()
    {
        return this.getEnergyStored() <= this.getMaxEnergyStored() - this.ueWattsPerTick;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return !this.getDisabled(0);
    }

    @Override
    public void readPacket(ByteArrayDataInput data)
    {
        try
        {
            if (this.worldObj.isRemote)
            {
                this.setEnergyStored(data.readFloat());
                this.disabled = data.readBoolean();
                this.launchPadRemovalDisabled = data.readBoolean();
                this.disableCooldown = data.readInt();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Packet getPacket()
    {
        return PacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.getEnergyStored(), this.getDisabled(0), this.getDisabled(1), this.disableCooldown);
    }

    @Override
    public ForgeDirection getElectricInputDirection()
    {
        return ForgeDirection.getOrientation(this.getBlockMetadata() - GCMarsBlockMachine.LAUNCH_CONTROLLER_METADATA + 2);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            switch (index)
            {
            case 0:
                this.disabled = disabled;
                break;
            case 1:
                this.launchPadRemovalDisabled = disabled;
                break;
            }
            
            this.disableCooldown = 20;
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        switch (index)
        {
        case 0:
            return this.disabled;
        case 1:
            return this.launchPadRemovalDisabled;
        }
        
        return true;
    }

    @Override
    public boolean canAttachToLandingPad(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        
        return tile instanceof GCCoreTileEntityLandingPad || tile instanceof GCCoreTileEntityCargoPad;
    }
}
