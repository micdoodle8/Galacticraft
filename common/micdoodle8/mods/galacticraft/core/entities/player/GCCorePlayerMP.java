package micdoodle8.mods.galacticraft.core.entities.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import micdoodle8.mods.galacticraft.api.event.oxygen.GCCoreOxygenSuffocationEvent;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCCoreDamageSource;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.command.GCCoreCommandGCInv;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityLander;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityMeteor;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityParaChest;
import micdoodle8.mods.galacticraft.core.event.GCCoreEventWakePlayer;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryExtended;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketSchematicList;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.item.Item;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
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
public class GCCorePlayerMP extends EntityPlayerMP
{
	private GCCoreInventoryExtended extendedInventory = new GCCoreInventoryExtended();

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

	public GCCorePlayerMP(MinecraftServer server, World world, String username, ItemInWorldManager itemInWorldManager)
	{
		super(server, world, username, itemInWorldManager);

		if (!GalacticraftCore.playersServer.containsKey(this.username))
		{
			GalacticraftCore.playersServer.put(this.username, this);
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

		if (par1EntityPlayer instanceof GCCorePlayerMP)
		{
			if (keepInv)
			{
				this.getExtendedInventory().copyInventory(((GCCorePlayerMP) par1EntityPlayer).extendedInventory);
			}
			else if (this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
			{
				this.getExtendedInventory().copyInventory(((GCCorePlayerMP) par1EntityPlayer).extendedInventory);
			}

			this.setSpaceStationDimensionID(((GCCorePlayerMP) par1EntityPlayer).getSpaceStationDimensionID());
		}
	}

	@Override
	protected void fall(float par1)
	{
		if (this.ridingEntity instanceof EntityAutoRocket || this.ridingEntity instanceof GCCoreEntityLander)
		{
			return;
		}

		super.fall(par1);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (!GalacticraftCore.playersServer.containsKey(this.username) || this.tick % 360 == 0)
		{
			GalacticraftCore.playersServer.put(this.username, this);
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

		this.updateStep();

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
				GCCoreEntityParaChest chest = new GCCoreEntityParaChest(this.worldObj, this.getRocketStacks(), this.getFuelLevel());

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
			this.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.UPDATE_OXYGEN_VALIDITY, new Object[] { Boolean.valueOf(this.oxygenSetupValid) }));
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
			this.playerNetServerHandler.ticksForFloatKick = 0;
		}

		this.updateSchematics();

		if (this.frequencyModuleInSlot == null && !this.receivedSoundWarning && this.tick > 0 && this.tick % 250 == 0 && this.worldObj.provider instanceof IGalacticraftWorldProvider && this.onGround)
		{
			this.sendChatToPlayer(ChatMessageComponent.createFromText(EnumColor.YELLOW + "I'll probably need a " + EnumColor.AQUA + GCCoreItems.basicItem.getItemStackDisplayName(new ItemStack(GCCoreItems.basicItem, 1, 19)) + EnumColor.YELLOW + " if I want to hear properly here."));
			this.receivedSoundWarning = true;
		}

		this.lastOxygenSetupValid = this.oxygenSetupValid;
		this.lastUnlockedSchematics = this.getUnlockedSchematics();

		this.lastOnGround = this.onGround;
	}

	private void updateStep()
	{
		if (this.worldObj != null && this.worldObj.provider instanceof GCMoonWorldProvider && !this.isAirBorne && this.ridingEntity == null)
		{
			if (this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ)) == GCCoreBlocks.blockMoon.blockID)
			{
				if (this.worldObj.getBlockMetadata(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ)) == 5)
				{
					int meta = -1;

					final int i = 1 + MathHelper.floor_double(this.rotationYaw * 8.0F / 360.0F + 0.5D) & 7;

					switch (this.lastStep)
					{
					case 1:
						switch (i)
						{
						case 0:
							meta = 2;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						case 1:
							meta = 4;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						case 2:
							meta = 2;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						case 3:
							meta = 2;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						case 4:
							meta = 2;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						case 5:
							meta = 2;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						case 6:
							meta = 2;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						case 7:
							meta = 2;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						}
						this.lastStep = 2;
						break;
					case 2:
						switch (i)
						{
						case 0:
							meta = 1;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						case 1:
							meta = 1;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						case 2:
							meta = 4;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						case 3:
							meta = 4;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						case 4:
							meta = 1;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						case 5:
							meta = 3;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						case 6:
							meta = 2;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						case 7:
							meta = 4;
							this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
							break;
						}
						this.lastStep = 1;
						this.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ), meta + 5, 3);
						break;
					default:
						this.lastStep = 1;
						break;
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

