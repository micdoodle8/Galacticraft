package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelSkeleton;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelSpider;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemSensorGlasses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@SideOnly(Side.CLIENT)
public class GCCoreRenderSkeleton extends RenderLiving
{
    private final ModelBase model = new GCCoreModelSkeleton(0.2F);
    
    public GCCoreRenderSkeleton()
    {
        super(new GCCoreModelSkeleton(), 1.0F);
        this.setRenderPassModel(new GCCoreModelSpider());
    }

    @Override
    protected void passSpecialRender(EntityLiving par1EntityLiving, double par2, double par4, double par6)
    {
        ClientProxyCore.TickHandlerClient.renderName(par1EntityLiving, par2, par4, par6);
    }
    
    @Override
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
    	final Minecraft minecraft = FMLClientHandler.instance().getClient();
		
        final WorldClient world = minecraft.theWorld;
        
        final EntityPlayerSP player = minecraft.thePlayer;
        
        ItemStack helmetSlot = null;
		
		if (player != null && player.inventory.armorItemInSlot(3) != null)
		{
			helmetSlot = player.inventory.armorItemInSlot(3);
		}
		
        if (helmetSlot != null && helmetSlot.getItem() instanceof GCCoreItemSensorGlasses && minecraft.currentScreen == null)
        {
            if (par2 == 1)
            {
                final float var4 = par1EntityLiving.ticksExisted * 2 + par3;
                this.loadTexture("/micdoodle8/mods/galacticraft/core/client/entities/power.png");
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                final float var5 = var4 * 0.01F;
                final float var6 = var4 * 0.01F;
                GL11.glTranslatef(var5, var6, 0.0F);
                this.setRenderPassModel(this.model);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glEnable(GL11.GL_BLEND);
                final float var7 = 0.5F;
                GL11.glColor4f(var7, var7, var7, 1.0F);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                return 1;
            }

            if (par2 == 2)
            {
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }
        
        if (par2 == 0)
        {
            this.loadTexture("/mob/spider_eyes.png");
            final float var4 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            final char var5 = 61680;
            final int var6 = var5 % 65536;
            final int var7 = var5 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
            return 1;
        }

        return -1;
    }
}
