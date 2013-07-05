package micdoodle8.mods.galacticraft.core.tile;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.tile.IWrenchable;
import micdoodle8.mods.galacticraft.API.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.core.GCCoreCompatibilityManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.block.IElectrical;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityDisableable;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class GCCoreTileEntityElectric extends TileEntityDisableable implements IWrenchable, IPowerReceptor, IEnergySink, IPacketReceiver, IDisableableMachine, IElectrical
{
    public int ueWattsPerTick;
    public double maxEnergy;
    public double ic2Energy;
    public double ic2EnergyPerTick;
    public IPowerProvider bcPowerProvider;
    @SideOnly(Side.CLIENT)
    public double bcEnergy;
    public double bcEnergyPerTick;
    public double ueWattsReceived;

    public boolean addedToEnergyNet = false;

    public boolean disabled = true;
    public int disableCooldown = 0;

    public abstract boolean shouldPullEnergy();

    public abstract void readPacket(ByteArrayDataInput data);

    public abstract Packet getPacket();

    public abstract ForgeDirection getElectricInputDirection();

    public abstract ItemStack getBatteryInSlot();

    public GCCoreTileEntityElectric(int ueWattsPerTick, double maxEnergy, double ic2EnergyPerTick, double bcEnergyPerTick)
    {
        this.ueWattsPerTick = ueWattsPerTick;
        this.maxEnergy = maxEnergy;
        this.ic2EnergyPerTick = ic2EnergyPerTick;
        this.bcEnergyPerTick = bcEnergyPerTick;

        if (PowerFramework.currentFramework != null)
        {
            this.bcPowerProvider = new GCCoreLinkedPowerProvider(this);
            this.bcPowerProvider.configure(20, 1, 10, 10, 1000);
        }
    }

    @Override
    public void invalidate()
    {
        this.unloadIC2();
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        this.unloadIC2();
        super.onChunkUnload();
    }

    private void unloadIC2()
    {
        if (this.addedToEnergyNet && this.worldObj != null)
        {
            if (GCCoreCompatibilityManager.isIc2Loaded())
            {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            }

            this.addedToEnergyNet = false;
        }
    }

    @Override
    public float getRequest(ForgeDirection direction)
    {
        if (this.shouldPullEnergy())
        {
            return this.ueWattsPerTick;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public void updateEntity()
    {
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
            this.ic2Energy = Math.max(this.ic2Energy - this.ic2EnergyPerTick, 0);

            if (this.getPowerProvider() != null && this.shouldPullEnergy())
            {
                this.getPowerProvider().useEnergy((float) this.bcEnergyPerTick / 2.0F, (float) this.bcEnergyPerTick / 2.0F, true);
            }

            if (this.shouldPullEnergy())
            {
                this.ueWattsReceived += ElectricItemHelper.dechargeItem(this.getBatteryInSlot(), this.ueWattsPerTick, this.getVoltage());
            }

            if (this.disableCooldown > 0)
            {
                this.disableCooldown--;
            }

            if (this.ticks % 3 == 0)
            {
                PacketManager.sendPacketToClients(this.getPacket(), this.worldObj, new Vector3(this), 12);
            }

            this.ueWattsReceived = Math.max(this.ueWattsReceived - this.ueWattsPerTick / 4, 0);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setDouble("ic2Energy", this.ic2Energy);
        nbt.setBoolean("isDisabled", this.getDisabled());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        this.ic2Energy = nbt.getDouble("ic2Energy");
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
    public int injectEnergy(Direction directionFrom, int amount)
    {
        this.ic2Energy += amount;

        int rejects = 0;

        if (this.ic2Energy > this.maxEnergy)
        {
            rejects = (int) (this.ic2Energy - this.maxEnergy);
            this.ic2Energy = this.maxEnergy;
        }

        return rejects;
    }

    @Override
    public int demandsEnergy()
    {
        return (int) (this.shouldPullEnergy() ? this.maxEnergy - this.ic2Energy : 0);
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
    {
        return direction.toForgeDirection() == this.getElectricInputDirection();
    }

    @Override
    public boolean isAddedToEnergyNet()
    {
        return this.addedToEnergyNet;
    }

    @Override
    public int getMaxSafeInput()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canConnect(ForgeDirection direction)
    {
        return direction == this.getElectricInputDirection();
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
    public void setPowerProvider(IPowerProvider provider)
    {
        ;
    }

    @Override
    public IPowerProvider getPowerProvider()
    {
        return this.bcPowerProvider;
    }

    @Override
    public void doWork()
    {
        ;
    }

    @Override
    public int powerRequest(ForgeDirection from)
    {
        if (this.getPowerProvider() == null)
        {
            return 0;
        }

        return (int) Math.min((this.getPowerProvider().getMaxEnergyStored() - this.getPowerProvider().getEnergyStored()) * GalacticraftCore.toBuildcraftEnergyScalar, this.getPowerProvider().getMaxEnergyReceived());
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
    public float getVoltage()
    {
        return 120;
    }

    @Override
    public float receiveElectricity(ElectricityPack electricityPack, boolean doReceive)
    {
        float energyReceived = electricityPack.getWatts();
        double energyUsed = Math.min(this.ueWattsPerTick - this.ueWattsReceived, energyReceived);

        if (doReceive)
        {
            this.ueWattsReceived += energyUsed; 
        }

        return (float) energyUsed;
    }
}
