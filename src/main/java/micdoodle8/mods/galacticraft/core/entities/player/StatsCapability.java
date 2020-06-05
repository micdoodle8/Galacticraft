package micdoodle8.mods.galacticraft.core.entities.player;

import com.google.common.collect.Maps;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.command.CommandGCInv;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.*;

public class StatsCapability extends GCPlayerStats
{
    public WeakReference<ServerPlayerEntity> player;

    public InventoryExtended extendedInventory = new InventoryExtended();

    public int airRemaining;
    public int airRemaining2;

    public int thermalLevel;
    public boolean thermalLevelNormalising;

    public int damageCounter;

    // temporary data while player is in planet selection GUI
    public int spaceshipTier = 1;
    public NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);
    public int fuelLevel;
    public Item rocketItem;
    public ItemStack launchpadStack;
    public int astroMinerCount = 0;
    private List<BlockVec3> activeAstroMinerChunks = new LinkedList<>();

    public boolean usingParachute;

    public ItemStack parachuteInSlot = ItemStack.EMPTY;
    public ItemStack lastParachuteInSlot = ItemStack.EMPTY;

    public ItemStack frequencyModuleInSlot = ItemStack.EMPTY;
    public ItemStack lastFrequencyModuleInSlot = ItemStack.EMPTY;

    public ItemStack maskInSlot = ItemStack.EMPTY;
    public ItemStack lastMaskInSlot = ItemStack.EMPTY;

    public ItemStack gearInSlot = ItemStack.EMPTY;
    public ItemStack lastGearInSlot = ItemStack.EMPTY;

    public ItemStack tankInSlot1 = ItemStack.EMPTY;
    public ItemStack lastTankInSlot1 = ItemStack.EMPTY;

    public ItemStack tankInSlot2 = ItemStack.EMPTY;
    public ItemStack lastTankInSlot2 = ItemStack.EMPTY;

    public ItemStack thermalHelmetInSlot = ItemStack.EMPTY;
    public ItemStack lastThermalHelmetInSlot = ItemStack.EMPTY;

    public ItemStack thermalChestplateInSlot = ItemStack.EMPTY;
    public ItemStack lastThermalChestplateInSlot = ItemStack.EMPTY;

    public ItemStack thermalLeggingsInSlot = ItemStack.EMPTY;
    public ItemStack lastThermalLeggingsInSlot = ItemStack.EMPTY;

    public ItemStack thermalBootsInSlot = ItemStack.EMPTY;
    public ItemStack lastThermalBootsInSlot = ItemStack.EMPTY;

    public ItemStack shieldControllerInSlot = ItemStack.EMPTY;
    public ItemStack lastShieldControllerInSlot = ItemStack.EMPTY;

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
    
