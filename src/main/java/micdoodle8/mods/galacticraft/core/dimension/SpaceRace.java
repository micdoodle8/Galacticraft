package micdoodle8.mods.galacticraft.core.dimension;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SpaceRace
{
    public static final String DEFAULT_NAME = "gui.space_race.unnamed";
    private static int lastSpaceRaceID = 0;
    private int spaceRaceID;
    private List<String> playerNames = Lists.newArrayList();
    public String teamName;
    private FlagData flagData;
    private Vector3 teamColor;
    private int ticksSpent;
    private Map<CelestialBody, Integer> celestialBodyStatusList = new HashMap<>(4, 1F);
    private Map<String, List<ItemStack>> schematicsToUnlock = new HashMap<>(4, 1F);

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
    }

    public void loadFromNBT(NBTTagCompound nbt)
    {
        this.teamName = nbt.getString("TeamName");
        if (ConfigManagerCore.enableDebug)
        {
            GCLog.info("Loading spacerace data for team " + this.teamName);
        }
        this.spaceRaceID = nbt.getInteger("SpaceRaceID");
        this.ticksSpent = (int) nbt.getLong("TicksSpent");  //Deal with legacy error
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

        tagList = nbt.getTagList("SchList", 10);
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tagAt = tagList.getCompoundTagAt(i);
            String name = tagAt.getString("Mem");
            if (name != null && this.playerNames.contains(name))
            {
                final NBTTagList itemList = tagAt.getTagList("Sch", 10);
                for (int j = 0; j < itemList.tagCount(); ++j)
                {
                    addNewSchematic(name, ItemStack.loadItemStackFromNBT(itemList.getCompoundTagAt(j)));
                }
            }
        }

        if (ConfigManagerCore.enableDebug)
        {
            GCLog.info("Loaded spacerace team data OK.");
        }
    }

    public void saveToNBT(NBTTagCompound nbt)
    {
        if (ConfigManagerCore.enableDebug)
        {
            GCLog.info("Saving spacerace data for team " + this.teamName);
        }
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

        tagList = new NBTTagList();
        for (Entry<String, List<ItemStack>> schematic : this.schematicsToUnlock.entrySet())
        {
            if (this.playerNames.contains(schematic.getKey()))
            {
                NBTTagCompound tagComp = new NBTTagCompound();
                tagComp.setString("Mem", schematic.getKey());
                final NBTTagList itemList = new NBTTagList();
                for (ItemStack stack : schematic.getValue())
                {
                    final NBTTagCompound itemTag = new NBTTagCompound();
                    stack.writeToNBT(itemTag);
                    itemList.appendTag(itemTag);
                }
                tagComp.setTag("Sch", itemList);
                tagList.appendTag(tagComp);
            }
        }
        nbt.setTag("SchList", tagList);

        if (ConfigManagerCore.enableDebug)
        {
            GCLog.info("Saved spacerace team data OK.");
        }
    }

    public void tick()
    {
        this.ticksSpent++;
    }

    public String getTeamName()
    {
        String ret = this.teamName;
        if (SpaceRace.DEFAULT_NAME.equals(ret))
        {
            ret = GCCoreUtil.translate(SpaceRace.DEFAULT_NAME);
        }
        return ret;
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

    /*
     * Used to store schematics which need to be unlocked for offline players
     */
    public void addNewSchematic(String member, ItemStack stack)
    {
        List<ItemStack> list;
        list = this.schematicsToUnlock.get(member);
        if (list == null)
        {
            list = new ArrayList<>(1);
            this.schematicsToUnlock.put(member, list);
        }
        list.add(stack);
    }

    /*
     * Used to give a stored schematic to a team player on next login
     */
    public void updatePlayerSchematics(EntityPlayerMP player)
    {
        List<ItemStack> list = this.schematicsToUnlock.get(PlayerUtil.getName(player));
        if (list != null)
        {
            for (ItemStack stack : list)
            {
                SchematicRegistry.unlockNewPage(player, stack);
            }
            this.schematicsToUnlock.remove(PlayerUtil.getName(player));
        }
    }
}
