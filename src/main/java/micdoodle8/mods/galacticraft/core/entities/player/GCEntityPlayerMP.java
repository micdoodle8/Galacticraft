package micdoodle8.mods.galacticraft.core.entities.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import micdoodle8.mods.galacticraft.api.event.oxygen.GCCoreOxygenSuffocationEvent;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.command.CommandGCInv;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import micdoodle8.mods.galacticraft.core.entities.EntityParachest;
import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCorePlayerMP.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCEntityPlayerMP extends EntityPlayerMP
{
	private InventoryExtended extendedInventory = new InventoryExtended();

	private int airRemaining;
	private int airRemaining2;

	private long tick;

	private int damageCounter;

	// temporary data while player is in planet selection GUI
	private int spaceshipTier = 1;
	private ItemStack[] rocketStacks = new ItemStack[9];
	private int rocketType;
	private int fuelLevel;
	private Item rocketItem;

	private boolean usingParachute;

	private ItemStack parachuteInSlot;
	private ItemStack lastParachuteInSlot;

	private ItemStack frequencyModuleInSlot;
	private ItemStack lastFrequencyModuleInSlot;

	private ItemStack maskInSlot;
	private ItemStack lastMaskInSlot;

	private ItemStack gearInSlot;
	private ItemStack lastGearInSlot;

	private ItemStack tankInSlot1;
	private ItemStack lastTankInSlot1;

	private ItemStack tankInSlot2;
	private ItemStack lastTankInSlot2;

	private int launchAttempts = 0;

	private boolean usingPlanetSelectionGui;

	private int openPlanetSelectionGuiCooldown;
	private boolean hasOpenedPlanetSelectionGui = false;

	private int chestSpawnCooldown;
	private micdoodle8.mods.galacticraft.api.vector.Vector3 chestSpawnVector;

	private int teleportCooldown;

	private int chatCooldown;

	private double distanceSinceLastStep;
	private int lastStep;

	private double coordsTeleportedFromX;
	private double coordsTeleportedFromZ;

	private int spaceStationDimensionID = -1;

	private boolean oxygenSetupValid;
	private boolean lastOxygenSetupValid;

	private boolean touchedGround;
	private boolean lastOnGround;

	private ArrayList<ISchematicPage> unlockedSchematics = new ArrayList<ISchematicPage>();
	private ArrayList<ISchematicPage> lastUnlockedSchematics = new ArrayList<ISchematicPage>();

	private int cryogenicChamberCooldown;

	private boolean receivedSoundWarning;

    public GCEntityPlayerMP(MinecraftServer server, WorldServer world, GameProfile profile, ItemInWorldManager itemInWorldManager)
	{
		super(server, world, profile, itemInWorldManager);

		if (!GalacticraftCore.playersServer.containsKey(this.getGameProfile().getName()))
		{
			GalacticraftCore.playersServer.put(this.getGameProfile().getName(), this);
		}
	}

	@Override
	public void onDeath(DamageSource damageSource)
	{
		GalacticraftCore.playersServer.remove(this);

		super.onDeath(damageSource);
	}

	@Override
	public void clonePlayer(EntityPlayer par1EntityPlayer, boolean keepInv)
	{
		super.clonePlayer(par1EntityPlayer, keepInv);

		if (par1EntityPlayer instanceof GCEntityPlayerMP)
		{
			if (keepInv)
			{
				this.getExtendedInventory().copyInventory(((GCEntityPlayerMP) par1EntityPlayer).extendedInventory);
			}
			else if (this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
			{
				this.getExtendedInventory().copyInventory(((GCEntityPlayerMP) par1EntityPlayer).extendedInventory);
			}

			this.setSpaceStationDimensionID(((GCEntityPlayerMP) par1EntityPlayer).getSpaceStationDimensionID());
		}
	}

	@Override
	protected void fall(float par1)
	{
		if (this.ridingEntity instanceof EntityAutoRocket || this.ridingEntity instanceof EntityLander)
		{
			return;
		}

		super.fall(par1);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (!GalacticraftCore.playersServer.containsKey(this.getGameProfile().getName()) || this.tick % 360 == 0)
		{
			GalacticraftCore.playersServer.put(this.getGameProfile().getName(), this);
		}

		if (this.tick >= Long.MAX_VALUE)
		{
			this.tick = 0;
		}

		this.tick++;

		if (this.cryogenicChamberCooldown > 0)
		{
			this.cryogenicChamberCooldown--;
		}

		if (!this.onGround && this.lastOnGround)
		{
			this.setTouchedGround(true);
		}

		if (this.getTeleportCooldown() > 0)
		{
			this.setTeleportCooldown(this.getTeleportCooldown() - 1);
		}

		if (this.getChatCooldown() > 0)
		{
			this.setChatCooldown(this.getChatCooldown() - 1);
		}

		if (this.openPlanetSelectionGuiCooldown > 0)
		{
			this.openPlanetSelectionGuiCooldown--;
		}

		if (this.getParachute())
		{
			this.fallDistance = 0.0F;
		}

		this.checkCurrentItem();

		if (!this.hasOpenedPlanetSelectionGui && this.openPlanetSelectionGuiCooldown == 1)
		{
			this.sendPlanetList();
			this.setUsingPlanetGui();
			this.hasOpenedPlanetSelectionGui = true;
		}

		if (this.usingPlanetSelectionGui)
		{
			this.sendPlanetList();
		}

		if (this.damageCounter > 0)
		{
			this.damageCounter--;
		}

		if (this.tick % 30 == 0 && this.worldObj.provider instanceof IGalacticraftWorldProvider)
		{
			this.sendAirRemainingPacket();
		}

		if (this.onGround && this.getParachute())
		{
			this.sendGearUpdatePacket(EnumModelPacket.REMOVE_PARACHUTE.getIndex());
			this.setUsingParachute(false);
		}

		this.checkGear();

		if (this.getChestSpawnCooldown() > 0)
		{
			this.setChestSpawnCooldown(this.getChestSpawnCooldown() - 1);
		}

		if (this.getChestSpawnCooldown() == 180)
		{
			if (this.getChestSpawnVector() != null)
			{
				EntityParachest chest = new EntityParachest(this.worldObj, this.getRocketStacks(), this.getFuelLevel());

				chest.setPosition(this.getChestSpawnVector().x, this.getChestSpawnVector().y, this.getChestSpawnVector().z);

				if (!this.worldObj.isRemote)
				{
					this.worldObj.spawnEntityInWorld(chest);
				}
			}
		}

		//

		if (this.getLaunchAttempts() > 0 && this.ridingEntity == null)
		{
			this.setLaunchAttempts(0);
		}

		this.checkOxygen();

		if (this.worldObj.provider instanceof IGalacticraftWorldProvider && (this.oxygenSetupValid != this.lastOxygenSetupValid || this.tick % 100 == 0))
		{
			GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_OXYGEN_VALIDITY, new Object[] { this.oxygenSetupValid }), this);
		}

		if (this.getParachute())
		{
			if (this.onGround)
			{
				this.setUsingParachute(false);
			}
		}

		this.throwMeteors();

		if (this.worldObj.provider instanceof IGalacticraftWorldProvider || this.usingPlanetSelectionGui)
		{
//			this.playerNetServerHandler.ticksForFloatKick = 0;
		}

		this.updateSchematics();

		if (this.frequencyModuleInSlot == null && !this.receivedSoundWarning && this.tick > 0 && this.tick % 250 == 0 && this.worldObj.provider instanceof IGalacticraftWorldProvider && this.onGround)
		{
			this.addChatMessage(new ChatComponentText(EnumColor.YELLOW + "I'll probably need a " + EnumColor.AQUA + GCItems.basicItem.getItemStackDisplayName(new ItemStack(GCItems.basicItem, 1, 19)) + EnumColor.YELLOW + " if I want to hear properly here."));
			this.receivedSoundWarning = true;
		}

		this.lastOxygenSetupValid = this.oxygenSetupValid;
		this.lastUnlockedSchematics = this.getUnlockedSchematics();

		this.lastOnGround = this.onGround;
	}

	@Override
    public void moveEntity(double par1, double par3, double par5)
    {
    	super.moveEntity(par1, par3, par5);
    	this.updateFeet(par1, par5);
    }

	private void updateFeet(double motionX, double motionZ)
	{
		double motionSqrd = (motionX * motionX + motionZ * motionZ);
		
		// If the player is on the moon, not airbourne and not riding anything
		if (motionSqrd > 0.001 && this.worldObj != null && this.worldObj.provider instanceof WorldProviderMoon && this.ridingEntity == null)
		{
			int iPosX = (int)Math.floor(this.posX);
			int iPosY = (int)Math.floor(this.posY - 1);
			int iPosZ = (int)Math.floor(this.posZ);
			
			// If the block below is the moon block
			if (this.worldObj.getBlock(iPosX, iPosY, iPosZ) == GCBlocks.blockMoon)
			{
				// And is the correct metadata (moon turf)
				if (this.worldObj.getBlockMetadata(iPosX, iPosY, iPosZ) == 5)
				{
					// If it has been long enough since the last step
					if (this.distanceSinceLastStep > 0.35)
					{
						Vector3 pos = new Vector3(this);
						// Set the footprint position to the block below and add random number to stop z-fighting
						pos.y = MathHelper.floor_double(this.posY - 1) + this.rand.nextFloat() / 100.0F;
						
						// Adjust footprint to left or right depending on step count
						switch (this.lastStep)
						{
						case 0:
							pos.translate(new Vector3(Math.sin(Math.toRadians(-this.rotationYaw + 90)) * 0.25, 0, Math.cos(Math.toRadians(-this.rotationYaw + 90)) * 0.25));
							break;
						case 1:
							pos.translate(new Vector3(Math.sin(Math.toRadians(-this.rotationYaw - 90)) * 0.25, 0, Math.cos(Math.toRadians(-this.rotationYaw - 90)) * 0.25));
							break;
						}
						
						TickHandlerServer.addFootprint(new Footprint(pos, this.rotationYaw), this.worldObj.provider.dimensionId);
						
						// Increment and cap step counter at 1
						this.lastStep++;
						this.lastStep %= 2;
						this.distanceSinceLastStep = 0;
					}
					else
					{
						this.distanceSinceLastStep += motionSqrd;
					}
				}
			}
		}
	}

	private void checkCurrentItem()
	{
		ItemStack theCurrentItem = this.inventory.getCurrentItem();
		if (this.worldObj.provider instanceof IGalacticraftWorldProvider && theCurrentItem != null)
		{
			final int var1 = theCurrentItem.stackSize;
			final int var2 = theCurrentItem.getItemDamage();

			if (this.inventory.getCurrentItem().getItem() == Item.getItemFromBlock(Blocks.torch))
			{
				final ItemStack stack = new ItemStack(GCBlocks.unlitTorch, var1, 0);
				this.inventory.mainInventory[this.inventory.currentItem] = stack;
			}
		}
		else if (!(this.worldObj.provider instanceof IGalacticraftWorldProvider) && theCurrentItem != null)
		{
			final int var1 = theCurrentItem.stackSize;
			final int var2 = theCurrentItem.getItemDamage();

			if (this.inventory.getCurrentItem().getItem() == Item.getItemFromBlock(GCBlocks.unlitTorch))
			{
				final ItemStack stack = new ItemStack(Blocks.torch, var1, 0);
				this.inventory.mainInventory[this.inventory.currentItem] = stack;
			}
		}
	}

	private void sendPlanetList()
	{
		HashMap<String, Integer> map = WorldUtil.getArrayOfPossibleDimensions(WorldUtil.getPossibleDimensionsForSpaceshipTier(this.getSpaceshipTier()), this);

		String temp = "";
		int count = 0;

		for (Entry<String, Integer> entry : map.entrySet())
		{
			temp = temp.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "." : ""));
			count++;
		}

		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DIMENSION_LIST, new Object[] { this.getGameProfile().getName(), temp }), this);
	}

	private void checkGear()
	{
		this.maskInSlot = this.getExtendedInventory().getStackInSlot(0);
		this.gearInSlot = this.getExtendedInventory().getStackInSlot(1);
		this.tankInSlot1 = this.getExtendedInventory().getStackInSlot(2);
		this.tankInSlot2 = this.getExtendedInventory().getStackInSlot(3);
		this.parachuteInSlot = this.getExtendedInventory().getStackInSlot(4);
		this.frequencyModuleInSlot = this.getExtendedInventory().getStackInSlot(5);

		//

		if (this.frequencyModuleInSlot != null && this.lastFrequencyModuleInSlot == null && this.frequencyModuleInSlot.getItem() == GCItems.basicItem && this.frequencyModuleInSlot.getItemDamage() == 19)
		{
			this.sendGearUpdatePacket(EnumModelPacket.ADD_FREQUENCY_MODULE.getIndex());
		}

		if (this.frequencyModuleInSlot == null && this.lastFrequencyModuleInSlot != null)
		{
			this.sendGearUpdatePacket(EnumModelPacket.REMOVE_FREQUENCY_MODULE.getIndex());
		}

		//

		if (this.maskInSlot != null && this.lastMaskInSlot == null && this.maskInSlot.getItem() == GCItems.oxMask)
		{
			this.sendGearUpdatePacket(EnumModelPacket.ADDMASK.getIndex());
		}

		if (this.maskInSlot == null && this.lastMaskInSlot != null)
		{
			this.sendGearUpdatePacket(EnumModelPacket.REMOVEMASK.getIndex());
		}

		//

		if (this.gearInSlot != null && this.lastGearInSlot == null && this.gearInSlot.getItem() == GCItems.oxygenGear)
		{
			this.sendGearUpdatePacket(EnumModelPacket.ADDGEAR.getIndex());
		}

		if (this.gearInSlot == null && this.lastGearInSlot != null)
		{
			this.sendGearUpdatePacket(EnumModelPacket.REMOVEGEAR.getIndex());
		}

		//

		if (this.tankInSlot1 != null && this.lastTankInSlot1 == null)
		{
			if (this.tankInSlot1.getItem() == GCItems.oxTankLight)
			{
				this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTGREENTANK.getIndex());
			}
			else if (this.tankInSlot1.getItem() == GCItems.oxTankMedium)
			{
				this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTORANGETANK.getIndex());
			}
			else if (this.tankInSlot1.getItem() == GCItems.oxTankHeavy)
			{
				this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTREDTANK.getIndex());
			}
		}

		if (this.tankInSlot1 == null && this.lastTankInSlot1 != null)
		{
			this.sendGearUpdatePacket(EnumModelPacket.REMOVE_LEFT_TANK.getIndex());
		}

		if (this.tankInSlot1 != null && this.lastTankInSlot1 != null)
		{
			if (this.tankInSlot1.getItem() != this.lastTankInSlot1.getItem())
			{
				if (this.tankInSlot1.getItem() == GCItems.oxTankLight)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTGREENTANK.getIndex());
				}
				else if (this.tankInSlot1.getItem() == GCItems.oxTankMedium)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTORANGETANK.getIndex());
				}
				else if (this.tankInSlot1.getItem() == GCItems.oxTankHeavy)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTREDTANK.getIndex());
				}
			}
		}

		//

		if (this.tankInSlot2 != null && this.lastTankInSlot2 == null)
		{
			if (this.tankInSlot2.getItem() == GCItems.oxTankLight)
			{
				this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTGREENTANK.getIndex());
			}
			else if (this.tankInSlot2.getItem() == GCItems.oxTankMedium)
			{
				this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTORANGETANK.getIndex());
			}
			else if (this.tankInSlot2.getItem() == GCItems.oxTankHeavy)
			{
				this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTREDTANK.getIndex());
			}
		}

		if (this.tankInSlot2 == null && this.lastTankInSlot2 != null)
		{
			this.sendGearUpdatePacket(EnumModelPacket.REMOVE_RIGHT_TANK.getIndex());
		}

		if (this.tankInSlot2 != null && this.lastTankInSlot2 != null)
		{
			if (this.tankInSlot2.getItem() != this.lastTankInSlot2.getItem())
			{
				if (this.tankInSlot2.getItem() == GCItems.oxTankLight)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTGREENTANK.getIndex());
				}
				else if (this.tankInSlot2.getItem() == GCItems.oxTankMedium)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTORANGETANK.getIndex());
				}
				else if (this.tankInSlot2.getItem() == GCItems.oxTankHeavy)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTREDTANK.getIndex());
				}
			}
		}

		if (this.getParachute() && this.parachuteInSlot == null && this.lastParachuteInSlot != null)
		{
			this.sendGearUpdatePacket(EnumModelPacket.REMOVE_PARACHUTE.getIndex());
		}

		if (this.getParachute() && this.parachuteInSlot != null && this.lastParachuteInSlot == null)
		{
			this.sendGearUpdatePacket(EnumModelPacket.ADD_PARACHUTE.getIndex());
		}

		if (this.parachuteInSlot != null && this.lastParachuteInSlot != null)
		{
			if (this.parachuteInSlot.getItemDamage() != this.lastParachuteInSlot.getItemDamage())
			{
				this.sendGearUpdatePacket(EnumModelPacket.ADD_PARACHUTE.getIndex());
			}
		}

		this.lastMaskInSlot = this.getExtendedInventory().getStackInSlot(0);
		this.lastGearInSlot = this.getExtendedInventory().getStackInSlot(1);
		this.lastTankInSlot1 = this.getExtendedInventory().getStackInSlot(2);
		this.lastTankInSlot2 = this.getExtendedInventory().getStackInSlot(3);
		this.lastParachuteInSlot = this.getExtendedInventory().getStackInSlot(4);
		this.lastFrequencyModuleInSlot = this.getExtendedInventory().getStackInSlot(5);
	}

	private void checkOxygen()
	{
		final ItemStack tankInSlot = this.getExtendedInventory().getStackInSlot(2);
		final ItemStack tankInSlot2 = this.getExtendedInventory().getStackInSlot(3);

		final int drainSpacing = OxygenUtil.getDrainSpacing(tankInSlot, tankInSlot2);

		if (this.worldObj.provider instanceof IGalacticraftWorldProvider && !this.capabilities.isCreativeMode)
		{
			if (tankInSlot == null)
			{
				this.airRemaining = 0;
			}

			if (tankInSlot2 == null)
			{
				this.airRemaining2 = 0;
			}

			if (drainSpacing > 0)
			{
				if (this.tick % drainSpacing == 0 && !OxygenUtil.isAABBInBreathableAirBlock(this))
				{
					if (tankInSlot != null && tankInSlot.getMaxDamage() - tankInSlot.getItemDamage() > 0)
					{
						tankInSlot.damageItem(1, this);
					}

					if (tankInSlot2 != null && tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage() > 0)
					{
						tankInSlot2.damageItem(1, this);
					}
				}

				if (tankInSlot != null)
				{
					this.airRemaining = tankInSlot.getMaxDamage() - tankInSlot.getItemDamage();
				}

				if (tankInSlot2 != null)
				{
					this.airRemaining2 = tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage();
				}
			}
			else
			{
				if (this.tick % 60 == 0)
				{
					if (OxygenUtil.isAABBInBreathableAirBlock(this))
					{
						if (this.airRemaining < 90 && tankInSlot != null)
						{
							this.airRemaining = Math.min(this.airRemaining + 1, tankInSlot.getMaxDamage() - tankInSlot.getItemDamage());
						}

						if (this.airRemaining2 < 90 && tankInSlot2 != null)
						{
							this.airRemaining2 = Math.min(this.airRemaining2 + 1, tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage());
						}
					}
					else
					{
						if (this.airRemaining > 0)
						{
							this.airRemaining = Math.max(this.airRemaining - 1, 0);
						}

						if (this.airRemaining2 > 0)
						{
							this.airRemaining2 = Math.max(this.airRemaining2 - 1, 0);
						}
					}
				}
			}

			final boolean airEmpty = this.airRemaining <= 0 && this.airRemaining2 <= 0;

			if (this.isOnLadder())
			{
				this.oxygenSetupValid = this.lastOxygenSetupValid;
			}
			else if ((!OxygenUtil.hasValidOxygenSetup(this) || airEmpty) && !OxygenUtil.isAABBInBreathableAirBlock(this))
			{
				this.oxygenSetupValid = false;
			}
			else
			{
				this.oxygenSetupValid = true;
			}

			if (!this.oxygenSetupValid && !this.worldObj.isRemote && this.isEntityAlive())
			{
				if (this.damageCounter == 0)
				{
					this.damageCounter = ConfigManagerCore.suffocationCooldown;

					GCCoreOxygenSuffocationEvent suffocationEvent = new GCCoreOxygenSuffocationEvent.Pre(this);
					MinecraftForge.EVENT_BUS.post(suffocationEvent);

					if (!suffocationEvent.isCanceled())
					{
						this.attackEntityFrom(DamageSourceGC.oxygenSuffocation, ConfigManagerCore.suffocationDamage);

						GCCoreOxygenSuffocationEvent suffocationEventPost = new GCCoreOxygenSuffocationEvent.Post(this);
						MinecraftForge.EVENT_BUS.post(suffocationEventPost);
					}
				}
			}
		}
		else if (this.tick % 20 == 0 && !this.capabilities.isCreativeMode && this.airRemaining < 90)
		{
			this.airRemaining += 1;
			this.airRemaining2 += 1;
		}
		else if (this.capabilities.isCreativeMode)
		{
			this.airRemaining = 90;
			this.airRemaining2 = 90;
		}
		else
		{
			this.oxygenSetupValid = true;
		}
	}

	private void throwMeteors()
	{
		if (this.worldObj.provider instanceof IGalacticraftWorldProvider && FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT)
		{
			if (((IGalacticraftWorldProvider) this.worldObj.provider).getMeteorFrequency() > 0)
			{
				final double f = ((IGalacticraftWorldProvider) this.worldObj.provider).getMeteorFrequency();

				if (this.worldObj.rand.nextInt(MathHelper.floor_double(f * 1000)) == 0)
				{
					final EntityPlayer closestPlayer = this.worldObj.getClosestPlayerToEntity(this, 100);

					if (closestPlayer == null || closestPlayer.getEntityId() <= this.getEntityId())
					{
						int x, y, z;
						double motX, motZ;
						x = this.worldObj.rand.nextInt(20) - 10;
						y = this.worldObj.rand.nextInt(20) + 200;
						z = this.worldObj.rand.nextInt(20) - 10;
						motX = this.worldObj.rand.nextDouble() * 5;
						motZ = this.worldObj.rand.nextDouble() * 5;

						final EntityMeteor meteor = new EntityMeteor(this.worldObj, this.posX + x, this.posY + y, this.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 1);

						if (!this.worldObj.isRemote)
						{
							this.worldObj.spawnEntityInWorld(meteor);
						}
					}
				}

				if (this.worldObj.rand.nextInt(MathHelper.floor_double(f * 3000)) == 0)
				{
					final EntityPlayer closestPlayer = this.worldObj.getClosestPlayerToEntity(this, 100);

					if (closestPlayer == null || closestPlayer.getEntityId() <= this.getEntityId())
					{
						int x, y, z;
						double motX, motZ;
						x = this.worldObj.rand.nextInt(20) - 10;
						y = this.worldObj.rand.nextInt(20) + 200;
						z = this.worldObj.rand.nextInt(20) - 10;
						motX = this.worldObj.rand.nextDouble() * 5;
						motZ = this.worldObj.rand.nextDouble() * 5;

						final EntityMeteor meteor = new EntityMeteor(this.worldObj, this.posX + x, this.posY + y, this.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 6);

						if (!this.worldObj.isRemote)
						{
							this.worldObj.spawnEntityInWorld(meteor);
						}
					}
				}
			}
		}
	}

	private void updateSchematics()
	{
		SchematicRegistry.addUnlockedPage(this, SchematicRegistry.getMatchingRecipeForID(0));
		SchematicRegistry.addUnlockedPage(this, SchematicRegistry.getMatchingRecipeForID(Integer.MAX_VALUE));

		Collections.sort(this.getUnlockedSchematics());

		if (this.playerNetServerHandler != null && (this.getUnlockedSchematics().size() != this.lastUnlockedSchematics.size() || this.tick % 100 == 0))
		{
			Integer[] iArray = new Integer[this.getUnlockedSchematics().size()];

			for (int i = 0; i < iArray.length; i++)
			{
				ISchematicPage page = this.getUnlockedSchematics().get(i);
				iArray[i] = page == null ? -2 : page.getPageID();
			}

			List<Object> objList = new ArrayList<Object>();
			objList.add(iArray);

			GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SCHEMATIC_LIST, objList), this);
		}
	}

	public void setUsingPlanetGui()
	{
		this.usingPlanetSelectionGui = true;
	}

	public void setNotUsingPlanetGui()
	{
		this.usingPlanetSelectionGui = false;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		this.airRemaining = nbt.getInteger("playerAirRemaining");
		this.damageCounter = nbt.getInteger("damageCounter");
		this.oxygenSetupValid = this.lastOxygenSetupValid = nbt.getBoolean("OxygenSetupValid");

		// Backwards compatibility
		NBTTagList nbttaglist = nbt.getTagList("Inventory", 10);
		this.getExtendedInventory().readFromNBTOld(nbttaglist);

		if (nbt.hasKey("ExtendedInventoryGC"))
		{
			this.getExtendedInventory().readFromNBT(nbt.getTagList("ExtendedInventoryGC", 10));
		}

		// Added for GCInv command - if tried to load an offline player's
		// inventory, load it now
		// (if there was no offline load, then the dontload flag in doLoad()
		// will make sure nothing happens)
		ItemStack[] saveinv = CommandGCInv.getSaveData(this.getGameProfile().getName().toLowerCase());
		if (saveinv != null)
		{
			CommandGCInv.doLoad(this);
		}

		if (nbt.hasKey("SpaceshipTier"))
		{
			this.setSpaceshipTier(nbt.getInteger("SpaceshipTier"));
		}

		this.setUsingParachute(nbt.getBoolean("usingParachute2"));
		this.usingPlanetSelectionGui = nbt.getBoolean("usingPlanetSelectionGui");
		this.setTeleportCooldown(nbt.getInteger("teleportCooldown"));
		this.coordsTeleportedFromX = nbt.getDouble("coordsTeleportedFromX");
		this.coordsTeleportedFromZ = nbt.getDouble("coordsTeleportedFromZ");
		this.setSpaceStationDimensionID(nbt.getInteger("spaceStationDimensionID"));

		if (nbt.getBoolean("usingPlanetSelectionGui"))
		{
			this.openPlanetSelectionGuiCooldown = 20;
		}

		final NBTTagList var23 = nbt.getTagList("RocketItems", 10);
		int length = nbt.getInteger("rocketStacksLength");
		boolean oldInventory = false;

		// Backwards Compatibility:
		if (length % 9 == 3)
		{
			oldInventory = true;
			length -= 1;
		}

		this.setRocketStacks(new ItemStack[length]);

		for (int var3 = 0; var3 < var23.tagCount(); ++var3)
		{
			final NBTTagCompound var4 = (NBTTagCompound) var23.getCompoundTagAt(var3);
			final int var5 = var4.getByte("Slot") & 255;

			if (var5 >= 0 && var5 < this.getRocketStacks().length)
			{
				this.getRocketStacks()[var5] = ItemStack.loadItemStackFromNBT(var4);
			}

			if (oldInventory)
			{
				if (var5 == this.getRocketStacks().length - 1)
				{
					this.rocketStacks[var5] = null;
				}

				if (var5 == this.getRocketStacks().length)
				{
					this.rocketStacks[var5 - 1] = ItemStack.loadItemStackFromNBT(var4);
				}
			}
		}

		this.unlockedSchematics = new ArrayList<ISchematicPage>();

		for (int i = 0; i < nbt.getTagList("Schematics", 10).tagCount(); ++i)
		{
			final NBTTagCompound nbttagcompound = (NBTTagCompound) nbt.getTagList("Schematics", 10).getCompoundTagAt(i);

			final int j = nbttagcompound.getInteger("UnlockedPage");

			SchematicRegistry.addUnlockedPage(this, SchematicRegistry.getMatchingRecipeForID(j));
		}

		Collections.sort(this.getUnlockedSchematics());

		this.cryogenicChamberCooldown = nbt.getInteger("CryogenicChamberCooldown");

		if (nbt.hasKey("ReceivedSoundWarning"))
		{
			this.receivedSoundWarning = nbt.getBoolean("ReceivedSoundWarning");
		}

		super.readEntityFromNBT(nbt);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("ExtendedInventoryGC", this.getExtendedInventory().writeToNBT(new NBTTagList()));
		nbt.setInteger("playerAirRemaining", this.airRemaining);
		nbt.setInteger("damageCounter", this.damageCounter);
		nbt.setBoolean("OxygenSetupValid", this.oxygenSetupValid);
		nbt.setBoolean("usingParachute2", this.getParachute());
		nbt.setBoolean("usingPlanetSelectionGui", this.usingPlanetSelectionGui);
		nbt.setInteger("teleportCooldown", this.getTeleportCooldown());
		nbt.setDouble("coordsTeleportedFromX", this.getCoordsTeleportedFromX());
		nbt.setDouble("coordsTeleportedFromZ", this.getCoordsTeleportedFromZ());
		nbt.setInteger("spaceStationDimensionID", this.getSpaceStationDimensionID());

		Collections.sort(this.getUnlockedSchematics());

		NBTTagList tagList = new NBTTagList();

		for (ISchematicPage page : this.getUnlockedSchematics())
		{
			if (page != null)
			{
				final NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setInteger("UnlockedPage", page.getPageID());
				tagList.appendTag(nbttagcompound);
			}
		}

		nbt.setTag("Schematics", tagList);

		nbt.setInteger("rocketStacksLength", this.getRocketStacks().length);
		nbt.setInteger("SpaceshipTier", this.getSpaceshipTier());

		final NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.getRocketStacks().length; ++var3)
		{
			if (this.getRocketStacks()[var3] != null)
			{
				final NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.getRocketStacks()[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		nbt.setTag("RocketItems", var2);

		nbt.setInteger("CryogenicChamberCooldown", this.cryogenicChamberCooldown);
		nbt.setBoolean("ReceivedSoundWarning", this.receivedSoundWarning);

		super.writeEntityToNBT(nbt);
	}

	private void sendAirRemainingPacket()
	{
		final float f1 = Float.valueOf(this.tankInSlot1 == null ? 0.0F : this.tankInSlot1.getMaxDamage() / 90.0F);
		final float f2 = Float.valueOf(this.tankInSlot2 == null ? 0.0F : this.tankInSlot2.getMaxDamage() / 90.0F);
		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_AIR_REMAINING, new Object[] { (int) Math.floor(this.airRemaining / f1), (int) Math.floor(this.airRemaining2 / f2), this.getGameProfile().getName() }), this);
	}

	private void sendGearUpdatePacket(int gearType)
	{
		this.sendGearUpdatePacket(gearType, -1);
	}

	private void sendGearUpdatePacket(int gearType, int subtype)
	{
		if (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.getGameProfile().getName()) != null)
		{
			GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_GEAR_SLOT, new Object[] { this.getGameProfile().getName(), gearType, subtype }), new TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, 50.0D));
		}
	}

	@Override
	public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
	{
		this.wakeUpPlayer(par1, par2, par3, false);
	}

	public void wakeUpPlayer(boolean par1, boolean par2, boolean par3, boolean bypass)
	{
		ChunkCoordinates c = this.playerLocation;

		if (c != null)
		{
			EventWakePlayer event = new EventWakePlayer(this, c.posX, c.posY, c.posZ, par1, par2, par3, bypass);
			MinecraftForge.EVENT_BUS.post(event);

			if (bypass || event.result == null || event.result == EnumStatus.OK)
			{
				super.wakeUpPlayer(par1, par2, par3);
			}
		}
	}

	public void setUsingParachute(boolean tf)
	{
		this.usingParachute = tf;

		if (tf)
		{
			int subtype = -1;

			if (this.parachuteInSlot != null)
			{
				subtype = this.parachuteInSlot.getItemDamage();
			}

			this.sendGearUpdatePacket(EnumModelPacket.ADD_PARACHUTE.getIndex(), subtype);
		}
		else
		{
			this.sendGearUpdatePacket(EnumModelPacket.REMOVE_PARACHUTE.getIndex());
		}
	}

	private boolean getParachute()
	{
		return this.usingParachute;
	}

	public double getCoordsTeleportedFromX()
	{
		return this.coordsTeleportedFromX;
	}

	public double getCoordsTeleportedFromZ()
	{
		return this.coordsTeleportedFromZ;
	}

	public void setCoordsTeleportedFromX(double coords)
	{
		this.coordsTeleportedFromX = coords;
	}

	public void setCoordsTeleportedFromZ(double coords)
	{
		this.coordsTeleportedFromZ = coords;
	}

	public ArrayList<ISchematicPage> getUnlockedSchematics()
	{
		return this.unlockedSchematics;
	}

	public int getSpaceStationDimensionID()
	{
		return this.spaceStationDimensionID;
	}

	public void setSpaceStationDimensionID(int spaceStationDimensionID)
	{
		this.spaceStationDimensionID = spaceStationDimensionID;
	}

	public int getTeleportCooldown()
	{
		return this.teleportCooldown;
	}

	public void setTeleportCooldown(int teleportCooldown)
	{
		this.teleportCooldown = teleportCooldown;
	}

	public ItemStack[] getRocketStacks()
	{
		return this.rocketStacks;
	}

	public void setRocketStacks(ItemStack[] rocketStacks)
	{
		this.rocketStacks = rocketStacks;
	}

	public boolean isTouchedGround()
	{
		return this.touchedGround;
	}

	public void setTouchedGround(boolean touchedGround)
	{
		this.touchedGround = touchedGround;
	}

	public int getRocketType()
	{
		return this.rocketType;
	}

	public void setRocketType(int rocketType)
	{
		this.rocketType = rocketType;
	}

	public int getFuelLevel()
	{
		return this.fuelLevel;
	}

	public void setFuelLevel(int fuelDamage)
	{
		this.fuelLevel = fuelDamage;
	}

	public int getChestSpawnCooldown()
	{
		return this.chestSpawnCooldown;
	}

	public void setChestSpawnCooldown(int chestSpawnCooldown)
	{
		this.chestSpawnCooldown = chestSpawnCooldown;
	}

	public micdoodle8.mods.galacticraft.api.vector.Vector3 getChestSpawnVector()
	{
		return this.chestSpawnVector;
	}

	public void setChestSpawnVector(micdoodle8.mods.galacticraft.api.vector.Vector3 chestSpawnVector)
	{
		this.chestSpawnVector = chestSpawnVector;
	}

	public InventoryExtended getExtendedInventory()
	{
		return this.extendedInventory;
	}

	public void setExtendedInventory(InventoryExtended extendedInventory)
	{
		this.extendedInventory = extendedInventory;
	}

	public int getLaunchAttempts()
	{
		return this.launchAttempts;
	}

	public void setLaunchAttempts(int launchAttempts)
	{
		this.launchAttempts = launchAttempts;
	}

	public int getChatCooldown()
	{
		return this.chatCooldown;
	}

	public void setChatCooldown(int chatCooldown)
	{
		this.chatCooldown = chatCooldown;
	}

	public int getSpaceshipTier()
	{
		return this.spaceshipTier;
	}

	public void setSpaceshipTier(int spaceshipTier)
	{
		this.spaceshipTier = spaceshipTier;
	}

	public Item getRocketItem()
	{
		return this.rocketItem;
	}

	public void setRocketItem(Item rocketItem)
	{
		this.rocketItem = rocketItem;
	}

	public int getCryogenicChamberCooldown()
	{
		return this.cryogenicChamberCooldown;
	}

	public void setCryogenicChamberCooldown(int cryogenicChamberCooldown)
	{
		this.cryogenicChamberCooldown = cryogenicChamberCooldown;
	}

	public static enum EnumModelPacket
	{
		ADDMASK(0),
		REMOVEMASK(1),
		ADDGEAR(2),
		REMOVEGEAR(3),
		ADDLEFTREDTANK(4),
		ADDLEFTORANGETANK(5),
		ADDLEFTGREENTANK(6),
		REMOVE_LEFT_TANK(7),
		ADDRIGHTREDTANK(8),
		ADDRIGHTORANGETANK(9),
		ADDRIGHTGREENTANK(10),
		REMOVE_RIGHT_TANK(11),
		ADD_PARACHUTE(12),
		REMOVE_PARACHUTE(13),
		ADD_FREQUENCY_MODULE(14),
		REMOVE_FREQUENCY_MODULE(15);

		private int index;

		private EnumModelPacket(int index)
		{
			this.index = index;
		}

		private int getIndex()
		{
			return this.index;
		}
	}
}
