package micdoodle8.mods.galacticraft.core.entities.player;

import com.google.common.collect.Maps;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.command.CommandGCInv;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class StatsCapability implements IStatsCapability
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

    public ArrayList<ISchematicPage> unlockedSchematics = new ArrayList<ISchematicPage>();
    public ArrayList<ISchematicPage> lastUnlockedSchematics = new ArrayList<ISchematicPage>();

    public int cryogenicChamberCooldown;

    public boolean receivedSoundWarning;
    public boolean receivedBedWarning;
    public boolean openedSpaceRaceManager = false;
    public boolean sentFlags = false;
    public boolean newInOrbit = true;
    public boolean newAdventureSpawn;
    public int buildFlags = 0;

    public int incrementalDamage = 0;

    public String startDimension = "";

    public WeakReference<EntityPlayerMP> getPlayer()
    {
        return player;
    }

    public void setPlayer(WeakReference<EntityPlayerMP> player)
    {
        this.player = player;
    }

    public InventoryExtended getExtendedInventory()
    {
        return extendedInventory;
    }

    public void setExtendedInventory(InventoryExtended extendedInventory)
    {
        this.extendedInventory = extendedInventory;
    }

    public int getAirRemaining()
    {
        return airRemaining;
    }

    public void setAirRemaining(int airRemaining)
    {
        this.airRemaining = airRemaining;
    }

    public int getAirRemaining2()
    {
        return airRemaining2;
    }

    public void setAirRemaining2(int airRemaining2)
    {
        this.airRemaining2 = airRemaining2;
    }

    public int getThermalLevel()
    {
        return thermalLevel;
    }

    public void setThermalLevel(int thermalLevel)
    {
        this.thermalLevel = thermalLevel;
    }

    public boolean isThermalLevelNormalising()
    {
        return thermalLevelNormalising;
    }

    public void setThermalLevelNormalising(boolean thermalLevelNormalising)
    {
        this.thermalLevelNormalising = thermalLevelNormalising;
    }

    public int getDamageCounter()
    {
        return damageCounter;
    }

    public void setDamageCounter(int damageCounter)
    {
        this.damageCounter = damageCounter;
    }

    public int getSpaceshipTier()
    {
        return spaceshipTier;
    }

    public void setSpaceshipTier(int spaceshipTier)
    {
        this.spaceshipTier = spaceshipTier;
    }

    public ItemStack[] getRocketStacks()
    {
        return rocketStacks;
    }

    public void setRocketStacks(ItemStack[] rocketStacks)
    {
        this.rocketStacks = rocketStacks;
    }

    public int getRocketType()
    {
        return rocketType;
    }

    public void setRocketType(int rocketType)
    {
        this.rocketType = rocketType;
    }

    public int getFuelLevel()
    {
        return fuelLevel;
    }

    public void setFuelLevel(int fuelLevel)
    {
        this.fuelLevel = fuelLevel;
    }

    public Item getRocketItem()
    {
        return rocketItem;
    }

    public void setRocketItem(Item rocketItem)
    {
        this.rocketItem = rocketItem;
    }

    public ItemStack getLaunchpadStack()
    {
        return launchpadStack;
    }

    public void setLaunchpadStack(ItemStack launchpadStack)
    {
        this.launchpadStack = launchpadStack;
    }

    public int getAstroMinerCount()
    {
        return astroMinerCount;
    }

    public void setAstroMinerCount(int astroMinerCount)
    {
        this.astroMinerCount = astroMinerCount;
    }

    public boolean isUsingParachute()
    {
        return usingParachute;
    }

    public void setUsingParachute(boolean usingParachute)
    {
        this.usingParachute = usingParachute;
    }

    public ItemStack getParachuteInSlot()
    {
        return parachuteInSlot;
    }

    public void setParachuteInSlot(ItemStack parachuteInSlot)
    {
        this.parachuteInSlot = parachuteInSlot;
    }

    public ItemStack getLastParachuteInSlot()
    {
        return lastParachuteInSlot;
    }

    public void setLastParachuteInSlot(ItemStack lastParachuteInSlot)
    {
        this.lastParachuteInSlot = lastParachuteInSlot;
    }

    public ItemStack getFrequencyModuleInSlot()
    {
        return frequencyModuleInSlot;
    }

    public void setFrequencyModuleInSlot(ItemStack frequencyModuleInSlot)
    {
        this.frequencyModuleInSlot = frequencyModuleInSlot;
    }

    public ItemStack getLastFrequencyModuleInSlot()
    {
        return lastFrequencyModuleInSlot;
    }

    public void setLastFrequencyModuleInSlot(ItemStack lastFrequencyModuleInSlot)
    {
        this.lastFrequencyModuleInSlot = lastFrequencyModuleInSlot;
    }

    public ItemStack getMaskInSlot()
    {
        return maskInSlot;
    }

    public void setMaskInSlot(ItemStack maskInSlot)
    {
        this.maskInSlot = maskInSlot;
    }

    public ItemStack getLastMaskInSlot()
    {
        return lastMaskInSlot;
    }

    public void setLastMaskInSlot(ItemStack lastMaskInSlot)
    {
        this.lastMaskInSlot = lastMaskInSlot;
    }

    public ItemStack getGearInSlot()
    {
        return gearInSlot;
    }

    public void setGearInSlot(ItemStack gearInSlot)
    {
        this.gearInSlot = gearInSlot;
    }

    public ItemStack getLastGearInSlot()
    {
        return lastGearInSlot;
    }

    public void setLastGearInSlot(ItemStack lastGearInSlot)
    {
        this.lastGearInSlot = lastGearInSlot;
    }

    public ItemStack getTankInSlot1()
    {
        return tankInSlot1;
    }

    public void setTankInSlot1(ItemStack tankInSlot1)
    {
        this.tankInSlot1 = tankInSlot1;
    }

    public ItemStack getLastTankInSlot1()
    {
        return lastTankInSlot1;
    }

    public void setLastTankInSlot1(ItemStack lastTankInSlot1)
    {
        this.lastTankInSlot1 = lastTankInSlot1;
    }

    public ItemStack getTankInSlot2()
    {
        return tankInSlot2;
    }

    public void setTankInSlot2(ItemStack tankInSlot2)
    {
        this.tankInSlot2 = tankInSlot2;
    }

    public ItemStack getLastTankInSlot2()
    {
        return lastTankInSlot2;
    }

    public void setLastTankInSlot2(ItemStack lastTankInSlot2)
    {
        this.lastTankInSlot2 = lastTankInSlot2;
    }

    public ItemStack getThermalHelmetInSlot()
    {
        return thermalHelmetInSlot;
    }

    public void setThermalHelmetInSlot(ItemStack thermalHelmetInSlot)
    {
        this.thermalHelmetInSlot = thermalHelmetInSlot;
    }

    public ItemStack getLastThermalHelmetInSlot()
    {
        return lastThermalHelmetInSlot;
    }

    public void setLastThermalHelmetInSlot(ItemStack lastThermalHelmetInSlot)
    {
        this.lastThermalHelmetInSlot = lastThermalHelmetInSlot;
    }

    public ItemStack getThermalChestplateInSlot()
    {
        return thermalChestplateInSlot;
    }

    public void setThermalChestplateInSlot(ItemStack thermalChestplateInSlot)
    {
        this.thermalChestplateInSlot = thermalChestplateInSlot;
    }

    public ItemStack getLastThermalChestplateInSlot()
    {
        return lastThermalChestplateInSlot;
    }

    public void setLastThermalChestplateInSlot(ItemStack lastThermalChestplateInSlot)
    {
        this.lastThermalChestplateInSlot = lastThermalChestplateInSlot;
    }

    public ItemStack getThermalLeggingsInSlot()
    {
        return thermalLeggingsInSlot;
    }

    public void setThermalLeggingsInSlot(ItemStack thermalLeggingsInSlot)
    {
        this.thermalLeggingsInSlot = thermalLeggingsInSlot;
    }

    public ItemStack getLastThermalLeggingsInSlot()
    {
        return lastThermalLeggingsInSlot;
    }

    public void setLastThermalLeggingsInSlot(ItemStack lastThermalLeggingsInSlot)
    {
        this.lastThermalLeggingsInSlot = lastThermalLeggingsInSlot;
    }

    public ItemStack getThermalBootsInSlot()
    {
        return thermalBootsInSlot;
    }

    public void setThermalBootsInSlot(ItemStack thermalBootsInSlot)
    {
        this.thermalBootsInSlot = thermalBootsInSlot;
    }

    public ItemStack getLastThermalBootsInSlot()
    {
        return lastThermalBootsInSlot;
    }

    public void setLastThermalBootsInSlot(ItemStack lastThermalBootsInSlot)
    {
        this.lastThermalBootsInSlot = lastThermalBootsInSlot;
    }

    public ItemStack getShieldControllerInSlot()
    {
        return shieldControllerInSlot;
    }

    public void setShieldControllerInSlot(ItemStack shieldControllerInSlot)
    {
        this.shieldControllerInSlot = shieldControllerInSlot;
    }

    public ItemStack getLastShieldControllerInSlot()
    {
        return lastShieldControllerInSlot;
    }

    public void setLastShieldControllerInSlot(ItemStack lastShieldControllerInSlot)
    {
        this.lastShieldControllerInSlot = lastShieldControllerInSlot;
    }

    public int getLaunchAttempts()
    {
        return launchAttempts;
    }

    public void setLaunchAttempts(int launchAttempts)
    {
        this.launchAttempts = launchAttempts;
    }

    public int getSpaceRaceInviteTeamID()
    {
        return spaceRaceInviteTeamID;
    }

    public void setSpaceRaceInviteTeamID(int spaceRaceInviteTeamID)
    {
        this.spaceRaceInviteTeamID = spaceRaceInviteTeamID;
    }

    public boolean isUsingPlanetSelectionGui()
    {
        return usingPlanetSelectionGui;
    }

    public void setUsingPlanetSelectionGui(boolean usingPlanetSelectionGui)
    {
        this.usingPlanetSelectionGui = usingPlanetSelectionGui;
    }

    public String getSavedPlanetList()
    {
        return savedPlanetList;
    }

    public void setSavedPlanetList(String savedPlanetList)
    {
        this.savedPlanetList = savedPlanetList;
    }

    public int getOpenPlanetSelectionGuiCooldown()
    {
        return openPlanetSelectionGuiCooldown;
    }

    public void setOpenPlanetSelectionGuiCooldown(int openPlanetSelectionGuiCooldown)
    {
        this.openPlanetSelectionGuiCooldown = openPlanetSelectionGuiCooldown;
    }

    public boolean hasOpenedPlanetSelectionGui()
    {
        return hasOpenedPlanetSelectionGui;
    }

    public void setHasOpenedPlanetSelectionGui(boolean hasOpenedPlanetSelectionGui)
    {
        this.hasOpenedPlanetSelectionGui = hasOpenedPlanetSelectionGui;
    }

    public int getChestSpawnCooldown()
    {
        return chestSpawnCooldown;
    }

    public void setChestSpawnCooldown(int chestSpawnCooldown)
    {
        this.chestSpawnCooldown = chestSpawnCooldown;
    }

    public Vector3 getChestSpawnVector()
    {
        return chestSpawnVector;
    }

    public void setChestSpawnVector(Vector3 chestSpawnVector)
    {
        this.chestSpawnVector = chestSpawnVector;
    }

    public int getTeleportCooldown()
    {
        return teleportCooldown;
    }

    public void setTeleportCooldown(int teleportCooldown)
    {
        this.teleportCooldown = teleportCooldown;
    }

    public int getChatCooldown()
    {
        return chatCooldown;
    }

    public void setChatCooldown(int chatCooldown)
    {
        this.chatCooldown = chatCooldown;
    }

    public double getDistanceSinceLastStep()
    {
        return distanceSinceLastStep;
    }

    public void setDistanceSinceLastStep(double distanceSinceLastStep)
    {
        this.distanceSinceLastStep = distanceSinceLastStep;
    }

    public int getLastStep()
    {
        return lastStep;
    }

    public void setLastStep(int lastStep)
    {
        this.lastStep = lastStep;
    }

    public double getCoordsTeleportedFromX()
    {
        return coordsTeleportedFromX;
    }

    public void setCoordsTeleportedFromX(double coordsTeleportedFromX)
    {
        this.coordsTeleportedFromX = coordsTeleportedFromX;
    }

    public double getCoordsTeleportedFromZ()
    {
        return coordsTeleportedFromZ;
    }

    public void setCoordsTeleportedFromZ(double coordsTeleportedFromZ)
    {
        this.coordsTeleportedFromZ = coordsTeleportedFromZ;
    }

    public HashMap<Integer, Integer> getSpaceStationDimensionData()
    {
        return spaceStationDimensionData;
    }

    public void setSpaceStationDimensionData(HashMap<Integer, Integer> spaceStationDimensionData)
    {
        this.spaceStationDimensionData = spaceStationDimensionData;
    }

    public boolean isOxygenSetupValid()
    {
        return oxygenSetupValid;
    }

    public void setOxygenSetupValid(boolean oxygenSetupValid)
    {
        this.oxygenSetupValid = oxygenSetupValid;
    }

    public boolean isLastOxygenSetupValid()
    {
        return lastOxygenSetupValid;
    }

    public void setLastOxygenSetupValid(boolean lastOxygenSetupValid)
    {
        this.lastOxygenSetupValid = lastOxygenSetupValid;
    }

    public boolean isTouchedGround()
    {
        return touchedGround;
    }

    public void setTouchedGround(boolean touchedGround)
    {
        this.touchedGround = touchedGround;
    }

    public boolean isLastOnGround()
    {
        return lastOnGround;
    }

    public void setLastOnGround(boolean lastOnGround)
    {
        this.lastOnGround = lastOnGround;
    }

    public boolean isInLander()
    {
        return inLander;
    }

    public void setInLander(boolean inLander)
    {
        this.inLander = inLander;
    }

    public boolean hasJustLanded()
    {
        return justLanded;
    }

    public void setJustLanded(boolean justLanded)
    {
        this.justLanded = justLanded;
    }

    public ArrayList<ISchematicPage> getUnlockedSchematics()
    {
        return unlockedSchematics;
    }

    public void setUnlockedSchematics(ArrayList<ISchematicPage> unlockedSchematics)
    {
        this.unlockedSchematics = unlockedSchematics;
    }

    public ArrayList<ISchematicPage> getLastUnlockedSchematics()
    {
        return lastUnlockedSchematics;
    }

    public void setLastUnlockedSchematics(ArrayList<ISchematicPage> lastUnlockedSchematics)
    {
        this.lastUnlockedSchematics = lastUnlockedSchematics;
    }

    public int getCryogenicChamberCooldown()
    {
        return cryogenicChamberCooldown;
    }

    public void setCryogenicChamberCooldown(int cryogenicChamberCooldown)
    {
        this.cryogenicChamberCooldown = cryogenicChamberCooldown;
    }

    public boolean hasReceivedSoundWarning()
    {
        return receivedSoundWarning;
    }

    public void setReceivedSoundWarning(boolean receivedSoundWarning)
    {
        this.receivedSoundWarning = receivedSoundWarning;
    }

    public boolean hasReceivedBedWarning()
    {
        return receivedBedWarning;
    }

    public void setReceivedBedWarning(boolean receivedBedWarning)
    {
        this.receivedBedWarning = receivedBedWarning;
    }

    public boolean hasOpenedSpaceRaceManager()
    {
        return openedSpaceRaceManager;
    }

    public void setOpenedSpaceRaceManager(boolean openedSpaceRaceManager)
    {
        this.openedSpaceRaceManager = openedSpaceRaceManager;
    }

    public boolean isSentFlags()
    {
        return sentFlags;
    }

    public void setSentFlags(boolean sentFlags)
    {
        this.sentFlags = sentFlags;
    }

    public boolean isNewInOrbit()
    {
        return newInOrbit;
    }

    public void setNewInOrbit(boolean newInOrbit)
    {
        this.newInOrbit = newInOrbit;
    }

    public boolean isNewAdventureSpawn()
    {
        return newAdventureSpawn;
    }

    public void setNewAdventureSpawn(boolean newAdventureSpawn)
    {
        this.newAdventureSpawn = newAdventureSpawn;
    }

    public int getBuildFlags()
    {
        return buildFlags;
    }

    public void setBuildFlags(int buildFlags)
    {
        this.buildFlags = buildFlags;
    }

    public int getIncrementalDamage()
    {
        return incrementalDamage;
    }

    public void setIncrementalDamage(int incrementalDamage)
    {
        this.incrementalDamage = incrementalDamage;
    }

    public String getStartDimension()
    {
        return startDimension;
    }

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
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt)
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
            ItemStack[] saveinv = CommandGCInv.getSaveData(p.getGameProfile().getName().toLowerCase());
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

        this.sentFlags = false;
        if (ConfigManagerCore.enableDebug)
        {
            GCLog.info("Loading GC player data for " + player.get().getGameProfile().getName() + " : " + this.buildFlags);
        }
    }

    public void copyFrom(IStatsCapability oldData, boolean keepInv)
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
        this.astroMinerCount = oldData.getAstroMinerCount();
        this.sentFlags = false;
    }
}
