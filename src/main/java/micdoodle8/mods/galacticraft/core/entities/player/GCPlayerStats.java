package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

public abstract class GCPlayerStats
{
    public static GCPlayerStats get(Entity entity)
    {
        return entity.getCapability(GCCapabilities.GC_STATS_CAPABILITY, null);
    }

    public abstract void saveNBTData(NBTTagCompound nbt);

    public abstract void loadNBTData(NBTTagCompound nbt);

    public abstract void copyFrom(GCPlayerStats oldData, boolean keepInv);

    public abstract WeakReference<EntityPlayerMP> getPlayer();

    public abstract void setPlayer(WeakReference<EntityPlayerMP> player);

    public abstract InventoryExtended getExtendedInventory();

    public abstract void setExtendedInventory(InventoryExtended extendedInventory);

    public abstract int getAirRemaining();

    public abstract void setAirRemaining(int airRemaining);

    public abstract int getAirRemaining2();

    public abstract void setAirRemaining2(int airRemaining2);

    public abstract int getThermalLevel();

    public abstract void setThermalLevel(int thermalLevel);

    public abstract boolean isThermalLevelNormalising();

    public abstract void setThermalLevelNormalising(boolean thermalLevelNormalising);

    public abstract int getDamageCounter();

    public abstract void setDamageCounter(int damageCounter);

    public abstract int getSpaceshipTier();

    public abstract void setSpaceshipTier(int spaceshipTier);

    public abstract NonNullList<ItemStack> getRocketStacks();

    public abstract void setRocketStacks(NonNullList<ItemStack> rocketStacks);

    public abstract int getRocketType();

    public abstract void setRocketType(int rocketType);

    public abstract int getFuelLevel();

    public abstract void setFuelLevel(int fuelLevel);

    public abstract Item getRocketItem();

    public abstract void setRocketItem(Item rocketItem);

    public abstract ItemStack getLaunchpadStack();

    public abstract void setLaunchpadStack(ItemStack launchpadStack);

    public abstract int getAstroMinerCount();

    public abstract void setAstroMinerCount(int astroMinerCount);

    public abstract List<BlockVec3> getActiveAstroMinerChunks();

    public abstract boolean isUsingParachute();

    public abstract void setUsingParachute(boolean usingParachute);

    public abstract ItemStack getParachuteInSlot();

    public abstract void setParachuteInSlot(ItemStack parachuteInSlot);

    public abstract ItemStack getLastParachuteInSlot();

    public abstract void setLastParachuteInSlot(ItemStack lastParachuteInSlot);

    public abstract ItemStack getFrequencyModuleInSlot();

    public abstract void setFrequencyModuleInSlot(ItemStack frequencyModuleInSlot);

    public abstract ItemStack getLastFrequencyModuleInSlot();

    public abstract void setLastFrequencyModuleInSlot(ItemStack lastFrequencyModuleInSlot);

    public abstract ItemStack getMaskInSlot();

    public abstract void setMaskInSlot(ItemStack maskInSlot);

    public abstract ItemStack getLastMaskInSlot();

    public abstract void setLastMaskInSlot(ItemStack lastMaskInSlot);

    public abstract ItemStack getGearInSlot();

    public abstract void setGearInSlot(ItemStack gearInSlot);

    public abstract ItemStack getLastGearInSlot();

    public abstract void setLastGearInSlot(ItemStack lastGearInSlot);

    public abstract ItemStack getTankInSlot1();

    public abstract void setTankInSlot1(ItemStack tankInSlot1);

    public abstract ItemStack getLastTankInSlot1();

    public abstract void setLastTankInSlot1(ItemStack lastTankInSlot1);

    public abstract ItemStack getTankInSlot2();

    public abstract void setTankInSlot2(ItemStack tankInSlot2);

    public abstract ItemStack getLastTankInSlot2();

    public abstract void setLastTankInSlot2(ItemStack lastTankInSlot2);

    public abstract ItemStack getThermalHelmetInSlot();

    public abstract void setThermalHelmetInSlot(ItemStack thermalHelmetInSlot);

    public abstract ItemStack getLastThermalHelmetInSlot();

    public abstract void setLastThermalHelmetInSlot(ItemStack lastThermalHelmetInSlot);

    public abstract ItemStack getThermalChestplateInSlot();

    public abstract void setThermalChestplateInSlot(ItemStack thermalChestplateInSlot);

    public abstract ItemStack getLastThermalChestplateInSlot();

    public abstract void setLastThermalChestplateInSlot(ItemStack lastThermalChestplateInSlot);

    public abstract ItemStack getThermalLeggingsInSlot();

    public abstract void setThermalLeggingsInSlot(ItemStack thermalLeggingsInSlot);

    public abstract ItemStack getLastThermalLeggingsInSlot();

    public abstract void setLastThermalLeggingsInSlot(ItemStack lastThermalLeggingsInSlot);

    public abstract ItemStack getThermalBootsInSlot();

    public abstract void setThermalBootsInSlot(ItemStack thermalBootsInSlot);

    public abstract ItemStack getLastThermalBootsInSlot();

