package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelSkeleton;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelSpider;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemSensorGlasses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
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
    protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) 
    {
    	GL11.glScalef(1.2F, 1.2F, 1.2F);
    }

    protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) 
    {
    	GL11.glPushMatrix();
    	GL11.glTranslatef(-0.3F, -0.3F, -0.6F);
    	GL11.glTranslatef(0.1F, 0.0F, 0.0F);
    	GL11.glRotatef(41, 0.0F, 1.0F, 0.0F);
    	GL11.glRotatef(-20, 1.0F, 0.0F, 0.0F);
    	GL11.glRotatef(-20, 0.0F, 0.0F, 1.0F);
    	GL11.glScalef(0.5F, 0.5F, 0.5F);
//    	GL11.glRotatef(00, 0.0F, 0.0F, 1.0F);
        this.renderManager.itemRenderer.renderItem(par1EntityLiving, new ItemStack(Item.bow), 0);
        GL11.glPopMatrix();

    	GL11.glPushMatrix();
    	GL11.glTranslatef(0.11F, -0.3F, -0.6F);
    	GL11.glTranslatef(0.1F, 0.0F, 0.0F);
    	GL11.glRotatef(46, 0.0F, 1.0F, 0.0F);
    	GL11.glRotatef(-20, 1.0F, 0.0F, 0.0F);
    	GL11.glRotatef(-20, 0.0F, 0.0F, 1.0F);
    	GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.renderManager.itemRenderer.renderItem(par1EntityLiving, new ItemStack(Item.bow), 0);
        GL11.glPopMatrix();
    }

    @Override
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
    	final Minecraft minecraft = FMLClientHandler.instance().getClient();

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

        return -1;
    }
}
