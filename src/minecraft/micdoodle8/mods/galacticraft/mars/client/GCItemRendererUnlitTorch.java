package micdoodle8.mods.galacticraft.mars.client;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.INVENTORY;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

import micdoodle8.mods.galacticraft.mars.GCMarsBlocks;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCItemRendererUnlitTorch implements IItemRenderer
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
