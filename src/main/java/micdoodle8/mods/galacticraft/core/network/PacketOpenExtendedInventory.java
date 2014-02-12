package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.player.EntityPlayer;

public class PacketOpenExtendedInventory implements IPacket
{
	// TODO Find out if there's a better way of doing this other than using a whole packet for it.
	
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
		player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiExtendedInventory, player.worldObj, 0, 0, 0);
	}
}
