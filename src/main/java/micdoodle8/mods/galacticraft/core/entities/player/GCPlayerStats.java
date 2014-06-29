package micdoodle8.mods.galacticraft.core.entities.player;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.command.CommandGCInv;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class GCPlayerStats implements IExtendedEntityProperties
{
	public static final String GC_PLAYER_PROP = "GCPlayerStats";

	public WeakReference<GCEntityPlayerMP> player;

	public InventoryExtended extendedInventory = new InventoryExtended();

	public int airRemaining;
	public int airRemaining2;

	public int thermalLevel;

	public int damageCounter;

	// temporary data while player is in planet selection GUI
	public int spaceshipTier = 1;
	public ItemStack[] rocketStacks = new ItemStack[2];
	public int rocketType;
	public int fuelLevel;
	public Item rocketItem;
	public ItemStack launchpadStack;

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

	public int launchAttempts = 0;

	public int spaceRaceInviteTeamID;

	public boolean usingPlanetSelectionGui;

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

	public int spaceStationDimensionID = -1;

	public boolean oxygenSetupValid;
	public boolean lastOxygenSetupValid;

	public boolean touchedGround;
	public boolean lastOnGround;

	public ArrayList<ISchematicPage> unlockedSchematics = new ArrayList<ISchematicPage>();
	public ArrayList<ISchematicPage> lastUnlockedSchematics = new ArrayList<ISchematicPage>();

	public int cryogenicChamberCooldown;

	public boolean receivedSoundWarning;

	public GCPlayerStats(GCEntityPlayerMP player)
	{
		this.player = new WeakReference<GCEntityPlayerMP>(player);
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
		nbt.setInteger("spaceStationDimensionID", this.spaceStationDimensionID);
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
		ItemStack[] saveinv = CommandGCInv.getSaveData(this.player.get().getGameProfile().getName().toLowerCase());
		if (saveinv != null)
		{
			CommandGCInv.doLoad(this.player.get());
		}

		if (nbt.hasKey("SpaceshipTier"))
		{
			this.spaceshipTier = nbt.getInteger("SpaceshipTier");
		}

		this.usingParachute = nbt.getBoolean("usingParachute2");
		this.usingPlanetSelectionGui = nbt.getBoolean("usingPlanetSelectionGui");
		this.teleportCooldown = nbt.getInteger("teleportCooldown");
		this.coordsTeleportedFromX = nbt.getDouble("coordsTeleportedFromX");
		this.coordsTeleportedFromZ = nbt.getDouble("coordsTeleportedFromZ");
		this.spaceStationDimensionID = nbt.getInteger("spaceStationDimensionID");

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

                if (var5 >= 0 && var5 < this.rocketStacks.length)
                {
                    this.rocketStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
                }
            }
        }

		this.unlockedSchematics = new ArrayList<ISchematicPage>();

		for (int i = 0; i < nbt.getTagList("Schematics", 10).tagCount(); ++i)
		{
			final NBTTagCompound nbttagcompound = nbt.getTagList("Schematics", 10).getCompoundTagAt(i);

			final int j = nbttagcompound.getInteger("UnlockedPage");

			SchematicRegistry.addUnlockedPage(this.player.get(), SchematicRegistry.getMatchingRecipeForID(j));
		}

		Collections.sort(this.unlockedSchematics);

		this.cryogenicChamberCooldown = nbt.getInteger("CryogenicChamberCooldown");

		if (nbt.hasKey("ReceivedSoundWarning"))
		{
			this.receivedSoundWarning = nbt.getBoolean("ReceivedSoundWarning");
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

	}

	@Override
	public void init(Entity entity, World world)
	{
	}

	public static void register(GCEntityPlayerMP player)
	{
		player.registerExtendedProperties(GCPlayerStats.GC_PLAYER_PROP, new GCPlayerStats(player));
	}

	public static GCPlayerStats get(GCEntityPlayerMP player)
	{
		return (GCPlayerStats) player.getExtendedProperties(GCPlayerStats.GC_PLAYER_PROP);
	}

	public void copyFrom(GCPlayerStats oldData, boolean keepInv)
	{
		if (keepInv)
		{
			this.extendedInventory.copyInventory(oldData.extendedInventory);
		}

		this.spaceStationDimensionID = oldData.spaceStationDimensionID;
	}
}
