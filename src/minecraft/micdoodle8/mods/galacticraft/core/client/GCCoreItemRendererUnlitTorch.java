package micdoodle8.mods.galacticraft.core.client;

import net.minecraft.src.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreItemRendererUnlitTorch implements IItemRenderer
{
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) 
	{
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) 
	{
		switch (helper)
		{
		case INVENTORY_BLOCK:
			return true;
		default:
			return false;
		}
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) 
	{
		switch (type)
		{
		case INVENTORY:
			renderTorchInInv();
		default:
		}
	}
	
	public void renderTorchInInv()
	{
		GL11.glPushMatrix();
		
        GL11.glPopMatrix();
	}
}
