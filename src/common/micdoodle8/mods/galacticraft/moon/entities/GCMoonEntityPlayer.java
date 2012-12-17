package micdoodle8.mods.galacticraft.moon.entities;

import micdoodle8.mods.galacticraft.moon.GalacticraftMoon;
import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent;

public class GCMoonEntityPlayer
{
	private final EntityPlayer currentPlayer;
	
	private int lastStep;
	
	public GCMoonEntityPlayer(EntityPlayer player) 
	{
		this.currentPlayer = player;
		GalacticraftMoon.moonPlayers.add(player);
		GalacticraftMoon.gcMoonPlayers.add(this);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public EntityPlayer getPlayer()
	{
		return this.currentPlayer;
	}
	
	@ForgeSubscribe
	public void onUpdate(LivingEvent event)
	{
		if (event.entityLiving instanceof EntityPlayer)
		{
			final EntityPlayer player = (EntityPlayer) event.entityLiving;
			
			final double j = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
			
			if (this.currentPlayer.worldObj != null && this.currentPlayer.worldObj.provider instanceof GCMoonWorldProvider && !this.currentPlayer.isAirBorne)
			{
				if (this.currentPlayer.worldObj.getBlockId(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ)) == GCMoonBlocks.moonGrass.blockID)
				{
					if (this.currentPlayer.worldObj.getBlockMetadata(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ)) == 0)
					{
						int meta = -1;
						
						final int i = 1 + MathHelper.floor_double((double)(this.currentPlayer.rotationYaw * 8.0F / 360.0F) + 0.5D) & 7;
						switch (this.lastStep)
						{
						case 1:
							switch (i)
							{
							case 0:
								meta = 2;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							case 1:
								meta = 4;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							case 2:
								meta = 2;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							case 3:
								meta = 2;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							case 4:
								meta = 2;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							case 5:
								meta = 2;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							case 6:
								meta = 2;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							case 7:
								meta = 2;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							}
							this.lastStep = 2;
							break;
						case 2:
							switch (i)
							{
							case 0:
								meta = 1;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							case 1:
								meta = 1;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							case 2:
								meta = 4;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							case 3:
								meta = 4;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							case 4:
								meta = 1;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							case 5:
								meta = 3;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							case 6:
								meta = 2;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							case 7:
								meta = 4;
								this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
								break;
							}
							this.lastStep = 1;
							this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), meta);
							break;
						default:
							this.lastStep = 1;
							break;
						}
					}
				}
			}
		}
	}
	
    public void readEntityFromNBT()
    {
    	final NBTTagCompound par1NBTTagCompound = this.currentPlayer.getEntityData();
    }

    public void writeEntityToNBT()
    {
    	final NBTTagCompound par1NBTTagCompound = this.currentPlayer.getEntityData();
    }
}