//    private BlockState[] panelLightingBases = new BlockState[BlockPanelLighting.PANELTYPES_LENGTH]; TODO Panel Lighting
    private int panelLightingColor = 0xf0f0e0;

    public StatsCapability()
    {
    }

    public StatsCapability(WeakReference<ServerPlayerEntity> player)
    {
        this.player = player;
    }

    @Override
    public WeakReference<ServerPlayerEntity> getPlayer()
    {
        return player;
    }

    @Override
    public void setPlayer(WeakReference<ServerPlayerEntity> player)
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
    public NonNullList<ItemStack> getRocketStacks()
    {
        return stacks;
    }

    @Override
    public void setRocketStacks(NonNullList<ItemStack> rocketStacks)
    {
        this.stacks = rocketStacks;
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
    @Nullable
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
    public void saveNBTData(CompoundNBT nbt)
    {
        nbt.put("ExtendedInventoryGC", this.extendedInventory.writeToNBT(new ListNBT()));
        nbt.putInt("playerAirRemaining", this.airRemaining);
        nbt.putInt("damageCounter", this.damageCounter);
        nbt.putBoolean("OxygenSetupValid", this.oxygenSetupValid);
        nbt.putBoolean("usingParachute2", this.usingParachute);
        nbt.putBoolean("usingPlanetSelectionGui", this.usingPlanetSelectionGui);
        nbt.putInt("teleportCooldown", this.teleportCooldown);
        nbt.putDouble("coordsTeleportedFromX", this.coordsTeleportedFromX);
        nbt.putDouble("coordsTeleportedFromZ", this.coordsTeleportedFromZ);
        nbt.putString("startDimension", this.startDimension);
        nbt.putString("spaceStationDimensionInfo", WorldUtil.spaceStationDataToString(this.spaceStationDimensionData));
        nbt.putInt("thermalLevel", this.thermalLevel);

        Collections.sort(this.unlockedSchematics);

        ListNBT tagList = new ListNBT();

        for (ISchematicPage page : this.unlockedSchematics)
        {
            if (page != null)
            {
                final CompoundNBT nbttagcompound = new CompoundNBT();
                nbttagcompound.putInt("UnlockedPage", page.getPageID());
                tagList.add(nbttagcompound);
            }
        }

        nbt.put("Schematics", tagList);

        nbt.putInt("rocketStacksLength", this.stacks.size());
        nbt.putInt("SpaceshipTier", this.spaceshipTier);
        nbt.putInt("FuelLevel", this.fuelLevel);
        if (this.rocketItem != null)
        {
            ItemStack returnRocket = new ItemStack(this.rocketItem, 1);
            nbt.put("ReturnRocket", returnRocket.write(new CompoundNBT()));
        }

        ListNBT nbttaglist = new ListNBT();

        for (int i = 0; i < this.stacks.size(); ++i)
        {
            ItemStack itemstack = (ItemStack)this.stacks.get(i);

            if (!itemstack.isEmpty())
            {
                CompoundNBT nbttagcompound = new CompoundNBT();
                nbttagcompound.putByte("Slot", (byte)i);
                itemstack.write(nbttagcompound);
                nbttaglist.add(nbttagcompound);
            }
        }

        if (!nbttaglist.isEmpty())
        {
            nbt.put("RocketItems", nbttaglist);
        }

        final CompoundNBT var4 = new CompoundNBT();
        if (this.launchpadStack != null)
        {
            nbt.put("LaunchpadStack", this.launchpadStack.write(var4));
        }
        else
        {
            nbt.put("LaunchpadStack", var4);
        }

        nbt.putInt("CryogenicChamberCooldown", this.cryogenicChamberCooldown);
        nbt.putBoolean("ReceivedSoundWarning", this.receivedSoundWarning);
        nbt.putBoolean("ReceivedBedWarning", this.receivedBedWarning);
        nbt.putInt("BuildFlags", this.buildFlags);
        nbt.putBoolean("ShownSpaceRace", this.openedSpaceRaceManager);
        nbt.putInt("AstroMinerCount", this.astroMinerCount);
        ListNBT astroList = new ListNBT();
        for (BlockVec3 data : this.activeAstroMinerChunks)
        {
            if (data != null)
            {
                astroList.add(data.writeToNBT(new CompoundNBT()));
            }
        }
        nbt.put("AstroData", astroList);
        
        nbt.putInt("GlassColor1", this.glassColor1);
        nbt.putInt("GlassColor2", this.glassColor2);
        nbt.putInt("GlassColor3", this.glassColor3);
        
//        ListNBT panelList = new ListNBT();
//        for (int i = 0; i < BlockPanelLighting.PANELTYPES_LENGTH; ++i)
//        {
//            final CompoundNBT stateNBT = new CompoundNBT();
//            BlockState bs = this.panelLightingBases[i];
//            if (bs != null)
//            {
//                TileEntityPanelLight.writeBlockState(stateNBT, bs);
//            }
//            panelList.appendTag(stateNBT);
//        } TODO Panel Lighting
//        nbt.put("PanLi", panelList);
        
        nbt.putInt("PanCo", this.panelLightingColor);
    }

    @Override
    public void loadNBTData(CompoundNBT nbt)
    {
        try
        {
            this.airRemaining = nbt.getInt("playerAirRemaining");
            this.damageCounter = nbt.getInt("damageCounter");
            this.oxygenSetupValid = this.lastOxygenSetupValid = nbt.getBoolean("OxygenSetupValid");
            this.thermalLevel = nbt.getInt("thermalLevel");

            // Backwards compatibility
            ListNBT nbttaglist = nbt.getList("Inventory", 10);
            this.extendedInventory.readFromNBTOld(nbttaglist);

            if (nbt.contains("ExtendedInventoryGC"))
            {
                this.extendedInventory.readFromNBT(nbt.getList("ExtendedInventoryGC", 10));
            }

            // Added for GCInv command - if tried to load an offline player's
            // inventory, load it now
            // (if there was no offline load, then the dontload flag in doLoad()
            // will make sure nothing happens)
            ServerPlayerEntity p = this.player.get();
            if (p != null)
            {
                ItemStack[] saveinv = CommandGCInv.getSaveData(PlayerUtil.getName(p).toLowerCase());
                if (saveinv != null)
                {
                    CommandGCInv.doLoad(p);
                }
            }

            if (nbt.contains("SpaceshipTier"))
            {
                this.spaceshipTier = nbt.getInt("SpaceshipTier");
            }

            //New keys in version 3.0.5.220
            if (nbt.contains("FuelLevel"))
            {
                this.fuelLevel = nbt.getInt("FuelLevel");
            }
            if (nbt.contains("ReturnRocket"))
            {
                ItemStack returnRocket = ItemStack.read(nbt.getCompound("ReturnRocket"));
                this.rocketItem = returnRocket.getItem();
            }

            this.usingParachute = nbt.getBoolean("usingParachute2");
            this.usingPlanetSelectionGui = nbt.getBoolean("usingPlanetSelectionGui");
            this.teleportCooldown = nbt.getInt("teleportCooldown");
            this.coordsTeleportedFromX = nbt.getDouble("coordsTeleportedFromX");
            this.coordsTeleportedFromZ = nbt.getDouble("coordsTeleportedFromZ");
            this.startDimension = nbt.contains("startDimension") ? nbt.getString("startDimension") : "";
            if (nbt.contains("spaceStationDimensionID"))
            {
                // If loading from an old save file, the home space station is always the overworld, so use 0 as home planet
                this.spaceStationDimensionData = WorldUtil.stringToSpaceStationData("0$" + nbt.getInt("spaceStationDimensionID"));
            }
            else
            {
                this.spaceStationDimensionData = WorldUtil.stringToSpaceStationData(nbt.getString("spaceStationDimensionInfo"));
            }

            if (nbt.getBoolean("usingPlanetSelectionGui"))
            {
                this.openPlanetSelectionGuiCooldown = 20;
            }

            if (nbt.contains("RocketItems") && nbt.contains("rocketStacksLength"))
            {
                int length = nbt.getInt("rocketStacksLength");

                this.stacks = NonNullList.withSize(length, ItemStack.EMPTY);

                ListNBT nbttaglist1 = nbt.getList("RocketItems", 10);

                for (int i = 0; i < nbttaglist1.size(); ++i)
                {
                    CompoundNBT nbttagcompound = nbttaglist1.getCompound(i);
                    int j = nbttagcompound.getByte("Slot") & 255;

                    if (j >= 0 && j < this.stacks.size())
                    {
                        this.stacks.set(j, ItemStack.read(nbttagcompound));
                    }
                }
            }

            this.unlockedSchematics = new ArrayList<ISchematicPage>();

            if (p != null)
            {
                for (int i = 0; i < nbt.getList("Schematics", 10).size(); ++i)
                {
                    final CompoundNBT nbttagcompound = nbt.getList("Schematics", 10).getCompound(i);

                    final int j = nbttagcompound.getInt("UnlockedPage");

                    SchematicRegistry.addUnlockedPage(p, SchematicRegistry.getMatchingRecipeForID(j));
                }
            }

            Collections.sort(this.unlockedSchematics);

            this.cryogenicChamberCooldown = nbt.getInt("CryogenicChamberCooldown");

            if (nbt.contains("ReceivedSoundWarning"))
            {
                this.receivedSoundWarning = nbt.getBoolean("ReceivedSoundWarning");
            }
            if (nbt.contains("ReceivedBedWarning"))
            {
                this.receivedBedWarning = nbt.getBoolean("ReceivedBedWarning");
            }

            if (nbt.contains("LaunchpadStack"))
            {
                this.launchpadStack = ItemStack.read(nbt.getCompound("LaunchpadStack"));
            }
            else
            {
                // for backwards compatibility with saves which don't have this tag - players can't lose launchpads
                this.launchpadStack = new ItemStack(GCBlocks.landingPad, 9);
            }

            if (nbt.contains("BuildFlags"))
            {
                this.buildFlags = nbt.getInt("BuildFlags");
            }

            if (nbt.contains("ShownSpaceRace"))
            {
                this.openedSpaceRaceManager = nbt.getBoolean("ShownSpaceRace");
            }

            if (nbt.contains("AstroMinerCount"))
            {
                this.astroMinerCount = nbt.getInt("AstroMinerCount");
            }
            if (nbt.contains("AstroData"))
            {
                this.activeAstroMinerChunks.clear();
                ListNBT astroList = nbt.getList("AstroData", 10);
                for (int i = 0; i < astroList.size(); ++i)
                {
                    final CompoundNBT nbttagcompound = astroList.getCompound(i);
                    BlockVec3 data = BlockVec3.readFromNBT(nbttagcompound);
                    this.activeAstroMinerChunks.add(data);
                }
//                if (GalacticraftCore.isPlanetsLoaded)
//                {
//                    AsteroidsTickHandlerServer.loadAstroChunkList(this.activeAstroMinerChunks);
//                } TODO Planets
            }

            if (nbt.contains("GlassColor1"))
            {
                this.glassColor1 = nbt.getInt("GlassColor1");
                this.glassColor2 = nbt.getInt("GlassColor2");
                this.glassColor3 = nbt.getInt("GlassColor3");
            }

//            if (nbt.contains("PanLi"))
//            {
//                final ListNBT panels = nbt.getList("PanLi", 10);
//                for (int i = 0; i < panels.size(); ++i)
//                {
//                    if (i == BlockPanelLighting.PANELTYPES_LENGTH) break;
//                    final CompoundNBT stateNBT = panels.getCompound(i);
//                    BlockState bs = TileEntityPanelLight.readBlockState(stateNBT);
//                    this.panelLightingBases[i] = (bs.getBlock() == Blocks.AIR) ? null : bs;
//                }
//            } TODO Panel Lighting

            if (nbt.contains("PanCo"))
            {
                this.panelLightingColor = nbt.getInt("PanCo");
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
//        this.panelLightingBases = oldData.getPanelLightingBases(); TODO Panel lighting
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

//    @Override
//    public BlockState[] getPanelLightingBases()
//    {
//        return panelLightingBases;
//    } TODO Panel Lighting

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

//    @Override
//    public Object[] getMiscNetworkedStats()
//    {
//        int length = 2 + BlockPanelLighting.PANELTYPES_LENGTH * 2;
//        Object[] result = new Object[length];
//        result[0] = this.getBuildFlags();
//        BlockPanelLighting.getNetworkedData(result, this.panelLightingBases);
//        result[length - 1] = this.panelLightingColor;
//        return result;
//    } TODO Panel Lighting

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
