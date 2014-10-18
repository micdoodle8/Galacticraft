package micdoodle8.mods.galacticraft.core.tile;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldProvider;

public class TileEntityTelemetry extends TileEntity
{   
	public Class clientClass;
	public int[] clientData = { -1 };
	public String clientName;
	public GameProfile clientGameProfile = null;

	public static HashSet<BlockVec3> loadedList = new HashSet<BlockVec3>();
	private MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();
	private Entity linkedEntity;
	private UUID toUpdate = null;
	private int pulseRate = 400;
	private int lastHurttime = 0;
	
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
			if (this.toUpdate != null)
			{
				this.addTrackedEntity(this.toUpdate);
				this.toUpdate = null;
			}
			
			String name;
			int data0 = -1;
			int data1 = -1;
			int data2 = -1;
			int data3 = -1;
			int data4 = -1;
			String strUUID = "";
			if (linkedEntity != null && !linkedEntity.isDead)
			{
				if (linkedEntity instanceof EntityPlayerMP)
					name = "$" + ((EntityPlayerMP) linkedEntity).getCommandSenderName();
				else
					name = (String) EntityList.classToStringMapping.get(linkedEntity.getClass());
				if (name == null)
				{
					GCLog.info("Telemetry Unit: Error finding name for "+linkedEntity.getClass().getSimpleName());
					name = "";
				}
				double xmotion = linkedEntity.motionX;
				double ymotion = linkedEntity instanceof EntityLivingBase ? linkedEntity.motionY + 0.078D : linkedEntity.motionY;
				double zmotion = linkedEntity.motionZ;
				data2 = (int) (MathHelper.sqrt_double(xmotion * xmotion + ymotion * ymotion + zmotion * zmotion) * 2000D);
				if (linkedEntity instanceof EntityLivingBase)
				{
					EntityLivingBase eLiving = (EntityLivingBase)linkedEntity;
					data0 = eLiving.hurtTime;
					
					//Calculate a "pulse rate" based on motion and taking damage
					this.pulseRate--;
					if (eLiving.hurtTime > this.lastHurttime) this.pulseRate += 100;
					this.lastHurttime = eLiving.hurtTime;
					if (eLiving.ridingEntity != null) data2 /= 4;  //reduced pulse effect if riding a vehicle
					else if (data2 > 1) this.pulseRate+=2;
					this.pulseRate += Math.max(data2 - pulseRate, 0) / 4;
					if (this.pulseRate > 2000) this.pulseRate = 2000;
					if (this.pulseRate < 400) this.pulseRate = 400;
					data2 = this.pulseRate / 10;
					
					data1 =  (int) (eLiving.getHealth() * 100 / eLiving.getMaxHealth());
					if (eLiving instanceof EntityPlayerMP)
					{
						data3 = ((EntityPlayerMP) eLiving).getFoodStats().getFoodLevel() * 5;
						GCPlayerStats stats = GCPlayerStats.get((EntityPlayerMP) eLiving);
						data4 = stats.airRemaining * 4096 + stats.airRemaining2;
						UUID uuid = ((EntityPlayerMP) eLiving).getUniqueID();
						if (uuid != null) strUUID = uuid.toString();
					}
				}
				else if (linkedEntity instanceof EntitySpaceshipBase)
				{
					EntitySpaceshipBase eShip = (EntitySpaceshipBase)linkedEntity; 
					data0 = eShip.timeUntilLaunch;
					data1 = (int) eShip.posY;
					data3 = eShip.getScaledFuelLevel(100);
					data4 = (int) eShip.rotationPitch;
				}
			}
			else
			{
				name = "";
			}
			GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_TELEMETRY, new Object[] { this.xCoord, this.yCoord, this.zCoord, name, data0, data1, data2, data3, data4, strUUID } ), new TargetPoint(this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 320D));
		}
	}
	
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        Long msb = nbt.getLong("entityUUIDMost");
        Long lsb = nbt.getLong("entityUUIDLeast");
        this.toUpdate = new UUID(msb, lsb);
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if (this.linkedEntity != null && !this.linkedEntity.isDead)
        {
	        nbt.setLong("entityUUIDMost", this.linkedEntity.getUniqueID().getMostSignificantBits());
	        nbt.setLong("entityUUIDLeast", this.linkedEntity.getUniqueID().getLeastSignificantBits());
        }
    }
       
    public void addTrackedEntity(UUID uuid)
    {
    	this.pulseRate = 400;
    	this.lastHurttime = 0;
    	List<Entity> eList = this.worldObj.loadedEntityList;
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

    public void removeTrackedEntity()
    {
    	this.pulseRate = 400;
    	this.linkedEntity = null;
    }
    
	public static TileEntityTelemetry getNearest(TileEntity te)
	{
		if (te == null) return null;
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

	/**
	 * Call this when a player wears a frequency module to check
	 * whether it has been linked with a Telemetry Unit.
	 * 
	 * @param ItemStack  The frequency module
	 * @param player
	 */
	public static void frequencyModulePlayer(ItemStack held, EntityPlayerMP player)
	{
		if (held == null) return;
		NBTTagCompound fmData = held.stackTagCompound;
		if (fmData != null && fmData.hasKey("teDim"))
		{
			int dim = fmData.getInteger("teDim");
			int x = fmData.getInteger("teCoordX");
			int y = fmData.getInteger("teCoordY");
			int z = fmData.getInteger("teCoordZ");
			WorldProvider wp = WorldUtil.getProviderForDimension(dim);
			if (wp == null) System.out.println("Frequency module worn: world provider is null.  This is a bug. "+dim);
			TileEntity te = wp.worldObj.getTileEntity(x, y, z);
			if (te instanceof TileEntityTelemetry)
			{
				if (player == null)
					((TileEntityTelemetry) te).removeTrackedEntity();
				else
					((TileEntityTelemetry) te).addTrackedEntity(player.getUniqueID());
			}
		}
	}
}
