package micdoodle8.mods.galacticraft.core.tile;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public class TileEntityTelemetry extends TileEntity
{   
	public Class clientClass;
	public int[] clientData;

	public static HashSet<BlockVec3> loadedList = new HashSet<BlockVec3>();
	private static MinecraftServer theServer;
	private Entity linkedEntity;
	
	static
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			theServer = FMLCommonHandler.instance().getMinecraftServerInstance();
		}
	}
	
	@Override
	public void validate()
	{
        super.validate();
        if (this.worldObj.isRemote)
        {
        	loadedList.add(new BlockVec3(this));
        }
	}
	
	@Override
	public void invalidate()
	{
		super.invalidate();
        if (this.worldObj.isRemote)
        {
        	loadedList.remove(new BlockVec3(this));
        }
	}

	@Override
	public void updateEntity()
	{
		if (!this.worldObj.isRemote)
		{
			String name;
			int data0 = -1;
			int data1 = -1;
			int data2 = -1;
			int data3 = -1;
			int data4 = -1;
			if (linkedEntity != null && !linkedEntity.isDead)
			{
				name = (String) EntityList.classToStringMapping.get(linkedEntity.getClass());
				data1 = linkedEntity instanceof EntityLivingBase ? (int) (((EntityLivingBase)linkedEntity).getHealth() * 2) : -1;
				double xmotion = linkedEntity.motionX;
				double ymotion = linkedEntity instanceof EntityLivingBase ? linkedEntity.motionY + 0.078D : linkedEntity.motionY;
				double zmotion = linkedEntity.motionZ;
				data2 = (int) (MathHelper.sqrt_double(xmotion * xmotion + ymotion * ymotion + zmotion * zmotion) * 2000D);
			}
			else
			{
				name = "";
			}
			GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_TELEMETRY, new Object[] { this.xCoord, this.yCoord, this.zCoord, name, data0, data1, data2, data3, data4 } ), new TargetPoint(this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 320D));
		}
	}
	
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
    }
       
    public void addTrackedEntity(UUID uuid)
    {
    	List<Entity> eList = this.worldObj.getLoadedEntityList();
    	for (Entity e : eList)
    	{
    		if (e.getUniqueID().equals(uuid))
    		{
    			this.linkedEntity = e;
    			return;
    		}
    	}
    	//TODO Add some kind of watcher to add the entity when next loaded
    	this.linkedEntity = null;
    }

	public static TileEntityTelemetry getNearest(TileEntity te)
	{
		BlockVec3 target = new BlockVec3(te);
		
		int distSq = 1025;
		BlockVec3 nearest = null;
		for (BlockVec3 telemeter : loadedList)
		{
			int dist = target.distanceSquared(telemeter); 
			if (dist < distSq)
			{
				distSq = dist;
				nearest = telemeter;
			}
		}
		
		if (nearest == null) return null;
		TileEntity result = nearest.getTileEntity(te.getWorldObj());
		if (result instanceof TileEntityTelemetry) return (TileEntityTelemetry) result;
		return null;
	}
	
	
}
