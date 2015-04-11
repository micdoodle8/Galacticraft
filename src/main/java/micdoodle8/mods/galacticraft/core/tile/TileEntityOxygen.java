package micdoodle8.mods.galacticraft.core.tile;

import cpw.mods.fml.relauncher.Side;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasHandler;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IOxygenNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenReceiver;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenStorage;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.oxygen.NetworkHelper;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;

public abstract class TileEntityOxygen extends TileBaseElectricBlock implements IOxygenReceiver, IOxygenStorage
{
    public float maxOxygen;
    public float oxygenPerTick;
    @NetworkedField(targetSide = Side.CLIENT)
    public float storedOxygen;
    public float lastStoredOxygen;
    public static int timeSinceOxygenRequest;

    public TileEntityOxygen(float maxOxygen, float oxygenPerTick)
    {
        this.maxOxygen = maxOxygen;
        this.oxygenPerTick = oxygenPerTick;
    }

    public int getScaledOxygenLevel(int scale)
    {
        return (int) Math.floor(this.getOxygenStored() * scale / (this.getMaxOxygenStored() - this.oxygenPerTick));
    }

    public abstract boolean shouldUseOxygen();

    public int getCappedScaledOxygenLevel(int scale)
    {
        return (int) Math.max(Math.min(Math.floor((double) this.storedOxygen / (double) this.maxOxygen * scale), scale), 0);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (TileEntityOxygen.timeSinceOxygenRequest > 0)
            {
                TileEntityOxygen.timeSinceOxygenRequest--;
            }

            if (this.shouldUseOxygen())
            {
                this.storedOxygen = Math.max(this.storedOxygen - this.oxygenPerTick, 0);
            }
        }

        this.lastStoredOxygen = this.storedOxygen;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        if (nbt.hasKey("storedOxygen"))
        {
            this.storedOxygen = nbt.getInteger("storedOxygen");
        }
        else
        {
            this.storedOxygen = nbt.getFloat("storedOxygenF");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setFloat("storedOxygenF", this.storedOxygen);
    }

    @Override
    public void setOxygenStored(float oxygen)
    {
        this.storedOxygen = Math.max(Math.min(oxygen, this.getMaxOxygenStored()), 0);
    }

    @Override
    public float getOxygenStored()
    {
        return this.storedOxygen;
    }

    @Override
    public float getMaxOxygenStored()
    {
        return this.maxOxygen;
    }

    public EnumSet<ForgeDirection> getOxygenInputDirections()
    {
        return EnumSet.allOf(ForgeDirection.class);
    }

