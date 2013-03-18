package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.FMLLog;

public class GCCoreSpaceStationData extends WorldSavedData
{
    private String spaceStationName = "New Space Station";
    private String owner;
    private NBTTagCompound dataCompound;
    
	public GCCoreSpaceStationData(String par1Str) 
	{
		super(par1Str);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) 
	{
		this.spaceStationName = nbttagcompound.getString("spaceStationName");
		this.owner = nbttagcompound.getString("owner");

        if (nbttagcompound.hasKey("DataCompound"))
        {
            this.dataCompound = nbttagcompound.getCompoundTag("DataCompound");
        }
        else
        {
            this.dataCompound = new NBTTagCompound();
        }
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) 
	{
		nbttagcompound.setString("spaceStationName", spaceStationName);
		nbttagcompound.setString("owner", owner);
		nbttagcompound.setCompoundTag("DataCompound", this.dataCompound);
	}
	
	public static GCCoreSpaceStationData getStationData(World var0, int var1, EntityPlayer player)
	{
        if (DimensionManager.getProviderType(var1) != GCCoreConfigManager.idDimensionOverworldOrbit)
        {
            return null;
        }
        else
        {
            String var2 = getSpaceStationID(var1);
            GCCoreSpaceStationData var3 = (GCCoreSpaceStationData)var0.loadItemData(GCCoreSpaceStationData.class, var2);

            if (var3 == null)
            {
                var3 = new GCCoreSpaceStationData(var2);
                var0.setItemData(var2, var3);
                var3.dataCompound = new NBTTagCompound();
                var3.owner = player.username;
                var3.markDirty();
            }

            return var3;
        }
	}

    public static GCCoreSpaceStationData getMPSpaceStationData(World var0, int var1, EntityPlayer player)
    {
        String var2 = getSpaceStationID(var1);
        GCCoreSpaceStationData var3 = (GCCoreSpaceStationData)var0.loadItemData(GCCoreSpaceStationData.class, var2);

        if (var3 == null)
        {
            var3 = new GCCoreSpaceStationData(var2);
            var0.setItemData(var2, var3);
            var3.dataCompound = new NBTTagCompound();
            var3.owner = player.username;
            var3.markDirty();
        }

        return var3;
    }
	
	public static String getSpaceStationID(int dimID)
	{
		return "spacestation_" + dimID;
	}
}
