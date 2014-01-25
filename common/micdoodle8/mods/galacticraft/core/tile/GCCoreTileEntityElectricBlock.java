package micdoodle8.mods.galacticraft.core.tile;

import java.util.EnumSet;

import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.ASMHelper.RuntimeInterface;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.power.ElectricityPack;
import micdoodle8.mods.galacticraft.power.core.item.ElectricItemHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

/**
 * GCCoreTileEntityElectric.java
 *
 * This file is part of the Galacticraft project
 *
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class GCCoreTileEntityElectricBlock extends GCCoreTileEntityUniversalElectrical implements IPacketReceiver, IDisableableMachine
{
    public float ueWattsPerTick;
    private final float ueMaxEnergy;

    public boolean disabled = true;
    public int disableCooldown = 0;

    public abstract boolean shouldPullEnergy();

    public abstract boolean shouldUseEnergy();

    public abstract void readPacket(ByteArrayDataInput data);

    public abstract Packet getPacket();

    public abstract ForgeDirection getElectricInputDirection();

    public abstract ItemStack getBatteryInSlot();

    public GCCoreTileEntityElectricBlock(float ueWattsPerTick, float maxEnergy)
    {
        this.ueMaxEnergy = maxEnergy;
        this.ueWattsPerTick = ueWattsPerTick;

        /*
         * if (PowerFramework.currentFramework != null) { this.bcPowerProvider =
         * new GCCoreLinkedPowerProvider(this);
         * this.bcPowerProvider.configure(20, 1, 10, 10, 1000); }
         */
    }

    @Override
    public float getMaxEnergyStored()
    {
        return this.ueMaxEnergy;
    }

    public int getScaledElecticalLevel(int i)
    {
        return (int) Math.floor(this.getEnergyStored() * i / (this.getMaxEnergyStored() - this.ueWattsPerTick));
    }

    @Override
    public float getRequest(ForgeDirection direction)
    {
        if (this.shouldPullEnergy())
        {
            return this.ueWattsPerTick * 2;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public float getProvide(ForgeDirection direction)
    {
        return 0;
    }

    @Override
    public void updateEntity()
    {
        if (this.shouldPullEnergy() && this.getEnergyStored() < this.getMaxEnergyStored() && this.getBatteryInSlot() != null && this.getElectricInputDirection() != null)
        {
            this.receiveElectricity(this.getElectricInputDirection(), ElectricityPack.getFromWatts(ElectricItemHelper.dischargeItem(this.getBatteryInSlot(), this.getRequest(ForgeDirection.UNKNOWN)), this.getVoltage()), true);
        }

        if (!this.worldObj.isRemote && this.shouldUseEnergy())
        {
            this.setEnergyStored(this.getEnergyStored() - this.ueWattsPerTick);
        }

        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (this.disableCooldown > 0)
            {
                this.disableCooldown--;
            }

            if (this.ticks % 3 == 0)
            {
            	GCCorePacketManager.sendPacketToClients(this.getPacket(), this.worldObj, new Vector3(this), this.getPacketRange());
            }
        }
    }

    protected double getPacketRange()
    {
        return 12.0D;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setBoolean("isDisabled", this.getDisabled(0));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        this.setDisabled(0, nbt.getBoolean("isDisabled"));
    }

    @Override
    public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        try
        {
            this.readPacket(dataStream);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            this.disabled = disabled;
            this.disableCooldown = 20;
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        return this.disabled;
    }

    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
    {
        return true;
    }

    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
    public short getFacing()
    {
        return (short) this.worldObj.getBlockMetadata(MathHelper.floor_double(this.xCoord), MathHelper.floor_double(this.yCoord), MathHelper.floor_double(this.zCoord));
    }

    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
    public void setFacing(short facing)
    {
        int change = 0;

        // Re-orient the block
        switch (this.getFacing())
        {
        case 0:
            change = 3;
            break;
        case 3:
            change = 1;
            break;
        case 1:
            change = 2;
            break;
        case 2:
            change = 0;
            break;
        }

        this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.xCoord), MathHelper.floor_double(this.yCoord), MathHelper.floor_double(this.zCoord), change, 3);
    }

    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
    public boolean wrenchCanRemove(EntityPlayer entityPlayer)
    {
        return true;
    }

    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
    public float getWrenchDropRate()
    {
        return 1.0F;
    }

    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
    {
        return Block.blocksList[this.getBlockType().blockID].getPickBlock(null, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public EnumSet<ForgeDirection> getInputDirections()
    {
        if (this.getElectricInputDirection() == null)
        {
            return EnumSet.noneOf(ForgeDirection.class);
        }

        return EnumSet.of(this.getElectricInputDirection());
    }
}
