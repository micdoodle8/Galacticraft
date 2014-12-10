package net.smart.render;

import cpw.mods.fml.client.registry.RenderingRegistry;

import net.minecraft.client.entity.*;
import net.minecraft.client.renderer.entity.*;

public abstract class SmartRenderContext extends SmartRenderUtilities
{
	public static void registerRenderers()
	{
		registerRenderers(net.smart.render.RenderPlayer.class);
	}

	public static void registerRenderers(Class<?> type)
	{
		Render render;
		try
		{
			render = (Render)type.newInstance();
		}
		catch (Exception e)
		{
			return;
		}

		RenderingRegistry.registerEntityRenderingHandler(EntityPlayerSP.class, render);
		RenderingRegistry.registerEntityRenderingHandler(EntityOtherPlayerMP.class, render);
		render.setRenderManager(RenderManager.instance);
	}
}
