package micdoodle8.mods.galacticraft.core.entities.player;

import com.google.common.collect.Maps;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockPanelLighting;
import micdoodle8.mods.galacticraft.core.command.CommandGCInv;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPanelLight;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.tick.AsteroidsTickHandlerServer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class StatsCapability extends GCPlayerStats
{
    public WeakReference<EntityPlayerMP> player;

    public InventoryExtended extendedInventory = new InventoryExtended();

    public int airRemaining;
    public int airRemaining2;

    public int thermalLevel;
    public boolean thermalLevelNormalising;

    public int damageCounter;

    // temporary data while player is in planet selection GUI
    public int spaceshipTier = 1;
    public ItemStack[] rocketStacks = new ItemStack[2];
    public int rocketType;
    public int fuelLevel;
    public Item rocketItem;
    public ItemStack launchpadStack;
    public int astroMinerCount = 0;
    private List<BlockVec3> activeAstroMinerChunks = new LinkedList<>();

    public boolean usingParachute;

    public ItemStack parachuteInSlot;
    public ItemStack lastParachuteInSlot;

    public ItemStack frequencyModuleInSlot;
    public ItemStack lastFrequencyModuleInSlot;

    public ItemStack maskInSlot;
    public ItemStack lastMaskInSlot;

    public ItemStack gearInSlot;
    public ItemStack lastGearInSlot;

    public ItemStack tankInSlot1;
    public ItemStack lastTankInSlot1;

    public ItemStack tankInSlot2;
    public ItemStack lastTankInSlot2;

    public ItemStack thermalHelmetInSlot;
    public ItemStack lastThermalHelmetInSlot;

    public ItemStack thermalChestplateInSlot;
    public ItemStack lastThermalChestplateInSlot;

    public ItemStack thermalLeggingsInSlot;
    public ItemStack lastThermalLeggingsInSlot;

    public ItemStack thermalBootsInSlot;
    public ItemStack lastThermalBootsInSlot;

    public ItemStack shieldControllerInSlot;
    public ItemStack lastShieldControllerInSlot;

    public int launchAttempts = 0;

    public int spaceRaceInviteTeamID;

    public boolean usingPlanetSelectionGui;
    public String savedPlanetList = "";
    public int openPlanetSelectionGuiCooldown;
    public boolean hasOpenedPlanetSelectionGui = false;

    public int chestSpawnCooldown;
    public micdoodle8.mods.galacticraft.api.vector.Vector3 chestSpawnVector;

    public int teleportCooldown;

    public int chatCooldown;

    public double distanceSinceLastStep;
    public int lastStep;

    public double coordsTeleportedFromX;
    public double coordsTeleportedFromZ;

    public HashMap<Integer, Integer> spaceStationDimensionData = Maps.newHashMap();

    public boolean oxygenSetupValid;
    public boolean lastOxygenSetupValid;

    public boolean touchedGround;
    public boolean lastOnGround;
    public boolean inLander;
    public boolean justLanded;

    public List<ISchematicPage> unlockedSchematics = new LinkedList<>();
    public List<ISchematicPage> lastUnlockedSchematics = new LinkedList<>();

    public int cryogenicChamberCooldown;

    public boolean receivedSoundWarning;
    public boolean receivedBedWarning;
    public boolean openedSpaceRaceManager = false;
    public boolean sentFlags = false;
    public boolean newInOrbit = true;
    public boolean newAdventureSpawn;
    public int buildFlags = 0;

    public int incrementalDamage = 0;
    private float savedSpeed = 0F;  // used by titanium armor

    public String startDimension = "";
    public int glassColor1 = -1;
    public int glassColor2 = -1;
    public int glassColor3 = -1;
    
    private IBlockState[] panelLightingBases = new IBlockState[BlockPanelLighting.PANELTYPES_LENGTH];
    private int panelLightingColor = 0xf0f0e0;

    @Override
    public WeakReference<EntityPlayerMP> getPlayer()
    {
        return player;
    }

    @Override
    public void setPlayer(WeakReference<EntityPlayerMP> player)
    {
        this.player = player;
    }

    @Override
    public InventoryExtended getExtendedInventory()
    {
        return extendedInventory;
    }

    @Override
    public void setExtendedInventory(InventoryExtended extendedInventory)
    {
        this.extendedInventory = extendedInventory;
    }

    @Override
    public int getAirRemaining()
    {
        return airRemaining;
    }

    @Override
    public void setAirRemaining(int airRemaining)
    {
        this.airRemaining = airRemaining;
    }

    @Override
    public int getAirRemaining2()
    {
        return airRemaining2;
    }

    @Override
    public void setAirRemaining2(int airRemaining2)
    {
        this.airRemaining2 = airRemaining2;
    }

    @Override
    public int getThermalLevel()
    {
        return thermalLevel;
    }

    @Override
    public void setThermalLevel(int thermalLevel)
    {
        this.thermalLevel = thermalLevel;
    }

    @Override
    public boolean isThermalLevelNormalising()
    {
        return thermalLevelNormalising;
    }

    @Override
    public void setThermalLevelNormalising(boolean thermalLevelNormalising)
    {
        this.thermalLevelNormalising = thermalLevelNormalising;
    }

    @Override
    public int getDamageCounter()
    {
        return damageCounter;
    }

    @Override
    public void setDamageCounter(int damageCounter)
    {
        this.damageCounter = damageCounter;
    }

    @Override
    public int getSpaceshipTier()
    {
        return spaceshipTier;
    }

    @Override
    public void setSpaceshipTier(int spaceshipTier)
    {
        this.spaceshipTier = spaceshipTier;
    }

    @Override
    public ItemStack[] getRocketStacks()
    {
        return rocketStacks;
    }

    @Override
    public void setRocketStacks(ItemStack[] rocketStacks)
    {
        this.rocketStacks = rocketStacks;
    }

    @Override
    public int getRocketType()
    {
        return rocketType;
    }

    @Override
    public void setRocketType(int rocketType)
    {
        this.rocketType = rocketType;
    }

    @Override
    public int getFuelLevel()
    {
        return fuelLevel;
    }

    @Override
    public void setFuelLevel(int fuelLevel)
    {
        this.fuelLevel = fuelLevel;
    }

    @Override
    public Item getRocketItem()
    {
        return rocketItem;
    }

    @Override
    public void setRocketItem(Item rocketItem)
    {
        this.rocketItem = rocketItem;
    }

    @Override
    public ItemStack getLaunchpadStack()
    {
        return launchpadStack;
    }

    @Override
    public void setLaunchpadStack(ItemStack launchpadStack)
    {
        this.launchpadStack = launchpadStack;
    }

    @Override
    public int getAstroMinerCount()
    {
        return astroMinerCount;
    }

    @Override
    public void setAstroMinerCount(int astroMinerCount)
    {
        this.astroMinerCount = astroMinerCount;
    }

    @Override
    public List<BlockVec3> getActiveAstroMinerChunks()
    {
        return this.activeAstroMinerChunks;
    }

    @Override
    public boolean isUsingParachute()
    {
        return usingParachute;
    }

    @Override
    public void setUsingParachute(boolean usingParachute)
    {
        this.usingParachute = usingParachute;
    }

    @Override
    public ItemStack getParachuteInSlot()
    {
        return parachuteInSlot;
    }

    @Override
    public void setParachuteInSlot(ItemStack parachuteInSlot)
    {
        this.parachuteInSlot = parachuteInSlot;
    }

    @Override
    public ItemStack getLastParachuteInSlot()
    {
        return lastParachuteInSlot;
    }

    @Override
    public void setLastParachuteInSlot(ItemStack lastParachuteInSlot)
    {
        this.lastParachuteInSlot = lastParachuteInSlot;
    }

    @Override
    public ItemStack getFrequencyModuleInSlot()
    {
        return frequencyModuleInSlot;
    }

    @Override
    public void setFrequencyModuleInSlot(ItemStack frequencyModuleInSlot)
    {
        this.frequencyModuleInSlot = frequencyModuleInSlot;
    }

    @Override
    public ItemStack getLastFrequencyModuleInSlot()
    {
        return lastFrequencyModuleInSlot;
    }

    @Override
    public void setLastFrequencyModuleInSlot(ItemStack lastFrequencyModuleInSlot)
    {
        this.lastFrequencyModuleInSlot = lastFrequencyModuleInSlot;
    }

    @Override
    public ItemStack getMaskInSlot()
    {
        return maskInSlot;
    }

    @Override
    public void setMaskInSlot(ItemStack maskInSlot)
    {
        this.maskInSlot = maskInSlot;
    }

    @Override
    public ItemStack getLastMaskInSlot()
    {
        return lastMaskInSlot;
    }

    @Override
    public void setLastMaskInSlot(ItemStack lastMaskInSlot)
    {
        this.lastMaskInSlot = lastMaskInSlot;
    }

    @Override
    public ItemStack getGearInSlot()
    {
        return gearInSlot;
    }

    @Override
    public void setGearInSlot(ItemStack gearInSlot)
    {
        this.gearInSlot = gearInSlot;
    }

    @Override
    public ItemStack getLastGearInSlot()
    {
        return lastGearInSlot;
    }

    @Override
    public void setLastGearInSlot(ItemStack lastGearInSlot)
    {
        this.lastGearInSlot = lastGearInSlot;
    }

    @Override
    public ItemStack getTankInSlot1()
    {
        return tankInSlot1;
    }

    @Override
    public void setTankInSlot1(ItemStack tankInSlot1)
    {
        this.tankInSlot1 = tankInSlot1;
    }

    @Override
    public ItemStack getLastTankInSlot1()
    {
        return lastTankInSlot1;
    }

    @Override
    public void setLastTankInSlot1(ItemStack lastTankInSlot1)
    {
        this.lastTankInSlot1 = lastTankInSlot1;
    }

    @Override
    public ItemStack getTankInSlot2()
    {
        return tankInSlot2;
    }

    @Override
    public void setTankInSlot2(ItemStack tankInSlot2)
    {
        this.tankInSlot2 = tankInSlot2;
    }

    @Override
    public ItemStack getLastTankInSlot2()
    {
        return lastTankInSlot2;
    }

    @Override
    public void setLastTankInSlot2(ItemStack lastTankInSlot2)
    {
        this.lastTankInSlot2 = lastTankInSlot2;
    }

    @Override
    public ItemStack getThermalHelmetInSlot()
    {
        return thermalHelmetInSlot;
    }

    @Override
    public void setThermalHelmetInSlot(ItemStack thermalHelmetInSlot)
    {
        this.thermalHelmetInSlot = thermalHelmetInSlot;
    }

    @Override
    public ItemStack getLastThermalHelmetInSlot()
    {
        return lastThermalHelmetInSlot;
    }

    @Override
    public void setLastThermalHelmetInSlot(ItemStack lastThermalHelmetInSlot)
    {
        this.lastThermalHelmetInSlot = lastThermalHelmetInSlot;
    }

    @Override
    public ItemStack getThermalChestplateInSlot()
    {
        return thermalChestplateInSlot;
    }

    @Override
    public void setThermalChestplateInSlot(ItemStack thermalChestplateInSlot)
    {
        this.thermalChestplateInSlot = thermalChestplateInSlot;
    }

    @Override
    public ItemStack getLastThermalChestplateInSlot()
    {
        return lastThermalChestplateInSlot;
    }

    @Override
    public void setLastThermalChestplateInSlot(ItemStack lastThermalChestplateInSlot)
    {
        this.lastThermalChestplateInSlot = lastThermalChestplateInSlot;
    }

    @Override
    public ItemStack getThermalLeggingsInSlot()
    {
        return thermalLeggingsInSlot;
    }

    @Override
    public void setThermalLeggingsInSlot(ItemStack thermalLeggingsInSlot)
    {
        this.thermalLeggingsInSlot = thermalLeggingsInSlot;
    }

    @Override
    public ItemStack getLastThermalLeggingsInSlot()
    {
        return lastThermalLeggingsInSlot;
    }

    @Override
    public void setLastThermalLeggingsInSlot(ItemStack lastThermalLeggingsInSlot)
    {
        this.lastThermalLeggingsInSlot = lastThermalLeggingsInSlot;
    }

    @Override
    public ItemStack getThermalBootsInSlot()
    {
        return thermalBootsInSlot;
    }

    @Override
    public void setThermalBootsInSlot(ItemStack thermalBootsInSlot)
    {
        this.thermalBootsInSlot = thermalBootsInSlot;
    }

    @Override
    public ItemStack getLastThermalBootsInSlot()
    {
        return lastThermalBootsInSlot;
    }

    @Override
    public void setLastThermalBootsInSlot(ItemStack lastThermalBootsInSlot)
    {
        this.lastThermalBootsInSlot = lastThermalBootsInSlot;
    }

    @Override
    public ItemStack getShieldControllerInSlot()
    {
        return shieldControllerInSlot;
    }

    @Override
    public void setShieldControllerInSlot(ItemStack shieldControllerInSlot)
    {
        this.shieldControllerInSlot = shieldControllerInSlot;
    }

    @Override
    public ItemStack getLastShieldControllerInSlot()
    {
        return lastShieldControllerInSlot;
    }

    @Override
    public void setLastShieldControllerInSlot(ItemStack lastShieldControllerInSlot)
    {
        this.lastShieldControllerInSlot = lastShieldControllerInSlot;
    }

    @Override
    public int getLaunchAttempts()
    {
        return launchAttempts;
    }

    @Override
    public void setLaunchAttempts(int launchAttempts)
    {
        this.launchAttempts = launchAttempts;
    }

    @Override
    public int getSpaceRaceInviteTeamID()
    {
        return spaceRaceInviteTeamID;
    }

    @Override
    public void setSpaceRaceInviteTeamID(int spaceRaceInviteTeamID)
    {
        this.spaceRaceInviteTeamID = spaceRaceInviteTeamID;
    }

    @Override
    public boolean isUsingPlanetSelectionGui()
    {
        return usingPlanetSelectionGui;
    }

    @Override
    public void setUsingPlanetSelectionGui(boolean usingPlanetSelectionGui)
    {
        this.usingPlanetSelectionGui = usingPlanetSelectionGui;
    }

    @Override
    public String getSavedPlanetList()
    {
        return savedPlanetList;
    }

    @Override
    public void setSavedPlanetList(String savedPlanetList)
    {
        this.savedPlanetList = savedPlanetList;
    }

    @Override
    public int getOpenPlanetSelectionGuiCooldown()
    {
        return openPlanetSelectionGuiCooldown;
    }

    @Override
    public void setOpenPlanetSelectionGuiCooldown(int openPlanetSelectionGuiCooldown)
    {
        this.openPlanetSelectionGuiCooldown = openPlanetSelectionGuiCooldown;
    }

    @Override
    public boolean hasOpenedPlanetSelectionGui()
    {
        return hasOpenedPlanetSelectionGui;
    }

    @Override
    public void setHasOpenedPlanetSelectionGui(boolean hasOpenedPlanetSelectionGui)
    {
        this.hasOpenedPlanetSelectionGui = hasOpenedPlanetSelectionGui;
    }

    @Override
    public int getChestSpawnCooldown()
    {
        return chestSpawnCooldown;
    }

    @Override
    public void setChestSpawnCooldown(int chestSpawnCooldown)
    {
        this.chestSpawnCooldown = chestSpawnCooldown;
    }

    @Override
    public Vector3 getChestSpawnVector()
    {
        return chestSpawnVector;
    }

    @Override
    public void setChestSpawnVector(Vector3 chestSpawnVector)
    {
        this.chestSpawnVector = chestSpawnVector;
    }

    @Override
    public int getTeleportCooldown()
    {
        return teleportCooldown;
    }

    @Override
    public void setTeleportCooldown(int teleportCooldown)
    {
        this.teleportCooldown = teleportCooldown;
    }

    @Override
    public int getChatCooldown()
    {
        return chatCooldown;
    }

    @Override
    public void setChatCooldown(int chatCooldown)
    {
        this.chatCooldown = chatCooldown;
    }

    @Override
    public double getDistanceSinceLastStep()
    {
        return distanceSinceLastStep;
    }

    @Override
    public void setDistanceSinceLastStep(double distanceSinceLastStep)
    {
        this.distanceSinceLastStep = distanceSinceLastStep;
    }

    @Override
    public int getLastStep()
    {
        return lastStep;
    }

    @Override
    public void setLastStep(int lastStep)
    {
        this.lastStep = lastStep;
    }

    @Override
    public double getCoordsTeleportedFromX()
    {
        return coordsTeleportedFromX;
    }

    @Override
    public void setCoordsTeleportedFromX(double coordsTeleportedFromX)
    {
        this.coordsTeleportedFromX = coordsTeleportedFromX;
    }

    @Override
    public double getCoordsTeleportedFromZ()
    {
        return coordsTeleportedFromZ;
    }

    @Override
    public void setCoordsTeleportedFromZ(double coordsTeleportedFromZ)
    {
        this.coordsTeleportedFromZ = coordsTeleportedFromZ;
    }

    @Override
    public HashMap<Integer, Integer> getSpaceStationDimensionData()
    {
        return spaceStationDimensionData;
    }

    @Override
    public void setSpaceStationDimensionData(HashMap<Integer, Integer> spaceStationDimensionData)
    {
        this.spaceStationDimensionData = spaceStationDimensionData;
    }

    @Override
    public boolean isOxygenSetupValid()
    {
        return oxygenSetupValid;
    }

    @Override
    public void setOxygenSetupValid(boolean oxygenSetupValid)
    {
        this.oxygenSetupValid = oxygenSetupValid;
    }

    @Override
    public boolean isLastOxygenSetupValid()
    {
        return lastOxygenSetupValid;
    }

    @Override
    public void setLastOxygenSetupValid(boolean lastOxygenSetupValid)
    {
        this.lastOxygenSetupValid = lastOxygenSetupValid;
    }

    @Override
    public boolean isTouchedGround()
    {
        return touchedGround;
    }

    @Override
    public void setTouchedGround(boolean touchedGround)
    {
        this.touchedGround = touchedGround;
    }

    @Override
    public boolean isLastOnGround()
    {
        return lastOnGround;
    }

    @Override
    public void setLastOnGround(boolean lastOnGround)
    {
        this.lastOnGround = lastOnGround;
    }

    @Override
    public boolean isInLander()
    {
        return inLander;
    }

    @Override
    public void setInLander(boolean inLander)
    {
        this.inLander = inLander;
    }

    @Override
    public boolean hasJustLanded()
    {
        return justLanded;
    }

    @Override
    public void setJustLanded(boolean justLanded)
    {
        this.justLanded = justLanded;
    }

    @Override
    public List<ISchematicPage> getUnlockedSchematics()
    {
        return unlockedSchematics;
    }

    @Override
    public void setUnlockedSchematics(List<ISchematicPage> unlockedSchematics)
    {
        this.unlockedSchematics = unlockedSchematics;
    }

    @Override
    public List<ISchematicPage> getLastUnlockedSchematics()
    {
        return lastUnlockedSchematics;
    }

    @Override
    public void setLastUnlockedSchematics(List<ISchematicPage> lastUnlockedSchematics)
    {
        this.lastUnlockedSchematics = lastUnlockedSchematics;
    }

    @Override
    public int getCryogenicChamberCooldown()
    {
        return cryogenicChamberCooldown;
    }

    @Override
    public void setCryogenicChamberCooldown(int cryogenicChamberCooldown)
    {
        this.cryogenicChamberCooldown = cryogenicChamberCooldown;
    }

    @Override
    public boolean hasReceivedSoundWarning()
    {
        return receivedSoundWarning;
    }

    @Override
    public void setReceivedSoundWarning(boolean receivedSoundWarning)
    {
        this.receivedSoundWarning = receivedSoundWarning;
    }

    @Override
    public boolean hasReceivedBedWarning()
    {
        return receivedBedWarning;
    }

    @Override
    public void setReceivedBedWarning(boolean receivedBedWarning)
    {
        this.receivedBedWarning = receivedBedWarning;
    }

    @Override
    public boolean hasOpenedSpaceRaceManager()
    {
        return openedSpaceRaceManager;
    }

    @Override
    public void setOpenedSpaceRaceManager(boolean openedSpaceRaceManager)
    {
        this.openedSpaceRaceManager = openedSpaceRaceManager;
    }

    @Override
    public boolean hasSentFlags()
    {
        return sentFlags;
    }

    @Override
    public void setSentFlags(boolean sentFlags)
    {
        this.sentFlags = sentFlags;
    }

    @Override
    public boolean isNewInOrbit()
    {
        return newInOrbit;
    }

    @Override
    public void setNewInOrbit(boolean newInOrbit)
    {
        this.newInOrbit = newInOrbit;
    }

    @Override
    public boolean isNewAdventureSpawn()
    {
        return newAdventureSpawn;
    }

    @Override
    public void setNewAdventureSpawn(boolean newAdventureSpawn)
    {
        this.newAdventureSpawn = newAdventureSpawn;
    }

    @Override
    public int getBuildFlags()
    {
        return buildFlags;
    }

    @Override
    public void setBuildFlags(int buildFlags)
    {
        this.buildFlags = buildFlags;
    }

    @Override
    public int getIncrementalDamage()
    {
        return incrementalDamage;
    }

    @Override
    public void setIncrementalDamage(int incrementalDamage)
    {
        this.incrementalDamage = incrementalDamage;
    }

    @Override
    public String getStartDimension()
    {
        return startDimension;
    }

    @Override
    public void setStartDimension(String startDimension)
    {
        this.startDimension = startDimension;
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt)
    {
        nbt.setTag("ExtendedInventoryGC", this.extendedInventory.writeToNBT(new NBTTagList()));
        nbt.setInteger("playerAirRemaining", this.airRemaining);
        nbt.setInteger("damageCounter", this.damageCounter);
        nbt.setBoolean("OxygenSetupValid", this.oxygenSetupValid);
        nbt.setBoolean("usingParachute2", this.usingParachute);
        nbt.setBoolean("usingPlanetSelectionGui", this.usingPlanetSelectionGui);
        nbt.setInteger("teleportCooldown", this.teleportCooldown);
        nbt.setDouble("coordsTeleportedFromX", this.coordsTeleportedFromX);
        nbt.setDouble("coordsTeleportedFromZ", this.coordsTeleportedFromZ);
        nbt.setString("startDimension", this.startDimension);
        nbt.setString("spaceStationDimensionInfo", WorldUtil.spaceStationDataToString(this.spaceStationDimensionData));
        nbt.setInteger("thermalLevel", this.thermalLevel);

        Collections.sort(this.unlockedSchematics);

        NBTTagList tagList = new NBTTagList();

        for (ISchematicPage page : this.unlockedSchematics)
        {
            if (page != null)
            {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setInteger("UnlockedPage", page.getPageID());
                tagList.appendTag(nbttagcompound);
            }
        }

        nbt.setTag("Schematics", tagList);

        nbt.setInteger("rocketStacksLength", this.rocketStacks.length);
        nbt.setInteger("SpaceshipTier", this.spaceshipTier);
        nbt.setInteger("FuelLevel", this.fuelLevel);
        if (this.rocketItem != null)
        {
            ItemStack returnRocket = new ItemStack(this.rocketItem, 1, this.rocketType);
            nbt.setTag("ReturnRocket", returnRocket.writeToNBT(new NBTTagCompound()));
        }

        final NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.rocketStacks.length; ++var3)
        {
            if (this.rocketStacks[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.rocketStacks[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        nbt.setTag("RocketItems", var2);
        final NBTTagCompound var4 = new NBTTagCompound();
        if (this.launchpadStack != null)
        {
            nbt.setTag("LaunchpadStack", this.launchpadStack.writeToNBT(var4));
        }
        else
        {
            nbt.setTag("LaunchpadStack", var4);
        }

        nbt.setInteger("CryogenicChamberCooldown", this.cryogenicChamberCooldown);
        nbt.setBoolean("ReceivedSoundWarning", this.receivedSoundWarning);
        nbt.setBoolean("ReceivedBedWarning", this.receivedBedWarning);
        nbt.setInteger("BuildFlags", this.buildFlags);
        nbt.setBoolean("ShownSpaceRace", this.openedSpaceRaceManager);
        nbt.setInteger("AstroMinerCount", this.astroMinerCount);
        NBTTagList astroList = new NBTTagList();
        for (BlockVec3 data : this.activeAstroMinerChunks)
        {
            if (data != null)
            {
                astroList.appendTag(data.writeToNBT(new NBTTagCompound()));
            }
        }
        nbt.setTag("AstroData", astroList);
        
        nbt.setInteger("GlassColor1", this.glassColor1);
        nbt.setInteger("GlassColor2", this.glassColor2);
        nbt.setInteger("GlassColor3", this.glassColor3);
        
        NBTTagList panelList = new NBTTagList();
        for (int i = 0; i < BlockPanelLighting.PANELTYPES_LENGTH; ++i)
        {
            final NBTTagCompound stateNBT = new NBTTagCompound();
            IBlockState bs = this.panelLightingBases[i];
            if (bs != null)
            {
                TileEntityPanelLight.writeBlockState(stateNBT, bs);
            }
            panelList.appendTag(stateNBT);
        }
        nbt.setTag("PanLi", panelList);
        
        nbt.setInteger("PanCo", this.panelLightingColor);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt)
    {
        try
        {
            this.airRemaining = nbt.getInteger("playerAirRemaining");
            this.damageCounter = nbt.getInteger("damageCounter");
            this.oxygenSetupValid = this.lastOxygenSetupValid = nbt.getBoolean("OxygenSetupValid");
            this.thermalLevel = nbt.getInteger("thermalLevel");

            // Backwards compatibility
            NBTTagList nbttaglist = nbt.getTagList("Inventory", 10);
            this.extendedInventory.readFromNBTOld(nbttaglist);

            if (nbt.hasKey("ExtendedInventoryGC"))
            {
                this.extendedInventory.readFromNBT(nbt.getTagList("ExtendedInventoryGC", 10));
            }

            // Added for GCInv command - if tried to load an offline player's
            // inventory, load it now
            // (if there was no offline load, then the dontload flag in doLoad()
            // will make sure nothing happens)
            EntityPlayerMP p = this.player.get();
            if (p != null)
            {
                ItemStack[] saveinv = CommandGCInv.getSaveData(PlayerUtil.getName(p).toLowerCase());
                if (saveinv != null)
                {
                    CommandGCInv.doLoad(p);
                }
            }

            if (nbt.hasKey("SpaceshipTier"))
            {
                this.spaceshipTier = nbt.getInteger("SpaceshipTier");
            }

            //New keys in version 3.0.5.220
            if (nbt.hasKey("FuelLevel"))
            {
                this.fuelLevel = nbt.getInteger("FuelLevel");
            }
            if (nbt.hasKey("ReturnRocket"))
            {
                ItemStack returnRocket = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("ReturnRocket"));
                if (returnRocket != null)
                {
                    this.rocketItem = returnRocket.getItem();
                    this.rocketType = returnRocket.getItemDamage();
                }
            }

            this.usingParachute = nbt.getBoolean("usingParachute2");
            this.usingPlanetSelectionGui = nbt.getBoolean("usingPlanetSelectionGui");
            this.teleportCooldown = nbt.getInteger("teleportCooldown");
            this.coordsTeleportedFromX = nbt.getDouble("coordsTeleportedFromX");
            this.coordsTeleportedFromZ = nbt.getDouble("coordsTeleportedFromZ");
            this.startDimension = nbt.hasKey("startDimension") ? nbt.getString("startDimension") : "";
            if (nbt.hasKey("spaceStationDimensionID"))
            {
                // If loading from an old save file, the home space station is always the overworld, so use 0 as home planet
                this.spaceStationDimensionData = WorldUtil.stringToSpaceStationData("0$" + nbt.getInteger("spaceStationDimensionID"));
            }
            else
            {
                this.spaceStationDimensionData = WorldUtil.stringToSpaceStationData(nbt.getString("spaceStationDimensionInfo"));
            }

            if (nbt.getBoolean("usingPlanetSelectionGui"))
            {
                this.openPlanetSelectionGuiCooldown = 20;
            }

            if (nbt.hasKey("RocketItems") && nbt.hasKey("rocketStacksLength"))
            {
                final NBTTagList var23 = nbt.getTagList("RocketItems", 10);
                int length = nbt.getInteger("rocketStacksLength");

                this.rocketStacks = new ItemStack[length];

                for (int var3 = 0; var3 < var23.tagCount(); ++var3)
                {
                    final NBTTagCompound var4 = var23.getCompoundTagAt(var3);
                    final int var5 = var4.getByte("Slot") & 255;

                    if (var5 < this.rocketStacks.length)
                    {
                        this.rocketStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
                    }
                }
            }

            this.unlockedSchematics = new ArrayList<ISchematicPage>();

            if (p != null)
            {
                for (int i = 0; i < nbt.getTagList("Schematics", 10).tagCount(); ++i)
                {
                    final NBTTagCompound nbttagcompound = nbt.getTagList("Schematics", 10).getCompoundTagAt(i);

                    final int j = nbttagcompound.getInteger("UnlockedPage");

                    SchematicRegistry.addUnlockedPage(p, SchematicRegistry.getMatchingRecipeForID(j));
                }
            }

            Collections.sort(this.unlockedSchematics);

            this.cryogenicChamberCooldown = nbt.getInteger("CryogenicChamberCooldown");

            if (nbt.hasKey("ReceivedSoundWarning"))
            {
                this.receivedSoundWarning = nbt.getBoolean("ReceivedSoundWarning");
            }
            if (nbt.hasKey("ReceivedBedWarning"))
            {
                this.receivedBedWarning = nbt.getBoolean("ReceivedBedWarning");
            }

            if (nbt.hasKey("LaunchpadStack"))
            {
                this.launchpadStack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("LaunchpadStack"));
                if (this.launchpadStack != null && this.launchpadStack.stackSize == 0)
                {
                    this.launchpadStack = null;
                }
            }
            else
            {
                // for backwards compatibility with saves which don't have this tag - players can't lose launchpads
                this.launchpadStack = new ItemStack(GCBlocks.landingPad, 9, 0);
            }

            if (nbt.hasKey("BuildFlags"))
            {
                this.buildFlags = nbt.getInteger("BuildFlags");
            }

            if (nbt.hasKey("ShownSpaceRace"))
            {
                this.openedSpaceRaceManager = nbt.getBoolean("ShownSpaceRace");
            }

            if (nbt.hasKey("AstroMinerCount"))
            {
                this.astroMinerCount = nbt.getInteger("AstroMinerCount");
            }
            if (nbt.hasKey("AstroData"))
            {
                this.activeAstroMinerChunks.clear();
                NBTTagList astroList = nbt.getTagList("AstroData", 10);
                for (int i = 0; i < astroList.tagCount(); ++i)
                {
                    final NBTTagCompound nbttagcompound = astroList.getCompoundTagAt(i);
                    BlockVec3 data = BlockVec3.readFromNBT(nbttagcompound);
                    this.activeAstroMinerChunks.add(data);
                }
                if (GalacticraftCore.isPlanetsLoaded)
                {
                    AsteroidsTickHandlerServer.loadAstroChunkList(this.activeAstroMinerChunks);
                }
            }

            if (nbt.hasKey("GlassColor1"))
            {
                this.glassColor1 = nbt.getInteger("GlassColor1");
                this.glassColor2 = nbt.getInteger("GlassColor2");
                this.glassColor3 = nbt.getInteger("GlassColor3");
            }

            if (nbt.hasKey("PanLi"))
            {
                final NBTTagList panels = nbt.getTagList("PanLi", 10);
                for (int i = 0; i < panels.tagCount(); ++i)
                {
                    if (i == BlockPanelLighting.PANELTYPES_LENGTH) break;
                    final NBTTagCompound stateNBT = panels.getCompoundTagAt(i);
                    IBlockState bs = TileEntityPanelLight.readBlockState(stateNBT);
                    this.panelLightingBases[i] = (bs.getBlock() == Blocks.air) ? null : bs;
                }
            }

            if (nbt.hasKey("PanCo"))
            {
                this.panelLightingColor = nbt.getInteger("PanCo");
            }

            
            GCLog.debug("Loading GC player data for " + PlayerUtil.getName(player.get()) + " : " + this.buildFlags);

            this.sentFlags = false;
        }
        catch (Exception e)
        {
            GCLog.severe("Found error in saved Galacticraft player data for " + PlayerUtil.getName(player.get()) + " - this should fix itself next relog.");
            e.printStackTrace();
        }

        GCLog.debug("Finished loading GC player data for " + PlayerUtil.getName(player.get()) + " : " + this.buildFlags);
    }

    @Override
    public void copyFrom(GCPlayerStats oldData, boolean keepInv)
    {
        if (keepInv)
        {
            this.extendedInventory.copyInventory(oldData.getExtendedInventory());
        }

        this.spaceStationDimensionData = oldData.getSpaceStationDimensionData();
        this.unlockedSchematics = oldData.getUnlockedSchematics();
        this.receivedSoundWarning = oldData.hasReceivedSoundWarning();
        this.receivedBedWarning = oldData.hasReceivedBedWarning();
        this.openedSpaceRaceManager = oldData.hasOpenedSpaceRaceManager();
        this.spaceRaceInviteTeamID = oldData.getSpaceRaceInviteTeamID();
        this.buildFlags = oldData.getBuildFlags();
        this.glassColor1 = oldData.getGlassColor1();
        this.glassColor2 = oldData.getGlassColor2();
        this.glassColor3 = oldData.getGlassColor3();
        this.panelLightingBases = oldData.getPanelLightingBases();
        this.panelLightingColor = oldData.getPanelLightingColor();
        this.astroMinerCount = oldData.getAstroMinerCount();
        this.activeAstroMinerChunks = oldData.getActiveAstroMinerChunks();
        this.sentFlags = false;
    }

    @Override
    public void setGlassColors(int color1, int color2, int color3)
    {
        boolean changes = false;
        if (this.glassColor1 != color1)
        {
            changes = true;
            this.glassColor1 = color1;
        }
        if (this.glassColor2 != color2)
        {
            changes = true;
            this.glassColor2 = color2;
        }
        if (this.glassColor3 != color3)
        {
            changes = true;
            this.glassColor3 = color3;
        }
        if (changes)
            ColorUtil.sendUpdatedColorsToPlayer(this);
    }
    
    @Override
    public int getGlassColor1()
    {
        return glassColor1;
    }

    @Override
    public int getGlassColor2()
    {
        return glassColor2;
    }

    @Override
    public int getGlassColor3()
    {
        return glassColor3;
    }

    @Override
    public IBlockState[] getPanelLightingBases()
    {
        return panelLightingBases;
    }

    @Override
    public int getPanelLightingColor()
    {
        return panelLightingColor;
    }

    @Override
    public void setPanelLightingColor(int color)
    {
        panelLightingColor = color;
    }

    @Override
    public Object[] getMiscNetworkedStats()
    {
        int length = 2 + BlockPanelLighting.PANELTYPES_LENGTH * 2;
        Object[] result = new Object[length];
        result[0] = this.getBuildFlags();
        BlockPanelLighting.getNetworkedData(result, this.panelLightingBases);
        result[length - 1] = this.panelLightingColor;
        return result;
    }

    @Override
    public void setSavedSpeed(float value)
    {
        this.savedSpeed = value;
    }

    @Override
    public float getSavedSpeed()
    {
        return this.savedSpeed;
    }
}
