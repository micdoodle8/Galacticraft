package micdoodle8.mods.galacticraft.core.dimension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.FMLLog;

public class SpaceRaceManager
{
	private static final Set<SpaceRace> spaceRaces = Sets.newHashSet();
	
	public static SpaceRace addSpaceRace(List<String> playerNames, String teamName, FlagData flagData)
	{
		SpaceRace spaceRace = new SpaceRace(playerNames, teamName, flagData);
		spaceRaces.add(spaceRace);
		return spaceRace;
	}
	
	public static SpaceRace addSpaceRace(SpaceRace spaceRace)
	{
		spaceRaces.add(spaceRace);
		return spaceRace;
	}
	
	public static void removeSpaceRace(SpaceRace race)
	{
		spaceRaces.remove(race);
	}
	
	public static void tick()
	{
		for (SpaceRace race : spaceRaces)
		{
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
			spaceRaces.add(race);
		}
	}
	
	public static void saveSpaceRaces(NBTTagCompound nbt)
	{
		NBTTagList tagList = new NBTTagList();
		
		for (SpaceRace race : spaceRaces)
		{
			NBTTagCompound nbt2 = new NBTTagCompound();
			race.saveToNBT(nbt2);
			tagList.appendTag(nbt2);
		}

		nbt.setTag("SpaceRaceList", tagList);
	}
	
	public static SpaceRace getSpaceRaceFromPlayer(String username)
	{
		for (SpaceRace race : spaceRaces)
		{			
			if (race.getPlayerNames().contains(username))
			{
				return race;
			}
		}
		
		return null;
	}
	
	public static SpaceRace getSpaceRaceFromID(int teamID)
	{
		for (SpaceRace race : spaceRaces)
		{
			if (race.getSpaceRaceID() == teamID)
			{
				return race;
			}
		}
		
		return null;
	}
	
	public static void sendSpaceRaceData(EntityPlayerMP toPlayer, SpaceRace spaceRace)
	{
		if (spaceRace != null)
		{
			List<Object> objList = new ArrayList<Object>();
			objList.add(spaceRace.getSpaceRaceID());
			objList.add(spaceRace.getTeamName());
			objList.add(spaceRace.getFlagData());
			objList.add(spaceRace.getPlayerNames().toArray(new String[spaceRace.getPlayerNames().size()]));
			GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACE_RACE_DATA, objList), toPlayer);
		}
	}
	
	public static ImmutableSet<SpaceRace> getSpaceRaces()
	{
		return ImmutableSet.copyOf(new HashSet<SpaceRace>(spaceRaces));
	}
}
