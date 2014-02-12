package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public class PacketIgniteRocket implements IPacket
{
	@Override
	public void encodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		if (player instanceof GCCorePlayerMP)
		{
			GCCorePlayerMP playerBase = (GCCorePlayerMP) player;
			
			if (!player.worldObj.isRemote && !player.isDead && player.ridingEntity != null && !player.ridingEntity.isDead && player.ridingEntity instanceof EntityTieredRocket)
			{
				EntityTieredRocket ship = (EntityTieredRocket) player.ridingEntity;

				if (ship.hasValidFuel())
				{
					ItemStack stack2 = null;

					if (playerBase != null)
					{
						stack2 = playerBase.getExtendedInventory().getStackInSlot(4);
					}

					if (stack2 != null && stack2.getItem() instanceof GCCoreItemParachute || playerBase != null && playerBase.getLaunchAttempts() > 0)
					{
						ship.igniteCheckingCooldown();
						playerBase.setLaunchAttempts(0);
					}
					else if (playerBase.getChatCooldown() == 0 && playerBase.getLaunchAttempts() == 0)
					{
						player.addChatMessage(new ChatComponentText("I don't have a parachute! If I press launch again, there's no going back!"));
						playerBase.setChatCooldown(250);
						playerBase.setLaunchAttempts(1);
					}
				}
				else if (playerBase.getChatCooldown() == 0)
				{
					player.addChatMessage(new ChatComponentText("I'll need to load in some rocket fuel first!"));
					playerBase.setChatCooldown(250);
				}
			}
		}
		
	}
}
