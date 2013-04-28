package micdoodle8.mods.galacticraft.core.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.API.ISchematicPage;
import micdoodle8.mods.galacticraft.API.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryTankRefill;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketSchematicList;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet16BlockItemSwitch;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet4UpdateTime;
import net.minecraft.network.packet.Packet70GameEvent;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class GCCorePlayerMP extends EntityPlayerMP
{
	private int airRemaining;
	private int airRemaining2;

	public boolean hasTank;

	public long tick;

	public ItemStack tankInSlot;

	public GCCoreInventoryTankRefill playerTankInventory = new GCCoreInventoryTankRefill(this);

	public boolean inPortal;
	public int timeUntilPortal;

	private final int dimensionToSend = -2;

	private int damageCounter;

    public int astronomyPointsLevel;

    public int astronomyPointsTotal;

    public float astronomyPoints;

    public ItemStack[] rocketStacks = new ItemStack[27];
    public int rocketType;
    public int fuelDamage;

	private boolean usingParachute;

	private ItemStack parachuteInSlot;
	private ItemStack lastParachuteInSlot;

	private ItemStack maskInSlot;
	private ItemStack lastMaskInSlot;

	private ItemStack gearInSlot;
	private ItemStack lastGearInSlot;

	private ItemStack tankInSlot1;
	private ItemStack lastTankInSlot1;

	private ItemStack tankInSlot2;
	private ItemStack lastTankInSlot2;

	public int launchAttempts = 0;

	private boolean usingPlanetSelectionGui;

	private int openPlanetSelectionGuiCooldown;
	private boolean hasOpenedPlanetSelectionGui = false;

	public int chestSpawnCooldown;

	public int teleportCooldown;
	
	public int chatCooldown;

	private int lastStep;

	public double coordsTeleportedFromX;
	public double coordsTeleportedFromZ;

	public int spaceStationDimensionID = -1;

	public boolean oxygenSetupValid;
	public boolean lastOxygenSetupValid;

	public ArrayList<ISchematicPage> unlockedSchematics = new ArrayList<ISchematicPage>();
	public ArrayList<ISchematicPage> lastUnlockedSchematics = new ArrayList<ISchematicPage>();

    public GCCorePlayerMP(MinecraftServer par1MinecraftServer, World par2World, String par3Str, ItemInWorldManager par4ItemInWorldManager)
    {
    	super(par1MinecraftServer, par2World, par3Str, par4ItemInWorldManager);
    }

	@Override
    public void onDeath(DamageSource var1)
    {
		GalacticraftCore.playersServer.remove(this);

        if (!this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
        {
            this.playerTankInventory.dropAllItems();
        }

    	super.onDeath(var1);
    }

    @Override
	public void clonePlayer(EntityPlayer par1EntityPlayer, boolean par2)
    {
    	super.clonePlayer(par1EntityPlayer, par2);

    	if (par1EntityPlayer instanceof GCCorePlayerMP)
    	{
        	this.spaceStationDimensionID = ((GCCorePlayerMP) par1EntityPlayer).spaceStationDimensionID;
    	}
    }

	@Override
    public void onUpdate()
    {
    	super.onUpdate();

    	if (!GalacticraftCore.playersServer.containsKey(this.username) || this.tick % 360 == 0)
    	{
    		GalacticraftCore.playersServer.put(this.username, this);
    	}

    	this.tick++;

    	Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

		if (this.worldObj != null && this.worldObj.provider instanceof GCMoonWorldProvider && !this.isAirBorne && this.ridingEntity == null)
		{
			if (this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1), MathHelper.floor_double(this.posZ)) == GCMoonBlocks.blockMoon.blockID)
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

    	if (this.teleportCooldown > 0)
    	{
    		this.teleportCooldown--;
    	}
    	
    	if (this.chatCooldown > 0)
    	{
    		this.chatCooldown--;
    	}

    	if (this.openPlanetSelectionGuiCooldown > 0)
    	{
    		this.openPlanetSelectionGuiCooldown--;
    	}

    	this.maskInSlot = this.playerTankInventory.getStackInSlot(0);
    	this.gearInSlot = this.playerTankInventory.getStackInSlot(1);
    	this.tankInSlot1 = this.playerTankInventory.getStackInSlot(2);
    	this.tankInSlot2 = this.playerTankInventory.getStackInSlot(3);
    	this.parachuteInSlot = this.playerTankInventory.getStackInSlot(4);

    	if (this.getParachute())
    	{
    		this.fallDistance = 0.0F;
    	}

		if (this.worldObj.provider instanceof IGalacticraftWorldProvider && this.inventory.getCurrentItem() != null)
	    {
	    	final int var1 = this.inventory.getCurrentItem().stackSize;
	    	final int var2 = this.inventory.getCurrentItem().getItemDamage();

			if (this.inventory.getCurrentItem().getItem().itemID == Block.torchWood.blockID)
			{
	        	final ItemStack stack = new ItemStack(GCCoreBlocks.unlitTorch, var1, 0);
	            this.inventory.mainInventory[this.inventory.currentItem] = stack;
			}
			else if (this.inventory.getCurrentItem().getItem().itemID == Item.bow.itemID)
			{
	        	final Hashtable<Integer, Enchantment> enchants = new Hashtable<Integer, Enchantment>();

	        	final NBTTagList list = this.inventory.getCurrentItem().getEnchantmentTagList();

	        	if (list != null)
	            {
	                for (int var7 = 0; var7 < list.tagCount(); ++var7)
	                {
	                    final int enchID = Integer.valueOf(((NBTTagCompound)list.tagAt(var7)).getShort("id"));
	                    final int enchLvl = Integer.valueOf(((NBTTagCompound)list.tagAt(var7)).getShort("lvl"));

	                    final Enchantment e = Enchantment.enchantmentsList[enchID];

	                    enchants.put(enchLvl, e);
	                }
	            }

	        	final ItemStack stack = new ItemStack(GCCoreItems.gravityBow, var1, var2);

	        	final Iterator<Entry<Integer, Enchantment>> it = enchants.entrySet().iterator();

	        	while (it.hasNext())
	        	{
	        		final Entry<Integer, Enchantment> entry = it.next();

	        		if (entry.getKey() != null && entry.getValue() != null)
	        		{
		        		stack.addEnchantment(entry.getValue(), entry.getKey());
	        		}
	        	}

	            this.inventory.mainInventory[this.inventory.currentItem] = stack;
			}
			else if (this.inventory.getCurrentItem().getItem().itemID == Block.sapling.blockID)
			{
				// No jungle trees...
				if (var2 != 3)
				{
		        	final ItemStack stack = new ItemStack(GCCoreBlocks.sapling, var1, var2);
		            this.inventory.mainInventory[this.inventory.currentItem] = stack;
				}
			}
	    }
	    else if (!(this.worldObj.provider instanceof IGalacticraftWorldProvider) && this.inventory.getCurrentItem() != null)
	    {
	    	final int var1 = this.inventory.getCurrentItem().stackSize;
	    	final int var2 = this.inventory.getCurrentItem().getItemDamage();

	    	if (this.inventory.getCurrentItem().getItem().itemID == GCCoreBlocks.unlitTorch.blockID)
	    	{
	        	final ItemStack stack = new ItemStack(Block.torchWood, var1, 0);
	            this.inventory.mainInventory[this.inventory.currentItem] = stack;
	    	}
	    	else if (this.inventory.getCurrentItem().getItem().itemID == GCCoreBlocks.sapling.blockID)
	    	{
	        	final ItemStack stack = new ItemStack(Block.sapling, var1, var2);
	            this.inventory.mainInventory[this.inventory.currentItem] = stack;
	    	}
	    	else if (this.inventory.getCurrentItem().getItem().itemID == GCCoreItems.gravityBow.itemID)
	    	{
	        	final Hashtable<Integer, Enchantment> enchants = new Hashtable<Integer, Enchantment>();

	        	final NBTTagList list = this.inventory.getCurrentItem().getEnchantmentTagList();

	        	if (list != null)
	            {
	                for (int var7 = 0; var7 < list.tagCount(); ++var7)
	                {
	                    final int enchID = Integer.valueOf(((NBTTagCompound)list.tagAt(var7)).getShort("id"));
	                    final int enchLvl = Integer.valueOf(((NBTTagCompound)list.tagAt(var7)).getShort("lvl"));

	                    final Enchantment e = Enchantment.enchantmentsList[enchID];

	                    enchants.put(enchLvl, e);
	                }
	            }

	        	final ItemStack stack = new ItemStack(Item.bow, var1, var2);

	        	final Iterator<Entry<Integer, Enchantment>> it = enchants.entrySet().iterator();

	        	while (it.hasNext())
	        	{
	        		final Entry<Integer, Enchantment> entry = it.next();

	        		if (entry.getKey() != null && entry.getValue() != null)
	        		{
		        		stack.addEnchantment(entry.getValue(), entry.getKey());
	        		}
	        	}

	            this.inventory.mainInventory[this.inventory.currentItem] = stack;
	    	}
	    }

		if (!this.hasOpenedPlanetSelectionGui && this.openPlanetSelectionGuiCooldown == 1)
		{
//	        if (this.spaceStationDimensionID == -1)
//	        {
//	        	WorldUtil.bindSpaceStationToNewDimension(this.worldObj, this);
//	        }

        	final Integer[] ids = WorldUtil.getArrayOfPossibleDimensions();

	    	final Set set = WorldUtil.getArrayOfPossibleDimensions(ids, this).entrySet();
	    	final Iterator i = set.iterator();

	    	String temp = "";

	    	for (int k = 0; i.hasNext(); k++)
	    	{
	    		final Map.Entry entry = (Map.Entry)i.next();
	    		temp = k == 0 ? temp.concat(String.valueOf(entry.getKey())) : temp.concat("." + String.valueOf(entry.getKey()));
	    	}

	        final Object[] toSend = {this.username, temp};

	        this.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 2, toSend));

	        this.setUsingPlanetGui();
	        this.hasOpenedPlanetSelectionGui = true;
		}

		if (this.usingPlanetSelectionGui)
		{
        	final Integer[] ids = WorldUtil.getArrayOfPossibleDimensions();

	    	final Set set = WorldUtil.getArrayOfPossibleDimensions(ids, this).entrySet();
	    	final Iterator i = set.iterator();

	    	String temp = "";

	    	for (int k = 0; i.hasNext(); k++)
	    	{
	    		final Map.Entry entry = (Map.Entry)i.next();
	    		temp = k == 0 ? temp.concat(String.valueOf(entry.getKey())) : temp.concat("." + String.valueOf(entry.getKey()));
	    	}

	        final Object[] toSend = {this.username, temp};

	        this.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 2, toSend));
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
			this.sendParachuteRemovalPacket();
			this.setParachute(false);
		}

		if (this.maskInSlot != null && this.lastMaskInSlot == null)
		{
			this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDMASK.getIndex());
		}

		if (this.maskInSlot == null && this.lastMaskInSlot != null)
		{
			this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVEMASK.getIndex());
		}

		//

		if (this.gearInSlot != null && this.lastGearInSlot == null)
		{
			this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDGEAR.getIndex());
		}

		if (this.gearInSlot == null && this.lastGearInSlot != null)
		{
			this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVEGEAR.getIndex());
		}

		//

		if (this.tankInSlot1 != null && this.lastTankInSlot1 == null)
		{
			if (this.tankInSlot1.getItem().itemID == GCCoreItems.lightOxygenTank.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTGREENTANK.getIndex());
			}
			else if (this.tankInSlot1.getItem().itemID == GCCoreItems.medOxygenTank.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTORANGETANK.getIndex());
			}
			else if (this.tankInSlot1.getItem().itemID == GCCoreItems.heavyOxygenTank.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTREDTANK.getIndex());
			}
		}

		if (this.tankInSlot1 == null && this.lastTankInSlot1 != null)
		{
			if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.lightOxygenTank.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTGREENTANK.getIndex());
			}
			else if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.medOxygenTank.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTORANGETANK.getIndex());
			}
			else if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.heavyOxygenTank.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTREDTANK.getIndex());
			}
		}

		if (this.tankInSlot1 != null && this.lastTankInSlot1 != null)
		{
			if (this.tankInSlot1.getItem().itemID != this.lastTankInSlot1.getItem().itemID)
			{
				if (this.tankInSlot1.getItem().itemID == GCCoreItems.lightOxygenTank.itemID)
				{
					if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.medOxygenTank.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTORANGETANK.getIndex());
					}
					else if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.heavyOxygenTank.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTREDTANK.getIndex());
					}

					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTGREENTANK.getIndex());
				}
				else if (this.tankInSlot1.getItem().itemID == GCCoreItems.medOxygenTank.itemID)
				{
					if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.lightOxygenTank.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTGREENTANK.getIndex());
					}
					else if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.heavyOxygenTank.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTREDTANK.getIndex());
					}

					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTORANGETANK.getIndex());
				}
				else if (this.tankInSlot1.getItem().itemID == GCCoreItems.heavyOxygenTank.itemID)
				{
					if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.lightOxygenTank.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTGREENTANK.getIndex());
					}
					else if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.medOxygenTank.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTORANGETANK.getIndex());
					}

					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTREDTANK.getIndex());
				}
			}
		}

		//

		if (this.tankInSlot2 != null && this.lastTankInSlot2 == null)
		{
			if (this.tankInSlot2.getItem().itemID == GCCoreItems.lightOxygenTank.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTGREENTANK.getIndex());
			}
			else if (this.tankInSlot2.getItem().itemID == GCCoreItems.medOxygenTank.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTORANGETANK.getIndex());
			}
			else if (this.tankInSlot2.getItem().itemID == GCCoreItems.heavyOxygenTank.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTREDTANK.getIndex());
			}
		}

		if (this.tankInSlot2 == null && this.lastTankInSlot2 != null)
		{
			if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.lightOxygenTank.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTGREENTANK.getIndex());
			}
			else if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.medOxygenTank.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTORANGETANK.getIndex());
			}
			else if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.heavyOxygenTank.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTREDTANK.getIndex());
			}
		}

		if (this.tankInSlot2 != null && this.lastTankInSlot2 != null)
		{
			if (this.tankInSlot2.getItem().itemID != this.lastTankInSlot2.getItem().itemID)
			{
				if (this.tankInSlot2.getItem().itemID == GCCoreItems.lightOxygenTank.itemID)
				{
					if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.medOxygenTank.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTORANGETANK.getIndex());
					}
					else if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.heavyOxygenTank.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTREDTANK.getIndex());
					}

					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTGREENTANK.getIndex());
				}
				else if (this.tankInSlot2.getItem().itemID == GCCoreItems.medOxygenTank.itemID)
				{
					if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.lightOxygenTank.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTGREENTANK.getIndex());
					}
					else if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.heavyOxygenTank.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTREDTANK.getIndex());
					}

					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTORANGETANK.getIndex());
				}
				else if (this.tankInSlot2.getItem().itemID == GCCoreItems.heavyOxygenTank.itemID)
				{
					if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.lightOxygenTank.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTGREENTANK.getIndex());
					}
					else if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.medOxygenTank.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTORANGETANK.getIndex());
					}

					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTREDTANK.getIndex());
				}
			}
		}

		if (this.chestSpawnCooldown > 0)
		{
			this.chestSpawnCooldown--;
		}

		//

		if (this.getParachute() && this.parachuteInSlot == null && this.lastParachuteInSlot != null)
		{
			this.sendParachuteRemovalPacket();
		}

		if (this.getParachute() && this.parachuteInSlot != null && this.lastParachuteInSlot == null)
		{
			this.sendParachuteAddPacket();
		}

		if (this.parachuteInSlot != null && this.lastParachuteInSlot == null)
		{
			this.sendPlayerParachuteTexturePacket(this);
		}

		if (this.parachuteInSlot != null && this.lastParachuteInSlot != null)
		{
			if (this.parachuteInSlot.getItemDamage() != this.lastParachuteInSlot.getItemDamage())
			{
				this.sendPlayerParachuteTexturePacket(this);
			}
		}

		if (this.tick % 60 == 0)
		{
			final Object[] toSend = {this.spaceStationDimensionID};
	    	this.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 18, toSend));

			this.sendPlayerParachuteTexturePacket(this);

			if (this.getParachute() && this.parachuteInSlot != null)
			{
				this.sendParachuteAddPacket();
			}
			else
			{
				this.sendParachuteRemovalPacket();
			}

			int id;

			if (this.maskInSlot != null)
			{
				id = this.maskInSlot.itemID;

				if (id == GCCoreItems.oxygenMask.itemID)
				{
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDMASK.getIndex());
				}
			}

			if (this.gearInSlot != null)
			{
				id = this.gearInSlot.itemID;

				if (id == GCCoreItems.oxygenGear.itemID)
				{
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDGEAR.getIndex());
				}
			}

			if (this.tankInSlot1 != null)
			{
				id = this.tankInSlot1.itemID;

				if (id == GCCoreItems.lightOxygenTank.itemID)
				{
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTGREENTANK.getIndex());
				}
				else if (id == GCCoreItems.medOxygenTank.itemID)
				{
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTORANGETANK.getIndex());
				}
				else if (id == GCCoreItems.heavyOxygenTank.itemID)
				{
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTREDTANK.getIndex());
				}
			}

			if (this.tankInSlot2 != null)
			{
				id = this.tankInSlot2.itemID;

				if (id == GCCoreItems.lightOxygenTank.itemID)
				{
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTGREENTANK.getIndex());
				}
				else if (id == GCCoreItems.medOxygenTank.itemID)
				{
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTORANGETANK.getIndex());
				}
				else if (id == GCCoreItems.heavyOxygenTank.itemID)
				{
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTREDTANK.getIndex());
				}
			}
		}

		if (this.launchAttempts > 0 && this.ridingEntity == null)
		{
			this.launchAttempts = 0;
		}

		if (this.worldObj.provider instanceof IGalacticraftWorldProvider)
		{
			this.fallDistance = 0.0F;
		}

		final ItemStack tankInSlot = this.playerTankInventory.getStackInSlot(2);
		final ItemStack tankInSlot2 = this.playerTankInventory.getStackInSlot(3);

		final int drainSpacing = OxygenUtil.getDrainSpacing(tankInSlot);
		final int drainSpacing2 = OxygenUtil.getDrainSpacing(tankInSlot2);

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

			if (drainSpacing > 0 && tankInSlot != null)
			{
	    		this.airRemaining = tankInSlot.getMaxDamage() - tankInSlot.getItemDamage();
			}

			if (drainSpacing2 > 0 && tankInSlot2 != null)
			{
	    		this.airRemaining2 = tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage();
			}

			if (drainSpacing > 0 && this.tick % drainSpacing == 0 && !OxygenUtil.isAABBInBreathableAirBlock(this) && tankInSlot.getMaxDamage() - tankInSlot.getItemDamage() > 0)
	    	{
	    		tankInSlot.damageItem(1, this);
	    	}

			if (drainSpacing2 > 0 && this.tick % drainSpacing2 == 0 && !OxygenUtil.isAABBInBreathableAirBlock(this) && tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage() > 0)
	    	{
	    		tankInSlot2.damageItem(1, this);
	    	}

			if (drainSpacing == 0 && this.tick % 60 == 0 && !OxygenUtil.isAABBInBreathableAirBlock(this) && this.airRemaining > 0)
			{
	    		this.airRemaining -= 1;
			}

			if (drainSpacing2 == 0 && this.tick % 60 == 0 && !OxygenUtil.isAABBInBreathableAirBlock(this) && this.airRemaining2 > 0)
			{
	    		this.airRemaining2 -= 1;
			}

			if (this.airRemaining < 0)
			{
				this.airRemaining = 0;
			}

			if (this.airRemaining2 < 0)
			{
				this.airRemaining2 = 0;
			}

			if (this.tick % 60 == 0 && OxygenUtil.isAABBInBreathableAirBlock(this) && this.airRemaining < 90 && tankInSlot != null)
			{
				this.airRemaining += 1;
			}

			if (this.tick % 60 == 0 && OxygenUtil.isAABBInBreathableAirBlock(this) && this.airRemaining2 < 90 && tankInSlot2 != null)
			{
				this.airRemaining2 += 1;
			}

    		final boolean flag5 = this.airRemaining <= 0 && this.airRemaining2 <= 0;
    		
    		final boolean invalid = !OxygenUtil.hasValidOxygenSetup(this) || flag5;

    		if (invalid && !OxygenUtil.isAABBInBreathableAirBlock(this))
			{
    			this.oxygenSetupValid = false;
    			
    			if (!this.worldObj.isRemote && this.isEntityAlive())
    			{
    				if (this.damageCounter == 0)
    	        	{
        				this.damageCounter = 100;
    		            this.attackEntityFrom(GalacticraftCore.oxygenSuffocation, 2);
    	        	}
    			}
			}
    		else
    		{
    			this.oxygenSetupValid = true;
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
		
		if (this.worldObj.provider instanceof IGalacticraftWorldProvider && (this.oxygenSetupValid != this.lastOxygenSetupValid || this.tick % 100 == 0))
		{
	        this.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 27, new Object[] {Boolean.valueOf(this.oxygenSetupValid)}));
		}

		if (this.timeUntilPortal > 0)
		{
			this.timeUntilPortal--;
		}

		if (this.getParachute())
		{
			if (this.onGround)
			{
				this.setParachute(false);
			}
		}

		if (this.worldObj.provider instanceof IGalacticraftWorldProvider && FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT)
		{
			if (((IGalacticraftWorldProvider)this.worldObj.provider).getMeteorFrequency() > 0)
			{
				final double f = ((IGalacticraftWorldProvider)this.worldObj.provider).getMeteorFrequency();

				if (this.worldObj.rand.nextInt(MathHelper.floor_double(f * 1000)) == 0)
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
				if (this.worldObj.rand.nextInt(MathHelper.floor_double(f * 3000)) == 0)
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

		SchematicRegistry.addUnlockedPage(this, SchematicRegistry.getMatchingRecipeForID(0));
		SchematicRegistry.addUnlockedPage(this, SchematicRegistry.getMatchingRecipeForID(Integer.MAX_VALUE));

		Collections.sort(this.unlockedSchematics);

    	if (this.tick % 200 == 0 || (this.unlockedSchematics.size() != this.lastUnlockedSchematics.size()))
    	{
	        this.playerNetServerHandler.sendPacketToPlayer(GCCorePacketSchematicList.buildSchematicListPacket(this.unlockedSchematics));
    	}

    	this.lastMaskInSlot = this.playerTankInventory.getStackInSlot(0);
    	this.lastGearInSlot = this.playerTankInventory.getStackInSlot(1);
    	this.lastTankInSlot1 = this.playerTankInventory.getStackInSlot(2);
    	this.lastTankInSlot2 = this.playerTankInventory.getStackInSlot(3);
    	this.lastParachuteInSlot = this.playerTankInventory.getStackInSlot(4);
    	
    	this.lastOxygenSetupValid = this.oxygenSetupValid;
    	this.lastUnlockedSchematics = this.unlockedSchematics;
	}

	@Deprecated
    public void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2, Teleporter teleporter)
    {
        final int var3 = par1EntityPlayerMP.dimension;
        final WorldServer var4 = par1EntityPlayerMP.mcServer.worldServerForDimension(par1EntityPlayerMP.dimension);
        par1EntityPlayerMP.dimension = par2;
        final WorldServer var5 = par1EntityPlayerMP.mcServer.worldServerForDimension(par1EntityPlayerMP.dimension);

        GCLog.info("Server attempting to transfer player " + par1EntityPlayerMP.username + " to dimension " + var5.provider.dimensionId);

        par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(par1EntityPlayerMP.dimension, (byte)par1EntityPlayerMP.worldObj.difficultySetting, var5.getWorldInfo().getTerrainType(), var5.getHeight(), par1EntityPlayerMP.theItemInWorldManager.getGameType()));
        var4.removePlayerEntityDangerously(par1EntityPlayerMP);
        par1EntityPlayerMP.isDead = false;
        this.transferEntityToWorld(par1EntityPlayerMP, var3, var4, var5, teleporter);
        this.func_72375_a(par1EntityPlayerMP, var4);
        par1EntityPlayerMP.playerNetServerHandler.setPlayerLocation(par1EntityPlayerMP.posX, par1EntityPlayerMP.posY, par1EntityPlayerMP.posZ, par1EntityPlayerMP.rotationYaw, par1EntityPlayerMP.rotationPitch);
        par1EntityPlayerMP.theItemInWorldManager.setWorld(var5);
        this.updateTimeAndWeatherForPlayer(par1EntityPlayerMP, var5);
        this.syncPlayerInventory(par1EntityPlayerMP);
        final Iterator var6 = par1EntityPlayerMP.getActivePotionEffects().iterator();

        while (var6.hasNext())
        {
            final PotionEffect var7 = (PotionEffect)var6.next();
            par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(par1EntityPlayerMP.entityId, var7));
        }

        this.setNotUsingPlanetGui();

        GameRegistry.onPlayerChangedDimension(this);
    }

    public void updateTimeAndWeatherForPlayer(EntityPlayerMP par1EntityPlayerMP, WorldServer par2WorldServer)
    {
        par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet4UpdateTime(par2WorldServer.getTotalWorldTime(), par2WorldServer.getWorldTime()));

        if (par2WorldServer.isRaining())
        {
            par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(1, 0));
        }
    }

    public void syncPlayerInventory(EntityPlayerMP par1EntityPlayerMP)
    {
        par1EntityPlayerMP.sendContainerToPlayer(par1EntityPlayerMP.inventoryContainer);
        par1EntityPlayerMP.setPlayerHealthUpdated();
        par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet16BlockItemSwitch(par1EntityPlayerMP.inventory.currentItem));
    }

    public void func_72375_a(EntityPlayerMP par1EntityPlayerMP, WorldServer par2WorldServer)
    {
        final WorldServer var3 = par1EntityPlayerMP.getServerForPlayer();

        if (par2WorldServer != null)
        {
            par2WorldServer.getPlayerManager().removePlayer(par1EntityPlayerMP);
        }

        var3.getPlayerManager().addPlayer(par1EntityPlayerMP);
        var3.theChunkProviderServer.loadChunk((int)par1EntityPlayerMP.posX >> 4, (int)par1EntityPlayerMP.posZ >> 4);
    }

    public void transferEntityToWorld(Entity par1Entity, int par2, WorldServer par3WorldServer, WorldServer par4WorldServer, Teleporter teleporter)
    {
        final WorldProvider pOld = par3WorldServer.provider;
        final WorldProvider pNew = par4WorldServer.provider;
        final double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
        double var5 = par1Entity.posX * moveFactor;
        double var7 = par1Entity.posZ * moveFactor;
        final double var11 = par1Entity.posX;
        final double var13 = par1Entity.posY;
        final double var15 = par1Entity.posZ;
        final float var17 = par1Entity.rotationYaw;
        par3WorldServer.theProfiler.startSection("moving");

        final int x = MathHelper.floor_double(par1Entity.posX);
      	final int z = MathHelper.floor_double(par1Entity.posZ);

      	final IChunkProvider var3 = par4WorldServer.getChunkProvider();

      	var3.loadChunk(x - 3 >> 4, z - 3 >> 4);
      	var3.loadChunk(x + 3 >> 4, z - 3 >> 4);
      	var3.loadChunk(x - 3 >> 4, z + 3 >> 4);
      	var3.loadChunk(x + 3 >> 4, z + 3 >> 4);

        if (par1Entity.dimension == 1)
        {
            ChunkCoordinates var18;

            if (par2 == 1)
            {
                var18 = par4WorldServer.getSpawnPoint();
            }
            else
            {
                var18 = par4WorldServer.getEntrancePortalLocation();
            }

            var5 = var18.posX;
            par1Entity.posY = var18.posY;
            var7 = var18.posZ;
            par1Entity.setLocationAndAngles(var5, par1Entity.posY, var7, 90.0F, 0.0F);

            if (par1Entity.isEntityAlive())
            {
                par3WorldServer.updateEntityWithOptionalForce(par1Entity, false);
            }
        }

        par3WorldServer.theProfiler.endSection();

        if (par2 != 1)
        {
            par3WorldServer.theProfiler.startSection("placing");
            var5 = MathHelper.clamp_int((int)var5, -29999872, 29999872);
            var7 = MathHelper.clamp_int((int)var7, -29999872, 29999872);

            if (par1Entity.isEntityAlive())
            {
                par4WorldServer.spawnEntityInWorld(par1Entity);
                par1Entity.setLocationAndAngles(var5, par1Entity.posY, var7, par1Entity.rotationYaw, par1Entity.rotationPitch);
                par4WorldServer.updateEntityWithOptionalForce(par1Entity, false);
                teleporter.placeInPortal(par1Entity, var11, var13, var15, var17);
            }

            par3WorldServer.theProfiler.endSection();
        }

        final int var9b = MathHelper.floor_double(par1Entity.posX);
      	final int var11b = MathHelper.floor_double(par1Entity.posZ);

      	for (int i = 0; i < 27; i++)
      	{
      		if (this.rocketStacks[i] == null)
      		{
      			switch (i)
      			{
      			case 0:
      		        this.rocketStacks[i] = new ItemStack(GCCoreItems.fuelCanister, 1, this.fuelDamage);
      				break;
      			case 25:
      		      	this.rocketStacks[i] = new ItemStack(GCCoreBlocks.landingPad, 9, 0);
      				break;
      			case 26:
      		        this.rocketStacks[i] = new ItemStack(GCCoreItems.spaceship, 1, this.rocketType);
      				break;
      			}
      		}
      	}

      	if (this.chestSpawnCooldown == 0)
      	{
          	final GCCoreEntityParaChest chest = new GCCoreEntityParaChest(par4WorldServer, this.rocketStacks);

      		chest.setPosition(var9b, 260, var11b);

          	if (!par4WorldServer.isRemote)
          	{
          		par4WorldServer.spawnEntityInWorld(chest);
          	}

          	this.chestSpawnCooldown = 200;
      	}

        par1Entity.setWorld(par4WorldServer);
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
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
		this.airRemaining = par1NBTTagCompound.getInteger("playerAirRemaining");
		this.damageCounter = par1NBTTagCompound.getInteger("damageCounter");
        final NBTTagList var2 = par1NBTTagCompound.getTagList("InventoryTankRefill");
        this.playerTankInventory.readFromNBT(var2);
        this.astronomyPoints = par1NBTTagCompound.getFloat("AstronomyPointsNum");
        this.astronomyPointsLevel = par1NBTTagCompound.getInteger("AstronomyPointsLevel");
        this.astronomyPointsTotal = par1NBTTagCompound.getInteger("AstronomyPointsTotal");
        this.setParachute(par1NBTTagCompound.getBoolean("usingParachute2"));
        this.usingPlanetSelectionGui = par1NBTTagCompound.getBoolean("usingPlanetSelectionGui");
        this.teleportCooldown = par1NBTTagCompound.getInteger("teleportCooldown");
        this.coordsTeleportedFromX = par1NBTTagCompound.getDouble("coordsTeleportedFromX");
        this.coordsTeleportedFromZ = par1NBTTagCompound.getDouble("coordsTeleportedFromZ");
        this.spaceStationDimensionID = par1NBTTagCompound.getInteger("spaceStationDimensionID");

        final NBTTagList schematics = par1NBTTagCompound.getTagList("Schematics");
        SchematicRegistry.readFromNBT(this, schematics);

        if (par1NBTTagCompound.getBoolean("usingPlanetSelectionGui"))
        {
        	this.openPlanetSelectionGuiCooldown = 20;
        }

        final NBTTagList var23 = par1NBTTagCompound.getTagList("RocketItems");
        this.rocketStacks = new ItemStack[27];

        for (int var3 = 0; var3 < var23.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = (NBTTagCompound)var23.tagAt(var3);
            final int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.rocketStacks.length)
            {
                this.rocketStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        super.readEntityFromNBT(par1NBTTagCompound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	par1NBTTagCompound.setInteger("playerAirRemaining", this.airRemaining);
    	par1NBTTagCompound.setInteger("damageCounter", this.damageCounter);
        par1NBTTagCompound.setTag("InventoryTankRefill", this.playerTankInventory.writeToNBT(new NBTTagList()));
        par1NBTTagCompound.setFloat("AstronomyPointsNum", this.astronomyPoints);
        par1NBTTagCompound.setInteger("AstronomyPointsLevel", this.astronomyPointsLevel);
        par1NBTTagCompound.setInteger("AstronomyPointsTotal", this.astronomyPointsTotal);
        par1NBTTagCompound.setBoolean("usingParachute2", this.getParachute());
        par1NBTTagCompound.setBoolean("usingPlanetSelectionGui", this.usingPlanetSelectionGui);
        par1NBTTagCompound.setInteger("teleportCooldown", this.teleportCooldown);
        par1NBTTagCompound.setDouble("coordsTeleportedFromX", this.coordsTeleportedFromX);
        par1NBTTagCompound.setDouble("coordsTeleportedFromZ", this.coordsTeleportedFromZ);
        par1NBTTagCompound.setInteger("spaceStationDimensionID", this.spaceStationDimensionID);
        par1NBTTagCompound.setTag("Schematics", SchematicRegistry.writeToNBT(this, new NBTTagList()));

        final NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.rocketStacks.length; ++var3)
        {
            if (this.rocketStacks[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.rocketStacks[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("RocketItems", var2);

        super.writeEntityToNBT(par1NBTTagCompound);
    }

    public void addExperience2(int par1)
    {
        final int var2 = Integer.MAX_VALUE - this.astronomyPointsTotal;

        if (par1 > var2)
        {
            par1 = var2;
        }

        par1 /= 10;

        this.astronomyPoints += (float)par1 / (float)this.astrBarCap();

        for (this.astronomyPointsTotal += par1; this.astronomyPoints >= 1.0F; this.astronomyPoints /= this.astrBarCap())
        {
            this.astronomyPoints = (this.astronomyPoints - 1.0F) * this.astrBarCap();
            this.addAstronomyLevel(1);
        }
    }

    public void addAstronomyLevel(int par1)
    {
        this.astronomyPointsLevel += par1;

        if (this.astronomyPointsLevel < 0)
        {
            this.astronomyPointsLevel = 0;
            this.astronomyPoints = 0.0F;
            this.astronomyPointsTotal = 0;
        }
    }

    public int astrBarCap()
    {
        return this.astronomyPointsLevel >= 30 ? 62 + (this.astronomyPointsLevel - 30) * 7 : this.astronomyPointsLevel >= 15 ? 17 + (this.astronomyPointsLevel - 15) * 3 : 17;
    }

	public void sendAirRemainingPacket()
	{
		final float f1 = Float.valueOf(this.tankInSlot1 == null ? 0.0F : this.tankInSlot1.getMaxDamage() / 90.0F);
		final float f2 = Float.valueOf(this.tankInSlot2 == null ? 0.0F : this.tankInSlot2.getMaxDamage() / 90.0F);

	  	final Object[] toSend = {MathHelper.floor_float(this.airRemaining / f1), MathHelper.floor_float(this.airRemaining2 / f2), this.username};

	  	this.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 0, toSend));
	}

	public void sendGearUpdatePacket(int i)
	{
	  	final Object[] toSend = {this.username, i};

	  	if (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.username) != null)
	  	{
	  		PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 50, this.worldObj.provider.dimensionId, PacketUtil.createPacket(GalacticraftCore.CHANNEL, 10, toSend));
	  	}
	}

	public void sendParachuteRemovalPacket()
	{
	  	final Object[] toSend = {this.username};

	  	if (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.username) != null)
	  	{
	  		PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 50, this.worldObj.provider.dimensionId, PacketUtil.createPacket(GalacticraftCore.CHANNEL, 5, toSend));
	  	}
	}

	public void sendParachuteAddPacket()
	{
	  	final Object[] toSend = {this.username};

	  	if (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.username) != null)
	  	{
	          PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 50, this.worldObj.provider.dimensionId, PacketUtil.createPacket(GalacticraftCore.CHANNEL, 4, toSend));
	  	}
	}

	public void sendOxygenSetupValidPacket()
	{
	}

	public void sendPlayerParachuteTexturePacket(GCCorePlayerMP player)
	{
		final ItemStack stack = player.playerTankInventory.getStackInSlot(4);
		String s;
		String s2 = null;
		if (stack != null && stack.getItem() instanceof GCCoreItemParachute)
		{
			s = stack.getItem().getUnlocalizedName(stack);

			s2 = s.replace("item.parachute_", "");
		}

	  	final Object[] toSend = {this.username, stack == null ? "none" : String.valueOf(s2)};

	  	if (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.username) != null)
	  	{
	          PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 50, this.worldObj.provider.dimensionId, PacketUtil.createPacket(GalacticraftCore.CHANNEL, 6, toSend));
	  	}
	}

    public void setParachute(boolean tf)
    {
    	this.usingParachute = tf;

    	if (tf)
    	{
        	this.sendParachuteAddPacket();
    	}
    	else
    	{
    		this.sendParachuteRemovalPacket();
    	}
    }

    public boolean getParachute()
    {
    	return this.usingParachute;
    }

    public static enum modelUpdatePacketTypes
    {
    	ADDMASK(0),
    	REMOVEMASK(1),
    	ADDGEAR(2),
    	REMOVEGEAR(3),
    	ADDLEFTREDTANK(4),
    	REMOVELEFTREDTANK(5),
    	ADDLEFTORANGETANK(6),
    	REMOVELEFTORANGETANK(7),
    	ADDLEFTGREENTANK(8),
    	REMOVELEFTGREENTANK(9),
    	ADDRIGHTREDTANK(10),
    	REMOVERIGHTREDTANK(11),
    	ADDRIGHTORANGETANK(12),
    	REMOVERIGHTORANGETANK(13),
    	ADDRIGHTGREENTANK(14),
    	REMOVERIGHTGREENTANK(15);

    	private int index;

    	private modelUpdatePacketTypes(int index)
    	{
    		this.index = index;
    	}

    	public int getIndex()
    	{
    		return this.index;
    	}
    }
}
