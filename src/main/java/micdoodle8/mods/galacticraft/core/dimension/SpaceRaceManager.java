package micdoodle8.mods.galacticraft.core.dimension;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;

import com.google.common.collect.Lists;

public class SpaceRaceManager
{
	private static List<SpaceRace> spaceRaceList = Lists.newArrayList();
	
	public static void addSpaceRace(List<String> playerNames, String teamName, FlagData flagData)
	{
		spaceRaceList.add(new SpaceRace(playerNames, teamName, flagData));
	}
	
	public static void removeSpaceRace(SpaceRace race)
	{
		spaceRaceList.remove(race);
	}
	
	public static void tick()
	{
		for (int i = 0; i < spaceRaceList.size(); i++)
		{
			SpaceRace race = spaceRaceList.get(i);
			boolean playerOnline = false;
			
			for (int j = 0; j < MinecraftServer.getServer().getConfigurationManager().playerEntityList.size(); j++)
			{
				Object o = MinecraftServer.getServer().getConfigurationManager().playerEntityList.get(j);
				
				if (o instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer)o;
					
					if (race.getPlayerNames().contains(player.getGameProfile().getName()))
					{
						CelestialBody body = GalaxyRegistry.getCelestialBodyFromDimensionID(player.worldObj.provider.dimensionId);
						
						if (body != null)
						{
							if (!race.getCelestialBodyStatusList().containsKey(body))
							{
								race.setCelestialBodyReached(body);
							}
						}
						
						playerOnline = true;
					}
				}
			}
			
			if (playerOnline)
			{
				race.tick();
			}			
		}
	}
	
	public static void loadSpaceRaces(NBTTagCompound nbt)
	{
		NBTTagList tagList = nbt.getTagList("SpaceRaceList", 10);
		
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound nbt2 = tagList.getCompoundTagAt(i);
			SpaceRace race = new SpaceRace();
			race.loadFromNBT(nbt2);
			spaceRaceList.add(race);
		}
	}
	
	public static void saveSpaceRaces(NBTTagCompound nbt)
	{
		NBTTagList tagList = new NBTTagList();
		
		for (int i = 0; i < spaceRaceList.size(); i++)
		{
			NBTTagCompound nbt2 = new NBTTagCompound();
			SpaceRace race = spaceRaceList.get(i);
			race.saveToNBT(nbt2);
			tagList.appendTag(nbt2);
		}

		nbt.setTag("SpaceRaceList", tagList);
	}
	
	public static SpaceRace getSpaceRaceFromPlayer(String username)
	{
		for (int i = 0; i < spaceRaceList.size(); i++)
		{
			SpaceRace race = spaceRaceList.get(i);
			
			if (race.getPlayerNames().contains(username))
			{
				return race;
			}
		}
		
		return null;
	}
	
	public static void sendSpaceRaceData(EntityPlayerMP toPlayer, String racePlayer)
	{
		SpaceRace spaceRace = SpaceRaceManager.getSpaceRaceFromPlayer(racePlayer);
		
		if (spaceRace != null)
		{
			List<Object> objList = new ArrayList<Object>();
			objList.add(spaceRace.getTeamName());
			objList.add(spaceRace.getFlagData());
			objList.add(spaceRace.getPlayerNames().toArray(new String[spaceRace.getPlayerNames().size()]));
			GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACE_RACE_DATA, objList), toPlayer);
		}
	}
}
