package micdoodle8.mods.galacticraft.core.proxy;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxyCore
{
	public void preInit(FMLPreInitializationEvent event)
	{
		;
	}

	public void init(FMLInitializationEvent event)
	{
		;
	}

	public void postInit(FMLPostInitializationEvent event)
	{
		;
	}

	public int getBlockRender(Block blockID)
	{
		return -1;
	}

	public int getTitaniumArmorRenderIndex()
	{
		return 0;
	}

	public int getSensorArmorRenderIndex()
	{
		return 0;
	}

	public World getClientWorld()
	{
		return null;
	}

	public void spawnParticle(String particleID, Vector3 position, Vector3 motion)
	{
		;
	}

	public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Vector3 color)
	{
		;
	}

	public World getWorldForID(int dimensionID)
	{
		return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimensionID);
	}

	public EntityPlayer getPlayerFromNetHandler(INetHandler handler)
	{
		if (handler instanceof NetHandlerPlayServer)
		{
			return ((NetHandlerPlayServer) handler).playerEntity;
		}
		else
		{
			return null;
		}
	}
}