			if (theCurrentItem.getItem().itemID == Block.torchWood.blockID)
			{
				final ItemStack stack = new ItemStack(GCCoreBlocks.unlitTorch, var1, 0);
				this.inventory.mainInventory[this.inventory.currentItem] = stack;
			}
			else if (theCurrentItem.getItem().itemID == Item.bow.itemID)
			{
				final Hashtable<Enchantment, Integer> enchants = new Hashtable<Enchantment, Integer>();

				final NBTTagList list = theCurrentItem.getEnchantmentTagList();

				if (list != null)
				{
					for (int var7 = 0; var7 < list.tagCount(); ++var7)
					{
						final int enchID = Integer.valueOf(((NBTTagCompound) list.tagAt(var7)).getShort("id"));
						final int enchLvl = Integer.valueOf(((NBTTagCompound) list.tagAt(var7)).getShort("lvl"));

						final Enchantment e = Enchantment.enchantmentsList[enchID];

						enchants.put(e, enchLvl);
					}
				}

				final ItemStack stack = new ItemStack(GCCoreItems.bowGravity, var1, var2);

				final Iterator<Entry<Enchantment, Integer>> it = enchants.entrySet().iterator();

				while (it.hasNext())
				{
					final Entry<Enchantment, Integer> entry = it.next();

					if (entry.getKey() != null && entry.getValue() != null)
					{
						stack.addEnchantment(entry.getKey(), entry.getValue());
					}
				}

				//Transfer any cherished name
				if (theCurrentItem.stackTagCompound != null && theCurrentItem.stackTagCompound.hasKey("display"))
		        {
		            NBTTagCompound nbttagcompound = theCurrentItem.stackTagCompound.getCompoundTag("display");

		            if (nbttagcompound.hasKey("Name"))
		            {
		            	stack.setItemName(nbttagcompound.getString("Name"));
		            }
		        }
				
				this.inventory.mainInventory[this.inventory.currentItem] = stack;
			}
		}
		else if (!(this.worldObj.provider instanceof IGalacticraftWorldProvider) && theCurrentItem != null)
		{
			final int var1 = theCurrentItem.stackSize;
			final int var2 = theCurrentItem.getItemDamage();

			if (theCurrentItem.getItem().itemID == GCCoreBlocks.unlitTorch.blockID)
			{
				final ItemStack stack = new ItemStack(Block.torchWood, var1, 0);
				this.inventory.mainInventory[this.inventory.currentItem] = stack;
			}
			else if (theCurrentItem.getItem().itemID == GCCoreItems.bowGravity.itemID)
			{
				final Hashtable<Enchantment, Integer> enchants = new Hashtable<Enchantment, Integer>();

				final NBTTagList list = theCurrentItem.getEnchantmentTagList();

				if (list != null)
				{
					for (int var7 = 0; var7 < list.tagCount(); ++var7)
					{
						final int enchID = Integer.valueOf(((NBTTagCompound) list.tagAt(var7)).getShort("id"));
						final int enchLvl = Integer.valueOf(((NBTTagCompound) list.tagAt(var7)).getShort("lvl"));

						final Enchantment e = Enchantment.enchantmentsList[enchID];

						enchants.put(e, enchLvl);
					}
				}

				final ItemStack stack = new ItemStack(Item.bow, var1, var2);

				final Iterator<Entry<Enchantment, Integer>> it = enchants.entrySet().iterator();

				while (it.hasNext())
				{
					final Entry<Enchantment, Integer> entry = it.next();

					if (entry.getKey() != null && entry.getValue() != null)
					{
						stack.addEnchantment(entry.getKey(), entry.getValue());
					}
				}

				//Transfer any cherished name
				if (theCurrentItem.stackTagCompound != null && theCurrentItem.stackTagCompound.hasKey("display"))
		        {
		            NBTTagCompound nbttagcompound = theCurrentItem.stackTagCompound.getCompoundTag("display");

		            if (nbttagcompound.hasKey("Name"))
		            {
		            	stack.setItemName(nbttagcompound.getString("Name"));
		            }
		        }

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

		this.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.UPDATE_DIMENSION_LIST, new Object[] { this.username, temp }));
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

		if (this.frequencyModuleInSlot != null && this.lastFrequencyModuleInSlot == null && this.frequencyModuleInSlot.getItem().itemID == GCCoreItems.basicItem.itemID && this.frequencyModuleInSlot.getItemDamage() == 19)
		{
			this.sendGearUpdatePacket(EnumModelPacket.ADD_FREQUENCY_MODULE.getIndex());
		}

		if (this.frequencyModuleInSlot == null && this.lastFrequencyModuleInSlot != null)
		{
			this.sendGearUpdatePacket(EnumModelPacket.REMOVE_FREQUENCY_MODULE.getIndex());
		}

		//

		if (this.maskInSlot != null && this.lastMaskInSlot == null && this.maskInSlot.getItem().itemID == GCCoreItems.oxMask.itemID)
		{
			this.sendGearUpdatePacket(EnumModelPacket.ADDMASK.getIndex());
		}

		if (this.maskInSlot == null && this.lastMaskInSlot != null)
		{
			this.sendGearUpdatePacket(EnumModelPacket.REMOVEMASK.getIndex());
		}

		//

		if (this.gearInSlot != null && this.lastGearInSlot == null && this.gearInSlot.getItem().itemID == GCCoreItems.oxygenGear.itemID)
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
			if (this.tankInSlot1.getItem().itemID == GCCoreItems.oxTankLight.itemID)
			{
				this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTGREENTANK.getIndex());
			}
			else if (this.tankInSlot1.getItem().itemID == GCCoreItems.oxTankMedium.itemID)
			{
				this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTORANGETANK.getIndex());
			}
			else if (this.tankInSlot1.getItem().itemID == GCCoreItems.oxTankHeavy.itemID)
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
			if (this.tankInSlot1.getItem().itemID != this.lastTankInSlot1.getItem().itemID)
			{
				if (this.tankInSlot1.getItem().itemID == GCCoreItems.oxTankLight.itemID)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTGREENTANK.getIndex());
				}
				else if (this.tankInSlot1.getItem().itemID == GCCoreItems.oxTankMedium.itemID)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTORANGETANK.getIndex());
				}
				else if (this.tankInSlot1.getItem().itemID == GCCoreItems.oxTankHeavy.itemID)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTREDTANK.getIndex());
				}
			}
		}

		//

		if (this.tankInSlot2 != null && this.lastTankInSlot2 == null)
		{
			if (this.tankInSlot2.getItem().itemID == GCCoreItems.oxTankLight.itemID)
			{
				this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTGREENTANK.getIndex());
			}
			else if (this.tankInSlot2.getItem().itemID == GCCoreItems.oxTankMedium.itemID)
			{
				this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTORANGETANK.getIndex());
			}
			else if (this.tankInSlot2.getItem().itemID == GCCoreItems.oxTankHeavy.itemID)
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
			if (this.tankInSlot2.getItem().itemID != this.lastTankInSlot2.getItem().itemID)
			{
				if (this.tankInSlot2.getItem().itemID == GCCoreItems.oxTankLight.itemID)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTGREENTANK.getIndex());
				}
				else if (this.tankInSlot2.getItem().itemID == GCCoreItems.oxTankMedium.itemID)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTORANGETANK.getIndex());
				}
				else if (this.tankInSlot2.getItem().itemID == GCCoreItems.oxTankHeavy.itemID)
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
					this.damageCounter = GCCoreConfigManager.suffocationCooldown;

					GCCoreOxygenSuffocationEvent suffocationEvent = new GCCoreOxygenSuffocationEvent.Pre(this);
					MinecraftForge.EVENT_BUS.post(suffocationEvent);

					if (!suffocationEvent.isCanceled())
					{
						this.attackEntityFrom(GCCoreDamageSource.oxygenSuffocation, GCCoreConfigManager.suffocationDamage);

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

					if (closestPlayer == null || closestPlayer.entityId <= this.entityId)
					{
						int x, y, z;
						double motX, motZ;
						x = this.worldObj.rand.nextInt(20) - 10;
						y = this.worldObj.rand.nextInt(20) + 200;
						z = this.worldObj.rand.nextInt(20) - 10;
						motX = this.worldObj.rand.nextDouble() * 5;
						motZ = this.worldObj.rand.nextDouble() * 5;

						final GCCoreEntityMeteor meteor = new GCCoreEntityMeteor(this.worldObj, this.posX + x, this.posY + y, this.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 1);

						if (!this.worldObj.isRemote)
						{
							this.worldObj.spawnEntityInWorld(meteor);
						}
					}
				}

				if (this.worldObj.rand.nextInt(MathHelper.floor_double(f * 3000)) == 0)
				{
					final EntityPlayer closestPlayer = this.worldObj.getClosestPlayerToEntity(this, 100);

					if (closestPlayer == null || closestPlayer.entityId <= this.entityId)
					{
						int x, y, z;
						double motX, motZ;
						x = this.worldObj.rand.nextInt(20) - 10;
						y = this.worldObj.rand.nextInt(20) + 200;
						z = this.worldObj.rand.nextInt(20) - 10;
						motX = this.worldObj.rand.nextDouble() * 5;
						motZ = this.worldObj.rand.nextDouble() * 5;

						final GCCoreEntityMeteor meteor = new GCCoreEntityMeteor(this.worldObj, this.posX + x, this.posY + y, this.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 6);

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
			this.playerNetServerHandler.sendPacketToPlayer(GCCorePacketSchematicList.buildSchematicListPacket(this.getUnlockedSchematics()));
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
		NBTTagList nbttaglist = nbt.getTagList("Inventory");
		this.getExtendedInventory().readFromNBTOld(nbttaglist);

		if (nbt.hasKey("ExtendedInventoryGC"))
		{
			this.getExtendedInventory().readFromNBT(nbt.getTagList("ExtendedInventoryGC"));
		}

		// Added for GCInv command - if tried to load an offline player's
		// inventory, load it now
		// (if there was no offline load, then the dontload flag in doLoad()
		// will make sure nothing happens)
		ItemStack[] saveinv = GCCoreCommandGCInv.getSaveData(this.username.toLowerCase());
		if (saveinv != null)
		{
			GCCoreCommandGCInv.doLoad(this);
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

		final NBTTagList var23 = nbt.getTagList("RocketItems");
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
			final NBTTagCompound var4 = (NBTTagCompound) var23.tagAt(var3);
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

		for (int i = 0; i < nbt.getTagList("Schematics").tagCount(); ++i)
		{
			final NBTTagCompound nbttagcompound = (NBTTagCompound) nbt.getTagList("Schematics").tagAt(i);

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

		final Object[] toSend = { MathHelper.floor_float(this.airRemaining / f1), MathHelper.floor_float(this.airRemaining2 / f2), this.username };

		this.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.AIR_REMAINING, toSend));
	}

	private void sendGearUpdatePacket(int gearType)
	{
		this.sendGearUpdatePacket(gearType, -1);
	}

	private void sendGearUpdatePacket(int gearType, int subtype)
	{
		final Object[] toSend = { this.username, gearType, subtype };

		if (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.username) != null)
		{
			PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 50, this.worldObj.provider.dimensionId, PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.UPDATE_GEAR_SLOT, toSend));
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
			GCCoreEventWakePlayer event = new GCCoreEventWakePlayer(this, c.posX, c.posY, c.posZ, par1, par2, par3, bypass);
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

	public GCCoreInventoryExtended getExtendedInventory()
	{
		return this.extendedInventory;
	}

	public void setExtendedInventory(GCCoreInventoryExtended extendedInventory)
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
