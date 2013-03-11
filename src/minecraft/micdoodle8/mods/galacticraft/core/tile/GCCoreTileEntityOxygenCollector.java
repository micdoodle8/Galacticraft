package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.EnumGas;
import mekanism.api.IGasAcceptor;
import mekanism.api.IGasStorage;
import mekanism.api.ITubeConnection;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.client.model.block.GCCoreModelFan;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenNetwork;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import cpw.mods.fml.common.FMLLog;

public class GCCoreTileEntityOxygenCollector extends GCCoreTileEntityElectricBase implements IGasStorage, ITubeConnection
{
	public int storedOxygen;
	
	public int MAX_OXYGEN = 180;
	
	public int outputSpeed = 16;

    public GCCoreModelFan fanModel = new GCCoreModelFan();
    
	public GCCoreTileEntityOxygenCollector(String name, double maxEnergy)
	{
		super(name, maxEnergy);
	}

    @Override
  	public void validate()
  	{
   		super.validate();

   		if (!this.isInvalid() && this.worldObj != null)
      	{
   		   	this.fanModel = new GCCoreModelFan();
      	}
  	}

	@Override
	public void updateTile() 
	{
		if(storedOxygen > MAX_OXYGEN)
		{
			storedOxygen = MAX_OXYGEN;
		}
		
		if(getGas(EnumGas.OXYGEN) > 0 && !worldObj.isRemote)
		{
	    	for(ForgeDirection orientation : ForgeDirection.values())
	    	{
	    		if(orientation != ForgeDirection.UNKNOWN)
	    		{
	    			setGas(EnumGas.OXYGEN, getGas(EnumGas.OXYGEN) - (Math.min(getGas(EnumGas.OXYGEN), this.outputSpeed) - OxygenNetwork.emitGasToNetwork(EnumGas.OXYGEN, Math.min(getGas(EnumGas.OXYGEN), this.outputSpeed), this, orientation)));

	    			TileEntity tileEntity = VectorHelper.getTileEntityFromSide(worldObj, new Vector3(this), orientation);

	    			if(tileEntity instanceof IGasAcceptor)
	    			{
	    				if(((IGasAcceptor)tileEntity).canReceiveGas(orientation.getOpposite(), EnumGas.OXYGEN))
	    				{
	    					int sendingGas = 0;
	    					
	    					if(getGas(EnumGas.OXYGEN) >= this.outputSpeed)
	    					{
	    						sendingGas = this.outputSpeed;
	    					}
	    					else if(getGas(EnumGas.OXYGEN) < this.outputSpeed)
	    					{
	    						sendingGas = getGas(EnumGas.OXYGEN);
	    					}

	    					int rejects = ((IGasAcceptor)tileEntity).transferGasToAcceptor(sendingGas, EnumGas.OXYGEN);

	    					setGas(EnumGas.OXYGEN, getGas(EnumGas.OXYGEN) - (sendingGas - rejects));
	    				}
	    			}
	    		}
	    	}
		}
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		double power = 0;

		for (int y = this.yCoord - 5; y <= this.yCoord + 5; y++)
		{
			for (int x = this.xCoord - 5; x <= this.xCoord + 5; x++)
			{
				for (int z = this.zCoord - 5; z <= this.zCoord + 5; z++)
				{
					final Block block = Block.blocksList[this.worldObj.getBlockId(x, y, z)];

					if (block != null && block instanceof BlockLeaves)
					{
						if (!this.worldObj.isRemote && this.worldObj.rand.nextInt(100000) == 0 && !GCCoreConfigManager.disableLeafDecay)
						{
							this.worldObj.setBlock(x, y, z, 0);
						}

						power++;
					}
				}
			}
		}
		
		this.setGas(EnumGas.OXYGEN, MathHelper.floor_double(power / 5));
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.storedOxygen = par1NBTTagCompound.getInteger("storedOxygen");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("storedOxygen", this.storedOxygen);
	}

	@Override
	public int getGas(EnumGas type) 
	{
		if (type == EnumGas.OXYGEN)
		{
			return this.storedOxygen;
		}
		
		return 0;
	}

	@Override
	public void setGas(EnumGas type, int amount) 
	{
		if (type == EnumGas.OXYGEN)
		{
			storedOxygen = Math.max(Math.min(amount, this.MAX_OXYGEN), 0);
		}
	}

	@Override
	public boolean canTubeConnect(ForgeDirection side) 
	{
		return (side != ForgeDirection.UP && side != ForgeDirection.DOWN);
	}
}
