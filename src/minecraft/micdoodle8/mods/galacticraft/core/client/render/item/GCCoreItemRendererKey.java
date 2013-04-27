package micdoodle8.mods.galacticraft.core.client.render.item;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelKey;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreItemRendererKey implements IItemRenderer
{
	GCCoreModelKey keyModel = new GCCoreModelKey();

	private void renderKey(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ, Object... data)
	{
        GL11.glPushMatrix();

//        if (type == ItemRenderType.EQUIPPED)
//        {
//            GL11.glScalef(7F, 7F, 7F);
//            GL11.glTranslatef(0.0F, 0.7F, 0.1F);
//
//            if (FMLClientHandler.instance().getClient().thePlayer.getItemInUseCount() > 0)
//            {
//                float var13b;
//                float var14b;
//                var13b = item.getMaxItemUseDuration() - (FMLClientHandler.instance().getClient().thePlayer.getItemInUseCount() + 1.0F);
//                var14b = var13b / 20.0F;
//                var14b = (var14b * var14b + var14b * 2.0F) / 3.0F;
//
//                if (var14b > 1.0F)
//                {
//                    var14b = 1.0F;
//                }
//
//                GL11.glRotatef(MathHelper.sin((var13b - 0.1F) * 0.3F) * 0.01F * (var14b - 0.1F) * 60, 1F, 0F, 0F);
//
//                GL11.glRotatef(var14b * 30F, 1F, 0F, 1F);
//                GL11.glTranslatef(0F, -(var14b * 0.2F), 0F);
//            }
//        }
        
        EntityItem entityItem = null;
        
        if (data.length == 2 && data[1] instanceof EntityItem)
        {
        	entityItem = (EntityItem) data[1];
        }
        
        if (type == ItemRenderType.INVENTORY)
        {
            GL11.glTranslatef(8.0F, 8.0F, 0F);
            GL11.glRotatef((MathHelper.sin((Sys.getTime() / 90F) / 20.0F) - 55.0F) * 50.0F, 0, 0, 1);
            GL11.glScalef(5.0F, 5.0F, 5.0F);
            GL11.glScalef(1.5F, 1.5F, 1.5F);
        }
        else if (type == ItemRenderType.ENTITY)
        {
            GL11.glTranslatef(0.0F, 2.0F, 0F);
            GL11.glScalef(3.0F, 3.0F, 3.0F);
        }
        else if (type == ItemRenderType.EQUIPPED)
        {
            GL11.glRotatef(-4.0F, 0, 1, 0);
            GL11.glRotatef(2.0F, 1, 0, 0);
            GL11.glTranslatef(3.0F, 2.0F, -0.6F);
            GL11.glScalef(3.0F, 3.0F, 3.0F);
        }
        
        GL11.glRotatef(45, 0, 0, 1);
        
        if (entityItem != null)
        {
            float f2 = MathHelper.sin(((float)entityItem.age + 1) / 10.0F + entityItem.hoverStart) * 0.1F + 0.1F;
            GL11.glRotatef(f2 * 90F - 45F, 0, 0, 1);
            GL11.glRotatef((float) (Math.sin(((float)(entityItem.age + 1) / 100.0F)) * 180.0F), 0, 1, 0);
        }

    	switch (item.getItemDamage())
    	{
    	default:
    		FMLClientHandler.instance().getClient().renderEngine.bindTexture("/micdoodle8/mods/galacticraft/core/client/entities/chest.png");
    		break;
    	}

        this.keyModel.renderAll();
        GL11.glPopMatrix();
	}


	/** IItemRenderer implementation **/

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		switch (type) 
		{
		case ENTITY:
			return true;
		case EQUIPPED:
			return true;
		case INVENTORY:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		switch (type)
		{
		case EQUIPPED:
			this.renderKey(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f, data);
			break;
		case INVENTORY:
			this.renderKey(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f, data);
			break;
		case ENTITY:
			this.renderKey(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f, data);
			break;
		default:
		}
	}

}