    public EnumSet<ForgeDirection> getOxygenOutputDirections()
    {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    @Override
    public boolean canConnect(ForgeDirection direction, NetworkType type)
    {
        if (direction == null || direction.equals(ForgeDirection.UNKNOWN))
        {
            return false;
        }

        if (type == NetworkType.OXYGEN)
        {
            return this.getOxygenInputDirections().contains(direction) || this.getOxygenOutputDirections().contains(direction);
        }
        if (type == NetworkType.POWER)
        //			return this.nodeAvailable(new EnergySourceAdjacent(direction));
        {
            return super.canConnect(direction, type);
        }

        return false;
    }

    @Override
    public float receiveOxygen(ForgeDirection from, float receive, boolean doReceive)
    {
        if (this.getOxygenInputDirections().contains(from))
        {
            if (!doReceive)
            {
                return this.getOxygenRequest(from);
            }

            return this.receiveOxygen(receive, doReceive);
        }

        return 0;
    }

    public float receiveOxygen(float receive, boolean doReceive)
    {
        if (receive > 0)
        {
            float prevEnergyStored = this.getOxygenStored();
            float newStoredEnergy = Math.min(prevEnergyStored + receive, this.getMaxOxygenStored());

            if (doReceive)
            {
                TileEntityOxygen.timeSinceOxygenRequest = 20;
                this.setOxygenStored(newStoredEnergy);
            }

            return Math.max(newStoredEnergy - prevEnergyStored, 0);
        }

        return 0;
    }

    @Override
    public float provideOxygen(ForgeDirection from, float request, boolean doProvide)
    {
        if (this.getOxygenOutputDirections().contains(from))
        {
            return this.provideOxygen(request, doProvide);
        }

        return 0;
    }

    public float provideOxygen(float request, boolean doProvide)
    {
        if (request > 0)
        {
            float requestedEnergy = Math.min(request, this.storedOxygen);

            if (doProvide)
            {
                this.setOxygenStored(this.storedOxygen - requestedEnergy);
            }

            return requestedEnergy;
        }

        return 0;
    }

    public void produceOxygen()
    {
        if (!this.worldObj.isRemote)
        {
            for (ForgeDirection direction : this.getOxygenOutputDirections())
            {
                if (direction != ForgeDirection.UNKNOWN)
                {
                    this.produceOxygen(direction);
                }
            }
        }
    }

    public boolean produceOxygen(ForgeDirection outputDirection)
    {
        float provide = this.getOxygenProvide(outputDirection);

        if (provide > 0)
        {
            TileEntity outputTile = new BlockVec3(this).modifyPositionFromSide(outputDirection).getTileEntity(this.worldObj);
            IOxygenNetwork outputNetwork = NetworkHelper.getOxygenNetworkFromTileEntity(outputTile, outputDirection);

            if (outputNetwork != null)
            {
                float powerRequest = outputNetwork.getRequest(this);

                if (powerRequest > 0)
                {
                    float toSend = Math.min(this.getOxygenStored(), provide);
                    float rejectedPower = outputNetwork.produce(toSend, this);

                    this.provideOxygen(Math.max(toSend - rejectedPower, 0), true);
                    return true;
                }
            }
            else if (outputTile instanceof IOxygenReceiver)
            {
                float requestedEnergy = ((IOxygenReceiver) outputTile).getOxygenRequest(outputDirection.getOpposite());

                if (requestedEnergy > 0)
                {
                    float toSend = Math.min(this.getOxygenStored(), provide);
                    float acceptedOxygen = ((IOxygenReceiver) outputTile).receiveOxygen(outputDirection.getOpposite(), toSend, true);
                    this.provideOxygen(acceptedOxygen, true);
                    return true;
                }
            }
            else if (EnergyConfigHandler.isMekanismLoaded())
            {
                //TODO Oxygen item handling - internal tank (IGasItem)
                //int acceptedOxygen = GasTransmission.addGas(itemStack, type, amount);
                //this.provideOxygen(acceptedOxygen, true);

                if (outputTile instanceof IGasHandler && ((IGasHandler) outputTile).canReceiveGas(outputDirection.getOpposite(), (Gas) EnergyConfigHandler.gasOxygen))
                {
                    GasStack toSend = new GasStack((Gas) EnergyConfigHandler.gasOxygen, (int) Math.floor(Math.min(this.getOxygenStored(), provide)));
                    int acceptedOxygen = 0;
                    try {
                    	acceptedOxygen = ((IGasHandler) outputTile).receiveGas(outputDirection.getOpposite(), toSend);
                    } catch (Exception e) { }
                    this.provideOxygen(acceptedOxygen, true);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public float getOxygenRequest(ForgeDirection direction)
    {
        if (this.shouldPullOxygen())
        {
            return this.oxygenPerTick * 2;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public boolean shouldPullOxygen()
    {
        return this.storedOxygen < this.maxOxygen;
    }

    @Override
    public float getOxygenProvide(ForgeDirection direction)
    {
        return 0;
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
    public int receiveGas(ForgeDirection side, GasStack stack, boolean doTransfer)
    {
    	if (!stack.getGas().getName().equals("oxygen")) return 0;  
    	return (int) Math.floor(this.receiveOxygen(stack.amount, doTransfer));
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
    public int receiveGas(ForgeDirection side, GasStack stack)
    {
    	return this.receiveGas(side, stack, true);
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
    public GasStack drawGas(ForgeDirection side, int amount, boolean doTransfer)
    {
        return new GasStack((Gas) EnergyConfigHandler.gasOxygen, (int) Math.floor(this.provideOxygen(amount, doTransfer)));
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
    public GasStack drawGas(ForgeDirection side, int amount)
    {
        return this.drawGas(side, amount, true);
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
    public boolean canReceiveGas(ForgeDirection side, Gas type)
    {
        return type.getName().equals("oxygen") && this.getOxygenInputDirections().contains(side);
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
    public boolean canDrawGas(ForgeDirection side, Gas type)
    {
        return type.getName().equals("oxygen") && this.getOxygenOutputDirections().contains(side);
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = "Mekanism")
    public boolean canTubeConnect(ForgeDirection side)
    {
        return this.canConnect(side, NetworkType.OXYGEN);
    }
}
