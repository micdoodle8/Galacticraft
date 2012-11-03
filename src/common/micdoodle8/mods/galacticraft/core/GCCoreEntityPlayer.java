package micdoodle8.mods.galacticraft.core;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import micdoodle8.mods.galacticraft.moon.GCMoonWorldProvider;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EntityDamageSource;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class GCCoreEntityPlayer
{
	private EntityPlayer currentPlayer;
	
	private int airRemaining;
	
	public boolean hasTank;
	
	public ItemStack tankInSlot;
	
	public GCCoreInventoryTankRefill playerTankInventory = new GCCoreInventoryTankRefill();
	
	public boolean inPortal;
	
	public boolean inSpaceship;
	
	public int timeUntilPortal;
	
	private int dimensionToSend = -2;
	
	private int damageCounter;
	
	public GCCoreEntityPlayer(EntityPlayer player) 
	{
		this.currentPlayer = player;
		GalacticraftCore.instance.players.add(player);
		GalacticraftCore.instance.gcPlayers.add(this);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public EntityPlayer getPlayer()
	{
		return this.currentPlayer;
	}

    public boolean isAABBInBreathableAirBlock()
    {
        int var3 = MathHelper.floor_double(this.currentPlayer.boundingBox.minX);
        int var4 = MathHelper.floor_double(this.currentPlayer.boundingBox.maxX + 1.0D);
        int var5 = MathHelper.floor_double(this.currentPlayer.boundingBox.minY);
        int var6 = MathHelper.floor_double(this.currentPlayer.boundingBox.maxY + 1.0D);
        int var7 = MathHelper.floor_double(this.currentPlayer.boundingBox.minZ);
        int var8 = MathHelper.floor_double(this.currentPlayer.boundingBox.maxZ + 1.0D);

        for (int var9 = var3; var9 < var4; ++var9)
        {
            for (int var10 = var5; var10 < var6; ++var10)
            {
                for (int var11 = var7; var11 < var8; ++var11)
                {
                    Block var12 = Block.blocksList[this.currentPlayer.worldObj.getBlockId(var9, var10, var11)];

                    if (var12 != null && var12.blockID == GCCoreBlocks.breatheableAir.blockID)
                    {
                        int var13 = this.currentPlayer.worldObj.getBlockMetadata(var9, var10, var11);
                        double var14 = var10 + 1;

                        if (var13 < 8)
                        {
                            var14 = var10 + 1 - var13 / 8.0D;
                        }

                        if (var14 >= this.currentPlayer.boundingBox.minY)
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
	
	@ForgeSubscribe
	public void onUpdate(LivingEvent event)
	{
		if (event.entityLiving instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
			
			if (player != null)
			{
				player.fallDistance = 0;
			}
			
			if (player.worldObj.provider instanceof GalacticraftWorldProvider && player.inventory.getCurrentItem() != null)
	        {
	        	int var1 = player.inventory.getCurrentItem().stackSize;
	        	int var2 = player.inventory.getCurrentItem().getItemDamage();
	        	
				if (player.inventory.getCurrentItem().getItem().shiftedIndex == Block.torchWood.blockID)
				{
		        	ItemStack stack = new ItemStack(GCCoreBlocks.unlitTorch, var1, 0);
		            player.inventory.mainInventory[player.inventory.currentItem] = stack;
				}
				else if (player.inventory.getCurrentItem().getItem().shiftedIndex == Item.bow.shiftedIndex)
				{
		        	Hashtable<Integer, Enchantment> enchants = new Hashtable<Integer, Enchantment>();
		        	
		        	NBTTagList list = player.inventory.getCurrentItem().getEnchantmentTagList();

		        	if (list != null)
		            {
		                for (int var7 = 0; var7 < list.tagCount(); ++var7)
		                {
		                    int enchID = Integer.valueOf(((NBTTagCompound)list.tagAt(var7)).getShort("id"));
		                    int enchLvl = Integer.valueOf(((NBTTagCompound)list.tagAt(var7)).getShort("lvl"));
		                    
		                    Enchantment e = Enchantment.enchantmentsList[enchID];
		                    
		                    enchants.put(enchLvl, e);
		                }
		            }
		        	
		        	ItemStack stack = new ItemStack(GCCoreItems.gravityBow, var1, var2);
		        	
		        	Iterator<Entry<Integer, Enchantment>> it = enchants.entrySet().iterator();
		        	
		        	while (it.hasNext())
		        	{
		        		Entry<Integer, Enchantment> entry = it.next();
		        		
		        		if (entry.getKey() != null && entry.getValue() != null)
		        		{
			        		stack.addEnchantment((Enchantment)entry.getValue(), entry.getKey());
		        		}
		        	}
		        	
		            player.inventory.mainInventory[player.inventory.currentItem] = stack;
				}
				else if (player.inventory.getCurrentItem().getItem().shiftedIndex == Block.sapling.blockID)
				{
					// No jungle trees...
					if (var2 != 3)
					{
			        	ItemStack stack = new ItemStack(GCCoreBlocks.sapling, var1, var2);
			            player.inventory.mainInventory[player.inventory.currentItem] = stack;
					}
				}
	        }
	        else if (!(player.worldObj.provider instanceof GalacticraftWorldProvider) && player.inventory.getCurrentItem() != null)
	        {
	        	int var1 = player.inventory.getCurrentItem().stackSize;
	        	int var2 = player.inventory.getCurrentItem().getItemDamage();
	        	
	        	if (player.inventory.getCurrentItem().getItem().shiftedIndex == GCCoreBlocks.unlitTorch.blockID)
	        	{
	            	ItemStack stack = new ItemStack(Block.torchWood, var1, 0);
	                player.inventory.mainInventory[player.inventory.currentItem] = stack;
	        	}
	        	else if (player.inventory.getCurrentItem().getItem().shiftedIndex == GCCoreBlocks.sapling.blockID)
	        	{
	            	ItemStack stack = new ItemStack(Block.sapling, var1, var2);
	                player.inventory.mainInventory[player.inventory.currentItem] = stack;
	        	}
	        	else if (player.inventory.getCurrentItem().getItem().shiftedIndex == GCCoreItems.gravityBow.shiftedIndex)
	        	{
		        	Hashtable<Integer, Enchantment> enchants = new Hashtable<Integer, Enchantment>();
		        	
		        	NBTTagList list = player.inventory.getCurrentItem().getEnchantmentTagList();

		        	if (list != null)
		            {
		                for (int var7 = 0; var7 < list.tagCount(); ++var7)
		                {
		                    int enchID = Integer.valueOf(((NBTTagCompound)list.tagAt(var7)).getShort("id"));
		                    int enchLvl = Integer.valueOf(((NBTTagCompound)list.tagAt(var7)).getShort("lvl"));
		                    
		                    Enchantment e = Enchantment.enchantmentsList[enchID];
		                    
		                    enchants.put(enchLvl, e);
		                }
		            }
		        	
	            	ItemStack stack = new ItemStack(Item.bow, var1, var2);
		        	
		        	Iterator<Entry<Integer, Enchantment>> it = enchants.entrySet().iterator();
		        	
		        	while (it.hasNext())
		        	{
		        		Entry<Integer, Enchantment> entry = it.next();
		        		
		        		if (entry.getKey() != null && entry.getValue() != null)
		        		{
			        		stack.addEnchantment((Enchantment)entry.getValue(), entry.getKey());
		        		}
		        	}
		        	
	                player.inventory.mainInventory[player.inventory.currentItem] = stack;
	        	}
	        }
			
			if (this.damageCounter > 0)
			{
				this.damageCounter--;
			}
	        
			if (GalacticraftCore.instance.tick % 10 == 0)
			{
				sendAirRemainingPacket();
			}

			ItemStack tankInSlot = playerTankInventory.getStackInSlot(0);
			
			int drainSpacing = GCCoreUtil.getDrainSpacing(tankInSlot);
						
			if (player.worldObj.provider instanceof GalacticraftWorldProvider && !player.capabilities.isCreativeMode)
	        {
				if (tankInSlot == null)
				{
					this.airRemaining = 0;
				}
				
				if (drainSpacing > 0)
				{
		    		this.airRemaining = 90 - tankInSlot.getItemDamage();
				}
				
				if (drainSpacing > 0 && GalacticraftCore.instance.tick % drainSpacing == 0 && !isAABBInBreathableAirBlock() && (90 - tankInSlot.getItemDamage()) > 0) 
		    	{
		    		tankInSlot.damageItem(1, player);
		    	}
				
				if (drainSpacing == 0 && GalacticraftCore.instance.tick % 20 == 0 && !isAABBInBreathableAirBlock() && this.airRemaining > 0)
				{
		    		this.airRemaining -= 1;
				}
				
				if (this.airRemaining < 0)
				{
					this.airRemaining = 0;
				}
				
				if (GalacticraftCore.instance.tick % 20 == 0 && isAABBInBreathableAirBlock() && this.airRemaining < 90 && tankInSlot != null)
				{
					this.airRemaining += 1;
				}
				
	        	if (damageCounter == 0) 
	        	{
	        		ItemStack helmetSlot = null;
	        		
	        		if (player.inventory.armorItemInSlot(3) != null)
	        		{
	        			helmetSlot = player.inventory.armorItemInSlot(3);
	        		}
	        		
	        		boolean flag = helmetSlot == null;
	        		boolean flag2 = helmetSlot != null && !(helmetSlot.getItem() instanceof GCCoreItemBreathableHelmet);
	        		boolean flag3 = helmetSlot != null && helmetSlot.getItem() instanceof GCCoreItemSensorGlasses && !((GCCoreItemSensorGlasses)helmetSlot.getItem()).attachedMask;
	        		boolean flag4 = helmetSlot != null && helmetSlot.getItem() instanceof GCCoreItemArmor && !((GCCoreItemArmor)helmetSlot.getItem()).attachedMask == true;
	        		boolean flag5 = this.airRemaining <= 0;
	        		boolean b = flag || flag2 || flag3 || flag4 || flag5;
	        		
	        		if (b && !isAABBInBreathableAirBlock()) 
					{
	        			if (!player.worldObj.isRemote && player.isEntityAlive())
	        			{
	        				if (this.damageCounter == 0) 
	        	        	{
		        				this.damageCounter = 100;
	        		            this.getPlayer().attackEntityFrom(DamageSource.inWall, 2);
	        	        	}
	        			}
					}
				}
	        }
			else if (GalacticraftCore.instance.tick % 20 == 0 && !player.capabilities.isCreativeMode && this.airRemaining < 90)
			{
				this.airRemaining += 1;
			}
			else if (player.capabilities.isCreativeMode)
			{
				this.airRemaining = 90;
			}
		}
		
		if (!this.inSpaceship && this.currentPlayer.ridingEntity != null && this.currentPlayer.ridingEntity instanceof GCCoreEntitySpaceship)
		{
			this.currentPlayer.getEntityData().setBoolean("inSpaceship", true);
		}
		
//		if (inSpaceship)
//		{
//			FMLLog.info("in");
//			GalacticraftCore.proxy.displayChoosePlanetGui();
//			this.inSpaceship = false;
//		}
		
		if (this.timeUntilPortal > 0)
		{
			this.timeUntilPortal--;
		}
		
		if (this.inPortal && timeUntilPortal == 0)
        {
			if (this.currentPlayer instanceof EntityPlayerMP && !this.currentPlayer.worldObj.isRemote)
			{
				EntityPlayerMP player = (EntityPlayerMP) this.currentPlayer;
				
	        	byte var5;
	        	
	            if (this.dimensionToSend != -2)
	            {
	            	var5 = (byte) this.dimensionToSend;
	            }
	            else
	            {
	            	var5 = 0;
	            }

	            player.mcServer.getConfigurationManager().transferPlayerToDimension(player, var5, new GCCoreTeleporter());
	            player.timeUntilPortal = 10;
	            
	            Object[] toSend = {0.0F};
	            PacketDispatcher.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 1, toSend), (Player)player);
	            
	            this.inPortal = false;
			}
        }
		
		if (this.currentPlayer.worldObj.provider instanceof GCMoonWorldProvider && FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT)
		{
			if (this.currentPlayer.worldObj.rand.nextInt(7000) == 0)
			{
				int x, y, z;
				double motX, motZ;
				x = this.currentPlayer.worldObj.rand.nextInt(20) - 10;
				y = this.currentPlayer.worldObj.rand.nextInt(20) + 200;
				z = this.currentPlayer.worldObj.rand.nextInt(20) - 10;
				motX = this.currentPlayer.worldObj.rand.nextDouble() * 5;
				motZ = this.currentPlayer.worldObj.rand.nextDouble() * 5;
				
				GCCoreEntityMeteor meteor = new GCCoreEntityMeteor(this.currentPlayer.worldObj, this.currentPlayer.posX + x, this.currentPlayer.posY + y, this.currentPlayer.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 1);
				
				if (!this.currentPlayer.worldObj.isRemote)
				{
					this.currentPlayer.worldObj.spawnEntityInWorld(meteor);
				}
			}
			if (this.currentPlayer.worldObj.rand.nextInt(20000) == 0)
			{
				int x, y, z;
				double motX, motZ;
				x = this.currentPlayer.worldObj.rand.nextInt(20) - 10;
				y = this.currentPlayer.worldObj.rand.nextInt(20) + 200;
				z = this.currentPlayer.worldObj.rand.nextInt(20) - 10;
				motX = this.currentPlayer.worldObj.rand.nextDouble() * 5;
				motZ = this.currentPlayer.worldObj.rand.nextDouble() * 5;
				
				GCCoreEntityMeteor meteor = new GCCoreEntityMeteor(this.currentPlayer.worldObj, this.currentPlayer.posX + x, this.currentPlayer.posY + y, this.currentPlayer.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 6);
				
				if (!this.currentPlayer.worldObj.isRemote)
				{
					this.currentPlayer.worldObj.spawnEntityInWorld(meteor);
				}
			}
		}
	}

    public void travelToTheEnd(int par1)
    {
    	if (this.currentPlayer instanceof EntityPlayerMP)
    	{
    		EntityPlayerMP player = (EntityPlayerMP) this.currentPlayer;
            player.mcServer.getConfigurationManager().transferPlayerToDimension(player, par1, new GCCoreTeleporter());
    	}
    }
    
    public void sendAirRemainingPacket()
    {
    	Object[] toSend = {this.airRemaining};
        FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.currentPlayer.username).playerNetServerHandler.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 0, toSend));
    }
    
    public void setInPortal(int par1)
    {
        if (this.currentPlayer.timeUntilPortal > 0)
        {
            this.currentPlayer.timeUntilPortal = 10;
        }
        else
        {
        	this.dimensionToSend = par1;
            this.inPortal = true;
        }
    }
	
    public void readEntityFromNBT()
    {
    	NBTTagCompound par1NBTTagCompound = this.currentPlayer.getEntityData();
		this.airRemaining = par1NBTTagCompound.getInteger("playerAirRemaining");
		this.damageCounter = par1NBTTagCompound.getInteger("damageCounter");
        NBTTagList var2 = par1NBTTagCompound.getTagList("InventoryTankRefill");
        this.playerTankInventory.readFromNBT2(var2);
        this.inSpaceship = par1NBTTagCompound.getBoolean("inSpaceship");
    }

    public void writeEntityToNBT()
    {
    	NBTTagCompound par1NBTTagCompound = this.currentPlayer.getEntityData();
    	par1NBTTagCompound.setInteger("playerAirRemaining", this.airRemaining);
    	par1NBTTagCompound.setInteger("damageCounter", this.damageCounter);
        par1NBTTagCompound.setTag("InventoryTankRefill", this.playerTankInventory.writeToNBT2(new NBTTagList()));
    }
}
