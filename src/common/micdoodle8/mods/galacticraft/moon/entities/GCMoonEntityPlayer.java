package micdoodle8.mods.galacticraft.moon.entities;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import micdoodle8.mods.galacticraft.moon.GalacticraftMoon;
import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
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

public class GCMoonEntityPlayer
{
	private EntityPlayer currentPlayer;
	
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
		if (event.entityLiving instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
			
			if (this.currentPlayer.worldObj != null && this.currentPlayer.worldObj.provider instanceof GCMoonWorldProvider && !this.currentPlayer.isAirBorne)
			{
				if (this.currentPlayer.worldObj.getBlockId(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ)) == GCMoonBlocks.moonGrass.blockID)
				{
					if (this.currentPlayer.worldObj.getBlockMetadata(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ)) == 0)
					{
						switch (this.lastStep)
						{
						case 1:
							this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), 2);
							this.lastStep = 2;
							break;
						case 2:
							this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), 1);
							this.lastStep = 1;
							break;
						default:
							this.currentPlayer.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), 1);
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
    	NBTTagCompound par1NBTTagCompound = this.currentPlayer.getEntityData();
    }

    public void writeEntityToNBT()
    {
    	NBTTagCompound par1NBTTagCompound = this.currentPlayer.getEntityData();
    }
}
