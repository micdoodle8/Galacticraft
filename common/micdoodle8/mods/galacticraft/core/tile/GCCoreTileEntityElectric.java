package micdoodle8.mods.galacticraft.core.tile;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.tile.IWrenchable;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.core.GCCoreCompatibilityManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.compatibility.TileEntityUniversalElectrical;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import com.google.common.io.ByteArrayDataInput;

public abstract class GCCoreTileEntityElectric extends TileEntityUniversalElectrical implements IWrenchable, IPacketReceiver, IDisableableMachine
{
    public float ueWattsPerTick;

    public boolean addedToEnergyNet = false;

    public boolean disabled = true;
    public int disableCooldown = 0;

    public abstract boolean shouldPullEnergy();

    public abstract boolean shouldUseEnergy();

    public abstract void readPacket(ByteArrayDataInput data);

    public abstract Packet getPacket();

    public abstract ForgeDirection getElectricInputDirection();

    public abstract ItemStack getBatteryInSlot();

    public GCCoreTileEntityElectric(float ueWattsPerTick, float maxEnergy)
    {
        super(maxEnergy);
        this.ueWattsPerTick = ueWattsPerTick;

        /*
         * if (PowerFramework.currentFramework != null) { this.bcPowerProvider =
         * new GCCoreLinkedPowerProvider(this);
         * this.bcPowerProvider.configure(20, 1, 10, 10, 1000); }
         */
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
        if (this.shouldPullEnergy() && this.getEnergyStored() < this.getMaxEnergyStored())
        {
            this.receiveElectricity(this.getInputDirection(), ElectricityPack.getFromWatts(ElectricItemHelper.dischargeItem(this.getBatteryInSlot(), this.getRequest(ForgeDirection.UNKNOWN)), this.getVoltage()), true);
        }

        if (!this.worldObj.isRemote && this.shouldUseEnergy())
        {
            this.setEnergyStored(this.getEnergyStored() - this.ueWattsPerTick);
        }

        super.updateEntity();

        if (!this.addedToEnergyNet && this.worldObj != null)
        {
            if (GCCoreCompatibilityManager.isIc2Loaded())
            {
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            }

            this.addedToEnergyNet = true;
        }

        if (!this.worldObj.isRemote)
        {
            if (this.disableCooldown > 0)
            {
                this.disableCooldown--;
            }

            if (this.ticks % 3 == 0)
            {
                PacketManager.sendPacketToClients(this.getPacket(), this.worldObj, new Vector3(this), 12);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setBoolean("isDisabled", this.getDisabled());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        this.setDisabled(nbt.getBoolean("isDisabled"));
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
    public void setDisabled(boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            this.disabled = disabled;
            this.disableCooldown = 20;
        }
    }

    @Override
    public boolean getDisabled()
    {
        return this.disabled;
    }

    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
    {
        return true;
    }

    @Override
    public short getFacing()
    {
        return (short) this.worldObj.getBlockMetadata(MathHelper.floor_double(this.xCoord), MathHelper.floor_double(this.yCoord), MathHelper.floor_double(this.zCoord));
    }

    @Override
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

    @Override
    public boolean wrenchCanRemove(EntityPlayer entityPlayer)
    {
        return true;
    }

    @Override
    public float getWrenchDropRate()
    {
        return 1.0F;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
    {
        return Block.blocksList[this.getBlockType().blockID].getPickBlock(null, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public ForgeDirection getInputDirection()
    {
        return this.getElectricInputDirection();
    }
    
    @Override
    public ForgeDirection getOutputDirection()
    {
        return null;
    }
}
