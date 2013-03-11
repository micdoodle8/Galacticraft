package micdoodle8.mods.galacticraft.core.tile;

import com.google.common.io.ByteArrayDataInput;

import mekanism.api.EnumGas;
import mekanism.api.IGasAcceptor;
import mekanism.api.ITubeConnection;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.model.block.GCCoreModelFan;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.block.IConnector;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.block.IVoltage;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.tile.TileEntityElectricityStorage;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreTileEntityOxygenDistributor extends TileEntityElectricityStorage implements IElectricityStorage, IPacketReceiver, IGasAcceptor, ITubeConnection, IConnector, IVoltage
{
	public int currentPower;
    public boolean active;
    
    public GCCoreModelFan fanModel1 = new GCCoreModelFan();
    public GCCoreModelFan fanModel2 = new GCCoreModelFan();
    public GCCoreModelFan fanModel3 = new GCCoreModelFan();
   	public GCCoreModelFan fanModel4 = new GCCoreModelFan();

    @Override
  	public void validate()
  	{
   		super.validate();

   		if (!this.isInvalid() && this.worldObj != null)
      	{
   		   	this.fanModel1 = new GCCoreModelFan();
   		    this.fanModel2 = new GCCoreModelFan();
   		    this.fanModel3 = new GCCoreModelFan();
   		   	this.fanModel4 = new GCCoreModelFan();
      	}
  	}

    public double getDistanceFrom2(double par1, double par3, double par5)
    {
        final double var7 = this.xCoord + 0.5D - par1;
        final double var9 = this.yCoord + 0.5D - par3;
        final double var11 = this.zCoord + 0.5D - par5;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.currentPower > 0)
		{
			this.currentPower -= 1;
		}

		if (this.currentPower < 1.0D)
		{
			this.active = false;
		}
		else
		{
			this.active = true;
		}

		if (!this.worldObj.isRemote)
		{
			for (int i = 0; i < ForgeDirection.values().length; i++)
	    	{
				int x, y, z;
				x = ForgeDirection.getOrientation(i).offsetX + this.xCoord;
				y = ForgeDirection.getOrientation(i).offsetY + this.yCoord;
				z = ForgeDirection.getOrientation(i).offsetZ + this.zCoord;

				if (this.active)
				{
					if (this.worldObj.getBlockId(x, y, z) == 0)
					{
						this.worldObj.setBlockWithNotify(x, y, z, GCCoreBlocks.breatheableAir.blockID);
					}

					this.updateAdjacentOxygenAdd(ForgeDirection.getOrientation(i).offsetX, ForgeDirection.getOrientation(i).offsetY, ForgeDirection.getOrientation(i).offsetZ);
			    }
				else if (!this.active)
				{
					this.updateAdjacentOxygenRemove(x, y, z);
				}
	    	}
		}

		if (this.active)
		{
			final int power = Math.min((int) Math.floor(this.currentPower / 3), 8);

			for (int j = -power; j <= power; j++)
			{
				for (int i = -power; i <= power; i++)
				{
					for (int k = -power; k <= power; k++)
					{
						if (this.worldObj.getBlockId(this.xCoord + i, this.yCoord + j, this.zCoord + k) == GCCoreBlocks.breatheableAir.blockID)
						{
							this.worldObj.scheduleBlockUpdate(this.xCoord + i, this.yCoord + j, this.zCoord + k, GCCoreBlocks.breatheableAir.blockID, GCCoreBlocks.breatheableAir.tickRate());
						}
						else if (this.worldObj.getBlockId(this.xCoord + i, this.yCoord + j, this.zCoord + k) == GCCoreBlocks.unlitTorch.blockID)
						{
							final int meta = this.worldObj.getBlockMetadata(this.xCoord + i, this.yCoord + j, this.zCoord + k);
							this.worldObj.setBlockAndMetadataWithNotify(this.xCoord + i, this.yCoord + j, this.zCoord + k, GCCoreBlocks.unlitTorchLit.blockID, meta);
						}
					}
				}
			}
		}
	}

	public void updateAdjacentOxygenAdd(int xOffset, int yOffset, int zOffset)
	{
		final TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + xOffset, this.yCoord + yOffset, this.zCoord + zOffset);

		if (tile != null && tile instanceof GCCoreTileEntityBreathableAir)
		{
			final GCCoreTileEntityBreathableAir air = (GCCoreTileEntityBreathableAir) tile;

			air.addDistributor(this);
		}
	}

	public void updateAdjacentOxygenRemove(int xOffset, int yOffset, int zOffset)
	{
		final TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + xOffset, this.yCoord + yOffset, this.zCoord + zOffset);

		if (tile != null && tile instanceof GCCoreTileEntityBreathableAir)
		{
			final GCCoreTileEntityBreathableAir air = (GCCoreTileEntityBreathableAir) tile;

			air.removeDistributor(this);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.currentPower = par1NBTTagCompound.getInteger("currentPower");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("currentPower", this.currentPower);
	}

	@Override
	public int transferGasToAcceptor(int amount, EnumGas type) 
	{
		if (type == EnumGas.OXYGEN)
		{
			currentPower = Math.max(this.currentPower, amount);
			return 0;
		}
		else
		{
			return amount;
		}
	}

	@Override
	public boolean canReceiveGas(ForgeDirection side, EnumGas type) 
	{
		return true;
	}

	@Override
	public boolean canTubeConnect(ForgeDirection side)
	{
		return (side != ForgeDirection.UP && side != ForgeDirection.DOWN);
	}

	@Override
	public boolean canConnect(ForgeDirection side)
	{
		return (side != ForgeDirection.UP && side != ForgeDirection.DOWN);
	}

	@Override
	public double getVoltage() 
	{
		return 120.0D;
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		
	}

	@Override
	public double getMaxJoules() 
	{
		return 2500000;
	}
}