package micdoodle8.mods.galacticraft.core.dimension;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SpaceRace
{
	private static int lastSpaceRaceID = 0;
	private int spaceRaceID;
	private List<String> playerNames = Lists.newArrayList();
	private String teamName;
	private FlagData flagData;
	private Vector3 teamColor;
	private int ticksSpent;
	private Map<CelestialBody, Integer> celestialBodyStatusList = new HashMap<CelestialBody, Integer>();

	public SpaceRace()
	{
	}

	public SpaceRace(List<String> playerNames, String teamName, FlagData flagData, Vector3 teamColor)
	{
		this.playerNames = playerNames;
		this.teamName = teamName;
		this.ticksSpent = 0;
		this.flagData = flagData;
		this.teamColor = teamColor;
		this.spaceRaceID = ++SpaceRace.lastSpaceRaceID;

		for (int i = 0; i < flagData.getWidth(); i++)
		{
			for (int j = 0; j < flagData.getHeight(); j++)
			{
				Vector3 vec = flagData.getColorAt(i, j);
			}
		}
	}

	public void loadFromNBT(NBTTagCompound nbt)
	{
		this.teamName = nbt.getString("TeamName");
		this.spaceRaceID = nbt.getInteger("SpaceRaceID");
		this.ticksSpent = nbt.getInteger("TicksSpent");
		this.flagData = FlagData.readFlagData(nbt);
		this.teamColor = new Vector3(nbt.getDouble("teamColorR"), nbt.getDouble("teamColorG"), nbt.getDouble("teamColorB"));

		NBTTagList tagList = nbt.getTagList("PlayerList", 10);
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound tagAt = tagList.getCompoundTagAt(i);
			this.playerNames.add(tagAt.getString("PlayerName"));
		}

		tagList = nbt.getTagList("CelestialBodyList", 10);
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound tagAt = tagList.getCompoundTagAt(i);

			CelestialBody body = GalaxyRegistry.getCelestialBodyFromUnlocalizedName(tagAt.getString("CelestialBodyName"));

			if (body != null)
			{
				this.celestialBodyStatusList.put(body, tagAt.getInteger("TimeTaken"));
			}
		}
	}

	public void saveToNBT(NBTTagCompound nbt)
	{
		nbt.setString("TeamName", this.teamName);
		nbt.setInteger("SpaceRaceID", this.spaceRaceID);
		nbt.setLong("TicksSpent", this.ticksSpent);
		this.flagData.saveFlagData(nbt);
		nbt.setDouble("teamColorR", this.teamColor.x);
		nbt.setDouble("teamColorG", this.teamColor.y);
		nbt.setDouble("teamColorB", this.teamColor.z);

		NBTTagList tagList = new NBTTagList();
		for (String player : this.playerNames)
		{
			NBTTagCompound tagComp = new NBTTagCompound();
			tagComp.setString("PlayerName", player);
			tagList.appendTag(tagComp);
		}

		nbt.setTag("PlayerList", tagList);

		tagList = new NBTTagList();
		for (Entry<CelestialBody, Integer> celestialBody : this.celestialBodyStatusList.entrySet())
		{
			NBTTagCompound tagComp = new NBTTagCompound();
			tagComp.setString("CelestialBodyName", celestialBody.getKey().getUnlocalizedName());
			tagComp.setInteger("TimeTaken", celestialBody.getValue());
			tagList.appendTag(tagComp);
		}

		nbt.setTag("CelestialBodyList", tagList);
	}

	public void tick()
	{
		this.ticksSpent++;
	}

	public String getTeamName()
	{
		return this.teamName;
	}

	public List<String> getPlayerNames()
	{
		return this.playerNames;
	}

	public FlagData getFlagData()
	{
		return this.flagData;
	}

	public void setFlagData(FlagData flagData)
	{
		this.flagData = flagData;
	}

	public Vector3 getTeamColor()
	{
		return this.teamColor;
	}

	public void setTeamColor(Vector3 teamColor)
	{
		this.teamColor = teamColor;
	}

	public void setTeamName(String teamName)
	{
		this.teamName = teamName;
	}

	public void setPlayerNames(List<String> playerNames)
	{
		this.playerNames = playerNames;
	}

	public void setSpaceRaceID(int raceID)
	{
		this.spaceRaceID = raceID;
	}

	public int getSpaceRaceID()
	{
		return this.spaceRaceID;
	}

	public Map<CelestialBody, Integer> getCelestialBodyStatusList()
	{
		return ImmutableMap.copyOf(this.celestialBodyStatusList);
	}

	public void setCelestialBodyReached(CelestialBody body)
	{
		this.celestialBodyStatusList.put(body, this.ticksSpent);
	}

	public int getTicksSpent()
	{
		return this.ticksSpent;
	}

	@Override
	public int hashCode()
	{
		return this.spaceRaceID;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof SpaceRace)
		{
			return ((SpaceRace) other).getSpaceRaceID() == this.getSpaceRaceID();
		}

		return false;
	}
}
