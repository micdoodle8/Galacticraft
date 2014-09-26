package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;

public class SpaceStationWorldData extends WorldSavedData
{
    private String spaceStationName = "NoName";
    private String owner = "NoOwner";
    private ArrayList<String> allowedPlayers = new ArrayList<String>();
    private NBTTagCompound dataCompound;

    public SpaceStationWorldData(String par1Str)
    {
        super(par1Str);
    }

    public ArrayList<String> getAllowedPlayers()
    {
        return this.allowedPlayers;
    }

    public String getOwner()
    {
        return this.owner;
    }

    public String getSpaceStationName()
    {
        return this.spaceStationName;
    }

    public void setSpaceStationName(String string)
    {
        this.spaceStationName = string;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        this.owner = nbttagcompound.getString("owner").toLowerCase().replace(".", "");
        this.spaceStationName = nbttagcompound.getString("spaceStationName");

        if (nbttagcompound.hasKey("dataCompound"))
        {
            this.dataCompound = nbttagcompound.getCompoundTag("dataCompound");
        }
        else
        {
            this.dataCompound = new NBTTagCompound();
        }

        final NBTTagList var2 = nbttagcompound.getTagList("allowedPlayers", 10);
        this.allowedPlayers = new ArrayList<String>();

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            final String var5 = var4.getString("allowedPlayer");

            if (!this.allowedPlayers.contains(var5.toLowerCase()))
            {
                this.allowedPlayers.add(var5.toLowerCase());
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setString("owner", this.owner);
        nbttagcompound.setString("spaceStationName", this.spaceStationName);
        nbttagcompound.setTag("dataCompound", this.dataCompound);

        final NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.allowedPlayers.size(); ++var3)
        {
            final String player = this.allowedPlayers.get(var3);

            if (player != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setString("allowedPlayer", player);
                var2.appendTag(var4);
            }
        }

        nbttagcompound.setTag("allowedPlayers", var2);
    }

    public static SpaceStationWorldData getStationData(World var0, int var1, EntityPlayer player)
    {
        int providerType = DimensionManager.getProviderType(var1);
        if (providerType != ConfigManagerCore.idDimensionOverworldOrbit && providerType != ConfigManagerCore.idDimensionOverworldOrbitStatic)
        {
            return null;
        }
        else
        {
            final String var2 = SpaceStationWorldData.getSpaceStationID(var1);
            SpaceStationWorldData var3 = (SpaceStationWorldData) var0.loadItemData(SpaceStationWorldData.class, var2);

            if (var3 == null)
            {
                var3 = new SpaceStationWorldData(var2);
                var0.setItemData(var2, var3);
                var3.dataCompound = new NBTTagCompound();

                if (player != null)
                {
                    var3.owner = player.getGameProfile().getName().toLowerCase().replace(".", "");
                }

                var3.spaceStationName = "Station: " + var3.owner;
                var3.allowedPlayers = new ArrayList<String>();

                if (player != null)
                {
                    var3.allowedPlayers.add(player.getGameProfile().getName().toLowerCase());
                }

                var3.markDirty();
            }

            if (var3.getSpaceStationName().replace(" ", "").isEmpty())
            {
                var3.setSpaceStationName("Station: " + var3.owner);
                var3.markDirty();
            }

            return var3;
        }
    }

    public static SpaceStationWorldData getMPSpaceStationData(World var0, int var1, EntityPlayer player)
    {
        final String var2 = SpaceStationWorldData.getSpaceStationID(var1);
        SpaceStationWorldData var3 = (SpaceStationWorldData) var0.loadItemData(SpaceStationWorldData.class, var2);

        if (var3 == null)
        {
            var3 = new SpaceStationWorldData(var2);
            var0.setItemData(var2, var3);
            var3.dataCompound = new NBTTagCompound();

            if (player != null)
            {
                var3.owner = player.getGameProfile().getName().toLowerCase().replace(".", "");
            }

            var3.spaceStationName = "Station: " + var3.owner;
            var3.allowedPlayers = new ArrayList<String>();

            if (player != null)
            {
                var3.allowedPlayers.add(player.getGameProfile().getName().toLowerCase());
            }

            var3.markDirty();
        }

        if (var3.getSpaceStationName().replace(" ", "").isEmpty())
        {
            var3.setSpaceStationName("Station: " + var3.owner);
            var3.markDirty();
        }

        return var3;
    }

    public static String getSpaceStationID(int dimID)
    {
        return "spacestation_" + dimID;
    }
}