    public abstract void setLastThermalBootsInSlot(ItemStack lastThermalBootsInSlot);

    public abstract ItemStack getShieldControllerInSlot();

    public abstract void setShieldControllerInSlot(ItemStack shieldControllerInSlot);

    public abstract ItemStack getLastShieldControllerInSlot();

    public abstract void setLastShieldControllerInSlot(ItemStack lastShieldControllerInSlot);

    public abstract int getLaunchAttempts();

    public abstract void setLaunchAttempts(int launchAttempts);

    public abstract int getSpaceRaceInviteTeamID();

    public abstract void setSpaceRaceInviteTeamID(int spaceRaceInviteTeamID);

    public abstract boolean isUsingPlanetSelectionGui();

    public abstract void setUsingPlanetSelectionGui(boolean usingPlanetSelectionGui);

    public abstract String getSavedPlanetList();

    public abstract void setSavedPlanetList(String savedPlanetList);

    public abstract int getOpenPlanetSelectionGuiCooldown();

    public abstract void setOpenPlanetSelectionGuiCooldown(int openPlanetSelectionGuiCooldown);

    public abstract boolean hasOpenedPlanetSelectionGui();

    public abstract void setHasOpenedPlanetSelectionGui(boolean hasOpenedPlanetSelectionGui);

    public abstract int getChestSpawnCooldown();

    public abstract void setChestSpawnCooldown(int chestSpawnCooldown);

    public abstract Vector3 getChestSpawnVector();

    public abstract void setChestSpawnVector(Vector3 chestSpawnVector);

    public abstract int getTeleportCooldown();

    public abstract void setTeleportCooldown(int teleportCooldown);

    public abstract int getChatCooldown();

    public abstract void setChatCooldown(int chatCooldown);

    public abstract double getDistanceSinceLastStep();

    public abstract void setDistanceSinceLastStep(double distanceSinceLastStep);

    public abstract int getLastStep();

    public abstract void setLastStep(int lastStep);

    public abstract double getCoordsTeleportedFromX();

    public abstract void setCoordsTeleportedFromX(double coordsTeleportedFromX);

    public abstract double getCoordsTeleportedFromZ();

    public abstract void setCoordsTeleportedFromZ(double coordsTeleportedFromZ);

    public abstract HashMap<Integer, Integer> getSpaceStationDimensionData();

    public abstract void setSpaceStationDimensionData(HashMap<Integer, Integer> spaceStationDimensionData);

    public abstract boolean isOxygenSetupValid();

    public abstract void setOxygenSetupValid(boolean oxygenSetupValid);

    public abstract boolean isLastOxygenSetupValid();

    public abstract void setLastOxygenSetupValid(boolean lastOxygenSetupValid);

    public abstract boolean isTouchedGround();

    public abstract void setTouchedGround(boolean touchedGround);

    public abstract boolean isLastOnGround();

    public abstract void setLastOnGround(boolean lastOnGround);

    public abstract boolean isInLander();

    public abstract void setInLander(boolean inLander);

    public abstract boolean hasJustLanded();

    public abstract void setJustLanded(boolean justLanded);

    public abstract List<ISchematicPage> getUnlockedSchematics();

    public abstract void setUnlockedSchematics(List<ISchematicPage> unlockedSchematics);

    public abstract List<ISchematicPage> getLastUnlockedSchematics();

    public abstract void setLastUnlockedSchematics(List<ISchematicPage> lastUnlockedSchematics);

    public abstract int getCryogenicChamberCooldown();

    public abstract void setCryogenicChamberCooldown(int cryogenicChamberCooldown);

    public abstract boolean hasReceivedSoundWarning();

    public abstract void setReceivedSoundWarning(boolean receivedSoundWarning);

    public abstract boolean hasReceivedBedWarning();

    public abstract void setReceivedBedWarning(boolean receivedBedWarning);

    public abstract boolean hasOpenedSpaceRaceManager();

    public abstract void setOpenedSpaceRaceManager(boolean openedSpaceRaceManager);

    public abstract boolean hasSentFlags();

    public abstract void setSentFlags(boolean sentFlags);

    public abstract boolean isNewInOrbit();

    public abstract void setNewInOrbit(boolean newInOrbit);

    public abstract boolean isNewAdventureSpawn();

    public abstract void setNewAdventureSpawn(boolean newAdventureSpawn);

    public abstract int getBuildFlags();

    public abstract void setBuildFlags(int buildFlags);

    public abstract int getIncrementalDamage();

    public abstract void setIncrementalDamage(int incrementalDamage);

    public abstract String getStartDimension();

    public abstract void setStartDimension(String startDimension);

    public abstract void setGlassColors(int color1, int color2, int color3);

    public abstract int getGlassColor1();

    public abstract int getGlassColor2();

    public abstract int getGlassColor3();

    public abstract IBlockState[] getPanelLightingBases();
    
    public abstract int getPanelLightingColor();

    public abstract void setPanelLightingColor(int color);

    public abstract Object[] getMiscNetworkedStats();

    public abstract void setSavedSpeed(float value);

    public abstract float getSavedSpeed();
}
