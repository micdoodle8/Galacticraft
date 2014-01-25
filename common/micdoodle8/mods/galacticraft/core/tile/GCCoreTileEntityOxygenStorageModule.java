package micdoodle8.mods.galacticraft.core.tile;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTransmission;
import mekanism.api.gas.IGasAcceptor;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockMachine2;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

/**
 * GCCoreTileEntityOxygenStorageModule.java
 *
 * This file is part of the Galacticraft project
 *
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityOxygenStorageModule extends GCCoreTileEntityOxygen implements IPacketReceiver
{
    public final Set<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();
    public int scaledOxygenLevel;
    public int lastScaledOxygenLevel;

    public static final int OUTPUT_PER_TICK = 100;

    public GCCoreTileEntityOxygenStorageModule()
    {
        super(0, 0, 60000, 0);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        this.scaledOxygenLevel = (int) Math.floor(this.storedOxygen * 16 / this.maxOxygen);

        if (this.scaledOxygenLevel != this.lastScaledOxygenLevel)
        {
            this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
        }

        if (!this.worldObj.isRemote)
        {
            int gasToSend = Math.min(this.storedOxygen, GCCoreTileEntityOxygenStorageModule.OUTPUT_PER_TICK);
            GasStack toSend = new GasStack(GalacticraftCore.gasOxygen, gasToSend);
            this.storedOxygen -= GasTransmission.emitGasToNetwork(toSend, this, this.getOxygenOutputDirection());

            Vector3 thisVec = new Vector3(this);
            TileEntity tileEntity = thisVec.modifyPositionFromSide(this.getOxygenOutputDirection()).getTileEntity(this.worldObj);

            if (tileEntity instanceof IGasAcceptor)
            {
                if (((IGasAcceptor) tileEntity).canReceiveGas(this.getOxygenInputDirection(), GalacticraftCore.gasOxygen))
                {
                    double sendingGas = 0;

                    if (this.storedOxygen >= GCCoreTileEntityOxygenStorageModule.OUTPUT_PER_TICK)
                    {
                        sendingGas = GCCoreTileEntityOxygenStorageModule.OUTPUT_PER_TICK;
                    }
                    else
                    {
                        sendingGas = this.storedOxygen;
                    }

                    this.storedOxygen -= sendingGas - ((IGasAcceptor) tileEntity).receiveGas(new GasStack(GalacticraftCore.gasOxygen, (int) Math.floor(sendingGas)));
                }
            }
        }

        this.lastScaledOxygenLevel = this.scaledOxygenLevel;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
    }

    @Override
    public float getRequest(ForgeDirection direction)
    {
        return 0;
    }

    @Override
    public float getProvide(ForgeDirection direction)
    {
        return 0;
    }

    @Override
    public EnumSet<ForgeDirection> getInputDirections()
    {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    @Override
    public EnumSet<ForgeDirection> getOutputDirections()
    {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    @Override
    public float getMaxEnergyStored()
    {
        return 0;
    }

    @Override
    public ForgeDirection getOxygenInputDirection()
    {
        return ForgeDirection.getOrientation(this.getBlockMetadata() - GCCoreBlockMachine2.OXYGEN_STORAGE_MODULE_METADATA + 2);
    }

    public ForgeDirection getOxygenOutputDirection()
    {
        return this.getOxygenInputDirection().getOpposite();
    }

    @Override
    public boolean canTubeConnect(ForgeDirection direction)
    {
        return direction == this.getOxygenInputDirection() || direction == this.getOxygenOutputDirection();
    }

    @Override
    public boolean shouldPullOxygen()
    {
        return this.storedOxygen < this.maxOxygen;
    }

    @Override
    public boolean shouldPullEnergy()
    {
        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return false;
    }

    @Override
    public void readPacket(ByteArrayDataInput data)
    {
        try
        {
            this.storedOxygen = data.readInt();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Packet getPacket()
    {
        return GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.storedOxygen);
    }

    @Override
    public ForgeDirection getElectricInputDirection()
    {
        return null;
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return null;
    }

    @Override
    public boolean shouldUseOxygen()
    {
        return false;
    }
}
