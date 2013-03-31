package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

public class GCCoreEntityOxygenBubble extends Entity implements IPacketReceiver
{
	private double size;
	
	protected long ticks = 0;
	
	private GCCoreTileEntityOxygenDistributor distributor;
	
	public GCCoreEntityOxygenBubble(World world, Vector3 mainBlockVec, GCCoreTileEntityOxygenDistributor distributor)
	{
		this (world);
		this.posX = mainBlockVec.x + 0.5D;
		this.posY = mainBlockVec.y + 1.0D;
		this.posZ = mainBlockVec.z + 0.5D;
		this.distributor = distributor;
	}
	
	public GCCoreEntityOxygenBubble(World world)
	{
		super (world);
		this.isImmuneToFire = true;
		this.ignoreFrustumCheck = true;
	}
    
    @Override
	public AxisAlignedBB getBoundingBox()
    {
        return null;
    }

    @Override
	public boolean canBePushed()
    {
        return false;
    }

    @Override
    public void onEntityUpdate()
    {
		if (this.ticks >= Long.MAX_VALUE)
		{
			this.ticks = 1;
		}

		this.ticks++;
		
    	super.onEntityUpdate();
    	
    	TileEntity tileAt = this.worldObj.getBlockTileEntity(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
    	
    	if (tileAt instanceof GCCoreTileEntityOxygenDistributor)
    	{
    		GCCoreTileEntityOxygenDistributor distributor = (GCCoreTileEntityOxygenDistributor) tileAt;
    		
    		this.distributor = distributor;
    	}
    	
    	if (!this.worldObj.isRemote && this.distributor != null)
    	{
    		this.size = distributor.power;
    		
    		Vector3 vec = new Vector3(this.distributor);

    		this.posX = vec.x + 0.5D;
    		this.posY = vec.y + 1.0D;
    		this.posZ = vec.z + 0.5D;
    	}
    	else if (!this.worldObj.isRemote)
    	{
    		this.setDead();
    	}

		if (!this.worldObj.isRemote && this.ticks % 5 == 0)
		{
			PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 50);
		}
    }

	public Packet getDescriptionPacket()
	{
		Packet p = GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.size);
		return p;
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		try
		{
			if (this.worldObj.isRemote)
			{
				this.size = dataStream.readDouble();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

    @Override
	public boolean canBeCollidedWith()
    {
        return false;
    }
	
	public void setSize(double bubbleSize)
	{
		this.size = bubbleSize;
	}
	
	public double getSize()
	{
		return this.size;
	}

	@Override
	protected void entityInit() 
	{
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) 
	{
		this.size = nbttagcompound.getDouble("bubbleSize");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) 
	{
		nbttagcompound.setDouble("bubbleSize", size);
	}
}
