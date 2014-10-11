package micdoodle8.mods.galacticraft.core.tile;

import java.util.HashSet;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityTelemetry extends TileEntity
{   
	public int clientTime1;
	public int clientTime2;

	public static HashSet<BlockVec3> loadedList = new HashSet<BlockVec3>();
	
	
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
			World w1 = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0);
			World w2 = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(ConfigManagerCore.idDimensionMoon);
			int time1 = w1 != null ? (int) ((w1.getWorldTime() + 6000L) % 24000L) : 0;
			int time2 = w2 != null ? (int) ((w2.getWorldTime() + 6000L) % 24000L) : 0;
			GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_TELEMETRY, new Object[] { this.xCoord, this.yCoord, this.zCoord, time1, time2 } ), new TargetPoint(this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 320D));
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
       
    public void onActivated()
    {
    	
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
