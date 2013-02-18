package micdoodle8.mods.galacticraft.core.entities;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import micdoodle8.mods.galacticraft.API.GCBlockBreathableAir;
import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreTeleporter;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tile.GCCoreInventoryTankRefill;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet16BlockItemSwitch;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet4UpdateTime;
import net.minecraft.network.packet.Packet70GameEvent;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.src.ServerPlayerBase;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class GCCorePlayerBase extends ServerPlayerBase
{
	private int airRemaining;
	private int airRemaining2;
	
	public boolean hasTank;
	
	public ItemStack tankInSlot;
	
	public GCCoreInventoryTankRefill playerTankInventory = new GCCoreInventoryTankRefill(this);
	
	public boolean inPortal;
	
	public int timeUntilPortal;
	
	private final int dimensionToSend = -2;
	
	private int damageCounter;

    public int astronomyPointsLevel;

    public int astronomyPointsTotal;

    public float astronomyPoints;
    
    public ItemStack[] rocketStacks = new ItemStack[28];
    public int rocketType;
	
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
	
	private int chestSpawnCooldown;

	public GCCorePlayerBase(ServerPlayerAPI var1)
	{
		super(var1);
	}
	
	public EntityPlayerMP getPlayer()
	{
		return this.player;
	}

	@Override
    public void onDeath(DamageSource var1)
    {
		GalacticraftCore.playersServer.remove(this);

        if (!this.player.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
        {
            this.playerTankInventory.dropAllItems();
        }
		
    	super.onDeath(var1);
    }

    public boolean isAABBInBreathableAirBlock()
    {
        final int var3 = MathHelper.floor_double(this.player.boundingBox.minX);
        final int var4 = MathHelper.floor_double(this.player.boundingBox.maxX + 1.0D);
        final int var5 = MathHelper.floor_double(this.player.boundingBox.minY);
        final int var6 = MathHelper.floor_double(this.player.boundingBox.maxY + 1.0D);
        final int var7 = MathHelper.floor_double(this.player.boundingBox.minZ);
        final int var8 = MathHelper.floor_double(this.player.boundingBox.maxZ + 1.0D);

        for (int var9 = var3; var9 < var4; ++var9)
        {
            for (int var10 = var5; var10 < var6; ++var10)
            {
                for (int var11 = var7; var11 < var8; ++var11)
                {
                    final Block var12 = Block.blocksList[this.player.worldObj.getBlockId(var9, var10, var11)];

                    if (var12 != null && var12 instanceof GCBlockBreathableAir)
                    {
                        final int var13 = this.player.worldObj.getBlockMetadata(var9, var10, var11);
                        double var14 = var10 + 1;

                        if (var13 < 8)
                        {
                            var14 = var10 + 1 - var13 / 8.0D;
                        }

                        if (var14 >= this.player.boundingBox.minY)
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean isAABBInPartialBlockWithOxygenNearby()
    {
        final int var3 = MathHelper.floor_double(this.player.boundingBox.minX);
        final int var4 = MathHelper.floor_double(this.player.boundingBox.maxX + 1.0D);
        final int var5 = MathHelper.floor_double(this.player.boundingBox.minY);
        final int var6 = MathHelper.floor_double(this.player.boundingBox.maxY + 1.0D);
        final int var7 = MathHelper.floor_double(this.player.boundingBox.minZ);
        final int var8 = MathHelper.floor_double(this.player.boundingBox.maxZ + 1.0D);

        for (int x = var3; x < var4; ++x)
        {
            for (int y = var5; y < var6; ++y)
            {
                for (int z = var7; z < var8; ++z)
                {
                    final Block block = Block.blocksList[this.player.worldObj.getBlockId(x, y, z)];

                    if (block != null && !block.isOpaqueCube())
                    {
                    	final boolean changed = false;
                    	
                    	for (int x1 = x - 1; x1 < x + 2; x1++)
                    	{
                        	for (int y1 = y - 1; y1 < y + 2; y1++)
                        	{
                            	for (int z1 = z - 1; z1 < z + 2; z1++)
                            	{
                                    final Block block2 = Block.blocksList[this.player.worldObj.getBlockId(x1, y1, z1)];
                                    
                                    if (block2 instanceof GCBlockBreathableAir)
                                    {
                                    	return true;
                                    }
                            	}
                        	}
                    	}
                    	
                    	if (!changed)
                    	{
                    		return false;
                    	}
                    }
                }
            }
        }

        return false;
    }
    
	@Override
    public void onUpdate()
    {
    	super.onUpdate();
    	
    	if (!GalacticraftCore.playersServer.containsKey(this.player.username))
    	{
    		GalacticraftCore.playersServer.put(this.player.username, this);
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
    		this.player.fallDistance = 0.0F;
    	}
    	
		if (this.player.worldObj.provider instanceof IGalacticraftWorldProvider && this.player.inventory.getCurrentItem() != null)
	    {
	    	final int var1 = this.player.inventory.getCurrentItem().stackSize;
	    	final int var2 = this.player.inventory.getCurrentItem().getItemDamage();
	    	
			if (this.player.inventory.getCurrentItem().getItem().itemID == Block.torchWood.blockID)
			{
	        	final ItemStack stack = new ItemStack(GCCoreBlocks.unlitTorch, var1, 0);
	            this.player.inventory.mainInventory[this.player.inventory.currentItem] = stack;
			}
			else if (this.player.inventory.getCurrentItem().getItem().itemID == Item.bow.itemID)
			{
	        	final Hashtable<Integer, Enchantment> enchants = new Hashtable<Integer, Enchantment>();
	        	
	        	final NBTTagList list = this.player.inventory.getCurrentItem().getEnchantmentTagList();
	
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
	        	
	            this.player.inventory.mainInventory[this.player.inventory.currentItem] = stack;
			}
			else if (this.player.inventory.getCurrentItem().getItem().itemID == Block.sapling.blockID)
			{
				// No jungle trees...
				if (var2 != 3)
				{
		        	final ItemStack stack = new ItemStack(GCCoreBlocks.sapling, var1, var2);
		            this.player.inventory.mainInventory[this.player.inventory.currentItem] = stack;
				}
			}
	    }
	    else if (!(this.player.worldObj.provider instanceof IGalacticraftWorldProvider) && this.player.inventory.getCurrentItem() != null)
	    {
	    	final int var1 = this.player.inventory.getCurrentItem().stackSize;
	    	final int var2 = this.player.inventory.getCurrentItem().getItemDamage();
	    	
	    	if (this.player.inventory.getCurrentItem().getItem().itemID == GCCoreBlocks.unlitTorch.blockID)
	    	{
	        	final ItemStack stack = new ItemStack(Block.torchWood, var1, 0);
	            this.player.inventory.mainInventory[this.player.inventory.currentItem] = stack;
	    	}
	    	else if (this.player.inventory.getCurrentItem().getItem().itemID == GCCoreBlocks.sapling.blockID)
	    	{
	        	final ItemStack stack = new ItemStack(Block.sapling, var1, var2);
	            this.player.inventory.mainInventory[this.player.inventory.currentItem] = stack;
	    	}
	    	else if (this.player.inventory.getCurrentItem().getItem().itemID == GCCoreItems.gravityBow.itemID)
	    	{
	        	final Hashtable<Integer, Enchantment> enchants = new Hashtable<Integer, Enchantment>();
	        	
	        	final NBTTagList list = this.player.inventory.getCurrentItem().getEnchantmentTagList();
	
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
	        	
	            this.player.inventory.mainInventory[this.player.inventory.currentItem] = stack;
	    	}
	    }
		
		if (!this.hasOpenedPlanetSelectionGui && this.openPlanetSelectionGuiCooldown == 1)
		{
        	final Integer[] ids = DimensionManager.getStaticDimensionIDs();
	    	
	    	final Set set = GCCoreUtil.getArrayOfPossibleDimensions(ids).entrySet();
	    	final Iterator i = set.iterator();
	    	
	    	String temp = "";
	    	
	    	for (int k = 0; i.hasNext(); k++)
	    	{
	    		final Map.Entry entry = (Map.Entry)i.next();
	    		temp = k == 0 ? temp.concat(String.valueOf(entry.getKey())) : temp.concat("." + String.valueOf(entry.getKey()));
	    	}

	        Object[] toSend = {this.getPlayer().username, temp};
	        
	        this.getPlayer().playerNetServerHandler.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 2, toSend));
			
	        this.setUsingPlanetGui();
	        this.hasOpenedPlanetSelectionGui = true;
		}
		
		if (this.damageCounter > 0)
		{
			this.damageCounter--;
		}
	    
		if (GalacticraftCore.tick % 30 == 0)
		{
			this.sendAirRemainingPacket();
			
			if (this.player.onGround)
			{
				this.sendParachuteRemovalPacket();
				this.setParachute(false);
			}
		}
		
		if (this.player.onGround && this.getParachute())
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
			if (this.tankInSlot1.getItem().itemID == GCCoreItems.lightOxygenTankFull.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTGREENTANK.getIndex());
			}
			else if (this.tankInSlot1.getItem().itemID == GCCoreItems.medOxygenTankFull.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTORANGETANK.getIndex());
			}
			else if (this.tankInSlot1.getItem().itemID == GCCoreItems.heavyOxygenTankFull.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTREDTANK.getIndex());
			}
		}
		
		if (this.tankInSlot1 == null && this.lastTankInSlot1 != null)
		{
			if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.lightOxygenTankFull.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTGREENTANK.getIndex());
			}
			else if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.medOxygenTankFull.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTORANGETANK.getIndex());
			}
			else if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.heavyOxygenTankFull.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTREDTANK.getIndex());
			}
		}
		
		if (this.tankInSlot1 != null && this.lastTankInSlot1 != null)
		{
			if (this.tankInSlot1.getItem().itemID != this.lastTankInSlot1.getItem().itemID)
			{
				if (this.tankInSlot1.getItem().itemID == GCCoreItems.lightOxygenTankFull.itemID)
				{
					if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.medOxygenTankFull.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTORANGETANK.getIndex());
					}
					else if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.heavyOxygenTankFull.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTREDTANK.getIndex());
					}
					
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTGREENTANK.getIndex());
				}
				else if (this.tankInSlot1.getItem().itemID == GCCoreItems.medOxygenTankFull.itemID)
				{
					if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.lightOxygenTankFull.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTGREENTANK.getIndex());
					}
					else if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.heavyOxygenTankFull.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTREDTANK.getIndex());
					}
					
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTORANGETANK.getIndex());
				}
				else if (this.tankInSlot1.getItem().itemID == GCCoreItems.heavyOxygenTankFull.itemID)
				{
					if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.lightOxygenTankFull.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVELEFTGREENTANK.getIndex());
					}
					else if (this.lastTankInSlot1.getItem().itemID == GCCoreItems.medOxygenTankFull.itemID)
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
			if (this.tankInSlot2.getItem().itemID == GCCoreItems.lightOxygenTankFull.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTGREENTANK.getIndex());
			}
			else if (this.tankInSlot2.getItem().itemID == GCCoreItems.medOxygenTankFull.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTORANGETANK.getIndex());
			}
			else if (this.tankInSlot2.getItem().itemID == GCCoreItems.heavyOxygenTankFull.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTREDTANK.getIndex());
			}
		}
		
		if (this.tankInSlot2 == null && this.lastTankInSlot2 != null)
		{
			if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.lightOxygenTankFull.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTGREENTANK.getIndex());
			}
			else if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.medOxygenTankFull.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTORANGETANK.getIndex());
			}
			else if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.heavyOxygenTankFull.itemID)
			{
				this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTREDTANK.getIndex());
			}
		}
		
		if (this.tankInSlot2 != null && this.lastTankInSlot2 != null)
		{
			if (this.tankInSlot2.getItem().itemID != this.lastTankInSlot2.getItem().itemID)
			{
				if (this.tankInSlot2.getItem().itemID == GCCoreItems.lightOxygenTankFull.itemID)
				{
					if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.medOxygenTankFull.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTORANGETANK.getIndex());
					}
					else if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.heavyOxygenTankFull.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTREDTANK.getIndex());
					}
					
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTGREENTANK.getIndex());
				}
				else if (this.tankInSlot2.getItem().itemID == GCCoreItems.medOxygenTankFull.itemID)
				{
					if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.lightOxygenTankFull.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTGREENTANK.getIndex());
					}
					else if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.heavyOxygenTankFull.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTREDTANK.getIndex());
					}
					
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTORANGETANK.getIndex());
				}
				else if (this.tankInSlot2.getItem().itemID == GCCoreItems.heavyOxygenTankFull.itemID)
				{
					if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.lightOxygenTankFull.itemID)
					{
						this.sendGearUpdatePacket(modelUpdatePacketTypes.REMOVERIGHTGREENTANK.getIndex());
					}
					else if (this.lastTankInSlot2.getItem().itemID == GCCoreItems.medOxygenTankFull.itemID)
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

		if (GalacticraftCore.tick % 30 == 0)
		{
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
				
				if (id == GCCoreItems.lightOxygenTankFull.itemID)
				{
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTGREENTANK.getIndex());
				}
				else if (id == GCCoreItems.medOxygenTankFull.itemID)
				{
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTORANGETANK.getIndex());
				}
				else if (id == GCCoreItems.heavyOxygenTankFull.itemID)
				{
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDLEFTREDTANK.getIndex());
				}
			}
			
			if (this.tankInSlot2 != null)
			{
				id = this.tankInSlot2.itemID;
				
				if (id == GCCoreItems.lightOxygenTankFull.itemID)
				{
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTGREENTANK.getIndex());
				}
				else if (id == GCCoreItems.medOxygenTankFull.itemID)
				{
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTORANGETANK.getIndex());
				}
				else if (id == GCCoreItems.heavyOxygenTankFull.itemID)
				{
					this.sendGearUpdatePacket(modelUpdatePacketTypes.ADDRIGHTREDTANK.getIndex());
				}
			}
		}
		
		if (this.launchAttempts > 0 && this.player.ridingEntity == null)
		{
			this.launchAttempts = 0;
		}
		
		if (this.player != null && this.player.worldObj.provider instanceof IGalacticraftWorldProvider)
		{
			this.player.fallDistance = 0.0F;
		}
	
		final ItemStack tankInSlot = this.playerTankInventory.getStackInSlot(2);
		final ItemStack tankInSlot2 = this.playerTankInventory.getStackInSlot(3);
		
		final int drainSpacing = GCCoreUtil.getDrainSpacing(tankInSlot);
		final int drainSpacing2 = GCCoreUtil.getDrainSpacing(tankInSlot2);
					
		if (this.player.worldObj.provider instanceof IGalacticraftWorldProvider && !this.player.capabilities.isCreativeMode)
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
	    		this.airRemaining = tankInSlot.getMaxDamage() - tankInSlot.getItemDamage() % 90;
			}
			
			if (drainSpacing2 > 0)
			{
	    		this.airRemaining2 = tankInSlot.getMaxDamage() - tankInSlot2.getItemDamage() % 90;
			}
			
			if (drainSpacing > 0 && GalacticraftCore.tick % drainSpacing == 0 && !this.isAABBInPartialBlockWithOxygenNearby() && !this.isAABBInBreathableAirBlock() && tankInSlot.getMaxDamage() - tankInSlot.getItemDamage() % 90 > 0)
	    	{
	    		tankInSlot.damageItem(1, this.player);
	    	}
			
			if (drainSpacing2 > 0 && GalacticraftCore.tick % drainSpacing2 == 0 && !this.isAABBInPartialBlockWithOxygenNearby() && !this.isAABBInBreathableAirBlock() && tankInSlot.getMaxDamage() - tankInSlot.getItemDamage() % 90 > 0)
	    	{
	    		tankInSlot2.damageItem(1, this.player);
	    	}
			
			if (drainSpacing == 0 && GalacticraftCore.tick % 60 == 0 && !this.isAABBInPartialBlockWithOxygenNearby() && !this.isAABBInBreathableAirBlock() && this.airRemaining > 0)
			{
	    		this.airRemaining -= 1;
			}
			
			if (drainSpacing2 == 0 && GalacticraftCore.tick % 60 == 0 && !this.isAABBInPartialBlockWithOxygenNearby() && !this.isAABBInBreathableAirBlock() && this.airRemaining2 > 0)
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
			
			if (GalacticraftCore.tick % 60 == 0 && (this.isAABBInBreathableAirBlock() ||  this.isAABBInPartialBlockWithOxygenNearby()) && this.airRemaining < 90 && tankInSlot != null)
			{
				this.airRemaining += 1;
			}
			
			if (GalacticraftCore.tick % 60 == 0 && (this.isAABBInBreathableAirBlock() ||  this.isAABBInPartialBlockWithOxygenNearby()) && this.airRemaining2 < 90 && tankInSlot2 != null)
			{
				this.airRemaining2 += 1;
			}
			
	    	if (this.damageCounter == 0)
	    	{
	    		ItemStack helmetSlot = null;
	    		
	    		if (this.player.inventory.armorItemInSlot(3) != null)
	    		{
	    			helmetSlot = this.player.inventory.armorItemInSlot(3);
	    		}
	    		
	    		final boolean flag5 = this.airRemaining <= 0 || this.airRemaining2 <= 0;
	    		final boolean invalid = !GCCoreUtil.hasValidOxygenSetup(this.player) || flag5;
	    		
	    		if (invalid && !this.isAABBInPartialBlockWithOxygenNearby() && !this.isAABBInBreathableAirBlock())
				{
	    			if (!this.player.worldObj.isRemote && this.player.isEntityAlive())
	    			{
	    				if (this.damageCounter == 0)
	    	        	{
	        				this.damageCounter = 100;
	    		            this.player.attackEntityFrom(DamageSource.inWall, 2);
	    	        	}
	    			}
				}
			}
	    }
		else if (GalacticraftCore.tick % 20 == 0 && !this.player.capabilities.isCreativeMode && this.airRemaining < 90)
		{
			this.airRemaining += 1;
			this.airRemaining2 += 1;
		}
		else if (this.player.capabilities.isCreativeMode)
		{
			this.airRemaining = 90;
			this.airRemaining2 = 90;
		}
		
		if (this.timeUntilPortal > 0)
		{
			this.timeUntilPortal--;
		}
		
		if (this.getParachute())
		{
			if (this.getPlayer().onGround)
			{
				this.setParachute(false);
			}
		}
		
		if (this.inPortal && this.timeUntilPortal == 0)
		{
			if (this.player instanceof EntityPlayerMP && !this.player.worldObj.isRemote)
			{
				final EntityPlayerMP player = this.player;
				
		    	byte var5;
		    	
		        if (this.dimensionToSend != -2)
		        {
		        	var5 = (byte) this.dimensionToSend;
		        }
		        else
		        {
		        	var5 = 0;
		        }
		
				for (int i = 0; i < player.mcServer.worldServerForDimension(var5).customTeleporters.size(); i++)
				{
					if (player.mcServer.worldServerForDimension(var5).customTeleporters.get(i) instanceof GCCoreTeleporter)
					{
		    			this.transferPlayerToDimension(player, var5, player.mcServer.worldServerForDimension(var5).customTeleporters.get(i));
					}
				}
				
		        player.timeUntilPortal = 10;

		        Object[] toSend = {0.0F};
		        
		        PacketDispatcher.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 1, toSend), (Player)player);
		        
		        this.inPortal = false;
			}
		}
		
		if (this.player.worldObj.provider instanceof IGalacticraftWorldProvider && FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT)
		{
			if (((IGalacticraftWorldProvider)this.player.worldObj.provider).getMeteorFrequency() > 0)
			{
				final float f = ((IGalacticraftWorldProvider)this.player.worldObj.provider).getMeteorFrequency();
				
				if (this.player.worldObj.rand.nextInt(MathHelper.floor_float(f * 1000)) == 0)
				{
					int x, y, z;
					double motX, motZ;
					x = this.player.worldObj.rand.nextInt(20) - 10;
					y = this.player.worldObj.rand.nextInt(20) + 200;
					z = this.player.worldObj.rand.nextInt(20) - 10;
					motX = this.player.worldObj.rand.nextDouble() * 5;
					motZ = this.player.worldObj.rand.nextDouble() * 5;
					
					final GCCoreEntityMeteor meteor = new GCCoreEntityMeteor(this.player.worldObj, this.player.posX + x, this.player.posY + y, this.player.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 1);
					
					if (!this.player.worldObj.isRemote)
					{
						this.player.worldObj.spawnEntityInWorld(meteor);
					}
				}
				if (this.player.worldObj.rand.nextInt(MathHelper.floor_float(f * 3000)) == 0)
				{
					int x, y, z;
					double motX, motZ;
					x = this.player.worldObj.rand.nextInt(20) - 10;
					y = this.player.worldObj.rand.nextInt(20) + 200;
					z = this.player.worldObj.rand.nextInt(20) - 10;
					motX = this.player.worldObj.rand.nextDouble() * 5;
					motZ = this.player.worldObj.rand.nextDouble() * 5;
					
					final GCCoreEntityMeteor meteor = new GCCoreEntityMeteor(this.player.worldObj, this.player.posX + x, this.player.posY + y, this.player.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 6);
					
					if (!this.player.worldObj.isRemote)
					{
						this.player.worldObj.spawnEntityInWorld(meteor);
					}
				}
			}
		}

    	this.lastMaskInSlot = this.playerTankInventory.getStackInSlot(0);
    	this.lastGearInSlot = this.playerTankInventory.getStackInSlot(1);
    	this.lastTankInSlot1 = this.playerTankInventory.getStackInSlot(2);
    	this.lastTankInSlot2 = this.playerTankInventory.getStackInSlot(3);
    	this.lastParachuteInSlot = this.playerTankInventory.getStackInSlot(4);
	}

    public void transferPlayerToDimension(EntityPlayerMP par1EntityPlayerMP, int par2, Teleporter teleporter)
    {
        final int var3 = par1EntityPlayerMP.dimension;
        final WorldServer var4 = par1EntityPlayerMP.mcServer.worldServerForDimension(par1EntityPlayerMP.dimension);
        par1EntityPlayerMP.dimension = par2;
        final WorldServer var5 = par1EntityPlayerMP.mcServer.worldServerForDimension(par1EntityPlayerMP.dimension);

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

        GameRegistry.onPlayerChangedDimension(this.player);
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
      
        this.rocketStacks[26] = new ItemStack(GCCoreItems.spaceship, 1, this.rocketType);
        this.rocketStacks[25] = new ItemStack(GCCoreItems.rocketFuelBucket, 1, 0);
      	this.rocketStacks[24] = new ItemStack(GCCoreBlocks.landingPad, 9, 0);
      
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
//        WorldProvider pOld = par3WorldServer.provider;
//        WorldProvider pNew = par4WorldServer.provider;
//        double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
//        double var5 = par1Entity.posX * moveFactor;
//        double var7 = par1Entity.posZ * moveFactor;
//        double var11 = par1Entity.posX;
//        double var13 = par1Entity.posY;
//        double var15 = par1Entity.posZ;
//        float var17 = par1Entity.rotationYaw;
//        par3WorldServer.theProfiler.startSection("moving");
//
//        final int x = MathHelper.floor_double(par1Entity.posX);
//        final int z = MathHelper.floor_double(par1Entity.posZ);
//
//        IChunkProvider var3 = par4WorldServer.getChunkProvider();
//
//        var3.loadChunk(x - 3 >> 4, z - 3 >> 4);
//        var3.loadChunk(x + 3 >> 4, z - 3 >> 4);
//        var3.loadChunk(x - 3 >> 4, z + 3 >> 4);
//        var3.loadChunk(x + 3 >> 4, z + 3 >> 4);
//
//        if (par1Entity.dimension == 1)
//        {
//            ChunkCoordinates var18;
//
//            if (par2 == 1)
//            {
//                var18 = par4WorldServer.getSpawnPoint();
//            }
//            else
//            {
//                var18 = par4WorldServer.getEntrancePortalLocation();
//            }
//
//            var5 = var18.posX;
//            par1Entity.posY = var18.posY;
//            var7 = var18.posZ;
//            par1Entity.setLocationAndAngles(var5, par1Entity.posY, var7, 90.0F, 0.0F);
//
//            if (par1Entity.isEntityAlive())
//            {
//                par3WorldServer.updateEntityWithOptionalForce(par1Entity, false);
//            }
//        }
//
//        par3WorldServer.theProfiler.endSection();
//
//        if (par2 != 1)
//        {
//            par3WorldServer.theProfiler.startSection("placing");
//            var5 = MathHelper.clamp_int((int)var5, -29999872, 29999872);
//            var7 = MathHelper.clamp_int((int)var7, -29999872, 29999872);
//
//            if (!par1Entity.worldObj.isRemote && par1Entity.isEntityAlive())
//            {
//                par4WorldServer.spawnEntityInWorld(par1Entity);
//                par1Entity.setLocationAndAngles(var5, par1Entity.posY, var7, par1Entity.rotationYaw, par1Entity.rotationPitch);
//                par4WorldServer.updateEntityWithOptionalForce(par1Entity, false);
//
//                if (teleporter instanceof GCCoreTeleporter)
//                {
//                    ((GCCoreTeleporter) teleporter).placeInPortal(par1Entity, var11, var13, var15, var17);
//                }
//            }
//
//            par3WorldServer.theProfiler.endSection();
//        }
//
//        par1Entity.setWorld(par4WorldServer);
//
//        final int var9b = MathHelper.floor_double(par1Entity.posX);
//        final int var11b = MathHelper.floor_double(par1Entity.posZ);
//
//        boolean changed = false;
//
//        this.rocketStacks[26] = new ItemStack(GCCoreItems.spaceship, 1, this.rocketType);
//        this.rocketStacks[25] = new ItemStack(GCCoreItems.rocketFuelBucket, 1, 0);
//        this.rocketStacks[24] = new ItemStack(GCCoreBlocks.landingPad, 9, 0);
//
//        GCCoreEntityParaChest chest = new GCCoreEntityParaChest(par1Entity.worldObj, this.rocketStacks);
//
//    	chest.setPosition(var9b, 260, var11b);
//
//        if (!par4WorldServer.isRemote)
//        {
//        	par4WorldServer.spawnEntityInWorld(chest);
//        }
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
        
        if (par1NBTTagCompound.getBoolean("usingPlanetSelectionGui"))
        {
        	this.openPlanetSelectionGuiCooldown = 20;
        }

        final NBTTagList var23 = par1NBTTagCompound.getTagList("RocketItems");
        this.rocketStacks = new ItemStack[28];

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
	  	final Object[] toSend = {this.airRemaining, this.airRemaining2, this.player.username};
	  	
	  	if (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.player.username) != null)
	  	{
	          FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.player.username).playerNetServerHandler.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 0, toSend));
	  	}
	}
	  
	public void sendGearUpdatePacket(int i)
	{
	  	final Object[] toSend = {this.player.username, i};
	  	
	  	if (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.player.username) != null)
	  	{
	          PacketDispatcher.sendPacketToAllPlayers(GCCoreUtil.createPacket("Galacticraft", 10, toSend));
	  	}
	}
	  
	public void sendParachuteRemovalPacket()
	{
	  	final Object[] toSend = {this.player.username};
	  	
	  	if (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.player.username) != null)
	  	{
	          PacketDispatcher.sendPacketToAllPlayers(GCCoreUtil.createPacket("Galacticraft", 5, toSend));
	  	}
	}
	  
	public void sendParachuteAddPacket()
	{
	  	final Object[] toSend = {this.player.username};
	  	
	  	if (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.player.username) != null)
	  	{
	          PacketDispatcher.sendPacketToAllPlayers(GCCoreUtil.createPacket("Galacticraft", 4, toSend));
	  	}
	}
	  
	public void sendPlayerParachuteTexturePacket(GCCorePlayerBase player)
	{
		final ItemStack stack = player.playerTankInventory.getStackInSlot(4);
		String s;
		String s2 = null;
		if (stack != null && stack.getItem() instanceof GCCoreItemParachute)
		{
			s = stack.getItem().getItemNameIS(stack);

			s2 = s.replace("item.parachute.", "");
		}
		
	  	final Object[] toSend = {this.player.username, stack == null ? "none" : String.valueOf(s2)};
	  	
	  	if (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.player.username) != null)
	  	{
	          PacketDispatcher.sendPacketToAllPlayers(GCCoreUtil.createPacket("Galacticraft", 6, toSend));
	  	}
	}

    public void travelToTheEnd(int par1)
    {
    	if (this.player instanceof EntityPlayerMP)
    	{
    		final EntityPlayerMP player = this.player;
    		for (int i = 0; i < player.mcServer.worldServerForDimension(par1).customTeleporters.size(); i++)
    		{
    			if (player.mcServer.worldServerForDimension(par1).customTeleporters.get(i) instanceof GCCoreTeleporter)
    			{
        			this.transferPlayerToDimension(player, par1, player.mcServer.worldServerForDimension(par1).customTeleporters.get(i));
    			}
    		}
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
