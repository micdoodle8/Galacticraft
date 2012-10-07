package micdoodle8.mods.galacticraft;

import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.src.ServerPlayerBase;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCPlayerBaseServer extends ServerPlayerBase
{
	private int airRemaining;
	
	public static GCPlayerBaseServer instance;
	
	public boolean hasTank;
	
	public ItemStack tankInSlot;
	
	public GCInventoryTankRefill playerTankInventory = new GCInventoryTankRefill();
	
	public boolean inPortal;
	
	public boolean inSpaceship;
	
	public int timeUntilPortal;
	
	private int dimensionToSend = -2;
	
	public GCPlayerBaseServer(ServerPlayerAPI var1) 
	{
		super(var1);
		this.instance = this;
		this.hasTank = false;
		Galacticraft.instance.serverPlayerBaseList.add(player);
		Galacticraft.instance.serverPlayerAPIs.add(this);
	}
	
	public EntityPlayerMP getPlayer()
	{
		return this.player;
	}

	@Override
    public void fall(float var1)
    {
        if (player.dimension == GCConfigManager.dimensionIDMars)
        {
        	;
        }
        else
        {
        	super.fall(var1);
        }
    }
    
    public static boolean handleBacterialMovement(EntityPlayer player)
    {
        return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), GCBlocks.bacterialSludge);
    }

    public boolean isAABBInBreathableAirBlock()
    {
        int var3 = MathHelper.floor_double(player.boundingBox.minX);
        int var4 = MathHelper.floor_double(player.boundingBox.maxX + 1.0D);
        int var5 = MathHelper.floor_double(player.boundingBox.minY);
        int var6 = MathHelper.floor_double(player.boundingBox.maxY + 1.0D);
        int var7 = MathHelper.floor_double(player.boundingBox.minZ);
        int var8 = MathHelper.floor_double(player.boundingBox.maxZ + 1.0D);

        for (int var9 = var3; var9 < var4; ++var9)
        {
            for (int var10 = var5; var10 < var6; ++var10)
            {
                for (int var11 = var7; var11 < var8; ++var11)
                {
                    Block var12 = Block.blocksList[player.worldObj.getBlockId(var9, var10, var11)];

                    if (var12 != null && var12.blockID == GCBlocks.breatheableAir.blockID)
                    {
                        int var13 = player.worldObj.getBlockMetadata(var9, var10, var11);
                        double var14 = (double)(var10 + 1);

                        if (var13 < 8)
                        {
                            var14 = (double)(var10 + 1) - (double)var13 / 8.0D;
                        }

                        if (var14 >= player.boundingBox.minY)
                        {
                            return true;
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
		if (Galacticraft.instance.tick % 10 == 0)
		{
			sendAirRemainingPacket();
		}
		
		if (handleBacterialMovement(player) && !player.capabilities.isCreativeMode && !player.isPotionActive(Potion.poison))
		{
			player.addPotionEffect(new PotionEffect(Potion.poison.id, 40, 0));
		}

		ItemStack tankInSlot = playerTankInventory.getStackInSlot(0);
		
		int drainSpacing = GCUtil.getDrainSpacing(tankInSlot);
		
		if (playerTankInventory.getStackInSlot(0) != null && this.airRemaining <= 0)
		{
			this.airRemaining = 0;
			playerTankInventory.consumeTankInSlot(0);
			this.airRemaining = 90;
		}
		
		if (player.dimension == GCConfigManager.dimensionIDMars && !player.capabilities.isCreativeMode)
        {
			if (drainSpacing > 0)
			{
	    		this.airRemaining = 90 - tankInSlot.getItemDamage();
			}
			
			if (drainSpacing > 0 && Galacticraft.instance.tick % drainSpacing == 0 && !isAABBInBreathableAirBlock()) 
	    	{
	    		tankInSlot.damageItem(1, player);
	    	}
			
			if (drainSpacing == 0 && Galacticraft.instance.tick % 20 == 0 && !isAABBInBreathableAirBlock())
			{
	    		this.airRemaining -= 1;
			}
			
			if (Galacticraft.instance.tick % 20 == 0 && isAABBInBreathableAirBlock() && this.airRemaining < 90 && tankInSlot != null)
			{
				this.airRemaining += 1;
			}
			
        	if (Galacticraft.instance.tick % 100 == 0) 
        	{
        		ItemStack helmetSlot = null;
        		
        		if (player.inventory.armorItemInSlot(3) != null)
        		{
        			helmetSlot = player.inventory.armorItemInSlot(3);
        		}
        		
        		if ((helmetSlot == null || !(helmetSlot.getItem() instanceof GCItemOxygenMask) || this.airRemaining <= 0) && !isAABBInBreathableAirBlock()) 
				{
					player.superDamageEntity(DamageSource.inWall, 2);
					player.worldObj.playSoundAtEntity(player, "", 1.0f, 1.0F);
					player.performHurtAnimation();
				}
			}
        }
		else if (Galacticraft.instance.tick % 20 == 0 && !player.capabilities.isCreativeMode && this.airRemaining < 90)
		{
			this.airRemaining += 1;
		}
		else if (player.capabilities.isCreativeMode)
		{
			this.airRemaining = 90;
		}
		
		super.onUpdate();
	}
	
	public void onUpdateEntity()
	{
		if (this.timeUntilPortal > 0)
		{
			this.timeUntilPortal--;
		}
		
		if (this.inPortal && timeUntilPortal == 0)
        {
        	byte var5;
        	
            if (this.dimensionToSend != -2)
            {
            	var5 = (byte) this.dimensionToSend;
            }
            else
            {
            	var5 = 0;
            }

            player.mcServer.getConfigurationManager().transferPlayerToDimension(player, var5, new GCTeleporter());
            player.timeUntilPortal = 10;
            player.timeInPortal = 0.0F;
            
            this.inPortal = false;
        }
        else
        {
            super.onUpdateEntity();
        }
	}
	
	@Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
		this.airRemaining = par1NBTTagCompound.getInteger("playerAirRemaining");
        NBTTagList var2 = par1NBTTagCompound.getTagList("InventoryTankRefill");
        this.playerTankInventory.readFromNBT2(var2);
		
		super.readEntityFromNBT(par1NBTTagCompound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	par1NBTTagCompound.setInteger("playerAirRemaining", this.airRemaining);
        par1NBTTagCompound.setTag("InventoryTankRefill", this.playerTankInventory.writeToNBT2(new NBTTagList()));
        
        super.writeEntityToNBT(par1NBTTagCompound);
    }
    
    public void sendAirRemainingPacket()
    {
    	Object[] toSend = {this.airRemaining};
        FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(player.username).playerNetServerHandler.sendPacketToPlayer(GCUtil.createPacket("Galacticraft", 0, toSend));
    }
    
    public void setInPortal(int par1)
    {
        if (player.timeUntilPortal > 0)
        {
            player.timeUntilPortal = 10;
        }
        else
        {
        	this.dimensionToSend = par1;
            this.inPortal = true;
        }
    }
}
