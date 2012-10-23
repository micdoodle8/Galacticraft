package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.API.GalacticraftWorldProvider;
import net.minecraft.src.AchievementList;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet70GameEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
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
                        double var14 = (double)(var10 + 1);

                        if (var13 < 8)
                        {
                            var14 = (double)(var10 + 1) - (double)var13 / 8.0D;
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
		        	ItemStack stack = new ItemStack(GCCoreItems.gravityBow, var1, var2);
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
	        }
	        
			if (GalacticraftCore.instance.tick % 10 == 0)
			{
				sendAirRemainingPacket();
			}

			ItemStack tankInSlot = playerTankInventory.getStackInSlot(0);
			
			int drainSpacing = GCCoreUtil.getDrainSpacing(tankInSlot);
			
			if (playerTankInventory.getStackInSlot(0) != null && this.airRemaining <= 0)
			{
				this.airRemaining = 0;
				playerTankInventory.consumeTankInSlot(0);
				this.airRemaining = 90;
			}
			
			if (player.worldObj.provider instanceof GalacticraftWorldProvider && !player.capabilities.isCreativeMode)
	        {
				if (drainSpacing > 0)
				{
		    		this.airRemaining = 90 - tankInSlot.getItemDamage();
				}
				
				if (drainSpacing > 0 && GalacticraftCore.instance.tick % drainSpacing == 0 && !isAABBInBreathableAirBlock()) 
		    	{
		    		tankInSlot.damageItem(1, player);
		    	}
				
				if (drainSpacing == 0 && GalacticraftCore.instance.tick % 20 == 0 && !isAABBInBreathableAirBlock())
				{
		    		this.airRemaining -= 1;
				}
				
				if (GalacticraftCore.instance.tick % 20 == 0 && isAABBInBreathableAirBlock() && this.airRemaining < 90 && tankInSlot != null)
				{
					this.airRemaining += 1;
				}
				
	        	if (GalacticraftCore.instance.tick % 100 == 0) 
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
						player.attackEntityFrom(DamageSource.inWall, 2);
						player.worldObj.playSoundAtEntity(player, "", 1.0f, 1.0F);
						player.performHurtAnimation();
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
	}

    public void travelToTheEnd(int par1)
    {
    	if (this.currentPlayer instanceof EntityPlayerMP)
    	{
    		EntityPlayerMP player = (EntityPlayerMP) this.currentPlayer;
    		
            if (player.dimension == 1 && par1 == 1)
            {
            	player.triggerAchievement(AchievementList.theEnd2);
            	player.worldObj.setEntityDead(player);
                player.playerConqueredTheEnd = true;
                player.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(4, 0));
            }
            else
            {
//                if (player.dimension == 1 && par1 == 0)
//                {
//                	player.triggerAchievement(AchievementList.theEnd);
//                    ChunkCoordinates var2 = player.mcServer.worldServerForDimension(par1).getEntrancePortalLocation();
//
//                    if (var2 != null)
//                    {
//                    	player.playerNetServerHandler.setPlayerLocation((double)var2.posX, (double)var2.posY, (double)var2.posZ, 0.0F, 0.0F);
//                    }
//
//                    par1 = 1;
//                }
//                else
//                {
//                	player.triggerAchievement(AchievementList.portal);
//                }
            	
            	FMLLog.info("" + par1);

                player.mcServer.getConfigurationManager().transferPlayerToDimension(player, par1, new GCCoreTeleporter());
            }
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
        NBTTagList var2 = par1NBTTagCompound.getTagList("InventoryTankRefill");
        this.playerTankInventory.readFromNBT2(var2);
    }

    public void writeEntityToNBT()
    {
    	NBTTagCompound par1NBTTagCompound = this.currentPlayer.getEntityData();
    	par1NBTTagCompound.setInteger("playerAirRemaining", this.airRemaining);
        par1NBTTagCompound.setTag("InventoryTankRefill", this.playerTankInventory.writeToNBT2(new NBTTagList()));
    }
}
