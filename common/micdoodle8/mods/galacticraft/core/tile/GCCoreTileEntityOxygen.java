package micdoodle8.mods.galacticraft.core.tile;

import java.util.EnumSet;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.power.NetworkHelper;
import micdoodle8.mods.galacticraft.power.NetworkType;
import micdoodle8.mods.galacticraft.power.core.grid.IOxygenNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

/**
 * GCCoreTileEntityOxygen.java
 *
 * This file is part of the Galacticraft project
 *
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class GCCoreTileEntityOxygen extends GCCoreTileEntityElectricBlock implements IOxygenReceiver, IOxygenStorage
{
    public float maxOxygen;
    public float oxygenPerTick;
    public float storedOxygen;
    public float lastStoredOxygen;
    public static int timeSinceOxygenRequest;

    public GCCoreTileEntityOxygen(float wattsPerTick, float maxEnergy, float maxOxygen, float oxygenPerTick)
    {
        super(wattsPerTick, maxEnergy);
        this.maxOxygen = maxOxygen;
        this.oxygenPerTick = oxygenPerTick;
    }
    
    public int getScaledOxygenLevel(int scale)
    {
        return (int) Math.floor((this.getOxygenStored() * scale) / (this.getMaxOxygenStored() - this.oxygenPerTick));
    }

    public abstract boolean shouldPullOxygen();

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
            if (GCCoreTileEntityOxygen.timeSinceOxygenRequest > 0)
            {
                GCCoreTileEntityOxygen.timeSinceOxygenRequest--;
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

	public void setOxygenStored(float oxygen)
	{
		this.storedOxygen = Math.max(Math.min(oxygen, this.getMaxOxygenStored()), 0);
	}

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

		return this.getElectricalInputDirections().contains(direction) || this.getElectricalOutputDirections().contains(direction) || this.getOxygenInputDirections().contains(direction) || this.getOxygenOutputDirections().contains(direction);
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
				timeSinceOxygenRequest = 20;
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
			if (!doProvide)
			{
				return this.getProvide(from);
			}

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
        if (!this.worldObj.isRemote && outputDirection != null && outputDirection != ForgeDirection.UNKNOWN)
        {
            float provide = this.getOxygenProvide(outputDirection);

            if (provide > 0)
            {
				Vector3 thisVec = new Vector3(this);
				TileEntity outputTile = new Vector3(this).modifyPositionFromSide(outputDirection).getTileEntity(this.worldObj);
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
                        float acceptedEnergy = ((IOxygenReceiver) outputTile).receiveOxygen(outputDirection.getOpposite(), toSend, true);
						this.provideOxygen(acceptedEnergy, true);
                        return true;
                    }
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
	public float getOxygenProvide(ForgeDirection direction)
	{
		return 0;
	}
}
