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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

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
    private final Map<CelestialBody, Integer> celestialBodyStatusList = new HashMap<>(4, 1F);
    private final Map<String, List<ItemStack>> schematicsToUnlock = new HashMap<>(4, 1F);

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

    public void loadFromNBT(CompoundNBT nbt)
    {
        this.teamName = nbt.getString("TeamName");
        if (ConfigManagerCore.enableDebug.get())
        {
            GCLog.info("Loading spacerace data for team " + this.teamName);
        }
        this.spaceRaceID = nbt.getInt("SpaceRaceID");
        this.ticksSpent = (int) nbt.getLong("TicksSpent");  //Deal with legacy error
        this.flagData = FlagData.readFlagData(nbt);
        this.teamColor = new Vector3(nbt.getFloat("teamColorR"), nbt.getFloat("teamColorG"), nbt.getFloat("teamColorB"));

        ListNBT tagList = nbt.getList("PlayerList", 10);
        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundNBT tagAt = tagList.getCompound(i);
            this.playerNames.add(tagAt.getString("PlayerName"));
        }

        tagList = nbt.getList("CelestialBodyList", 10);
        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundNBT tagAt = tagList.getCompound(i);

            CelestialBody body = GalaxyRegistry.getCelestialBodyFromUnlocalizedName(tagAt.getString("CelestialBodyName"));

            if (body != null)
            {
                this.celestialBodyStatusList.put(body, tagAt.getInt("TimeTaken"));
            }
        }

        tagList = nbt.getList("SchList", 10);
        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundNBT tagAt = tagList.getCompound(i);
            String name = tagAt.getString("Mem");
            if (this.playerNames.contains(name))
            {
                final ListNBT itemList = tagAt.getList("Sch", 10);
                for (int j = 0; j < itemList.size(); ++j)
                {
                    addNewSchematic(name, ItemStack.read(itemList.getCompound(j)));
                }
            }
        }

        if (ConfigManagerCore.enableDebug.get())
        {
            GCLog.info("Loaded spacerace team data OK.");
        }
    }

    public void saveToNBT(CompoundNBT nbt)
    {
        if (ConfigManagerCore.enableDebug.get())
        {
            GCLog.info("Saving spacerace data for team " + this.teamName);
        }
        nbt.putString("TeamName", this.teamName);
        nbt.putInt("SpaceRaceID", this.spaceRaceID);
        nbt.putLong("TicksSpent", this.ticksSpent);
        this.flagData.saveFlagData(nbt);
        nbt.putDouble("teamColorR", this.teamColor.x);
        nbt.putDouble("teamColorG", this.teamColor.y);
        nbt.putDouble("teamColorB", this.teamColor.z);

        ListNBT tagList = new ListNBT();
        for (String player : this.playerNames)
        {
            CompoundNBT tagComp = new CompoundNBT();
            tagComp.putString("PlayerName", player);
            tagList.add(tagComp);
        }

        nbt.put("PlayerList", tagList);

        tagList = new ListNBT();
        for (Entry<CelestialBody, Integer> celestialBody : this.celestialBodyStatusList.entrySet())
        {
            CompoundNBT tagComp = new CompoundNBT();
            tagComp.putString("CelestialBodyName", celestialBody.getKey().getUnlocalizedName());
            tagComp.putInt("TimeTaken", celestialBody.getValue());
            tagList.add(tagComp);
        }
        nbt.put("CelestialBodyList", tagList);

        tagList = new ListNBT();
        for (Entry<String, List<ItemStack>> schematic : this.schematicsToUnlock.entrySet())
        {
            if (this.playerNames.contains(schematic.getKey()))
            {
                CompoundNBT tagComp = new CompoundNBT();
                tagComp.putString("Mem", schematic.getKey());
                final ListNBT itemList = new ListNBT();
                for (ItemStack stack : schematic.getValue())
                {
                    final CompoundNBT itemTag = new CompoundNBT();
                    stack.write(itemTag);
                    itemList.add(itemTag);
                }
                tagComp.put("Sch", itemList);
                tagList.add(tagComp);
            }
        }
        nbt.put("SchList", tagList);

        if (ConfigManagerCore.enableDebug.get())
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
    public void updatePlayerSchematics(ServerPlayerEntity player)
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
