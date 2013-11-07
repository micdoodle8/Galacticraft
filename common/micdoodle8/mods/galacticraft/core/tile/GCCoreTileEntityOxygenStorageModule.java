package micdoodle8.mods.galacticraft.core.tile;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import mekanism.api.gas.EnumGas;
import mekanism.api.gas.GasTransmission;
import mekanism.api.gas.IGasAcceptor;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockMachine2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import com.google.common.io.ByteArrayDataInput;

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
            int gasToSend = (int) Math.min(this.storedOxygen, OUTPUT_PER_TICK);
            
            this.storedOxygen -= gasToSend - GasTransmission.emitGasToNetwork(EnumGas.OXYGEN, gasToSend, this, this.getOxygenInputDirection().getOpposite());

            final TileEntity tileEntity = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), this.getOxygenInputDirection().getOpposite());

            if (tileEntity instanceof IGasAcceptor)
            {
                if (((IGasAcceptor) tileEntity).canReceiveGas(this.getOxygenInputDirection(), EnumGas.OXYGEN))
                {
                    double sendingGas = 0;

                    if (this.storedOxygen >= OUTPUT_PER_TICK)
                    {
                        sendingGas = OUTPUT_PER_TICK;
                    }
                    else
                    {
                        sendingGas = this.storedOxygen;
                    }

                    this.storedOxygen -= sendingGas - ((IGasAcceptor) tileEntity).transferGasToAcceptor(MathHelper.floor_double(sendingGas), EnumGas.OXYGEN);
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

    @Override
    public boolean canTubeConnect(ForgeDirection direction)
    {
        return direction == this.getOxygenInputDirection() || direction == this.getOxygenInputDirection().getOpposite();
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
        return PacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.storedOxygen);
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
