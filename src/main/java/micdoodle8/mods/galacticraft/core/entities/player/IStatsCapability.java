package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public interface IStatsCapability
{
    void saveNBTData(NBTTagCompound nbt);

    void loadNBTData(NBTTagCompound nbt);

    void copyFrom(IStatsCapability oldData, boolean keepInv);

    WeakReference<EntityPlayerMP> getPlayer();

    void setPlayer(WeakReference<EntityPlayerMP> player);

    InventoryExtended getExtendedInventory();

    void setExtendedInventory(InventoryExtended extendedInventory);

    int getAirRemaining();

    void setAirRemaining(int airRemaining);

    int getAirRemaining2();

    void setAirRemaining2(int airRemaining2);

    int getThermalLevel();

    void setThermalLevel(int thermalLevel);

    boolean isThermalLevelNormalising();

    void setThermalLevelNormalising(boolean thermalLevelNormalising);

    int getDamageCounter();

    void setDamageCounter(int damageCounter);

    int getSpaceshipTier();

    void setSpaceshipTier(int spaceshipTier);

    NonNullList<ItemStack> getRocketStacks();

    void setRocketStacks(NonNullList<ItemStack> rocketStacks);

    int getRocketType();

    void setRocketType(int rocketType);

    int getFuelLevel();

    void setFuelLevel(int fuelLevel);

    Item getRocketItem();

    void setRocketItem(Item rocketItem);

    ItemStack getLaunchpadStack();

    void setLaunchpadStack(ItemStack launchpadStack);

    int getAstroMinerCount();

    void setAstroMinerCount(int astroMinerCount);

    boolean isUsingParachute();

    void setUsingParachute(boolean usingParachute);

    ItemStack getParachuteInSlot();

    void setParachuteInSlot(ItemStack parachuteInSlot);

    ItemStack getLastParachuteInSlot();

    void setLastParachuteInSlot(ItemStack lastParachuteInSlot);

    ItemStack getFrequencyModuleInSlot();

    void setFrequencyModuleInSlot(ItemStack frequencyModuleInSlot);

    ItemStack getLastFrequencyModuleInSlot();

    void setLastFrequencyModuleInSlot(ItemStack lastFrequencyModuleInSlot);

    ItemStack getMaskInSlot();

    void setMaskInSlot(ItemStack maskInSlot);

    ItemStack getLastMaskInSlot();

    void setLastMaskInSlot(ItemStack lastMaskInSlot);

    ItemStack getGearInSlot();

    void setGearInSlot(ItemStack gearInSlot);

    ItemStack getLastGearInSlot();

    void setLastGearInSlot(ItemStack lastGearInSlot);

    ItemStack getTankInSlot1();

    void setTankInSlot1(ItemStack tankInSlot1);

    ItemStack getLastTankInSlot1();

    void setLastTankInSlot1(ItemStack lastTankInSlot1);

    ItemStack getTankInSlot2();

    void setTankInSlot2(ItemStack tankInSlot2);

    ItemStack getLastTankInSlot2();

    void setLastTankInSlot2(ItemStack lastTankInSlot2);

    ItemStack getThermalHelmetInSlot();

    void setThermalHelmetInSlot(ItemStack thermalHelmetInSlot);

    ItemStack getLastThermalHelmetInSlot();

    void setLastThermalHelmetInSlot(ItemStack lastThermalHelmetInSlot);

    ItemStack getThermalChestplateInSlot();

    void setThermalChestplateInSlot(ItemStack thermalChestplateInSlot);

    ItemStack getLastThermalChestplateInSlot();

    void setLastThermalChestplateInSlot(ItemStack lastThermalChestplateInSlot);

    ItemStack getThermalLeggingsInSlot();

    void setThermalLeggingsInSlot(ItemStack thermalLeggingsInSlot);

    ItemStack getLastThermalLeggingsInSlot();

    void setLastThermalLeggingsInSlot(ItemStack lastThermalLeggingsInSlot);

    ItemStack getThermalBootsInSlot();

    void setThermalBootsInSlot(ItemStack thermalBootsInSlot);

    ItemStack getLastThermalBootsInSlot();

    void setLastThermalBootsInSlot(ItemStack lastThermalBootsInSlot);

    ItemStack getShieldControllerInSlot();

    void setShieldControllerInSlot(ItemStack shieldControllerInSlot);

    ItemStack getLastShieldControllerInSlot();

    void setLastShieldControllerInSlot(ItemStack lastShieldControllerInSlot);

    int getLaunchAttempts();

    void setLaunchAttempts(int launchAttempts);

    int getSpaceRaceInviteTeamID();

    void setSpaceRaceInviteTeamID(int spaceRaceInviteTeamID);

    boolean isUsingPlanetSelectionGui();

    void setUsingPlanetSelectionGui(boolean usingPlanetSelectionGui);

    String getSavedPlanetList();

    void setSavedPlanetList(String savedPlanetList);

    int getOpenPlanetSelectionGuiCooldown();

    void setOpenPlanetSelectionGuiCooldown(int openPlanetSelectionGuiCooldown);

    boolean hasOpenedPlanetSelectionGui();

    void setHasOpenedPlanetSelectionGui(boolean hasOpenedPlanetSelectionGui);

    int getChestSpawnCooldown();

    void setChestSpawnCooldown(int chestSpawnCooldown);

    Vector3 getChestSpawnVector();

    void setChestSpawnVector(Vector3 chestSpawnVector);

    int getTeleportCooldown();

    void setTeleportCooldown(int teleportCooldown);

    int getChatCooldown();

    void setChatCooldown(int chatCooldown);

    double getDistanceSinceLastStep();

    void setDistanceSinceLastStep(double distanceSinceLastStep);

    int getLastStep();

    void setLastStep(int lastStep);

    double getCoordsTeleportedFromX();

    void setCoordsTeleportedFromX(double coordsTeleportedFromX);

    double getCoordsTeleportedFromZ();

    void setCoordsTeleportedFromZ(double coordsTeleportedFromZ);

    HashMap<Integer, Integer> getSpaceStationDimensionData();

    void setSpaceStationDimensionData(HashMap<Integer, Integer> spaceStationDimensionData);

    boolean isOxygenSetupValid();

    void setOxygenSetupValid(boolean oxygenSetupValid);

    boolean isLastOxygenSetupValid();

    void setLastOxygenSetupValid(boolean lastOxygenSetupValid);

    boolean isTouchedGround();

    void setTouchedGround(boolean touchedGround);

    boolean isLastOnGround();

    void setLastOnGround(boolean lastOnGround);

    boolean isInLander();

    void setInLander(boolean inLander);

    boolean hasJustLanded();

    void setJustLanded(boolean justLanded);

    ArrayList<ISchematicPage> getUnlockedSchematics();

    void setUnlockedSchematics(ArrayList<ISchematicPage> unlockedSchematics);

    ArrayList<ISchematicPage> getLastUnlockedSchematics();

    void setLastUnlockedSchematics(ArrayList<ISchematicPage> lastUnlockedSchematics);

    int getCryogenicChamberCooldown();

    void setCryogenicChamberCooldown(int cryogenicChamberCooldown);

    boolean hasReceivedSoundWarning();

    void setReceivedSoundWarning(boolean receivedSoundWarning);

    boolean hasReceivedBedWarning();

    void setReceivedBedWarning(boolean receivedBedWarning);

    boolean hasOpenedSpaceRaceManager();

    void setOpenedSpaceRaceManager(boolean openedSpaceRaceManager);

    boolean hasSentFlags();

    void setSentFlags(boolean sentFlags);

    boolean isNewInOrbit();

    void setNewInOrbit(boolean newInOrbit);

    boolean isNewAdventureSpawn();

    void setNewAdventureSpawn(boolean newAdventureSpawn);

    int getBuildFlags();

    void setBuildFlags(int buildFlags);

    int getIncrementalDamage();

    void setIncrementalDamage(int incrementalDamage);

    String getStartDimension();

    void setStartDimension(String startDimension);

    void setGlassColors(int color1, int color2, int color3);

    int getGlassColor1();

    int getGlassColor2();

    int getGlassColor3();
}
