package micdoodle8.mods.galacticraft.core.energy.tile;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

import java.util.EnumSet;

public abstract class TileBaseElectricBlock extends TileBaseUniversalElectrical implements IPacketReceiver, IDisableableMachine, IConnector
{
    //	public int energyPerTick = 200;
    //	private final float ueMaxEnergy;

    @NetworkedField(targetSide = Side.CLIENT)
    public boolean disabled = false;
    @NetworkedField(targetSide = Side.CLIENT)
    public int disableCooldown = 0;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean hasEnoughEnergyToRun = false;

    public boolean shouldPullEnergy()
    {
        return this.shouldUseEnergy() || this.getEnergyStoredGC(null) < this.getMaxEnergyStoredGC();
    }

    public abstract boolean shouldUseEnergy();

    public abstract EnumFacing getElectricInputDirection();

    public abstract ItemStack getBatteryInSlot();

    //	public TileBaseElectricBlock()
    //	{
    //		this.storage.setMaxReceive(ueWattsPerTick);
    //		this.storage.setMaxExtract(0);
    //		this.storage.setCapacity(maxEnergy);
    ////		this.ueMaxEnergy = maxEnergy;
    ////		this.ueWattsPerTick = ueWattsPerTick;
    //
    //		/*
    //		 * if (PowerFramework.currentFramework != null) { this.bcPowerProvider =
    //		 * new GCCoreLinkedPowerProvider(this);
    //		 * this.bcPowerProvider.configure(20, 1, 10, 10, 1000); }
    //		 */
    //	}

    //	@Override
    //	public float getMaxEnergyStored()
    //	{
    //		return this.ueMaxEnergy;
    //	}

    public int getScaledElecticalLevel(int i)
    {
        return (int) Math.floor(this.getEnergyStoredGC(null) * i / this.getMaxEnergyStoredGC(null));
        //- this.ueWattsPerTick;
    }

    //	@Override
    //	public float getRequest(EnumFacing direction)
    //	{
    //		if (this.shouldPullEnergy())
    //		{
    //			return this.ueWattsPerTick * 2;
    //		}
    //		else
    //		{
    //			return 0;
    //		}
    //	}
    //
    //	@Override
    //	public float getProvide(EnumFacing direction)
    //	{
    //		return 0;
    //	}

    @Override
    public void update()
    {
        if (!this.worldObj.isRemote)
        {
            if (this.shouldPullEnergy() && this.getEnergyStoredGC(null) < this.getMaxEnergyStoredGC(null) && this.getBatteryInSlot() != null && this.getElectricInputDirection() != null)
            {
                this.discharge(this.getBatteryInSlot());
                // this.receiveElectricity(this.getElectricInputDirection(),
                // ElectricityPack.getFromWatts(ElectricItemHelper.dischargeItem(this.getBatteryInSlot(),
                // this.getRequest(EnumFacing.UNKNOWN)), this.getVoltage()),
                // true);
            }

            if (this.getEnergyStoredGC(null) > this.storage.getMaxExtract())
            {
                this.hasEnoughEnergyToRun = true;
                if (this.shouldUseEnergy())
                {
                    this.storage.extractEnergyGC(this.storage.getMaxExtract(), false);
                }
            }
            else
            {
                this.hasEnoughEnergyToRun = false;
                if (this.getEnergyStoredGC(null) > 0) this.storage.extractEnergyGC(5F, false);
            }
        }

        super.update();

        if (!this.worldObj.isRemote)
        {
            if (this.disableCooldown > 0)
            {
                this.disableCooldown--;
            }
        }
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
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            this.disabled = disabled;
            this.disableCooldown = 10;
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        return this.disabled;
    }

//    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
//    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
//    {
//        return false;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
//    public short getFacing()
//    {
//        return (short) this.worldObj.getBlockMetadata(MathHelper.floor_double(this.getPos().getX()), MathHelper.floor_double(this.getPos().getY()), MathHelper.floor_double(this.getPos().getZ()));
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
//    public void setFacing(short facing)
//    {
//
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
//    public boolean wrenchCanRemove(EntityPlayer entityPlayer)
//    {
//        return false;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
//    public float getWrenchDropRate()
//    {
//        return 1.0F;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
//    public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
//    {
//        return this.getBlockType().getPickBlock(null, this.worldObj, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
//    } TODO

    @Override
    public EnumSet<EnumFacing> getElectricalInputDirections()
    {
        if (this.getElectricInputDirection() == null)
        {
            return EnumSet.noneOf(EnumFacing.class);
        }

        return EnumSet.of(this.getElectricInputDirection());
    }

    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return this.worldObj.getTileEntity(this.getPos()) == this && entityplayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        if (direction == null || type != NetworkType.POWER)
        {
            return false;
        }

        return direction == this.getElectricInputDirection();
    }
}
