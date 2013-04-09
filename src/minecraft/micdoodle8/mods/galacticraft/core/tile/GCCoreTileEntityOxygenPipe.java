package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.IPressurizedTube;
import mekanism.api.ITubeConnection;
import micdoodle8.mods.galacticraft.API.IColorable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;

import com.google.common.io.ByteArrayDataInput;

public class GCCoreTileEntityOxygenPipe extends TileEntity implements ITubeConnection, IPressurizedTube, IColorable, IPacketReceiver
{
	private byte pipeColor = 15;
	
	@Override
	public boolean canTransferGas()
	{
		return true;
	}
	
	@Override
	public void setColor(byte col)
	{
		this.pipeColor = col;
		
		if (this.worldObj != null && this.worldObj.isRemote)
		{
			Vector3 thisVec = new Vector3(this);
			this.worldObj.markBlockForRenderUpdate(thisVec.intX(), thisVec.intY(), thisVec.intZ());
		}
	}

	@Override
	public byte getColor()
	{
		return this.pipeColor;
	}

	public void onAdjacentColorChanged(Vector3 thisVec, Vector3 updatedVec)
	{
		this.worldObj.markBlockForUpdate(thisVec.intX(), thisVec.intY(), thisVec.intZ());
	}

	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
    	
    	if (par1NBTTagCompound.hasKey("pipeColor"))
    	{
    		this.setColor(par1NBTTagCompound.getByte("pipeColor"));
    	}
    }

	@Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeToNBT(par1NBTTagCompound);
    	
    	par1NBTTagCompound.setByte("pipeColor", this.getColor());
    }

	@Override
	public boolean canTubeConnect(ForgeDirection side) 
	{
		TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + side.offsetX, this.yCoord + side.offsetY, this.zCoord + side.offsetZ);
		
		if (tile != null && tile instanceof IColorable)
		{
			byte color = ((IColorable) tile).getColor();
			
			if (color == this.getColor())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			if (this.worldObj.isRemote)
			{
				this.setColor(dataStream.readByte());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}